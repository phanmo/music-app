package com.fpoly.pro226.music_app.utilities.components

import android.app.Application
import com.fpoly.pro226.music_app.utilities.components.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MusicApplication : Application() {
    /**
     * @property  [externalScope]  use to launch CoroutineScope with purpose as a global
     * instead of using GlobalScope
     * */
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    val appContainer = AppContainer(externalScope)

}