package ca.rmrobinson.mobileinfo.widget

import android.content.Context
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.util.Log
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TelephonyManagerCallback (private val context: Context) : TelephonyCallback(), TelephonyCallback.ServiceStateListener {
    override fun onServiceStateChanged(state: ServiceState) {
        Log.d("widget", "state is now " + state.state + " on carrier " + state.operatorNumeric + " with name " + state.operatorAlphaLong)

        MainScope().launch {
            MobileNetworkWidget().updateAll(context)
        }
    }
}