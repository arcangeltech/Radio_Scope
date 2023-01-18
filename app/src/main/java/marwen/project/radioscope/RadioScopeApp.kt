package marwen.project.radioscope

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RadioScopeApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}