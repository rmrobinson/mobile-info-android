package ca.rmrobinson.mobileinfo

/**
 * Contains information about the state of a given IP network
 */
data class IpNetworkDetails(
    var availability: IpNetworkAvailability,
    var ifaceName: String?,

    var linkAddresses: ArrayList<String>,
    var dnsServers: ArrayList<String>,
    var dnsSearchDomains: String?,

    val isMetered: Boolean,
    var isBlocked: Boolean,

    var signalStrength: Int,

    var capabilities: IntArray,
)
