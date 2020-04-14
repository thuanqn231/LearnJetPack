package com.example.learnjetpack.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnjetpack.model.DogBreed

class DetailViewModel:ViewModel() {
     var dogs = MutableLiveData<DogBreed>()
    fun fetch(){
        val dog = DogBreed("1","Pet","Pet","10","Big Size","Pet","Pet")
        dogs.value = dog
    }
}