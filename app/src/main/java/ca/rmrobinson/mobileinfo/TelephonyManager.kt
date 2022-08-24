package ca.rmrobinson.mobileinfo

import android.telephony.TelephonyManager

fun TelephonyManager.getSimDetails(): SimDetails {
    return SimDetails(
        state = this.simState,
        carrierId = this.simCarrierId,
        carrierIdName = this.simCarrierIdName.toString(),
        countryIso = this.simCountryIso,
        operator = this.simOperator,
        operatorName = this.simOperatorName,
        specificCarrierId = this.simSpecificCarrierId,
        specificCarrierIdName = this.simSpecificCarrierIdName.toString(),
    )
}

fun TelephonyManager.getPhoneDetails(permRequired: String): PhoneDetails {
    try {
        return PhoneDetails(
            deviceSoftwareVersion = this.deviceSoftwareVersion,
            hasIccCard = this.hasIccCard(),
            supportedModemCount = this.supportedModemCount,
            activeModemCount = this.activeModemCount,
            tac = this.typeAllocationCode,
            isDataCapable = this.isDataCapable,
            isDataConnectionAllowed = this.isDataConnectionAllowed,
            isDataEnabled = this.isDataEnabled,
            isDataRoamingEnabled = this.isDataRoamingEnabled,
            isMultiSIMSupported = this.isMultiSimSupported,
            isVoiceCapable = this.isVoiceCapable,
            type = this.phoneType,
            name = android.os.Build.MODEL,
            manufacturer = android.os.Build.MANUFACTURER,
        )
    } catch (se: SecurityException) {
        return PhoneDetails(
            deviceSoftwareVersion = permRequired,
            hasIccCard = this.hasIccCard(),
            supportedModemCount = this.supportedModemCount,
            activeModemCount = this.activeModemCount,
            tac = permRequired,
            isDataCapable = false,
            isDataConnectionAllowed = false,
            isDataEnabled = false,
            isDataRoamingEnabled = false,
            isMultiSIMSupported = TelephonyManager.MULTISIM_NOT_SUPPORTED_BY_HARDWARE,
            isVoiceCapable = false,
            type = TelephonyManager.PHONE_TYPE_NONE,
            name = android.os.Build.MODEL,
            manufacturer = android.os.Build.MANUFACTURER,
        )
    }
}

fun TelephonyManager.getMobileNetworkDetails(): MobileNetworkDetails {
    try {
        var forbiddenPlmns = emptyArray<String>()
        if (this.forbiddenPlmns != null) {
            forbiddenPlmns = this.forbiddenPlmns
        }

        return MobileNetworkDetails(
            voiceNetworkType = this.voiceNetworkType,
            dataNetworkType = this.dataNetworkType,
            isRoaming = this.isNetworkRoaming,
            dataState = this.dataState,
            equivalentHomePLMNs = this.equivalentHomePlmns,
            forbiddenPLMNs = forbiddenPlmns,
            groupIdLevel1 = this.groupIdLevel1,
            countryIso = this.networkCountryIso,
            operator = this.networkOperator,
            operatorName = this.networkOperatorName,
            serviceState = this.serviceState,
            signalStrength = this.signalStrength,
        )
    } catch (se: SecurityException) {
        return MobileNetworkDetails(
            voiceNetworkType = TelephonyManager.NETWORK_TYPE_UNKNOWN,
            dataNetworkType = TelephonyManager.NETWORK_TYPE_UNKNOWN,
            isRoaming = this.isNetworkRoaming,
            dataState = this.dataState,
            equivalentHomePLMNs = emptyList(),
            forbiddenPLMNs = emptyArray(),
            groupIdLevel1 = "",
            countryIso = this.networkCountryIso,
            operator = this.networkOperator,
            operatorName = this.networkOperatorName,
            serviceState = null,
            signalStrength = this.signalStrength,
        )
    }
}
