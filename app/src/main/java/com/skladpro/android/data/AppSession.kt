package com.skladpro.android.data

class AppSession {
    var currentEmployeeId: String? = null
        private set

    var token: String? = null
        private set

    fun start(employeeId: String, token: String) {
        currentEmployeeId = employeeId
        this.token = token
    }

    fun clear() {
        currentEmployeeId = null
        token = null
    }
}
