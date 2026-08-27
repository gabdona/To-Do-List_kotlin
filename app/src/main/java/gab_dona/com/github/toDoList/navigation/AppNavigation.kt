package gab_dona.com.github.toDoList.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import gab_dona.com.github.toDoList.ui.FormularioTarefaScreen
import gab_dona.com.github.toDoList.ui.ListaTarefasScreen
import gab_dona.com.github.toDoList.viewmodel.TarefaViewModel

@Composable
fun AppNavigation(viewModel: TarefaViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaTarefasScreen(
                viewModel = viewModel,
                onNovaTarefa = { navController.navigate("formulario/0") },
                onEditarTarefa = { id -> navController.navigate("formulario/$id") }
            )
        }
        composable(
            route = "formulario/{tarefaId}",
            arguments = listOf(
                navArgument("tarefaId") { type = NavType.IntType; defaultValue = 0 }
            )
        ) { backStackEntry ->
            val tarefaId = backStackEntry.arguments?.getInt("tarefaId") ?: 0
            FormularioTarefaScreen(
                viewModel = viewModel,
                tarefaId = tarefaId,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}