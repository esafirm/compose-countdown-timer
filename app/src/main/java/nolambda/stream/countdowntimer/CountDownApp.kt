package nolambda.stream.countdowntimer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CountDownApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppBackgroundDetector.startListening()

        // Setup Service Locator
        val appModule = module {
            single<TimerNotificationManager> { AppTimerNotificationManager(context = get()) }
            single<BackgroundDetector> { AppBackgroundDetector }
            viewModel { TimerViewModel(get(), get()) }
        }

        startKoin {
            androidLogger()
            androidContext(this@CountDownApp)
            modules(appModule)
        }
    }
}