package dev.sumanth.paysquareroot.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Credentials")

class PreferencesManager(private val context: Context) {

    suspend fun saveCredentials(username: String, password: String) {
        context.dataStore.edit {
            it[PreferenceKeys.USERNAME] = username
            it[PreferenceKeys.PASSWORD] = password
        }
    }

    val getUsername: Flow<String?> = context.dataStore.data.map { it[PreferenceKeys.USERNAME] }
    val getPassword: Flow<String?> = context.dataStore.data.map { it[PreferenceKeys.PASSWORD] }
}

object PreferenceKeys {
    val USERNAME = stringPreferencesKey("username")
    val PASSWORD = stringPreferencesKey("password")
}