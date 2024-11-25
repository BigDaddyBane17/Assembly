package com.example.computershop.navigation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.presentation.client_screens.AddOrderScreen
import com.example.computershop.presentation.client_screens.AuthenticationViewModel
import com.example.computershop.presentation.client_screens.LoginScreen
import com.example.computershop.presentation.client_screens.OrderInfoScreen
import com.example.computershop.presentation.client_screens.OrderListState
import com.example.computershop.presentation.client_screens.OrdersScreen
import com.example.computershop.presentation.client_screens.RegistrationScreen
import com.example.computershop.presentation.manager_screens.EditOrderStatusScreen
import com.example.computershop.presentation.manager_screens.ManagerAuthenticationViewModel
import com.example.computershop.presentation.manager_screens.ManagerClientsOrdersScreen
import com.example.computershop.presentation.manager_screens.ManagerLoginScreen
import com.example.computershop.presentation.manager_screens.ManagerOrderInfoScreen
import com.example.computershop.presentation.manager_screens.ManagerOrderListState
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationHost(
    databaseHelper: DatabaseHelper,
    authenticationViewModel: AuthenticationViewModel = koinViewModel(),
    managerAuthenticationViewModel: ManagerAuthenticationViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                authenticationViewModel = authenticationViewModel,
                onLoginSuccess = { userId ->
                    navController.navigate("orders/$userId") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegistrationScreen(
                authenticationViewModel = authenticationViewModel,
                onRegistrationSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "orders/{idClient}",
            arguments = listOf(navArgument("idClient") { type = NavType.IntType })
        ) { backStackEntry ->
            val idClient = backStackEntry.arguments?.getInt("idClient") ?: -1
            OrdersScreen(
                databaseHelper = databaseHelper,
                idClient = idClient,
                navController = navController,
                onAddOrder = { navController.navigate("addOrder/$idClient") }
            )
        }


        composable(
            route = "addOrder/{idClient}",
            arguments = listOf(navArgument("idClient") { type = NavType.IntType })
        ) { backStackEntry ->
            val idClient = backStackEntry.arguments?.getInt("idClient") ?: -1
            AddOrderScreen(
                idClient = idClient,
                onOrderAdded = { navController.popBackStack() },
                databaseHelper = databaseHelper
            )
        }


        composable(
            route = "orderInfo/{idOrderInfo}",
            arguments = listOf(navArgument("idOrderInfo") { type = NavType.IntType })
        ) { backStackEntry ->
            val idOrder = backStackEntry.arguments?.getInt("idOrderInfo") ?: -1
            val selectedOrder = databaseHelper.getOrderById(idOrder)
            Log.d("123", "$selectedOrder")
            if (selectedOrder != null) {
                OrderInfoScreen(
                    state = OrderListState(selectedOrder = selectedOrder)
                )
            } else {
                Text("Заказ не найден")
            }
        }

        composable("managerLogin") {
            ManagerLoginScreen(
                managerAuthenticationViewModel = managerAuthenticationViewModel,
                onLoginSuccess = {
                    navController.navigate("managerOrders") {
                        popUpTo("managerLogin") { inclusive = true }
                    }
                }
            )
        }


        composable("managerOrders") {
            ManagerClientsOrdersScreen(
                databaseHelper = databaseHelper,
                navController = navController,
                managerAuthenticationViewModel = managerAuthenticationViewModel
            )
        }


        composable(
            route = "managerOrderInfo/{idOrderInfo}",
            arguments = listOf(navArgument("idOrderInfo") { type = NavType.IntType })
        ) { backStackEntry ->
            val idOrder = backStackEntry.arguments?.getInt("idOrderInfo") ?: -1
            val selectedOrder = databaseHelper.getOrderById(idOrder)
            Log.d("123", "$selectedOrder")
            if (selectedOrder != null) {
                ManagerOrderInfoScreen (
                    state = ManagerOrderListState(selectedOrder = selectedOrder),
                    databaseHelper = databaseHelper,
                    onEditStatusClick = { order ->
                        navController.navigate("editOrderStatus/${order.id}")
                    }
                )
            } else {
                Text("Заказ не найден")
            }
        }

        composable(
            route = "editOrderStatus/{idOrder}",
            arguments = listOf(navArgument("idOrder") { type = NavType.IntType })
        ) { backStackEntry ->
            val idOrder = backStackEntry.arguments?.getInt("idOrder") ?: -1
            val order = databaseHelper.getOrderById(idOrder)

            if (order != null) {
                EditOrderStatusScreen(
                    order = order,
                    onSave = { newStatus ->
                        databaseHelper.updateOrderStatus(idOrder, newStatus)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                    databaseHelper = databaseHelper
                )
            } else {
                Text("Заказ не найден")
            }
        }
    }
}

