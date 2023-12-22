package nolambda.stream.countdowntimer.timer

/**
 * A CountDownTimer that can be paused and resumed
 */
interface CountDownListener {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}