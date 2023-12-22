package nolambda.stream.countdowntimer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

object AppBackgroundDetector {

    var isInBackground = false

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