package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ConversationEntity
import com.example.data.repository.ChatRepository
import com.example.model.Persona
import com.example.model.PersonaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    val conversations: StateFlow<List<ConversationEntity>> = repository.allConversations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _activeConversationId = MutableStateFlow<Long?>(null)
    val activeConversationId: StateFlow<Long?> = _activeConversationId.asStateFlow()

    private val _selectedPersona = MutableStateFlow(PersonaRepository.personas.first())
    val selectedPersona: StateFlow<Persona> = _selectedPersona.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _customTemperature = MutableStateFlow<Float?>(null)
    val customTemperature: StateFlow<Float?> = _customTemperature.asStateFlow()

    private val _showCustomBackground = MutableStateFlow(true)
    val showCustomBackground: StateFlow<Boolean> = _showCustomBackground.asStateFlow()

    private val _backgroundDim = MutableStateFlow(0.70f)
    val backgroundDim: StateFlow<Float> = _backgroundDim.asStateFlow()

    private var currentJob: Job? = null

    // Reactive messages list for currently active conversation
    val messages: StateFlow<List<ChatMessageEntity>> = _activeConversationId.flatMapLatest { convId ->
        if (convId != null) {
            repository.getMessagesForConversation(convId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Observe conversations and auto-select latest or create if empty
        viewModelScope.launch {
            repository.allConversations.collect { list ->
                if (_activeConversationId.value == null && list.isNotEmpty()) {
                    val first = list.first()
                    _activeConversationId.value = first.id
                    _selectedPersona.value = PersonaRepository.getPersonaById(first.personaId)
                }
            }
        }
    }

    fun selectConversation(id: Long) {
        _activeConversationId.value = id
        viewModelScope.launch {
            val conv = conversations.value.find { it.id == id }
            if (conv != null) {
                _selectedPersona.value = PersonaRepository.getPersonaById(conv.personaId)
            }
        }
    }

    fun startNewConversation(personaId: String? = null) {
        viewModelScope.launch {
            val targetPersona = if (personaId != null) {
                PersonaRepository.getPersonaById(personaId)
            } else {
                _selectedPersona.value
            }
            val newId = repository.createNewConversation(
                title = "محادثة جديدة",
                personaId = targetPersona.id
            )
            _activeConversationId.value = newId
            _selectedPersona.value = targetPersona
            _inputText.value = ""
        }
    }

    fun selectPersona(persona: Persona) {
        _selectedPersona.value = persona
        val currentConvId = _activeConversationId.value
        if (currentConvId != null) {
            viewModelScope.launch {
                repository.updateConversationPersona(currentConvId, persona.id)
            }
        }
    }

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun setTemperature(temperature: Float) {
        _customTemperature.value = temperature
    }

    fun setShowCustomBackground(show: Boolean) {
        _showCustomBackground.value = show
    }

    fun setBackgroundDim(dim: Float) {
        _backgroundDim.value = dim.coerceIn(0.1f, 0.95f)
    }

    fun sendMessage(customPrompt: String? = null) {
        val textToSend = customPrompt ?: _inputText.value.trim()
        if (textToSend.isBlank() || _isGenerating.value) return

        if (customPrompt == null) {
            _inputText.value = ""
        }

        viewModelScope.launch {
            var convId = _activeConversationId.value
            if (convId == null) {
                convId = repository.createNewConversation(
                    title = "محادثة جديدة",
                    personaId = _selectedPersona.value.id
                )
                _activeConversationId.value = convId
            }

            _isGenerating.value = true
            currentJob = launch {
                repository.sendMessage(
                    conversationId = convId,
                    userPrompt = textToSend,
                    personaId = _selectedPersona.value.id,
                    temperature = _customTemperature.value
                )
                _isGenerating.value = false
            }
        }
    }

    fun retryLastMessage() {
        val currentMessages = messages.value
        val lastUserMessage = currentMessages.findLast { it.role == "user" }
        if (lastUserMessage != null && !_isGenerating.value) {
            sendMessage(lastUserMessage.text)
        }
    }

    fun deleteConversation(id: Long) {
        viewModelScope.launch {
            repository.deleteConversation(id)
            if (_activeConversationId.value == id) {
                val remaining = conversations.value.filter { it.id != id }
                if (remaining.isNotEmpty()) {
                    _activeConversationId.value = remaining.first().id
                    _selectedPersona.value = PersonaRepository.getPersonaById(remaining.first().personaId)
                } else {
                    _activeConversationId.value = null
                }
            }
        }
    }

    fun clearAllConversations() {
        viewModelScope.launch {
            repository.clearAllConversations()
            _activeConversationId.value = null
        }
    }

    class Factory(private val repository: ChatRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(repository) as T
        }
    }
}
