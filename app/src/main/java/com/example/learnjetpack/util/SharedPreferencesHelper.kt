package com.example.learnjetpack.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import  androidx.core.content.edit
class SharedPreferencesHelper {
    companion object{
        private  const val PREF_TIME = "Pref_time"
        private  var pref:SharedPreferences ? = null
        private  var Lock = Any()
        @Volatile private  var instances: SharedPreferencesHelper?=null
        operator  fun  invoke(context: Context): SharedPreferencesHelper = instances?: synchronized(Lock){
            instances?: buildHelper(context).also{
                instances = it
            }
        }
        private fun  buildHelper(context: Context): SharedPreferencesHelper{
                pref = PreferenceManager.getDefaultSharedPreferences(context)
            return  SharedPreferencesHelper()
        }
    }
    fun saveUpdateTime(time:Long){
        pref?.edit(commit = true){
                putLong(PREF_TIME,time)
        }
    }
    fun getUpdateTime() = pref?.getLong(PREF_TIME,0)
    fun getCacheDuration() = pref?.getString("pref_cache_duration","")

}