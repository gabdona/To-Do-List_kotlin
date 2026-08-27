package gab_dona.com.github.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import gab_dona.com.github.toDoList.navigation.AppNavigation

import gab_dona.com.github.toDoList.ui.theme.FiaptodolistTheme
import gab_dona.com.github.toDoList.viewmodel.TarefaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiaptodolistTheme {
                val viewModel: TarefaViewModel = viewModel(
                    factory = TarefaViewModel.factory(applicationContext)
                )
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}