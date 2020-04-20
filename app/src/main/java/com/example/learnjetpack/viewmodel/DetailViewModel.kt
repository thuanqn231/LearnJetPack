package com.example.learnjetpack.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnjetpack.model.DogBreed
import com.example.learnjetpack.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application):BaseViewModel(application) {
     var dogs = MutableLiveData<DogBreed>()
    fun fetch(id: Int){
        launch {
           val dog = DogDatabase(getApplication()).dogDao().getDog(id)
            dogs.value = dog
        }

    }
}