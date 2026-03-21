package com.suica.widget

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * セットアップ画面
 * - 通知アクセス権限の付与
 * - 手動での残高入力
 */
class SetupActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var balanceInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(48, 48, 48, 48)
        }

        // タイトル
        layout.addView(TextView(this).apply {
            text = "Suica残高ウィジェット"
            textSize = 20f
            gravity = Gravity.CENTER
            layoutParams = linearParams().apply {
                bottomMargin = 32
            }
        })

        // 通知アクセス状態
        statusText = TextView(this).apply {
            textSize = 14f
            gravity = Gravity.CENTER
            layoutParams = linearParams().apply {
                bottomMargin = 16
            }
        }
        layout.addView(statusText)

        // 通知アクセス設定ボタン
        layout.addView(Button(this).apply {
            text = "通知アクセスを許可"
            layoutParams = linearParams().apply {
                bottomMargin = 32
            }
            setOnClickListener { openNotificationSettings() }
        })

        // 区切り線の代わりにテキスト
        layout.addView(TextView(this).apply {
            text = "── または手動で入力 ──"
            textSize = 12f
            gravity = Gravity.CENTER
            layoutParams = linearParams().apply {
                topMargin = 16
                bottomMargin = 16
            }
        })

        // 手動入力
        balanceInput = EditText(this).apply {
            hint = "残高 (例: 3500)"
            inputType = InputType.TYPE_CLASS_NUMBER
            gravity = Gravity.CENTER
            layoutParams = linearParams().apply {
                bottomMargin = 16
            }
        }
        layout.addView(balanceInput)

        layout.addView(Button(this).apply {
            text = "残高を更新"
            setOnClickListener { saveManualBalance() }
        })

        // 現在の残高を表示
        val currentBalance = BalanceStore.getBalance(this)
        if (currentBalance != null) {
            balanceInput.setText(currentBalance.toString())
        }

        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        updatePermissionStatus()
    }

    private fun updatePermissionStatus() {
        val enabled = isNotificationListenerEnabled()
        statusText.text = if (enabled) {
            "✓ 通知アクセス: 許可済み\nSuica利用時に自動更新されます"
        } else {
            "✗ 通知アクセス: 未許可\n自動更新には通知アクセスが必要です"
        }
    }

    private fun isNotificationListenerEnabled(): Boolean {
        val cn = ComponentName(this, SuicaNotificationListener::class.java)
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(cn.flattenToString()) == true
    }

    private fun openNotificationSettings() {
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    private fun saveManualBalance() {
        val text = balanceInput.text.toString().trim()
        val balance = text.toIntOrNull()
        if (balance == null || balance < 0) {
            Toast.makeText(this, "正しい金額を入力してください", Toast.LENGTH_SHORT).show()
            return
        }

        BalanceStore.saveBalance(this, balance)
        SuicaWidgetProvider.updateAllWidgets(this)
        Toast.makeText(this, "残高を更新しました: ${BalanceStore.formatBalance(balance)}", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun linearParams() = LinearLayout.LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )
}
