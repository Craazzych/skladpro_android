package com.skladpro.android.data.remote

import com.skladpro.android.data.AppSession
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
    private val session: AppSession,
    private val baseUrl: String = ApiConfig.BASE_URL,
    private val client: HttpClient = createHttpClient()
) {
    suspend fun getItems(): List<InventoryItemDto> =
        client.get("$baseUrl/api/items") { authorize() }.bodyOrThrow()

    suspend fun createItem(request: InventoryItemRequest): InventoryItemDto =
        client.post("$baseUrl/api/items") {
            authorize()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    suspend fun updateItem(id: String, request: InventoryItemRequest): InventoryItemDto =
        client.put("$baseUrl/api/items/$id") {
            authorize()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    suspend fun deleteItem(id: String) {
        client.delete("$baseUrl/api/items/$id") { authorize() }.ensureSuccess()
    }

    suspend fun applyStockOperation(id: String, quantityDelta: Double): InventoryItemDto =
        client.post("$baseUrl/api/items/$id/operations") {
            authorize()
            contentType(ContentType.Application.Json)
            setBody(StockOperationRequest(quantityDelta))
        }.bodyOrThrow()

    suspend fun updateDelivery(
        id: String,
        expectedDeliveryDate: String?,
        expectedDeliveryQuantity: Double?
    ): InventoryItemDto =
        client.put("$baseUrl/api/items/$id/delivery") {
            authorize()
            contentType(ContentType.Application.Json)
            setBody(DeliveryRequest(expectedDeliveryDate, expectedDeliveryQuantity))
        }.bodyOrThrow()

    suspend fun getEmployees(): List<EmployeeDto> =
        client.get("$baseUrl/api/employees") { authorize() }.bodyOrThrow()

    suspend fun createEmployee(request: CreateEmployeeRequest): EmployeeDto =
        client.post("$baseUrl/api/employees") {
            authorize()
            contentType(ContentType.Application.Json)
            setBody(request)
        }.bodyOrThrow()

    suspend fun deleteEmployee(id: String) {
        client.delete("$baseUrl/api/employees/$id") {
            authorize()
        }.ensureSuccess()
    }

    suspend fun login(login: String, password: String): LoginResponse =
        client.post("$baseUrl/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(login, password))
        }.bodyOrThrow()

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

    private fun io.ktor.client.request.HttpRequestBuilder.authorize() {
        val token = session.token ?: error("Требуется повторный вход")
        header("Authorization", "Bearer $token")
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
