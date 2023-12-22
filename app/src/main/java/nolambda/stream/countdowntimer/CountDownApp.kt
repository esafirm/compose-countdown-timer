package nolambda.stream.countdowntimer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

// App module. This placed as top level function to be accessible by Koin test
internal val appModule = module {
    single<TimerNotificationManager> { AppTimerNotificationManager(context = get()) }
    single<BackgroundDetector> { AppBackgroundDetector }
    viewModel { TimerViewModel(get(), get()) }
}

class CountDownApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppBackgroundDetector.startListening()

        startKoin {
            androidLogger()
            androidContext(this@CountDownApp)
            modules(appModule)
        }
    }
}