package com.skladpro.android.data

import com.skladpro.android.data.remote.SkladProApi
import com.skladpro.android.data.repository.NetworkAuthRepository
import com.skladpro.android.data.repository.NetworkEmployeeRepository
import com.skladpro.android.data.repository.NetworkInventoryRepository

object AppContainer {
    val session = AppSession()
    private val api = SkladProApi(session)

    val authRepository = NetworkAuthRepository(api, session)
    val employeeRepository = NetworkEmployeeRepository(api)
    val inventoryRepository = NetworkInventoryRepository(api)
}
