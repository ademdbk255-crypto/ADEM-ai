package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.db.AppDatabase
import com.example.data.repository.ChatRepository
import com.example.ui.ChatScreen
import com.example.ui.ChatViewModel
import com.example.ui.theme.AdemAiTheme
import com.example.ui.theme.AdemDarkBackground

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val repository by lazy { ChatRepository(database) }
    private val viewModel by viewModels<ChatViewModel> {
        ChatViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdemAiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AdemDarkBackground
                ) {
                    ChatScreen(viewModel = viewModel)
                }
            }
        }
    }
}
