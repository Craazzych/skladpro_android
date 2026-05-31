package com.skladpro.android.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemDto(
    val id: String,
    val name: String,
    val sku: String,
    val category: String,
    val unit: String,
    val quantity: Double,
    val minQuantity: Double,
    val department: String,
    val shelf: String,
    val cell: String,
    val expectedDeliveryDate: String? = null,
    val expectedDeliveryQuantity: Double? = null
)

@Serializable
data class InventoryItemRequest(
    val name: String,
    val sku: String,
    val category: String,
    val unit: String,
    val quantity: Double,
    val minQuantity: Double,
    val department: String,
    val shelf: String,
    val cell: String,
    val expectedDeliveryDate: String? = null,
    val expectedDeliveryQuantity: Double? = null
)

@Serializable
data class StockOperationRequest(val quantityDelta: Double)

@Serializable
data class DeliveryRequest(
    val expectedDeliveryDate: String? = null,
    val expectedDeliveryQuantity: Double? = null
)

@Serializable
data class EmployeeDto(
    val id: String,
    val fullName: String,
    val login: String,
    val role: String,
    val status: String,
    val temporaryPassword: String? = null
)

@Serializable
data class CreateEmployeeRequest(
    val fullName: String,
    val login: String,
    val role: String
)

@Serializable
data class LoginRequest(val login: String, val password: String)

@Serializable
data class LoginResponse(
    val employee: EmployeeDto,
    val token: String
)

@Serializable
data class ActivateEmployeeRequest(
    val login: String,
    val temporaryPassword: String,
    val newPassword: String
)

@Serializable
data class ErrorResponse(val message: String)
