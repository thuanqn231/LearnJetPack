package com.example.learnjetpack.model

import com.google.gson.annotations.SerializedName

data class DogBreed(
    
    @SerializedName("id")
    val breedID:String?,
    @SerializedName("name")
                    val dogBreed: String?,
    @SerializedName("bred_for")
                    val bredFor:String?,
    @SerializedName("life_span")
                    val lifeSpan:String?,
    @SerializedName("breed_group")
                    val breedGroup:String?,
    @SerializedName("temperament")
                    val temperament:String?,
    @SerializedName("url")
                    val urlImage:String?
)