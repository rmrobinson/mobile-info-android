package ca.rmrobinson.mobileinfo

import android.telephony.ServiceState
import android.telephony.TelephonyManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A helper to convert an MCC MNC pair into a presentable string. This can be used
 * if the network operator name may be obscured by the SIM.
 *
 * The contents for this function were sourced from https://en.wikipedia.org/wiki/Mobile_network_codes_in_ITU_region_3xx_(North_America)
 *
 * @param mccmnc The pair of Mobile Country Code (MCC) and Mobile Network Code (MNC) to resolve
 * @return The brand operating this MCCMNC pair; or an empty string if none was found
 */
fun carrierNameFromMCCMNC(mccmnc: String): String {
    val name = when (mccmnc) {
        "302130" -> "Xplornet"
        "302131" -> "Xplornet"
        "302220" -> "Telus Mobility"
        "302270" -> "EastLink"
        "302290" -> "Airtel Wireless"
        "302300" -> "ECOTEL"
        "302310" -> "ECOTEL"
        "302320" -> "Rogers Wireless"
        "302340" -> "Execulink"
        "302370" -> "Fido"
        "302380" -> "Keewaytinook Mobile"
        "302420" -> "ABC"
        "302480" -> "Qiniq"
        "302490" -> "Freedom Mobile"
        "302500" -> "Videotron"
        "302510" -> "Videotron"
        "302530" -> "Keewaytinook Mobile"
        "302590" -> "Quadro Mobility"
        "302610" -> "Bell Mobility"
        "302620" -> "ICE Wireless"
        "302650" -> "TBaytel"
        "302660" -> "MTS"
        "302680" -> "SaskTel"
        "302690" -> "Bell"
        "302710" -> "Globalstar"
        "302720" -> "Rogers Wireless"
        "302760" -> "Public Mobile"
        "302770" -> "TNW Wireless"
        "302780" -> "SaskTel"
        "302880" -> "Bell / Telus / SaskTel"
        "302940" -> "Wightman Mobility"
        "310004" -> "Verizon"
        "310005" -> "Verizon"
        "310006" -> "Verizon"
        "310012" -> "Verizon"
        "310020" -> "Union Wireless"
        "310030" -> "AT&T"
        "310032" -> "IT&E Wireless"
        "310034" -> "Airpeak"
        "310050" -> "GCI"
        "310053" -> "Virgin Mobile"
        "310054" -> "Alltel US"
        "310066" -> "U.S. Cellular"
        "310070" -> "AT&T"
        "310080" -> "AT&T"
        "310090" -> "AT&T"
        "310100" -> "Plateau Wireless"
        "310110" -> "IT&E Wireless"
        "310120" -> "T-Mobile"
        "310130" -> "Carolina West Wireless"
        "310140" -> "GTA Wireless"
        "310150" -> "AT&T"
        "310160" -> "T-Mobile"
        "310170" -> "AT&T"
        "310180" -> "West Central"
        "310190" -> "GCI"
        "310260" -> "T-Mobile"
        "310320" -> "Cellular One"
        "310340" -> "Limitless Mobile"
        "310360" -> "Pioneer Cellular"
        "310370" -> "Docomo"
        "310390" -> "Cellular One of East Texas"
        "310400" -> "IT&E Wireless"
        "310410" -> "AT&T"
        "310430" -> "GCI"
        "310440" -> "Numerex"
        "310450" -> "Viaero"
        "310460" -> "Conecto"
        "310480" -> "IT&E Wireless"
        "310490" -> "T-Mobile"
        "310500" -> "Alltel"
        "310540" -> "Phoenix"
        "310570" -> "Broadpoint"
        "310580" -> "Inland Cellular"
        "310600" -> "Cellcom"
        "310640" -> "Numerex"
        "310650" -> "Jasper"
        "310680" -> "AT&T"
        "310690" -> "Limitless Mobile"
        "310710" -> "ASTAC"
        "310740" -> "Viaero"
        "310750" -> "Appalachian Wireless"
        "310840" -> "telna Mobile"
        "310850" -> "Aeris"
        "310860" -> "Five Star Wireless"
        "310880" -> "DTC Wireless"
        "310900" -> "Mid-Rivers Wireless"
        "310920" -> "James Valley Wireless"
        "310930" -> "Copper Valley Wireless"
        "310950" -> "AT&T"
        "310960" -> "STRATA"
        "310970" -> "Globalstar"
        "310990" -> "Evolve Broadband"
        "311010" -> "Chariton Valley"
        "311012" -> "Verizon"
        "311020" -> "Chariton Valley"
        "311030" -> "Indigo Wireless"
        "311040" -> "Choice Wireless"
        "311050" -> "Thumb Cellular"
        "311060" -> "Space Data"
        "311070" -> "AT&T"
        "311080" -> "Pine Cellular"
        "311090" -> "AT&T"
        "311100" -> "Nex-Tech Wireless"
        "311120" -> "IT&E Wireless"
        "311140" -> "Bravado Wireless"
        "311150" -> "Wilkes Cellular"
        "311170" -> "Tampnet"
        "311210" -> "Telnyx"
        "311220" -> "U.S. Cellular"
        "311230" -> "C Spire"
        "311240" -> "Cordova Wireless"
        "311250" -> "IT&E Wireless"
        "311320" -> "Choice Wireless"
        "311330" -> "Bug Tussel Wireless"
        "311340" -> "Illinois Valley Cellular"
        "311350" -> "Nemont"
        "311370" -> "GCI Wireless"
        "311380" -> "New Dimension Wireless"
        "311410" -> "Chat Mobility"
        "311420" -> "NorthwestCell"
        "311440" -> "Bluegrass Cellular"
        "311450" -> "PTCI"
        "311470" -> "Viya"
        "311480" -> "Verizon"
        "311490" -> "T-Mobile"
        "311530" -> "NewCore"
        "311550" -> "Choice Wireless"
        "311560" -> "OTZ Cellular"
        "311580" -> "U.S. Cellular"
        "311600" -> "Limitless Mobile"
        "311640" -> "Rock Wireless"
        "311650" -> "United Wireless"
        "311660" -> "Metro"
        "311670" -> "Pine Belt Wireless"
        "311690" -> "TeleBEEPER"
        "311740" -> "Telalaska Cellular"
        "311780" -> "ASTCA"
        "311800" -> "Bluegrass Cellular"
        "311810" -> "Bluegrass Cellular"
        "311830" -> "Thumb Cellular"
        "311840" -> "Cellcom"
        "311850" -> "Cellcom"
        "311860" -> "STRATA"
        "311870" -> "Boost Mobile"
        "311882" -> "T-Mobile"
        "311900" -> "GigSky Mobile"
        "311950" -> "ETC"
        "311970" -> "Big River Broadband"
        "311990" -> "VTel Wireless"
        "312030" -> "Bravado Wireless"
        "312040" -> "Custer Telephone Co-op"
        "312080" -> "SyncSouth"
        "312120" -> "Appalachian Wireless"
        "312130" -> "Appalachian Wireless"
        "312150" -> "NorthwestCell"
        "312160" -> "Chat Mobility"
        "312170" -> "Chat Mobility"
        "312180" -> "Limitless Mobile"
        "312210" -> "Aspenta"
        "312220" -> "Chariton Valley"
        "312260" -> "NewCore"
        "312270" -> "Pioneer Cellular"
        "312280" -> "Pioneer Cellular"
        "312300" -> "telna Mobile"
        "312310" -> "Clear Stream Communications"
        "312320" -> "RTC Communications"
        "312330" -> "Nemont"
        "312350" -> "Triangle Communications"
        "312370" -> "Choice Wireless"
        "312380" -> "Copper Valley Wireless"
        "312390" -> "FTC Wireless"
        "312400" -> "Mid-Rivers Wireless"
        "312420" -> "Nex-Tech Wireless"
        "312430" -> "Silver Star Communications"
        "312470" -> "Carolina West Wireless"
        "312530" -> "T-Mobile"
        "312590" -> "NMU"
        "312620" -> "GlobeTouch"
        "312670" -> "FirstNet"
        "312720" -> "Southern LINC"
        "312730" -> "Triangle Communications"
        "312780" -> "Redzone Wireless"
        "312810" -> "BBCP"
        "312870" -> "GigSky Mobile"
        "312900" -> "ClearTalk"
        "313020" -> "CTC Wireless"
        "313040" -> "NNTC Wireless"
        "313060" -> "Country Wireless"
        "313080" -> "Speedwavz"
        "313090" -> "Vivint Wireless"
        "313100" -> "FirstNet"
        "313260" -> "Expeto Wireless"
        "313340" -> "Dish"
        "313460" -> "Mobi"
        "313510" -> "Claro"
        "313560" -> "Transit Wireless"
        "313860" -> "Nextlink"
        "313890" -> "TCOE"
        "313930" -> "Rock Wireless"
        else -> ""
    }

    return name
}

@Composable
fun multiSimCapableToString(multiSimCapable: Int): String {
    return when (multiSimCapable) {
        TelephonyManager.MULTISIM_ALLOWED -> stringResource(R.string.multi_sim_capable_yes)
        TelephonyManager.MULTISIM_NOT_SUPPORTED_BY_HARDWARE -> stringResource(R.string.multi_sim_capable_not_by_hardware)
        TelephonyManager.MULTISIM_NOT_SUPPORTED_BY_CARRIER -> stringResource(R.string.multi_sim_capable_not_by_carrier)
        else -> stringResource(R.string.multi_sim_capable_unknown)
    }
}

@Composable
fun phoneTypeToString(phoneType: Int): String {
    return when (phoneType) {
        TelephonyManager.PHONE_TYPE_GSM -> stringResource(R.string.phone_type_gsm)
        TelephonyManager.PHONE_TYPE_CDMA -> stringResource(R.string.phone_type_cdma)
        TelephonyManager.PHONE_TYPE_SIP -> stringResource(R.string.phone_type_sim)
        TelephonyManager.PHONE_TYPE_NONE -> stringResource(R.string.phone_type_none)
        else -> stringResource(R.string.phone_type_unknown)
    }
}

@Composable
fun simStateToString(simState: Int): String {
    return when (simState) {
        TelephonyManager.SIM_STATE_ABSENT -> stringResource(R.string.sim_state_absent)
        TelephonyManager.SIM_STATE_CARD_IO_ERROR -> stringResource(R.string.sim_state_card_io_error)
        TelephonyManager.SIM_STATE_CARD_RESTRICTED -> stringResource(R.string.sim_state_card_restricted)
        TelephonyManager.SIM_STATE_NETWORK_LOCKED -> stringResource(R.string.sim_state_network_locked)
        TelephonyManager.SIM_STATE_NOT_READY -> stringResource(R.string.sim_state_not_ready)
        TelephonyManager.SIM_STATE_PERM_DISABLED -> stringResource(R.string.sim_state_permission_denied)
        TelephonyManager.SIM_STATE_PIN_REQUIRED -> stringResource(R.string.sim_state_pin_required)
        TelephonyManager.SIM_STATE_PUK_REQUIRED -> stringResource(R.string.sim_state_puk_required)
        TelephonyManager.SIM_STATE_READY -> stringResource(R.string.sim_state_ready)
        else -> stringResource(R.string.sim_state_unknown)
    }
}

@Composable
fun radioStateToString(radioState: Int): String {
    return when (radioState) {
        ServiceState.STATE_IN_SERVICE -> stringResource(R.string.radio_state_in_service)
        ServiceState.STATE_POWER_OFF -> stringResource(R.string.radio_state_off)
        ServiceState.STATE_EMERGENCY_ONLY -> stringResource(R.string.radio_state_emergency)
        ServiceState.STATE_OUT_OF_SERVICE -> stringResource(R.string.radio_state_out_of_service)
        else -> stringResource(R.string.radio_state_unknown)
    }
}

@Composable
fun dataStateToString(dataState: Int): String {
    return when (dataState) {
        TelephonyManager.DATA_DISCONNECTED -> stringResource(R.string.data_disconnected)
        TelephonyManager.DATA_CONNECTING -> stringResource(R.string.data_connecting)
        TelephonyManager.DATA_CONNECTED -> stringResource(R.string.data_connected)
        TelephonyManager.DATA_SUSPENDED -> stringResource(R.string.data_suspended)
        TelephonyManager.DATA_DISCONNECTING -> stringResource(R.string.data_disconnecting)
        else -> stringResource(R.string.data_unknown)
    }
}

@Composable
fun networkTypeToString(networkType: Int): String {
    return when (networkType) {
        TelephonyManager.NETWORK_TYPE_1xRTT -> stringResource(R.string.network_1xrtt)
        TelephonyManager.NETWORK_TYPE_CDMA -> stringResource(R.string.network_cdma)
        TelephonyManager.NETWORK_TYPE_EDGE -> stringResource(R.string.network_edge)
        TelephonyManager.NETWORK_TYPE_EHRPD -> stringResource(R.string.network_ehrpd)
        TelephonyManager.NETWORK_TYPE_EVDO_0 -> stringResource(R.string.network_evdo0)
        TelephonyManager.NETWORK_TYPE_EVDO_A -> stringResource(R.string.network_evdoa)
        TelephonyManager.NETWORK_TYPE_EVDO_B -> stringResource(R.string.network_evdob)
        TelephonyManager.NETWORK_TYPE_GPRS -> stringResource(R.string.network_gprs)
        TelephonyManager.NETWORK_TYPE_GSM -> stringResource(R.string.network_gsm)
        TelephonyManager.NETWORK_TYPE_HSDPA -> stringResource(R.string.network_hsdpa)
        TelephonyManager.NETWORK_TYPE_HSPA -> stringResource(R.string.network_hspa)
        TelephonyManager.NETWORK_TYPE_HSPAP -> stringResource(R.string.network_hspap)
        TelephonyManager.NETWORK_TYPE_HSUPA -> stringResource(R.string.network_hsupa)
        TelephonyManager.NETWORK_TYPE_IDEN -> stringResource(R.string.network_iden)
        TelephonyManager.NETWORK_TYPE_IWLAN -> stringResource(R.string.network_iwlan)
        TelephonyManager.NETWORK_TYPE_LTE -> stringResource(R.string.network_lte)
        TelephonyManager.NETWORK_TYPE_NR -> stringResource(R.string.network_nr)
        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> stringResource(R.string.network_tdscdma)
        TelephonyManager.NETWORK_TYPE_UMTS -> stringResource(R.string.network_umts)
        else -> stringResource(R.string.network_unknown)
    }
}
