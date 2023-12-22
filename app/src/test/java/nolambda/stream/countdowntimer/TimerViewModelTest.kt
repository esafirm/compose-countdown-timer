package nolambda.stream.countdowntimer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import nolambda.stream.countdowntimer.helper.MainCoroutineRule
import nolambda.stream.countdowntimer.helper.TestBackgroundDetector
import nolambda.stream.countdowntimer.helper.TestCountDownListener
import nolambda.stream.countdowntimer.helper.TestTimerNotificationManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TimerViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    private lateinit var timerNotificationManager: TestTimerNotificationManager
    private lateinit var appBackgroundDetector: TestBackgroundDetector
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var timer: TestCountDownListener

    @Before
    fun setup() {
        timerNotificationManager = TestTimerNotificationManager()
        appBackgroundDetector = TestBackgroundDetector()
        timer = TestCountDownListener()
        timerViewModel = TimerViewModel(timerNotificationManager, appBackgroundDetector, timer)
    }

    @Test
    fun `when timer is started, state should be RUNNING`() = runTest {
        timerViewModel.startOrPause()

        assertEquals(TimerState.RUNNING, timerViewModel.timerState.value)
    }

    @Test
    fun `when timer is paused, state should be PAUSED`() = runTest {
        timerViewModel.startOrPause() // Start
        timerViewModel.startOrPause() // Pause

        assertEquals(TimerState.PAUSED, timerViewModel.timerState.value)
    }

    @Test
    fun `when timer is stopped, state should be STOPPED and time should be reset`() = runTest {
        timerViewModel.startOrPause() // Start
        timerViewModel.stop() // Stop

        assertEquals(TimerState.STOPPED, timerViewModel.timerState.value)
        assertEquals("01:00.00", timerViewModel.timeText.value)
    }

    @Test
    fun `when timer ends, notification should be shown if app is in background`() = runTest {
        appBackgroundDetector.isInBackgroundValue = true

        // Simulate timer is finished
        timer.listener?.onFinish()

        // Verify that showTimerUpNotification was called
        assert(timerNotificationManager.isTimerUpNotificationShown)
    }

    @Test
    fun `when timer ends, notification should not be shown if app is in foreground`() = runTest {
        appBackgroundDetector.isInBackgroundValue = false

        // Simulate timer is finished
        timer.listener?.onFinish()

        // Verify that showTimerUpNotification was not called
        assert(!timerNotificationManager.isTimerUpNotificationShown)
    }
}
