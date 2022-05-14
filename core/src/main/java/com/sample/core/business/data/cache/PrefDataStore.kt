package com.sample.core.business.data.cache

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.sample.core.di.qualifier.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PrefDataStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.createDataStore(name = STORE_NAME)

    /*val uiModeFlow: Flow<UiMode> = dataStore.data
        .catch {
            android.util.Log.d("preff", "exception")
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            android.util.Log.d("preff", "preference value=${preference[IS_LIST_MODE]}")
            when (preference[IS_LIST_MODE] ?: false) {
                true -> UiMode.LIST
                false -> UiMode.GRID
            }
        }
*/
    suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[IS_LIST_MODE] = when (uiMode) {
                UiMode.LIST -> true
                UiMode.GRID -> false
            }
        }
    }

    suspend fun getUiMode(): Boolean {
        return dataStore.data.first()[IS_LIST_MODE] ?: false
    }

    companion object {
        private const val STORE_NAME = "pref_data_store"
        val IS_LIST_MODE = preferencesKey<Boolean>("ui_mode")
    }

}

enum class UiMode {
    LIST, GRID
}
