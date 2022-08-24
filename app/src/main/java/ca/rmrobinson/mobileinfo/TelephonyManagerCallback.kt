package ca.rmrobinson.mobileinfo

import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime

class TelephonyManagerCallback(
    private val lastUpdated: MutableLiveData<LocalDateTime>,
    private val networkState: MutableLiveData<MobileNetworkDetails>,
    private val phoneState: MutableLiveData<PhoneDetails>,
) : TelephonyCallback(), TelephonyCallback.DataConnectionStateListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.UserMobileDataStateListener {
    override fun onServiceStateChanged(state: ServiceState) {
        var oldNetworkState = networkState.value?.copy()

        oldNetworkState!!.serviceState = state
        oldNetworkState.isRoaming = state.roaming

        if (state.state == ServiceState.STATE_OUT_OF_SERVICE) {
            oldNetworkState.operator = ""
            oldNetworkState.operatorName = ""
            oldNetworkState.countryIso = ""
            oldNetworkState.isRoaming = false
        }

        networkState.postValue(oldNetworkState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onDataConnectionStateChanged(dataState: Int, netType: Int) {
        var oldNetworkState = networkState.value?.copy()

        oldNetworkState!!.dataNetworkType = netType
        oldNetworkState!!.dataState = dataState
        networkState.postValue(oldNetworkState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onUserMobileDataStateChanged(dataEnabled: Boolean) {
        var oldPhoneState = phoneState.value?.copy()

        oldPhoneState!!.isDataEnabled = dataEnabled
        phoneState.postValue(oldPhoneState)
        lastUpdated.postValue(LocalDateTime.now())

    }
}
