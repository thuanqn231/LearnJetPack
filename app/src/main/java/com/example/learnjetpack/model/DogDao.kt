package com.example.learnjetpack.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogBreed: DogBreed):List<Long>

    @Query("SELECT * FROM DogBreed")
    suspend fun getAllDogs():List<DogBreed>

    @Query("SELECT * FROM DogBreed where uuid = :dogID")
    suspend fun getDog(dogID :Int): DogBreed

    @Query("Delete from DogBreed")
    suspend fun  deleteAllDogs()


}