package ca.rmrobinson.mobileinfo

import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.FEATURE_TELEPHONY
import android.os.Bundle
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import ca.rmrobinson.mobileinfo.widget.MobileNetworkWidget
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pm = packageManager
        val telMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        if (!pm.hasSystemFeature(FEATURE_TELEPHONY)) {
            setContent {
                ErrorCard("Does not have cell modem")
            }
            return
        }

        permissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d("ui", "permission granted 1")

                    setContent {
                        MobileInfoCard(getDisplayState(telMgr))
                    }
                    MainScope().launch {
                        MobileNetworkWidget().updateAll(applicationContext)
                    }
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.d("ui", "permission denied")
                    setContent {
                        ErrorCard(error = "Access to mobile network necessary for functionality")
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        val telMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d("ui", "permission granted 2")

                setContent {
                    MobileInfoCard(getDisplayState(telMgr))
                }

                MainScope().launch {
                    MobileNetworkWidget().updateAll(applicationContext)
                }
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.READ_PHONE_STATE
            ) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                Log.e("ui", "show why needed")
                setContent {
                    PermissionPromptCard(this, permissionLauncher)
                }
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                permissionLauncher.launch(
                    Manifest.permission.READ_PHONE_STATE
                )
            }
        }
    }
}

data class DisplayState(
    val lastUpdated: LocalDateTime,
    val phoneState: PhoneState,
    val simState: SIMState,
    val networkState: MobileNetworkState
)

fun getDisplayState(tm: TelephonyManager): DisplayState {
    var phoneState: PhoneState
    var networkState: MobileNetworkState

    try {
        phoneState = PhoneState(
            deviceSoftwareVersion = tm.deviceSoftwareVersion,
            hasIccCard = tm.hasIccCard(),
            supportedModemCount = tm.supportedModemCount,
            activeModemCount = tm.activeModemCount,
            tac = tm.typeAllocationCode,
            isDataCapable = tm.isDataCapable,
            isDataConnectionAllowed = tm.isDataConnectionAllowed,
            isDataEnabled = tm.isDataEnabled,
            isDataRoamingEnabled = tm.isDataRoamingEnabled,
            isMultiSIMSupported = tm.isMultiSimSupported,
            isVoiceCapable = tm.isVoiceCapable,
            type = tm.phoneType,
            name = android.os.Build.MODEL,
            manufacturer = android.os.Build.MANUFACTURER,
        )
    } catch (se: SecurityException) {
        phoneState = PhoneState(
            deviceSoftwareVersion = "Permission required",
            hasIccCard = tm.hasIccCard(),
            supportedModemCount = tm.supportedModemCount,
            activeModemCount = tm.activeModemCount,
            tac = "Permission required",
            isDataCapable = false,
            isDataConnectionAllowed = false,
            isDataEnabled = false,
            isDataRoamingEnabled = false,
            isMultiSIMSupported = MULTISIM_NOT_SUPPORTED_BY_HARDWARE,
            isVoiceCapable = false,
            type = PHONE_TYPE_NONE,
            name = android.os.Build.MODEL,
            manufacturer = android.os.Build.MANUFACTURER,
        )
    }

    try {
        networkState = MobileNetworkState(
            voiceNetworkType = tm.voiceNetworkType,
            dataNetworkType = tm.dataNetworkType,
            isRoaming = tm.isNetworkRoaming,
            dataState = tm.dataState,
            equivalentHomePLMNs = tm.equivalentHomePlmns,
            forbiddenPLMNs = tm.forbiddenPlmns,
            groupIdLevel1 = tm.groupIdLevel1,
            countryIso = tm.networkCountryIso,
            operator = tm.networkOperator,
            operatorName = tm.networkOperatorName,
            serviceState = tm.serviceState,
            signalStrength = tm.signalStrength,
        )
    } catch (se: SecurityException) {
        networkState = MobileNetworkState(
            voiceNetworkType = TelephonyManager.NETWORK_TYPE_UNKNOWN,
            dataNetworkType = TelephonyManager.NETWORK_TYPE_UNKNOWN,
            isRoaming = tm.isNetworkRoaming,
            dataState = tm.dataState,
            equivalentHomePLMNs = emptyList(),
            forbiddenPLMNs = emptyArray(),
            groupIdLevel1 = "",
            countryIso = tm.networkCountryIso,
            operator = tm.networkOperator,
            operatorName = tm.networkOperatorName,
            serviceState = null,
            signalStrength = tm.signalStrength,
        )
    }

    return DisplayState(
        lastUpdated = LocalDateTime.now(),
        phoneState = phoneState,
        simState = SIMState(
            state = tm.simState,
            carrierId = tm.simCarrierId,
            carrierIdName = tm.simCarrierIdName.toString(),
            countryIso = tm.simCountryIso,
            operator = tm.simOperator,
            operatorName = tm.simOperatorName,
            specificCarrierId = tm.simSpecificCarrierId,
            specificCarrierIdName = tm.simSpecificCarrierIdName.toString(),
        ),
        networkState = networkState,
    )
}

@Composable
fun ErrorCard(error: String) {
    Text(text = error)
}

@Composable
fun PermissionPromptCard(
    activity: ComponentActivity,
    permLauncher: ActivityResultLauncher<String>
) {
    Column {
        Row {
            Text(text = "To display mobile network info 'Phone' permission required")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row {
            Button(
                onClick = {
                    activity.setContent {
                        ErrorCard(error = "Access to mobile network necessary for functionality")
                    }
                }
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = {
                    permLauncher.launch(
                        Manifest.permission.READ_PHONE_STATE
                    )
                }
            ) {
                Text(text = "Proceed")
            }
        }
    }
}

@Composable
fun RowEntry(label: String, content: String) {
    Row(
        modifier = Modifier
            .border(1.dp, Color.Gray)
            .padding(10.dp),
    ) {
        Text(
            text = label,
            modifier = Modifier.width(150.dp),
            color = Color.DarkGray,
            fontSize = 12.sp
        )
        Text(
            text = content,
            modifier = Modifier.fillMaxWidth(),
            color = Color.DarkGray,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PhoneInfoCard(state: PhoneState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(3.dp, Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(R.color.teal_200))
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_phone_android_24),
                contentDescription = "Phone"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Phone State")
        }
        RowEntry(label = "Device Name", content = state.name)
        RowEntry(label = "Manufacturer", content = state.manufacturer)
        RowEntry(label = "Supported Modem Count", content = state.supportedModemCount.toString())
        RowEntry(label = "Active Modem Count", content = state.activeModemCount.toString())
        RowEntry(label = "Has ICC Card?", content = state.hasIccCard.toString())
        RowEntry(label = "Device Type", content = phoneTypeToString(phoneType = state.type))
        RowEntry(label = "Type Allocation Code", content = state.tac.toString())
        RowEntry(label = "Software Version", content = state.deviceSoftwareVersion.toString())
        RowEntry(label = "Voice Capable?", content = state.isVoiceCapable.toString())
        RowEntry(label = "Data Capable?", content = state.isDataEnabled.toString())
        RowEntry(
            label = "Data Connection Allowed?",
            content = state.isDataConnectionAllowed.toString()
        )
        RowEntry(label = "Data Enabled?", content = state.isDataEnabled.toString())
        RowEntry(label = "Data Roaming Enabled?", content = state.isDataRoamingEnabled.toString())
        RowEntry(
            label = "Multi SIM Supported?",
            content = multiSimCapableToString(multiSimCapable = state.isMultiSIMSupported)
        )
    }
}

@Composable
fun SIMStateCard(state: SIMState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(3.dp, Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(R.color.teal_200))
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sim_card_2_line),
                contentDescription = "SIM card"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "SIM State")
        }
        RowEntry(label = "State", content = simStateToString(simState = state.state))
        RowEntry(label = "Carrier Country Code", content = state.countryIso)
        RowEntry(label = "Carrier ID", content = state.carrierId.toString())
        RowEntry(label = "Carrier ID Name", content = state.carrierIdName)
        RowEntry(label = "Specific Carrier ID", content = state.specificCarrierId.toString())
        RowEntry(label = "Specific Carrier ID Name", content = state.specificCarrierIdName)
        RowEntry(label = "Operator", content = state.operator)
        RowEntry(label = "Operator Name", content = state.operatorName)
    }
}

@Composable
fun NetworkInfoCard(state: MobileNetworkState) {
    var operatorBrand = carrierNameFromMCCMNC(state.operator)
    if (operatorBrand.isEmpty()) {
        operatorBrand = state.operatorName
    }

    var forbiddenPlmns = ""
    for (plmn in state.forbiddenPLMNs) {
        if (!forbiddenPlmns.isEmpty()) {
            forbiddenPlmns += ", "
        }
        forbiddenPlmns += plmn
    }

    var homePlmns = ""
    for (plmn in state.equivalentHomePLMNs) {
        if (!homePlmns.isEmpty()) {
            homePlmns += ", "
        }
        homePlmns += plmn
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(3.dp, Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(R.color.teal_200))
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_signal_cellular_alt_24),
                contentDescription = "Cellular Network"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Cellular Network State")
        }
        RowEntry(
            label = "Voice Network Type",
            content = networkTypeToString(networkType = state.voiceNetworkType)
        )
        RowEntry(
            label = "Data Network Type",
            content = networkTypeToString(networkType = state.dataNetworkType)
        )
        if (state.serviceState != null) {
            RowEntry(
                label = "Service State",
                content = radioStateToString(radioState = state.serviceState.state)
            )
            RowEntry(label = "Channel", content = state.serviceState.channelNumber.toString())
            //RowEntry(label = "Network Registration Info", content = state.serviceState.networkRegistrationInfoList.toString())
        } else {
            RowEntry(label = "Service State", content = stringResource(R.string.service_state_unavailable))
        }
        RowEntry(label = "Data State", content = dataStateToString(dataState = state.dataState))
        RowEntry(label = "Is Roaming?", content = state.isRoaming.toString())
        RowEntry(label = "Network Country Code", content = state.countryIso)
        RowEntry(label = "Operator", content = state.operator)
        RowEntry(label = "Operator Name", content = state.operatorName)
        RowEntry(label = "Operator Brand", content = operatorBrand)
        if (state.signalStrength != null) {
            RowEntry(label = "Signal Strength", content = state.signalStrength.level.toString())
        } else {
            RowEntry(label = "Signal Strength", content = stringResource(R.string.signal_strength_unknown))
        }
        RowEntry(label = "Group ID Level 1", content = state.groupIdLevel1.toString())
        RowEntry(label = "Home PLMNs", content = homePlmns)
        RowEntry(label = "Forbidden PLMNs", content = forbiddenPlmns)
    }
}

@Composable
fun MobileInfoCard(state: DisplayState) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Last Updated at " + state.lastUpdated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        PhoneInfoCard(state = state.phoneState)
        SIMStateCard(state = state.simState)
        NetworkInfoCard(state = state.networkState)
    }
}

/*
Network Info:
- IP address
- internet IP address
 */
