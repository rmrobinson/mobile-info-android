package ca.rmrobinson.mobileinfo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL

/**
 * Retrieve the IP network details of an IP network on a cellular transport
 * Will return as unavailable if the cellular transport isn't active.
 */
fun ConnectivityManager.getMobileIpNetworkDetails(): IpNetworkDetails {
    val net = this.activeNetwork
    val netCap = this.getNetworkCapabilities(net)
    val linkProp = this.getLinkProperties(net)

    if (netCap == null ||
        linkProp == null ||
        !netCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    ) {
        return IpNetworkDetails(
            availability = IpNetworkAvailability.UNAVAILABLE,
            ifaceName = "",
            linkAddresses = ArrayList(),
            dnsServers = ArrayList(),
            dnsSearchDomains = "",
            isMetered = false,
            isBlocked = false,
            signalStrength = 0,
            capabilities = intArrayOf(),
        )
    }

    var linkAddresses: ArrayList<String> = ArrayList()
    for (linkAddress in linkProp.linkAddresses) {
        linkAddresses.add(linkAddress.address.hostAddress.orEmpty())
    }

    var dnsServers: ArrayList<String> = ArrayList()
    for (dnsServer in linkProp.dnsServers) {
        dnsServers.add(dnsServer.hostAddress.orEmpty())
    }

    var isBlocked = netCap.hasCapability(NET_CAPABILITY_CAPTIVE_PORTAL)

    return IpNetworkDetails(
        availability = IpNetworkAvailability.AVAILABLE,
        ifaceName = linkProp.interfaceName,
        linkAddresses = linkAddresses,
        dnsServers = dnsServers,
        dnsSearchDomains = linkProp.domains,
        isMetered = false,
        isBlocked = isBlocked,
        signalStrength = netCap.signalStrength,
        capabilities = netCap.capabilities,
    )
}
