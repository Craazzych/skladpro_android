package com.skladpro.android.data

import com.skladpro.android.data.remote.SkladProApi
import com.skladpro.android.data.repository.NetworkAuthRepository
import com.skladpro.android.data.repository.NetworkEmployeeRepository
import com.skladpro.android.data.repository.NetworkInventoryRepository

object AppContainer {
    private val api = SkladProApi()
    val session = AppSession()

    val authRepository = NetworkAuthRepository(api)
    val employeeRepository = NetworkEmployeeRepository(api, session)
    val inventoryRepository = NetworkInventoryRepository(api)
}
