package nolambda.stream.countdowntimer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nolambda.stream.countdowntimer.timer.AppListenableCountDownTimer
import nolambda.stream.countdowntimer.timer.CountDownListener
import nolambda.stream.countdowntimer.timer.ListenableCountDownTimer
import java.util.concurrent.TimeUnit

/**
 * A [ViewModel] for timer screen
 */
class TimerViewModel(
    private val timerNotificationManager: TimerNotificationManager,
    private val appBackgroundDetector: BackgroundDetector,
    private val timer: ListenableCountDownTimer = AppListenableCountDownTimer(INITIAL_TIME, TIMER_TICK)
) : ViewModel() {

    companion object {
        private val INITIAL_TIME = TimeUnit.MINUTES.toMillis(1)

        private const val TIMER_TICK = 10L // 10ms
        private const val ZERO_TIME_TEXT = "00:00.00"
    }

    private val initialTimeText = String.format(
        "%02d:%02d.%02d",
        TimeUnit.MILLISECONDS.toMinutes(INITIAL_TIME),
        TimeUnit.MILLISECONDS.toSeconds(INITIAL_TIME) % 60,
        TimeUnit.MILLISECONDS.toMillis(INITIAL_TIME) % 100
    )

    private var _time = MutableStateFlow(initialTimeText)
    val timeText: StateFlow<String> = _time

    private var _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState

    private var _indicatorProgress = MutableStateFlow(1F)
    val indicatorProgress: StateFlow<Float> = _indicatorProgress

    init {
        timer.addListener(object : CountDownListener {
            override fun onTick(millisUntilFinished: Long) {
                // Format the time to be displayed "00:00.00"
                val formattedTime = String.format(
                    "%02d:%02d.%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toMillis(millisUntilFinished) % 100
                )

                _time.value = formattedTime
                _indicatorProgress.value = millisUntilFinished.toFloat() / INITIAL_TIME
            }

            override fun onFinish() {
                stop(resetState = false)
                onTimerEnd()
            }
        })
    }

    override fun onCleared() {
        // Stop the timer on clear to avoid temporary
        // leak on the timer
        timer.restart()
    }

    fun startOrPause() {
        val nextValue = when (_timerState.value) {
            TimerState.RUNNING -> TimerState.PAUSED
            TimerState.PAUSED, TimerState.STOPPED -> TimerState.RUNNING
        }

        when (nextValue) {
            TimerState.RUNNING -> timer.start()
            TimerState.PAUSED -> timer.pause()
            TimerState.STOPPED -> {}
        }

        _timerState.value = nextValue
    }

    fun stop(resetState: Boolean = true) {
        timer.restart()
        _timerState.value = TimerState.STOPPED

        if (resetState) {
            _time.value = initialTimeText
            _indicatorProgress.value = 1F
        } else {
            // Make sure the time is 00:00.00
            _time.value = ZERO_TIME_TEXT
            _indicatorProgress.value = 0F
        }
    }

    /**
     * A callback when timer is ended
     */
    private fun onTimerEnd() {
        val isInBackground = appBackgroundDetector.isInBackground()
        if (isInBackground) {
            timerNotificationManager.showTimerUpNotification()
        }
    }
}

enum class TimerState {
    RUNNING, PAUSED, STOPPED
}
