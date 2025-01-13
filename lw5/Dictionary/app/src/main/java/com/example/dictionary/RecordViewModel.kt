package com.example.dictionary

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class RecordViewModel : ViewModel() {
    private val recordDao = StorageApp.db.recordDao()

    val records: Flow<List<RecordItem>> = recordDao.getAllAsFlow()

    private val selectedDateMillis = MutableStateFlow<Long?>(null)

    val filteredRecords: Flow<List<RecordItem>> = combine(records, selectedDateMillis) { diaryRecords, dateMillis ->
        if (dateMillis != null) {
            val startOfDayMillis = Calendar.getInstance().apply {
                timeInMillis = dateMillis
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endOfDayMillis = Calendar.getInstance().apply {
                timeInMillis = dateMillis
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            diaryRecords.filter { it.date in startOfDayMillis..endOfDayMillis }
        } else {
            diaryRecords
        }
    }

    fun setSelectedDate(dateMillis: Long?) {
        selectedDateMillis.value = dateMillis
    }

    suspend fun getRecords(): List<RecordItem> {
        val deferred = viewModelScope.async {
            recordDao.getAll()
        }

        return deferred.await()
    }

    fun createRecord(title: String, content: String) {
        viewModelScope.launch {
            if (recordDao.findByTitle(title) != null) {
                return@launch
            }

            val randomUid = UUID.randomUUID().toString()
            val newDiaryRecord = RecordItem(
                randomUid,
                title,
                content,
                System.currentTimeMillis(),
            )

            recordDao.insertAll(newDiaryRecord)
        }
    }

    fun updateRecord(uid: String, title: String, content: String, date: Long) {
        viewModelScope.launch {
            val diaryRecord = RecordItem(
                uid,
                title,
                content,
                date,
            )
            recordDao.updateAll(diaryRecord)
        }
    }

    fun deleteDiaryRecord(diaryRecord: RecordItem) {
        viewModelScope.launch {
            recordDao.delete(diaryRecord)
        }
    }
}