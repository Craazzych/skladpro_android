package com.skladpro.android.presentation.inventory

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeliveryDateValidatorTest {
    @Test
    fun `blank delivery date is allowed`() {
        assertNull(validateDeliveryDate(""))
    }

    @Test
    fun `invalid calendar date is rejected`() {
        assertEquals(
            "Дата должна существовать и быть не раньше сегодняшней",
            validateDeliveryDate("12.18.2022")
        )
    }

    @Test
    fun `past delivery date is rejected`() {
        assertEquals(
            "Дата поставки не может быть раньше сегодняшней",
            validateDeliveryDate("01.01.2020")
        )
    }

    @Test
    fun `future delivery date is allowed`() {
        val futureDate = LocalDate
            .now()
            .plusDays(1)
            .format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))

        assertNull(validateDeliveryDate(futureDate))
    }
}
