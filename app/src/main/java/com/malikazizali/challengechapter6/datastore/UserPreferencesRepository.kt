package com.malikazizali.challengechapter6.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.malikazizali.protodatastore.UserProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class UserPreferencesRepository (private val context: Context){
    //create datastore proto
    private val Context.userPreferencesStore: DataStore<UserProto> by dataStore(
        fileName = "userData",
        serializer = UserPreferencesSerializer
    )

    //save datastore proto
    suspend fun saveData(namaLengkap : String, username : String, password : String, session : String) {
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setNamaLengkap(namaLengkap).build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setUsername(username).build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setPassword(password).build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setSession(session).build()
        }
    }

    //set session
    suspend fun saveSession(session : String) {
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setSession(session).build()
        }
    }

    //delete datastore proto
    suspend fun deleteData() {
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearNamaLengkap().build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearUsername().build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearPassword().build()
        }
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearSession().build()
        }
    }


    //read datastore proto
    val readData: Flow<UserProto> = context.userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("Log", "Error reading sort order preferences.", exception)
                emit(UserProto.getDefaultInstance())
            } else {
                throw exception
            }
        }


}