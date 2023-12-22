package nolambda.stream.countdowntimer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * This is a simple interface to detect if the app is in background or not.
 */
interface BackgroundDetector {
    fun isInBackground(): Boolean
}

/**
 * This is a simple implementation of [BackgroundDetector] using [ProcessLifecycleOwner].
 */
object AppBackgroundDetector : BackgroundDetector {

    private var isInBackground = false

    override fun isInBackground(): Boolean = isInBackground

    fun startListening() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_STOP) {
                    isInBackground = true
                } else if (event == Lifecycle.Event.ON_START) {
                    isInBackground = false
                }
            }
        })
    }
}