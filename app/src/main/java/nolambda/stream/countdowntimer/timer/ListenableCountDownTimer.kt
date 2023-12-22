package nolambda.stream.countdowntimer.timer

/**
 * A listener for [ListenableCountDownTimer]
 */
interface ListenableCountDownTimer {
    fun start()
    fun pause()
    fun restart()

    fun addListener(listener: CountDownListener)
}
