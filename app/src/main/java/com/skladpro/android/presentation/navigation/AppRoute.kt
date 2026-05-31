package com.skladpro.android.presentation.navigation

sealed class AppRoute(val path: String) {
    data object Login : AppRoute("login")
    data object Activation : AppRoute("activation")
    data object PasswordSetup : AppRoute("password_setup")
    data object InventoryList : AppRoute("inventory")
    data object AddInventoryItem : AppRoute("inventory/add")
    data object Employees : AppRoute("employees")
    data object AddEmployee : AppRoute("employees/add")
    data object EditInventoryItem : AppRoute("inventory/edit/{itemId}") {
        const val ITEM_ID_ARG = "itemId"

        fun createPath(itemId: String): String = "inventory/edit/$itemId"
    }

    data object InventoryDetails : AppRoute("inventory/{itemId}") {
        const val ITEM_ID_ARG = "itemId"

        fun createPath(itemId: String): String = "inventory/$itemId"
    }
}
