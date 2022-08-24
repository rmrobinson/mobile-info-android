package ca.rmrobinson.mobileinfo

/**
 * Contains fields relevant to a given SIM card
 *
 * Requires the device have:
 *  - FEATURE_TELEPHONY_SUBSCRIPTION
 */
data class SimDetails(
    var state: Int,
    val carrierId: Int,
    val carrierIdName: String,
    val countryIso: String,
    // Available when state = SIM_STATE_READY
    val operator: String,
    // Available when state = SIM_STATE_READY
    val operatorName: String,
    val specificCarrierId: Int,
    val specificCarrierIdName: String,
)
