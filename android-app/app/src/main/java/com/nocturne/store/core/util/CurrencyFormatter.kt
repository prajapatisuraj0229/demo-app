package com.nocturne.store.core.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val format = NumberFormat.getCurrencyInstance(Locale.US)
    fun format(value: Double): String = format.format(value)
}
