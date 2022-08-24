package ca.rmrobinson.mobileinfo

import android.content.pm.PackageManager
import android.content.pm.PackageManager.FEATURE_TELEPHONY
import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.MutableLiveData
import ca.rmrobinson.mobileinfo.widget.MobileNetworkWidget
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private var lastUpdated: MutableLiveData<LocalDateTime> = MutableLiveData()

    private var currMobileNetworkDetails: MutableLiveData<MobileNetworkDetails> = MutableLiveData()

    private var currMobileIpNetworkDetails: MutableLiveData<IpNetworkDetails> = MutableLiveData()

    private var currPhoneDetails: MutableLiveData<PhoneDetails> = MutableLiveData()

    private var currSimDetails: MutableLiveData<SimDetails> = MutableLiveData()

    private var telephonyCallback: TelephonyManagerCallback =
        TelephonyManagerCallback(lastUpdated, currMobileNetworkDetails, currPhoneDetails)

    private var connectivityCallback: ConnectivityManagerCallback =
        ConnectivityManagerCallback(lastUpdated, currMobileIpNetworkDetails)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pm = packageManager
        val telMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (!pm.hasSystemFeature(FEATURE_TELEPHONY)) {
            setContent {
                ErrorCard(getString(R.string.no_cell_modem_error))
            }
            return
        }

        currMobileNetworkDetails.value = telMgr.getMobileNetworkDetails()
        currPhoneDetails.value = telMgr.getPhoneDetails(getString(R.string.perm_required_label))
        currSimDetails.value = telMgr.getSimDetails()

        currMobileIpNetworkDetails.value = connMgr.getMobileIpNetworkDetails()
        lastUpdated.value = LocalDateTime.now()

        val permissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d("ui", "permission granted 1")

                    refresh()

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
                        ErrorCard(error = stringResource(R.string.phone_permission_missing_error))
                    }
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d("ui", "permission granted 2")

                refresh()

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
                    Manifest.permission.READ_PHONE_STATE,
                )
            }
        }

        telMgr.registerTelephonyCallback(Executors.newCachedThreadPool(), telephonyCallback)

        val mobileNetworkRequestBuilder = NetworkRequest.Builder()
        mobileNetworkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        connMgr.registerNetworkCallback(mobileNetworkRequestBuilder.build(), connectivityCallback)

        setContent {
            val lastUpdatedTime = lastUpdated.observeAsState()
            val phoneState = currPhoneDetails.observeAsState()
            val simState = currSimDetails.observeAsState()
            val mobileNetworkState = currMobileNetworkDetails.observeAsState()
            val mobileIpNetworkState = currMobileIpNetworkDetails.observeAsState()

            MobileInfoCard(
                lastUpdated = lastUpdatedTime,
                phoneState = phoneState,
                simDetails = simState,
                mobileNetworkDetails = mobileNetworkState,
                mobileIpNetworkDetails = mobileIpNetworkState,
                onRefresh = { refresh() }
            )
        }
    }

    override fun onResume() {
        super.onResume()

        refresh()

        MainScope().launch {
            MobileNetworkWidget().updateAll(applicationContext)
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        val telMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telMgr.unregisterTelephonyCallback(telephonyCallback)

        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr.unregisterNetworkCallback(connectivityCallback)
    }

    private fun refresh() {
        val telMgr = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        // Can use .value since this happens on main thread
        currPhoneDetails.value = telMgr.getPhoneDetails(getString(R.string.perm_required_label))
        currSimDetails.value = telMgr.getSimDetails()
        currMobileNetworkDetails.value = telMgr.getMobileNetworkDetails()
        currMobileIpNetworkDetails.value = connMgr.getMobileIpNetworkDetails()
        lastUpdated.value = LocalDateTime.now()
    }
}
