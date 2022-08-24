package ca.rmrobinson.mobileinfo

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            Text(text = stringResource(R.string.phone_permission_reason))
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row {
            Button(
                onClick = {
                    activity.setContent {
                        ErrorCard(error = stringResource(R.string.phone_permission_missing_error))
                    }
                }
            ) {
                Text(text = stringResource(R.string.phone_permission_cancel_label))
            }
            Button(
                onClick = {
                    permLauncher.launch(
                        Manifest.permission.READ_PHONE_STATE
                    )
                }
            ) {
                Text(text = stringResource(R.string.phone_permission_proceed_label))
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
fun PhoneInfoCard(state: PhoneDetails) {
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
                contentDescription = stringResource(R.string.phone_section_description)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(R.string.phone_section_header))
        }
        RowEntry(label = stringResource(R.string.phone_device_name_label), content = state.name)
        RowEntry(label = stringResource(R.string.phone_manufacturer_label), content = state.manufacturer)
        RowEntry(label = stringResource(R.string.phone_supported_modem_count_label),
            content = state.supportedModemCount.toString())
        RowEntry(label = stringResource(R.string.phone_active_modem_count_label),
            content = state.activeModemCount.toString())
        RowEntry(label = stringResource(R.string.phone_has_icc_card_label),
            content = boolToString(v = state.hasIccCard))
        RowEntry(label = stringResource(R.string.phone_device_type_label),
            content = phoneTypeToString(phoneType = state.type))
        RowEntry(label = stringResource(R.string.phone_tac_label),
            content = state.tac.toString())
        RowEntry(label = stringResource(R.string.phone_sw_version_label),
            content = state.deviceSoftwareVersion.toString())
        RowEntry(label = stringResource(R.string.phone_voice_capable_label),
            content = boolToString(v = state.isVoiceCapable))
        RowEntry(label = stringResource(R.string.phone_data_capable_label),
            content = boolToString(v = state.isDataEnabled))
        RowEntry(
            label = stringResource(R.string.phone_data_connection_allowed_label),
            content = boolToString(v = state.isDataConnectionAllowed)
        )
        RowEntry(label = stringResource(R.string.phone_data_enabled_label),
            content = boolToString(v = state.isDataEnabled))
        RowEntry(
            label = stringResource(R.string.phone_data_roaming_enabled_lable),
            content = boolToString(v = state.isDataRoamingEnabled)
        )
        RowEntry(
            label = stringResource(R.string.phone_multi_sim_supported_label),
            content = multiSimCapableToString(multiSimCapable = state.isMultiSIMSupported)
        )
    }
}

@Composable
fun SimStateCard(state: SimDetails) {
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
                contentDescription = stringResource(R.string.sim_state_header_description)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(R.string.sim_state_header))
        }
        RowEntry(label = stringResource(R.string.sim_state_state_label),
            content = simStateToString(simState = state.state))
        RowEntry(label = stringResource(R.string.sim_state_carrier_country_code_label),
            content = state.countryIso.uppercase())
        RowEntry(label = stringResource(R.string.sim_state_carrier_id),
            content = state.carrierId.toString())
        RowEntry(label = stringResource(R.string.sim_state_carrier_id_name_label),
            content = state.carrierIdName)
        RowEntry(label = stringResource(R.string.sim_state_specific_carrier_id_label),
            content = state.specificCarrierId.toString())
        RowEntry(label = stringResource(R.string.sim_state_specific_carrier_id_name_label),
            content = state.specificCarrierIdName)
        RowEntry(label = stringResource(R.string.sim_state_operator_label), content = state.operator)
        RowEntry(label = stringResource(R.string.sim_state_operator_name_label),
            content = state.operatorName)
    }
}

@Composable
fun IpNetworkCard(state: IpNetworkDetails, networkHeader: String) {
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
                painter = painterResource(id = R.drawable.ic_baseline_settings_ethernet_24),
                contentDescription = stringResource(R.string.ip_network_section_description)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = networkHeader)
        }
        RowEntry(
            label = stringResource(R.string.ip_network_availability_label),
            content = networkAvailabilityToString(availability = state.availability)
        )

        if (state.availability == IpNetworkAvailability.AVAILABLE) {
            RowEntry(label = stringResource(R.string.ip_network_interface_name_label), content = state.ifaceName!!)
            for (linkAddress in state.linkAddresses) {
                RowEntry(label = stringResource(R.string.ip_network_link_address_label), content = linkAddress)
            }
            for (dnsAddress in state.dnsServers) {
                RowEntry(label = stringResource(R.string.ip_network_dns_addresses_label), content = dnsAddress)
            }
            RowEntry(label = stringResource(R.string.ip_network_dns_search_domains_label),
                content = state.dnsSearchDomains.orEmpty())
            RowEntry(label = stringResource(R.string.ip_network_blocked_label),
                content = boolToString(v = state.isBlocked))
            RowEntry(label = stringResource(R.string.ip_network_metered_label),
                content = boolToString(v = state.isMetered))
            RowEntry(
                label = stringResource(R.string.ip_network_capabilities_label),
                content = netCapabilitiesToString(capabilities = state.capabilities)
            )
            RowEntry(label = stringResource(R.string.ip_network_signal_strength_label),
                content = state.signalStrength.toString())
        } else {
            RowEntry(
                label = stringResource(R.string.ip_network_details_label),
                content = stringResource(R.string.ip_network_not_available_reason)
            )
        }
    }
}

@Composable
fun MobileNetworkInfoCard(state: MobileNetworkDetails) {
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
                contentDescription = stringResource(R.string.mobile_network_header_description)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(R.string.mobile_network_header))
        }
        RowEntry(
            label = stringResource(R.string.mobile_network_voice_network_type_label),
            content = networkTypeToString(networkType = state.voiceNetworkType)
        )
        RowEntry(
            label = stringResource(R.string.mobile_network_data_network_type_label),
            content = networkTypeToString(networkType = state.dataNetworkType)
        )
        if (state.serviceState != null) {
            RowEntry(
                label = "Service State",
                content = radioStateToString(radioState = state.serviceState!!.state)
            )
            RowEntry(label = "Channel", content = state.serviceState!!.channelNumber.toString())
        } else {
            RowEntry(
                label = "Service State",
                content = stringResource(R.string.service_state_unavailable)
            )
        }
        RowEntry(label = stringResource(R.string.mobile_network_data_state_label),
            content = dataStateToString(dataState = state.dataState))
        RowEntry(label = stringResource(R.string.mobile_network_is_roaming_label),
            content = boolToString(v = state.isRoaming))
        RowEntry(label = stringResource(R.string.mobile_network_network_country_code_label),
            content = state.countryIso.uppercase())
        RowEntry(label = stringResource(R.string.mobile_network_operator_label), content = state.operator)
        RowEntry(label = stringResource(R.string.mobile_network_operator_name_label), content = state.operatorName)
        RowEntry(label = stringResource(R.string.mobile_network_operator_brand_label), content = operatorBrand)
        if (state.signalStrength != null) {
            RowEntry(label = stringResource(R.string.mobile_network_signal_strength_label),
                content = state.signalStrength.level.toString())
        } else {
            RowEntry(
                label = stringResource(R.string.mobile_network_signal_strength_label),
                content = stringResource(R.string.signal_strength_unknown)
            )
        }
        RowEntry(label = stringResource(R.string.mobile_network_group_id_label),
            content = state.groupIdLevel1.toString())
        RowEntry(label = stringResource(R.string.mobile_network_home_plmns_label), content = homePlmns)
        RowEntry(label = stringResource(R.string.mobile_network_forbidden_plmns_label), content = forbiddenPlmns)
    }
}

@Composable
fun MobileInfoCard(
    lastUpdated: State<LocalDateTime?>,
    mobileNetworkDetails: State<MobileNetworkDetails?>,
    mobileIpNetworkDetails: State<IpNetworkDetails?>,
    phoneState: State<PhoneDetails?>,
    simDetails: State<SimDetails?>,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.last_updated_label))
                Text(
                    text = lastUpdated.value!!
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Button(onClick = onRefresh) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_refresh_24),
                    contentDescription = stringResource(R.string.refresh_label)
                )
            }
        }
        MobileNetworkInfoCard(state = mobileNetworkDetails.value!!)
        IpNetworkCard(state = mobileIpNetworkDetails.value!!,
            networkHeader = stringResource(R.string.mobile_ip_network_header))
        SimStateCard(state = simDetails.value!!)
        PhoneInfoCard(state = phoneState.value!!)
    }

}
