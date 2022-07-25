package ca.rmrobinson.mobileinfo.widget

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MobileNetworkWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MobileNetworkWidget()

    private lateinit var telCallback: TelephonyCallback

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        telCallback = TelephonyManagerCallback(context)

        val telMgr =
            context.getSystemService(ComponentActivity.TELEPHONY_SERVICE) as TelephonyManager

        telMgr.registerTelephonyCallback(Executors.newSingleThreadExecutor(), telCallback)
        Log.d("widget", "registered tel manager callback")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        val telMgr =
            context.getSystemService(ComponentActivity.TELEPHONY_SERVICE) as TelephonyManager

        if (this::telCallback.isInitialized) {
            telMgr.unregisterTelephonyCallback(telCallback)
        }
        Log.d("widget", "unregistered tel manager callback")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        MainScope().launch {
            MobileNetworkWidget().updateAll(context)
        }
    }
}

