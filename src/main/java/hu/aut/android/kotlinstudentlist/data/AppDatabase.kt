package hu.aut.android.kotlinstudentlist.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
/*
Elkészíti az adatbázist, a StudentItem alapján lesz a tábla
 */
@Database(entities = arrayOf(StudentItem::class), version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personItemDao(): StudentItemDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "person.db")
                        .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}