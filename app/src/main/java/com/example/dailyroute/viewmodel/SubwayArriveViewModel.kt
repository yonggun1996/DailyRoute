package com.example.dailyroute.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyroute.repo.SubwayArriveRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class SubwayArriveViewModel(private val repository: SubwayArriveRepo): ViewModel() {
    private val _data = MutableStateFlow<JSONObject?>(null)
    val data: StateFlow<JSONObject?> get() = _data

    fun fetchData(stationID: Int, statnNm: String) {
        viewModelScope.launch {
            _data.value = repository.getSubwayArriveData(stationID, statnNm)
        }
    }
}