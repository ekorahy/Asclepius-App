package com.dicoding.asclepius.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class StringResourceFormatter {
    data class DynamicString(
        val value: String
    ) : StringResourceFormatter()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : StringResourceFormatter()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(
                resId, *args
            )
        }
    }
}