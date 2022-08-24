package ca.rmrobinson.mobileinfo

import android.telephony.ServiceState
import android.telephony.SignalStrength

/**
 * Contains fields describing the current state of a connected mobile network
 *
 * Requires a device have:
 *  - FEATURE_TELEPHONY_DATA
 *  - FEATURE_TELEPHONY_RADIO_ACCESS
 *  - FEATURE_TELEPHONY_SUBSCRIPTION
 */
data class MobileNetworkDetails(
    // Requires READ_PHONE_STATE
    val voiceNetworkType: Int,
    var isRoaming: Boolean,
    // Requires READ_PHONE_STATE
    var dataNetworkType: Int,
    var dataState: Int,
    // Requires READ_PHONE_STATE
    val equivalentHomePLMNs: List<String>,
    // Requires READ_PHONE_STATE
    val forbiddenPLMNs: Array<String>,
    // Requires READ_PHONE_STATE
    val groupIdLevel1: String?,
    var countryIso: String,
    var operator: String,
    var operatorName: String,
    // Requires READ_PHONE_STATE
    var serviceState: ServiceState?,
    val signalStrength: SignalStrength?,
)
