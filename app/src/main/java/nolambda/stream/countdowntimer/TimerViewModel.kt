package nolambda.stream.countdowntimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * A [ViewModel] for timer screen
 */
class TimerViewModel(
    private val timerNotificationManager: TimerNotificationManager,
    private val appBackgroundDetector: BackgroundDetector
) : ViewModel() {

    companion object {
        private val INITIAL_TIME = TimeUnit.MINUTES.toMillis(1)

        private const val TIMER_TICK = 10L // 100ms
    }

    private var _time = MutableStateFlow(INITIAL_TIME)
    val time: StateFlow<Long> = _time

    private var _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState

    private var _indicatorProgress = MutableStateFlow(1F)
    val indicatorProgress: StateFlow<Float> = _indicatorProgress

    private var timerJob: Job? = null

    fun startOrPause() {
        _timerState.value = when (_timerState.value) {
            TimerState.RUNNING -> TimerState.PAUSED
            TimerState.PAUSED, TimerState.STOPPED -> TimerState.RUNNING
        }
        if (_timerState.value == TimerState.RUNNING) {
            startTimer()
        }
    }

    fun stop(resetState: Boolean = true) {
        viewModelScope.launch {
            timerJob?.cancel()
            _timerState.value = TimerState.STOPPED

            if (resetState) {
                _time.value = INITIAL_TIME
                _indicatorProgress.value = 1F
            }
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_timerState.value == TimerState.RUNNING && _time.value > 0L) {
                delay(TIMER_TICK)
                _time.value -= TIMER_TICK
                _indicatorProgress.value = _time.value.toFloat() / INITIAL_TIME
            }
            if (_time.value <= 0L) {
                stop(resetState = false)
                onTimerEnd()
            }
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
