package com.example.dictionary

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Entity
data class RecordItem(
    @PrimaryKey
    val uid: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "date")
    val date: Long,
)

@Dao
interface RecordDao {
    @Query("SELECT * FROM recorditem")
    suspend fun getAll(): List<RecordItem>

    @Query("SELECT * FROM recorditem")
    fun getAllAsFlow(): Flow<List<RecordItem>>

    @Insert
    suspend fun insertAll(vararg diaryRecord: RecordItem)

    @Update
    suspend fun updateAll(vararg diaryRecord: RecordItem)

    @Query("SELECT * FROM recorditem " +
            "WHERE title LIKE :title LIMIT 1")
    suspend fun findByTitle(title: String): RecordItem?

    @Delete
    suspend fun delete(record: RecordItem)
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    entities = [
        RecordItem::class,
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}