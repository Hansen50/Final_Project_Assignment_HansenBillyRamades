package com.example.final_project_assignment_hansenbillyramades.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object DataStoreConstant {
    val IS_LOGIN = booleanPreferencesKey("IS_LOGIN")
    val IS_ONBOARDED = booleanPreferencesKey("IS_ONBOARDED")
}

class PreferenceDataStore private constructor(private val dataStore: DataStore<Preferences>) {


    suspend fun setOnboardedStatus(onboarded: Boolean) {
        dataStore.edit { preferences -> preferences[DataStoreConstant.IS_ONBOARDED] = onboarded }
    }

    suspend fun getUserOnboarded(): Boolean {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[DataStoreConstant.IS_ONBOARDED] ?: false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferenceDataStore? = null

        fun getInstance(dataStore: DataStore<Preferences>): PreferenceDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferenceDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}