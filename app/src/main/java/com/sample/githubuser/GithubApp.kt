package com.sample.githubuser

import android.app.Application
import com.sample.core.business.data.cache.PrefDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class GithubApp : Application() {

    var isListView = true

    companion object {
        lateinit var instance: GithubApp
    }

    init {
        GlobalScope.launch {
            isListView = PrefDataStore(this@GithubApp).getUiMode()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}
