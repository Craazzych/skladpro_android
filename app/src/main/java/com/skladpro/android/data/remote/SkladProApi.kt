package com.skladpro.android.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class SkladProApi(
    private val baseUrl: String = ApiConfig.BASE_URL,
    private val client: HttpClient = createHttpClient()
) {
    suspend fun getItems(): List<InventoryItemDto> =
        client.get("$baseUrl/api/items").bodyOrThrow()

    suspend fun createItem(request: InventoryItemRequest): InventoryItemDto =
        client.post("$baseUrl/api/items") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    suspend fun updateItem(id: String, request: InventoryItemRequest): InventoryItemDto =
        client.put("$baseUrl/api/items/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    suspend fun deleteItem(id: String) {
        client.delete("$baseUrl/api/items/$id").ensureSuccess()
    }

    suspend fun applyStockOperation(id: String, quantityDelta: Double): InventoryItemDto =
        client.post("$baseUrl/api/items/$id/operations") {
            contentType(ContentType.Application.Json)
            setBody(StockOperationRequest(quantityDelta))
        }.bodyOrThrow()

    suspend fun updateDelivery(
        id: String,
        expectedDeliveryDate: String?,
        expectedDeliveryQuantity: Double?
    ): InventoryItemDto =
        client.put("$baseUrl/api/items/$id/delivery") {
            contentType(ContentType.Application.Json)
            setBody(DeliveryRequest(expectedDeliveryDate, expectedDeliveryQuantity))
        }.bodyOrThrow()

    suspend fun getEmployees(): List<EmployeeDto> =
        client.get("$baseUrl/api/employees").bodyOrThrow()

    suspend fun createEmployee(request: CreateEmployeeRequest): EmployeeDto =
        client.post("$baseUrl/api/employees") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    suspend fun deleteEmployee(id: String, actorId: String) {
        client.delete("$baseUrl/api/employees/$id") {
            header("X-Actor-Employee-Id", actorId)
        }.ensureSuccess()
    }

    suspend fun login(login: String, password: String): EmployeeDto =
        client.post("$baseUrl/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(login, password))
        }.bodyOrThrow<LoginResponse>().employee

    suspend fun activate(login: String, temporaryPassword: String, newPassword: String) {
        client.post("$baseUrl/api/auth/activate") {
            contentType(ContentType.Application.Json)
            setBody(ActivateEmployeeRequest(login, temporaryPassword, newPassword))
        }.ensureSuccess()
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
        ensureSuccess()
        return body()
    }

    private suspend fun HttpResponse.ensureSuccess() {
        if (status.isSuccess()) return
        val message = runCatching { body<ErrorResponse>().message }
            .getOrDefault("Ошибка сервера: ${status.value}")
        throw ApiException(message)
    }

    companion object {
        private fun createHttpClient(): HttpClient {
            return HttpClient(Android) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                        }
                    )
                }
            }
        }
    }
}

class ApiException(message: String) : IllegalStateException(message)
