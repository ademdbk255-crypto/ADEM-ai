package com.example.ui

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.R
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ChatBubble
import com.example.ui.components.ConversationDrawerContent
import com.example.ui.components.PersonaChipSelector
import com.example.ui.components.QuickPromptsSection
import com.example.ui.components.SettingsDialog
import com.example.ui.components.TypingIndicator
import com.example.ui.theme.AdemBorderSubtle
import com.example.ui.theme.AdemCyanPrimary
import com.example.ui.theme.AdemDarkBackground
import com.example.ui.theme.AdemDarkSurface
import com.example.ui.theme.AdemDarkSurfaceVariant
import com.example.ui.theme.AdemIndigoSecondary
import com.example.ui.theme.AdemPurpleTertiary
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val activeConvId by viewModel.activeConversationId.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val selectedPersona by viewModel.selectedPersona.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val customTemperature by viewModel.customTemperature.collectAsStateWithLifecycle()
    val showCustomBackground by viewModel.showCustomBackground.collectAsStateWithLifecycle()
    val backgroundDim by viewModel.backgroundDim.collectAsStateWithLifecycle()

    var showSettingsDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Voice recognition launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.updateInputText(spokenText)
            }
        }
    }

    // Wrap in RTL Layout for native Arabic experience
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = AdemDarkBackground
                ) {
                    ConversationDrawerContent(
                        conversations = conversations,
                        activeConversationId = activeConvId,
                        onSelectConversation = { id ->
                            viewModel.selectConversation(id)
                            coroutineScope.launch { drawerState.close() }
                        },
                        onNewConversation = {
                            viewModel.startNewConversation()
                            coroutineScope.launch { drawerState.close() }
                        },
                        onDeleteConversation = { id ->
                            viewModel.deleteConversation(id)
                        },
                        onClearAll = {
                            viewModel.clearAllConversations()
                        },
                        onOpenSettings = {
                            coroutineScope.launch { drawerState.close() }
                            showSettingsDialog = true
                        }
                    )
                }
            }
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(AdemDarkBackground)
            ) {
                if (showCustomBackground) {
                    Image(
                        painter = painterResource(id = R.drawable.adem_bg_user_1787206785650),
                        contentDescription = "Background Wallpaper",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Dimming overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AdemDarkBackground.copy(alpha = backgroundDim))
                    )
                    // Subtle ambient gradient for depth
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        AdemDarkBackground.copy(alpha = 0.5f),
                                        Color.Transparent,
                                        AdemDarkBackground.copy(alpha = 0.9f)
                                    )
                                )
                            )
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, AdemCyanPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.adem_ai_logo_1787206486323),
                                        contentDescription = "ADEM ai",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Column {
                                    Text(
                                        text = "ADEM ai",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = AdemCyanPrimary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = selectedPersona.nameAr,
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }
                                },
                                modifier = Modifier.testTag("menu_drawer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.startNewConversation() },
                                modifier = Modifier.testTag("top_new_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Chat",
                                    tint = AdemCyanPrimary
                                )
                            }

                            IconButton(
                                onClick = { showSettingsDialog = true },
                                modifier = Modifier.testTag("settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = AdemDarkSurface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .imePadding()
                ) {
                    // Persona Selector Bar
                    PersonaChipSelector(
                        selectedPersonaId = selectedPersona.id,
                        onPersonaSelected = { persona ->
                            viewModel.selectPersona(persona)
                        }
                    )

                    // Chat Content Area
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (messages.isEmpty()) {
                            // Empty State / Welcome Screen
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(RoundedCornerShape(22.dp))
                                        .border(2.dp, AdemCyanPrimary, RoundedCornerShape(22.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.adem_ai_logo_1787206486323),
                                        contentDescription = "ADEM ai Logo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "مرحباً بك في ADEM ai",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = selectedPersona.descriptionAr,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                QuickPromptsSection(
                                    persona = selectedPersona,
                                    onPromptClick = { prompt ->
                                        viewModel.sendMessage(prompt)
                                    }
                                )
                            }
                        } else {
                            // Message List
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                items(messages, key = { it.id }) { message ->
                                    ChatBubble(
                                        message = message,
                                        onRetry = { viewModel.retryLastMessage() }
                                    )
                                }

                                if (isGenerating) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                        ) {
                                            TypingIndicator(personaName = selectedPersona.nameAr)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Input Bar
                    Surface(
                        color = AdemDarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AdemBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Speech to Text button
                            IconButton(
                                onClick = {
                                    try {
                                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                            putExtra(
                                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                            )
                                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar")
                                            putExtra(RecognizerIntent.EXTRA_PROMPT, "تحدث الآن مع ADEM ai...")
                                        }
                                        speechLauncher.launch(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "التعرف الصوتي غير متوفر على هذا الجهاز", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .testTag("voice_input_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Input",
                                    tint = AdemIndigoSecondary
                                )
                            }

                            // Text Input Field
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { viewModel.updateInputText(it) },
                                placeholder = {
                                    Text(
                                        text = "اسأل ADEM ai أي شيء...",
                                        fontSize = 14.sp,
                                        color = Color(0xFF64748B)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_input_field"),
                                shape = RoundedCornerShape(24.dp),
                                maxLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = AdemDarkSurfaceVariant,
                                    unfocusedContainerColor = AdemDarkSurfaceVariant,
                                    focusedBorderColor = AdemCyanPrimary,
                                    unfocusedBorderColor = AdemBorderSubtle,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                trailingIcon = {
                                    if (inputText.isNotBlank()) {
                                        IconButton(
                                            onClick = { viewModel.updateInputText("") },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear",
                                                tint = Color(0xFF94A3B8),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(
                                    onSend = { viewModel.sendMessage() }
                                )
                            )

                            // Send Button
                            FilledIconButton(
                                onClick = { viewModel.sendMessage() },
                                enabled = inputText.isNotBlank() && !isGenerating,
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("send_message_button"),
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = AdemCyanPrimary,
                                    contentColor = Color(0xFF090D16),
                                    disabledContainerColor = Color(0xFF1E293B),
                                    disabledContentColor = Color(0xFF475569)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

        // Settings Dialog
        if (showSettingsDialog) {
            SettingsDialog(
                currentTemperature = customTemperature ?: selectedPersona.defaultTemperature,
                onSaveTemperature = { newTemp ->
                    viewModel.setTemperature(newTemp)
                },
                showBackground = showCustomBackground,
                onToggleBackground = { enabled ->
                    viewModel.setShowCustomBackground(enabled)
                },
                currentBackgroundDim = backgroundDim,
                onSaveBackgroundDim = { dim ->
                    viewModel.setBackgroundDim(dim)
                },
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}
