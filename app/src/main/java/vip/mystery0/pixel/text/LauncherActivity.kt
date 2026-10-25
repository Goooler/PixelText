package vip.mystery0.pixel.text

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/** 桌面别名只负责转发，避免切换别名时系统移除正在显示的主界面。 */
class LauncherActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            Intent(this, MainActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP,
            ),
        )
        finish()
    }
}
