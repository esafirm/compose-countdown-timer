package nolambda.stream.countdowntimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * An interface to handle notification related to timer
 */
interface TimerNotificationManager {
    fun showTimerUpNotification()
}

/**
 * A [TimerNotificationManager] implementation that uses [NotificationManager]
 */
class AppTimerNotificationManager(
    private val context: Context
) : TimerNotificationManager {
    private val appContext get() = context.applicationContext

    private val notificationManager by lazy {
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun showTimerUpNotification() {
        val notificationChannelId = "timer_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Timer",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(appContext, notificationChannelId)
            .setContentTitle("Timer")
            .setContentText("Timer is up!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }
}
