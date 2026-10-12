package vip.mystery0.pixel.text.domain.model

/** 只表示当前阶段的真实处理进度；预计时间不包含后续阶段。 */
data class DataInitializationProgress(
    val phase: InitializationPhase,
    val completed: Long,
    val total: Long?,
    val elapsedMillis: Long,
    val estimatedRemainingMillis: Long?,
    val isWaiting: Boolean,
)
