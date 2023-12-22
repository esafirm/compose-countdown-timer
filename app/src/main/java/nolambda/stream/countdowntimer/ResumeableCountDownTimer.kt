package nolambda.stream.countdowntimer

import android.os.CountDownTimer

/**
 * A CountDownTimer that can be paused and resumed
 */
abstract class ResumeableCountDownTimer(
    private val millisInFuture: Long,
    private val interval: Long
) {

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime: Long = 0
    private var isTimerPaused: Boolean = true

    init {
        this.remainingTime = millisInFuture
    }

    @Synchronized
    fun start() {
        if (isTimerPaused) {
            countDownTimer = object : CountDownTimer(remainingTime, interval) {
                override fun onFinish() {
                    onTimerFinish()
                    restart()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    onTimerTick(millisUntilFinished)
                }

            }.apply {
                start()
            }
            isTimerPaused = false
        }
    }

    fun pause() {
        if (!isTimerPaused) {
            countDownTimer.cancel()
        }
        isTimerPaused = true
    }

    fun restart() {
        countDownTimer.cancel()
        remainingTime = millisInFuture
        isTimerPaused = true
    }

    abstract fun onTimerTick(millisUntilFinished: Long)
    abstract fun onTimerFinish()

}