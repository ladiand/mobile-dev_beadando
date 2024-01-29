package hu.aut.android.kotlinstudentlist.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
/*
Itt az adatbázis műveletek találhatóak.
Új adattagkor (új StudentItem adattag), nem szükséges módosítani itt.
 */
@Dao
interface StudentItemDAO {

    //Az összes listázása
    @Query("SELECT * FROM studentitem")
    fun findAllItems(): List<StudentItem>

    //Egy elem beszúrása
    @Insert
    fun insertItem(item: StudentItem): Long
    //Egy törlése
    @Delete
    fun deleteItem(item: StudentItem)
    //Egy módosítása
    @Update
    fun updateItem(item: StudentItem)

}
