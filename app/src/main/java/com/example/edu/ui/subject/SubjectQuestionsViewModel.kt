package com.example.edu.ui.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONObject

class SubjectQuestionsViewModel : ViewModel() {
    private val _config = MutableLiveData<JSONObject>()
    val config: LiveData<JSONObject> = _config

    fun setConfig(json: JSONObject) {
        _config.value = json
    }
}