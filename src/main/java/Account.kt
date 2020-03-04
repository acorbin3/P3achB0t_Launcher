class Account {

    var id: String = ""
    var username: String = ""
    var password: String = ""
    var pin: String = ""
    var script: String = ""
    var minRuntimeSec: Int = 0 // Units: Seconds
    var maxRuntimeSec: Int = 0 // Units: Seconds
    var userBreaks: Boolean = false
    var minBreakTimeSec: Int = 0 // Units: Seconds
    var maxBreakTimeSec: Int = 0 // Units: Seconds
    var banned: Boolean = false
    var proxy: String = "none"  // SOCKS5;185.244.192.119:7670 or none
    var world: Int = 80
    var startAutomatically: Boolean = true

    override fun toString(): String {
        return "$id [user:$username, pass:$password, pin:$pin, script:$script, minRuntime:$minRuntimeSec, maxRuntime:$maxRuntimeSec," +
                "useBreaks:$userBreaks, minBreakTime:$minBreakTimeSec,maxBreakTime:$maxBreakTimeSec, banned:$banned, " +
                "proxy:$proxy, world:$world]"
    }
}