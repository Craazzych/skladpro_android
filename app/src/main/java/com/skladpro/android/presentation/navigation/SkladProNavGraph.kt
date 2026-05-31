package com.skladpro.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skladpro.android.domain.model.UserRole
import com.skladpro.android.presentation.auth.ActivationScreen
import com.skladpro.android.presentation.auth.AuthViewModel
import com.skladpro.android.presentation.auth.LoginScreen
import com.skladpro.android.presentation.auth.PasswordSetupScreen
import com.skladpro.android.presentation.employees.AddEmployeeRoute
import com.skladpro.android.presentation.employees.EmployeesRoute
import com.skladpro.android.presentation.employees.EmployeesViewModel
import com.skladpro.android.presentation.inventory.AddInventoryItemRoute
import com.skladpro.android.presentation.inventory.EditInventoryItemRoute
import com.skladpro.android.presentation.inventory.InventoryDetailsRoute
import com.skladpro.android.presentation.inventory.InventoryListRoute
import com.skladpro.android.presentation.inventory.InventoryViewModel

@Composable
fun SkladProNavGraph(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val inventoryViewModel: InventoryViewModel = viewModel()
    val employeesViewModel: EmployeesViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()
    var currentRole by rememberSaveable { mutableStateOf(UserRole.Worker) }
    var currentEmployeeId by rememberSaveable { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = AppRoute.Login.path
    ) {
        composable(AppRoute.Login.path) {
            LoginScreen(
                onOpenActivation = {
                    navController.navigate(AppRoute.Activation.path)
                },
                onLogin = { login, password ->
                    authViewModel.login(login, password) { employee ->
                        currentRole = employee.role
                        currentEmployeeId = employee.id
                        navController.navigate(AppRoute.InventoryList.path) {
                            popUpTo(AppRoute.Login.path) {
                                inclusive = true
                            }
                        }
                    }
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }

        composable(AppRoute.Activation.path) {
            ActivationScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onActivate = { login, temporaryPassword ->
                    authViewModel.prepareActivation(login, temporaryPassword)
                    navController.navigate(AppRoute.PasswordSetup.path)
                },
                errorMessage = authState.errorMessage
            )
        }

        composable(AppRoute.PasswordSetup.path) {
            PasswordSetupScreen(
                onBackToActivation = {
                    navController.popBackStack()
                },
                onPasswordSaved = { newPassword ->
                    authViewModel.activate(newPassword) {
                        navController.navigate(AppRoute.Login.path) {
                            popUpTo(AppRoute.Login.path) {
                                inclusive = false
                            }
                        }
                    }
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }

        composable(AppRoute.InventoryList.path) {
            InventoryListRoute(
                role = currentRole,
                darkTheme = darkTheme,
                viewModel = inventoryViewModel,
                onDarkThemeChange = onDarkThemeChange,
                onOpenEmployees = {
                    navController.navigate(AppRoute.Employees.path)
                },
                onAddItem = {
                    navController.navigate(AppRoute.AddInventoryItem.path)
                },
                onLogout = {
                    authViewModel.logout()
                    currentRole = UserRole.Worker
                    currentEmployeeId = null
                    navController.navigate(AppRoute.Login.path) {
                        popUpTo(AppRoute.InventoryList.path) {
                            inclusive = true
                        }
                    }
                },
                onOpenDetails = { itemId ->
                    navController.navigate(AppRoute.InventoryDetails.createPath(itemId))
                }
            )
        }

        composable(AppRoute.Employees.path) {
            EmployeesRoute(
                viewModel = employeesViewModel,
                currentEmployeeId = currentEmployeeId,
                onBack = {
                    navController.popBackStack()
                },
                onAddEmployee = {
                    navController.navigate(AppRoute.AddEmployee.path)
                }
            )
        }

        composable(AppRoute.AddEmployee.path) {
            AddEmployeeRoute(
                viewModel = employeesViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoute.AddInventoryItem.path) {
            AddInventoryItemRoute(
                viewModel = inventoryViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoute.EditInventoryItem.path,
            arguments = listOf(
                navArgument(AppRoute.EditInventoryItem.ITEM_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments
                ?.getString(AppRoute.EditInventoryItem.ITEM_ID_ARG)
                .orEmpty()

            EditInventoryItemRoute(
                itemId = itemId,
                viewModel = inventoryViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoute.InventoryDetails.path,
            arguments = listOf(
                navArgument(AppRoute.InventoryDetails.ITEM_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments
                ?.getString(AppRoute.InventoryDetails.ITEM_ID_ARG)
                .orEmpty()

            InventoryDetailsRoute(
                itemId = itemId,
                role = currentRole,
                viewModel = inventoryViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onEdit = {
                    navController.navigate(AppRoute.EditInventoryItem.createPath(itemId))
                },
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }
    }
}
