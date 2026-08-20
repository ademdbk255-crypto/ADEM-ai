package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.GenerationConfig
import com.example.data.api.GeminiClient
import com.example.data.api.Part
import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ConversationEntity
import com.example.model.PersonaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepository(private val database: AppDatabase) {

    val allConversations: Flow<List<ConversationEntity>> =
        database.conversationDao().getAllConversations()

    fun getMessagesForConversation(conversationId: Long): Flow<List<ChatMessageEntity>> {
        return database.chatMessageDao().getMessagesForConversation(conversationId)
    }

    suspend fun createNewConversation(title: String = "محادثة جديدة", personaId: String = "general"): Long {
        return withContext(Dispatchers.IO) {
            val conversation = ConversationEntity(
                title = title,
                personaId = personaId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            database.conversationDao().insertConversation(conversation)
        }
    }

    suspend fun updateConversationPersona(conversationId: Long, personaId: String) {
        withContext(Dispatchers.IO) {
            val conv = database.conversationDao().getConversationById(conversationId)
            if (conv != null) {
                database.conversationDao().updateConversation(
                    conv.copy(personaId = personaId, updatedAt = System.currentTimeMillis())
                )
            }
        }
    }

    suspend fun updateConversationTitle(conversationId: Long, newTitle: String) {
        withContext(Dispatchers.IO) {
            val conv = database.conversationDao().getConversationById(conversationId)
            if (conv != null) {
                database.conversationDao().updateConversation(
                    conv.copy(title = newTitle, updatedAt = System.currentTimeMillis())
                )
            }
        }
    }

    suspend fun deleteConversation(conversationId: Long) {
        withContext(Dispatchers.IO) {
            database.conversationDao().deleteConversationById(conversationId)
        }
    }

    suspend fun clearAllConversations() {
        withContext(Dispatchers.IO) {
            database.conversationDao().clearAllConversations()
        }
    }

    suspend fun sendMessage(
        conversationId: Long,
        userPrompt: String,
        personaId: String,
        temperature: Float? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // 1. Save user message to database
            val userMessage = ChatMessageEntity(
                conversationId = conversationId,
                role = "user",
                text = userPrompt.trim(),
                timestamp = System.currentTimeMillis(),
                personaId = personaId
            )
            database.chatMessageDao().insertMessage(userMessage)

            // Update conversation title if default
            val conv = database.conversationDao().getConversationById(conversationId)
            if (conv != null) {
                val newTitle = if (conv.title == "محادثة جديدة" || conv.title.isBlank()) {
                    val preview = userPrompt.trim().take(35)
                    if (userPrompt.trim().length > 35) "$preview..." else preview
                } else {
                    conv.title
                }
                database.conversationDao().updateConversation(
                    conv.copy(
                        title = newTitle,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }

            // 2. Prepare past conversation history (last 16 messages for context window management)
            val history = database.chatMessageDao().getMessagesListForConversation(conversationId)
            val recentHistory = history.takeLast(16)

            val apiContents = recentHistory.map { msg ->
                Content(
                    role = if (msg.role == "user") "user" else "model",
                    parts = listOf(Part(text = msg.text))
                )
            }

            val persona = PersonaRepository.getPersonaById(personaId)
            val effectiveTemp = temperature ?: persona.defaultTemperature

            val systemInstruction = Content(
                parts = listOf(
                    Part(
                        text = "${persona.systemInstruction}\n\nاسم التطبيق: ADEM ai. كن دائماً واضحاً، ذكياً، دقيقاً، وساعد المستخدم بأفضل شكل ممكن."
                    )
                )
            )

            val request = GenerateContentRequest(
                contents = apiContents,
                generationConfig = GenerationConfig(
                    temperature = effectiveTemp,
                    topP = 0.95f,
                    topK = 40,
                    maxOutputTokens = 4096
                ),
                systemInstruction = systemInstruction
            )

            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                val errorMsg = "تنبيه: لم يتم ضبط مفتاح Gemini API Key في لوحة Secrets في AI Studio. يرجى إضافته لتفعيل الذكاء الاصطناعي بالكامل."
                val errorEntity = ChatMessageEntity(
                    conversationId = conversationId,
                    role = "model",
                    text = errorMsg,
                    timestamp = System.currentTimeMillis(),
                    isError = true,
                    personaId = personaId
                )
                database.chatMessageDao().insertMessage(errorEntity)
                return@withContext Result.failure(Exception(errorMsg))
            }

            val response = GeminiClient.apiService.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "عذراً، لم أتمكن من الحصول على إجابة، يرجى المحاولة مرة أخرى."

            // 3. Save model response
            val modelMessage = ChatMessageEntity(
                conversationId = conversationId,
                role = "model",
                text = responseText,
                timestamp = System.currentTimeMillis(),
                isError = false,
                personaId = personaId
            )
            database.chatMessageDao().insertMessage(modelMessage)

            Result.success(responseText)
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error sending message", e)
            val errorText = "حدث خطأ أثناء التواصل مع خوادم ADEM ai:\n${e.localizedMessage ?: e.message}"
            val errorEntity = ChatMessageEntity(
                conversationId = conversationId,
                role = "model",
                text = errorText,
                timestamp = System.currentTimeMillis(),
                isError = true,
                personaId = personaId
            )
            database.chatMessageDao().insertMessage(errorEntity)
            Result.failure(e)
        }
    }
}
