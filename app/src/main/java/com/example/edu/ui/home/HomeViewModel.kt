package com.example.edu.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.io.File

class HomeViewModel : ViewModel() {
    private val _config = MutableLiveData<JSONObject>()
    val config: LiveData<JSONObject> = _config

    fun setConfig(json: JSONObject) {
        Log.d("EDU", "$json")
        _config.value = json
    }
}