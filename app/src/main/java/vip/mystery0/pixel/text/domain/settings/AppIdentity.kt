package vip.mystery0.pixel.text.domain.settings

enum class AppDisplayName(val storageValue: String, val label: String) {
    CHISU_FULL("chisu_full", "原点尺素"),
    SMS_FULL("sms_full", "原点短信"),
    JIANXUN_FULL("jianxun_full", "原点简讯"),
    PIXEL_TEXT("pixel_text", "Pixel Text"),
    CHISU("chisu", "尺素"),
    SMS("sms", "短信"),
}

enum class AppIconStyle(val storageValue: String, val label: String) {
    VIOLET("violet", "紫罗兰"),
    BLUE("blue", "晴空蓝"),
    GREEN("green", "青松绿"),
    APRICOT("apricot", "暖杏橙"),
    CLASSIC("classic", "经典"),
}

data class AppIdentity(
    val displayName: AppDisplayName = AppDisplayName.CHISU_FULL,
    val iconStyle: AppIconStyle = AppIconStyle.VIOLET,
)
