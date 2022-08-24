package ca.rmrobinson.mobileinfo.widget

import android.Manifest
import android.content.pm.PackageManager
import android.telephony.ServiceState
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import ca.rmrobinson.mobileinfo.carrierNameFromMCCMNC
import ca.rmrobinson.mobileinfo.R

class MobileNetworkWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
        Log.d("widget", "refreshing widget content")
        val telMgr =
            LocalContext.current.getSystemService(ComponentActivity.TELEPHONY_SERVICE) as TelephonyManager

        if (ContextCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            MobilePermissionsRequiredCard()
        } else {
            var serviceState = telMgr.serviceState?.state
            if (serviceState == null) {
                serviceState = ServiceState.STATE_OUT_OF_SERVICE
            }

            MobileInfoWidgetCard(
                state = MobileInfo(
                    operator = telMgr.networkOperator,
                    operatorName = telMgr.networkOperatorName,
                    dataState = telMgr.dataState,
                    networkType = telMgr.dataNetworkType,
                    radioState = serviceState,
                )
            )
        }
    }
}

@Composable
fun appWidgetBackgroundModifier() = GlanceModifier
    .padding(10.dp)
    .appWidgetBackground()
    .background(R.color.teal_200)
    .cornerRadius(32.dp)

@Composable
fun MobileInfoWidgetBase(
    modifier: GlanceModifier = GlanceModifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = appWidgetBackgroundModifier().then(
            GlanceModifier.clickable(
                actionStartActivity<ca.rmrobinson.mobileinfo.MainActivity>())).then(modifier),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Composable
fun MobilePermissionsRequiredCard() {
    MobileInfoWidgetBase {
        Text(
            text = "Enable permissions",
        )
    }
}

@Composable
fun MobileInfoWidgetCard(state: MobileInfo) {
    var operatorName = "-"
    var operator = "-"
    if (state.radioState == ServiceState.STATE_IN_SERVICE ||
        state.radioState == ServiceState.STATE_EMERGENCY_ONLY) {
        operatorName = carrierNameFromMCCMNC(state.operator)
        operator = state.operator

        if (operatorName.isEmpty()) {
            operatorName = state.operatorName
        }
    }

    MobileInfoWidgetBase {
        Row {
            Spacer(modifier = GlanceModifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier.wrapContentSize()
            ) {
                Text(
                    text = operator, style = TextStyle(
                        fontSize = 16.sp,
                        color = ColorProvider(Color.White),
                    )
                )
                Text(
                    text = "Operator", style = TextStyle(
                        fontSize = 10.sp,
                        color = ColorProvider(Color.Gray),
                    )
                )
            }
            Spacer(modifier = GlanceModifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier.wrapContentSize()
            ) {
                Text(
                    text = operatorName, style = TextStyle(
                        fontSize = 16.sp,
                        color = ColorProvider(Color.White),
                    )
                )
                Text(
                    text = "Name", style = TextStyle(
                        fontSize = 10.sp,
                        color = ColorProvider(Color.Gray),
                    )
                )
            }
            Spacer(modifier = GlanceModifier.width(10.dp))
        }
    }
}
