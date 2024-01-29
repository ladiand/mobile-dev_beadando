package hu.aut.android.kotlinstudentlist.data

import java.io.Serializable

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/*
Adatbázis táblát készti el.
Táblanév:studentitem.
Oszlopok:studentId, name,  age, failed (tantárgy bukás), address.
@PrimaryKey(autoGenerate = true): elsődleges kulcs, automatikusan generálva.
Ide szükséges a bővítés új adattal.
 */
@Entity(tableName = "studentitem")
data class StudentItem(@PrimaryKey(autoGenerate = true) var studentId: Long?,
                       @ColumnInfo(name = "name") var name: String,
                       @ColumnInfo(name = "age") var age: Int,
                       @ColumnInfo(name = "failed") var failed: Boolean,
                       @ColumnInfo(name = "address") var address: String
) : Serializable
