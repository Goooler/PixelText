package vip.mystery0.pixel.text.ui.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vip.mystery0.pixel.text.domain.model.DataInitializationProgress
import vip.mystery0.pixel.text.domain.model.InitializationPhase

@Composable
internal fun DataInitializationProgressContent(
    phase: InitializationPhase,
    progress: DataInitializationProgress?,
) {
    // 阶段切换时不展示上一个阶段的处理数量和预计时间。
    val currentProgress = progress?.takeIf { it.phase == phase }
    val waiting = currentProgress?.isWaiting == true
    val step = when (phase) {
        InitializationPhase.MIRROR -> 1
        InitializationPhase.MMS_TEXT -> 2
        InitializationPhase.SPAM -> 3
        InitializationPhase.VERIFICATION, InitializationPhase.COMPLETE -> 4
    }
    val description = when (phase) {
        InitializationPhase.MIRROR -> "正在导入短信和彩信"
        InitializationPhase.MMS_TEXT -> "正在解析彩信文本"
        InitializationPhase.SPAM -> "正在识别历史骚扰短信"
        InitializationPhase.VERIFICATION -> "正在建立验证码索引"
        InitializationPhase.COMPLETE -> "正在完成数据准备"
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "首次准备会处理全部历史消息，数量较多时可能需要几分钟。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "步骤 $step/4 · $description",
            style = MaterialTheme.typography.titleSmall,
        )
        val total = currentProgress?.total
        val completed = currentProgress?.completed?.coerceAtLeast(0) ?: 0L
        if (total != null && total > 0) {
            val fraction = (completed.toDouble() / total).coerceIn(0.0, 1.0).toFloat()
            Text(
                text = "已处理 $completed / $total 条 · ${(fraction * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
            )
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            if (currentProgress != null) {
                Text(
                    text = if (total == 0L) "当前阶段无需处理消息" else "已处理 $completed 条",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (!waiting) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
        currentProgress?.let {
            Text(
                text = "当前阶段已耗时 ${formatInitializationDuration(it.elapsedMillis)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = when {
                waiting && phase == InitializationPhase.VERIFICATION ->
                    "等待继续；已完成阶段会保留，验证码索引将重新扫描"
                waiting -> "等待继续，已完成的处理进度会保留"
                phase == InitializationPhase.COMPLETE -> "即将完成，请稍候…"
                currentProgress?.estimatedRemainingMillis != null ->
                    "当前阶段预计还需约 ${formatInitializationDuration(currentProgress.estimatedRemainingMillis)}（不含后续阶段）"
                else -> "正在估算当前阶段用时"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun formatInitializationDuration(millis: Long): String {
    val seconds = (millis.coerceAtLeast(0) / 1_000)
    return when {
        seconds < 1 -> "不到 1 秒"
        seconds < 60 -> "$seconds 秒"
        seconds % 60 == 0L -> "${seconds / 60} 分钟"
        else -> "${seconds / 60} 分 ${seconds % 60} 秒"
    }
}
