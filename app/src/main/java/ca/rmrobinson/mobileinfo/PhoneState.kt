package ca.rmrobinson.mobileinfo

/**
 * Contains fields relevant to the state of a given handset
 *
 * Requires the device have:
 *  - FEATURE_TELEPHONY_GSM
 *  - FEATURE_TELEPHONY_RADIO_ACCESS
 */
data class PhoneState (
    val deviceSoftwareVersion: String?,
    val hasIccCard: Boolean,
    val supportedModemCount: Int,
    val activeModemCount: Int,
    val tac: String?,
    val isDataCapable: Boolean,
    val isDataConnectionAllowed: Boolean,
    val isDataEnabled: Boolean,
    val isDataRoamingEnabled: Boolean,
    val isMultiSIMSupported: Int,
    val isVoiceCapable: Boolean,
    val type: Int,
    val name: String,
    val manufacturer: String,
)