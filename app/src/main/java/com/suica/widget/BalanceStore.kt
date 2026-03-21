package com.suica.widget

import android.content.Context
import java.text.NumberFormat
import java.util.Locale

/**
 * SharedPreferences を使った残高の永続化
 */
object BalanceStore {

    private const val PREFS_NAME = "suica_widget_prefs"
    private const val KEY_BALANCE = "balance"
    private const val KEY_LAST_UPDATED = "last_updated"

    fun saveBalance(context: Context, balance: Int) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_BALANCE, balance)
            .putLong(KEY_LAST_UPDATED, System.currentTimeMillis())
            .apply()
    }

    fun getBalance(context: Context): Int? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (prefs.contains(KEY_BALANCE)) prefs.getInt(KEY_BALANCE, 0) else null
    }

    fun getLastUpdated(context: Context): Long {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getLong(KEY_LAST_UPDATED, 0)
    }

    fun formatBalance(balance: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale.JAPAN)
        return "¥${formatter.format(balance)}"
    }
}
