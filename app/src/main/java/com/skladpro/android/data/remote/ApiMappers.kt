package com.skladpro.android.data.remote

import com.skladpro.android.domain.model.EmployeeProfile
import com.skladpro.android.domain.model.EmployeeStatus
import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.domain.model.UserRole

fun InventoryItemDto.toDomain(): InventoryItem {
    return InventoryItem(
        id = id,
        name = name,
        sku = sku,
        category = category,
        unit = unit,
        quantity = quantity,
        minQuantity = minQuantity,
        department = department,
        shelf = shelf,
        cell = cell,
        expectedDeliveryDate = expectedDeliveryDate,
        expectedDeliveryQuantity = expectedDeliveryQuantity
    )
}

fun InventoryItem.toRequest(): InventoryItemRequest {
    return InventoryItemRequest(
        name = name,
        sku = sku,
        category = category,
        unit = unit,
        quantity = quantity,
        minQuantity = minQuantity,
        department = department,
        shelf = shelf,
        cell = cell,
        expectedDeliveryDate = expectedDeliveryDate,
        expectedDeliveryQuantity = expectedDeliveryQuantity
    )
}

fun EmployeeDto.toDomain(): EmployeeProfile {
    return EmployeeProfile(
        id = id,
        fullName = fullName,
        login = login,
        role = if (role == "admin") UserRole.Admin else UserRole.Worker,
        status = if (status == "active") EmployeeStatus.Active else EmployeeStatus.PendingActivation,
        temporaryPassword = temporaryPassword
    )
}
