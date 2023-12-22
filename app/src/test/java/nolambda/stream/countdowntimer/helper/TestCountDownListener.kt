package nolambda.stream.countdowntimer.helper

import nolambda.stream.countdowntimer.timer.CountDownListener
import nolambda.stream.countdowntimer.timer.ListenableCountDownTimer

class TestCountDownListener : ListenableCountDownTimer {

    var listener: CountDownListener? = null

    override fun start() {
    }

    override fun pause() {
    }

    override fun restart() {
    }

    override fun addListener(listener: CountDownListener) {
        this.listener = listener
    }
}
