package com.skladpro.android.data

class AppSession {
    var currentEmployeeId: String? = null
        private set

    fun start(employeeId: String) {
        currentEmployeeId = employeeId
    }

    fun clear() {
        currentEmployeeId = null
    }
}
