package com.example.learnjetpack.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnjetpack.model.DogBreed
import com.example.learnjetpack.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel:ViewModel(){
     val dogs = MutableLiveData<List<DogBreed>>()
     val loading= MutableLiveData<Boolean>()
     val dogsLoadError = MutableLiveData<Boolean>()
    private  val dogsService = DogsApiService()
    private  val disposable=CompositeDisposable()
    fun refresh(){
//        val dog1 = DogBreed("1","Pet","Pet",10,"Big Size","Pet","Pet")
//        val dog2 = DogBreed("2","Ngao Tay Tang","Ngao Tay Tang",10,"Big Size","Pet","Pet")
//        val dog3 = DogBreed("23","Cho Phu Quoc","Cho Phu Quoc",10,"Big Size","Pet","Pet")
//        val dogList = arrayListOf<DogBreed>(dog1,dog2,dog3)
//        dogs.value = dogList
//        loading.value = false
//        dogsLoadError.value = false
        fetchFromRemote()
    }
    private  fun  fetchFromRemote(){
        loading.value = true
//        disposable.add(
//            dogsService.getDogs()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){
//                    override fun onSuccess(valueDogs: List<DogBreed>?) {
//                        dogs.value = valueDogs
//                        loading.value = false
//                        dogsLoadError.value = false
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        loading.value= false
//                        dogsLoadError.value= true
//                        e?.printStackTrace()
//                     }
//
//                })
//        )
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(value: List<DogBreed>?) {
                        dogs.value = value
                        loading.value = false
                        dogsLoadError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loading.value = false
                        dogsLoadError.value = true
                        e?.printStackTrace()
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}