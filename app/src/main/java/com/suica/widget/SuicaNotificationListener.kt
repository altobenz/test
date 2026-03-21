package com.suica.widget

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/**
 * モバイルSuicaの通知から残高を自動取得する NotificationListenerService
 *
 * モバイルSuicaアプリ (jp.co.jreast.mobilesuica) が決済時に送る通知から
 * 残高情報を抽出してウィジェットを更新する。
 */
class SuicaNotificationListener : NotificationListenerService() {

    companion object {
        // モバイルSuicaのパッケージ名
        private const val SUICA_PACKAGE = "jp.co.jreast.mobilesuica"

        // 残高パターン: "残高 1,234円" or "残高：1234円" or "残高1,234円" など
        private val BALANCE_PATTERN = Regex("""残高[：:\s]*([0-9,]+)\s*円""")

        // 代替パターン: "¥1,234" 形式
        private val YEN_PATTERN = Regex("""[¥￥]\s*([0-9,]+)""")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != SUICA_PACKAGE) return

        val notification = sbn.notification
        val extras = notification.extras

        // 通知テキストから残高を探す
        val textFields = listOfNotNull(
            extras.getCharSequence("android.text")?.toString(),
            extras.getCharSequence("android.title")?.toString(),
            extras.getCharSequence("android.bigText")?.toString(),
            extras.getCharSequence("android.subText")?.toString()
        )

        val allText = textFields.joinToString(" ")
        val balance = extractBalance(allText) ?: return

        // 残高を保存してウィジェットを更新
        BalanceStore.saveBalance(this, balance)
        SuicaWidgetProvider.updateAllWidgets(this)
    }

    private fun extractBalance(text: String): Int? {
        // まず "残高" パターンで試す
        BALANCE_PATTERN.find(text)?.let { match ->
            return parseAmount(match.groupValues[1])
        }

        // 次に "¥" パターンで試す
        YEN_PATTERN.find(text)?.let { match ->
            return parseAmount(match.groupValues[1])
        }

        return null
    }

    private fun parseAmount(amountStr: String): Int? {
        return amountStr.replace(",", "").toIntOrNull()
    }
}
