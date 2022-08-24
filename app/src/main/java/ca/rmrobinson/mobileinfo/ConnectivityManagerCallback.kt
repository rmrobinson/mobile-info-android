package ca.rmrobinson.mobileinfo

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime

class ConnectivityManagerCallback(
    private val lastUpdated: MutableLiveData<LocalDateTime>,
    private val mobileIpNetworkDetails: MutableLiveData<IpNetworkDetails>,
) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        Log.d("connmgr", "onAvailable triggered")

        var oldState = mobileIpNetworkDetails.value?.copy()

        oldState!!.availability = IpNetworkAvailability.AVAILABLE
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        super.onBlockedStatusChanged(network, blocked)

        Log.d("connmgr", "onBlockedStatus triggered")

        var oldState = mobileIpNetworkDetails.value?.copy()

        oldState!!.isBlocked = blocked
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onUnavailable() {
        super.onUnavailable()

        Log.d("connmgr", "onUnavailable triggered")

        var oldState = mobileIpNetworkDetails.value?.copy()

        oldState!!.availability = IpNetworkAvailability.UNAVAILABLE
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)

        Log.d("connmgr", "onCapabilitiesChanged triggered")

        var oldState = mobileIpNetworkDetails.value?.copy()

        oldState!!.capabilities = networkCapabilities.capabilities
        oldState!!.signalStrength = networkCapabilities.signalStrength
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)

        Log.d("connmgr", "onLinkPropertiesChanged triggered")

        var oldState = mobileIpNetworkDetails.value?.copy()

        var newLinkAddresses: ArrayList<String> = ArrayList()
        for (linkAddress in linkProperties.linkAddresses) {
            newLinkAddresses.add(linkAddress.address.hostAddress.orEmpty())
        }

        var newDnsServers: ArrayList<String> = ArrayList()
        for (dnsServer in linkProperties.dnsServers) {
            newDnsServers.add(dnsServer.hostAddress.orEmpty())
        }

        oldState!!.linkAddresses = newLinkAddresses
        oldState!!.dnsServers = newDnsServers
        oldState!!.dnsSearchDomains = linkProperties.domains
        oldState!!.ifaceName = linkProperties.interfaceName
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)

        Log.d("connmgr", "onLosing changed")

        var oldState = mobileIpNetworkDetails.value?.copy()

        oldState!!.availability = IpNetworkAvailability.LOSING
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }

    override fun onLost(network: Network) {
        super.onLost(network)

        Log.d("connmgr", "onLost changed")

        var oldState = mobileIpNetworkDetails.value?.copy()

        oldState!!.availability = IpNetworkAvailability.LOST
        mobileIpNetworkDetails.postValue(oldState)
        lastUpdated.postValue(LocalDateTime.now())
    }
}
