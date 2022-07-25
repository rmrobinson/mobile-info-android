package ca.rmrobinson.mobileinfo.widget

data class MobileInfo (
    val operator: String,
    val operatorName: String,
    val dataState: Int,
    val networkType: Int,
    val radioState: Int,
)
