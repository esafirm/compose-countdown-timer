package nolambda.stream.countdowntimer.helper

import nolambda.stream.countdowntimer.BackgroundDetector

class TestBackgroundDetector : BackgroundDetector {
    override fun isInBackground(): Boolean = isInBackgroundValue
    var isInBackgroundValue = false
}
