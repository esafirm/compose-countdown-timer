package nolambda.stream.countdowntimer.helper

import nolambda.stream.countdowntimer.TimerNotificationManager

class TestTimerNotificationManager : TimerNotificationManager {

    var isTimerUpNotificationShown = false

    override fun showTimerUpNotification() {
        isTimerUpNotificationShown = true
    }
}