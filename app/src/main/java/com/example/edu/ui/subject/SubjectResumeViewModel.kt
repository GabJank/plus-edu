package com.example.edu.ui.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class SubjectResumeViewModel : ViewModel() {
    private val _config = MutableLiveData<SubjectItem>()
    val config: LiveData<SubjectItem> = _config

    fun setConfig(json: SubjectItem) {
        _config.value = json
    }
}