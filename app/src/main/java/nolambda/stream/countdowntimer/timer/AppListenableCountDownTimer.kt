package nolambda.stream.countdowntimer.timer

import android.os.CountDownTimer

/**
 * A CountDownTimer that can be paused and resumed
 */
class AppListenableCountDownTimer(
    private val millisInFuture: Long,
    private val interval: Long
) : ListenableCountDownTimer {

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime: Long = 0
    private var isTimerPaused: Boolean = true

    private var listener: CountDownListener? = null

    init {
        this.remainingTime = millisInFuture
    }

    override fun addListener(listener: CountDownListener) {
        this.listener = listener
    }

    @Synchronized
    override fun start() {
        if (isTimerPaused) {
            countDownTimer = object : CountDownTimer(remainingTime, interval) {
                override fun onFinish() {
                    listener?.onFinish()
                    restart()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    listener?.onTick(millisUntilFinished)
                }

            }.apply {
                start()
            }
            isTimerPaused = false
        }
    }

    override fun pause() {
        if (!isTimerPaused) {
            countDownTimer.cancel()
        }
        isTimerPaused = true
    }

    override fun restart() {
        countDownTimer.cancel()
        remainingTime = millisInFuture
        isTimerPaused = true
    }
}