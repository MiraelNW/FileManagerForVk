package com.miraeldev.filemanagerforvk.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FileListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: FileDbModel)

    @Query("SELECT * from FileList WHERE absolutePath=:path")
    suspend fun getFileFromDb(path: String): FileDbModel?
}