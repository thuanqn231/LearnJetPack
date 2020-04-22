package com.example.learnjetpack.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.learnjetpack.model.DogBreed
import com.example.learnjetpack.model.DogDao
import com.example.learnjetpack.model.DogDatabase
import com.example.learnjetpack.model.DogsApiService
import com.example.learnjetpack.util.NotificationsHelper
import com.example.learnjetpack.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

//class ListViewModel:ViewModel(){
class ListViewModel(application: Application) : BaseViewModel(application) {
    private  var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private  var prefHelper = SharedPreferencesHelper(getApplication())
    val dogs = MutableLiveData<List<DogBreed>>()
    val loading = MutableLiveData<Boolean>()
    val dogsLoadError = MutableLiveData<Boolean>()
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()
    fun refresh() {
//        val dog1 = DogBreed("1","Pet","Pet",10,"Big Size","Pet","Pet")
//        val dog2 = DogBreed("2","Ngao Tay Tang","Ngao Tay Tang",10,"Big Size","Pet","Pet")
//        val dog3 = DogBreed("23","Cho Phu Quoc","Cho Phu Quoc",10,"Big Size","Pet","Pet")
//        val dogList = arrayListOf<DogBreed>(dog1,dog2,dog3)
//        dogs.value = dogList
//        loading.value = false
//        dogsLoadError.value = false
        val updateTime: Long? = prefHelper.getUpdateTime()
//        checkCacheDuration()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime  < refreshTime  ){
            fetchFromDatabase()
        }else{
            fetchFromRemote()
        }

    }
    private  fun  checkCacheDuration(){
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt()?:5 * 60
            refreshTime = cachePreferenceInt.times( 1000 * 1000 * 1000L)
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }
    fun refreshBypassCache(){
        fetchFromRemote()
    }
    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val dao = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dao)
            Toast.makeText(getApplication(),"Dogs retrieved from database",Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchFromRemote() {
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
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
//                    override fun onSuccess(value: List<DogBreed>?) {
//                        dogs.value = value
//                        loading.value = false
//                        dogsLoadError.value = false
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        loading.value = false
//                        dogsLoadError.value = true
//                        e?.printStackTrace()
//                    }

                    override fun onSuccess(t: List<DogBreed>) {
                        Toast.makeText(getApplication(),"Dogs retrieved from Remote",Toast.LENGTH_SHORT).show()
                        storeDogsLocally(t)
                        NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        dogsLoadError.value = true
                        e?.printStackTrace()
                    }

                })
        )

    }

    private fun dogsRetrieved(list: List<DogBreed>) {
        dogs.value = list
        loading.value = false
        dogsLoadError.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }

            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}