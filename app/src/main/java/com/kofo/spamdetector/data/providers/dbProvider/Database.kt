package com.kofo.spamdetector.data.providers.dbProvider

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kofo.spamdetector.data.model.SmsMlResult


@androidx.room.Database(entities = [SmsMlResult::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun smsMlResultDao(): SmsMlResultDao?

    companion object {
        @Volatile
        var INSTANCE: Database? = null

        @JvmStatic
        fun getDatabase(context: Context?): Database? {
            if (INSTANCE == null) {
                synchronized(Database::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context!!, Database::class.java, "SmsMlResult.db"
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return INSTANCE
        }
    }
}