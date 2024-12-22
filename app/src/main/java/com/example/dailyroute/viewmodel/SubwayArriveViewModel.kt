package com.example.dailyroute.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyroute.repo.SubwayArriveRepo
import com.example.dailyroute.repo.SubwayData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class SubwayArriveViewModel(private val repository: SubwayArriveRepo): ViewModel() {
    private val _data = MutableStateFlow<JSONObject?>(null)
    val data: StateFlow<JSONObject?> get() = _data

    fun fetchData(stationID: Int, statnNm: String) {
        viewModelScope.launch {
            _data.value = repository.getSubwayArriveData(stationID, statnNm)
            Log.d("DailyRoot", "fetchDataResult: ${_data.value}")
        }
    }

    // jsonObject를 토대로 선택한 노선의 데이터만 추출
    fun getSubwayByIdArray(jsonArray: JSONArray, subwayId: String): List<SubwayData> {
        // Moshi를 사용하여 JSON을 List로 변환
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // List<SubwayData>로 변환할 JSON 어댑터 생성
        val listType = Types.newParameterizedType(List::class.java, SubwayData::class.java)
        val jsonAdapter = moshi.adapter<List<SubwayData>>(listType)

        // JSONArray를 List<SubwayData>로 변환
        val subwayList: List<SubwayData> = jsonAdapter.fromJson(jsonArray.toString()) ?: emptyList()

        // subwayId가 "1001"인 모든 항목을 찾아서 반환
        return subwayList.filter { it.subwayId == subwayId }
    }

    // 추출된 데이터를 바탕으로
    fun extractStationFromTrainLine(subwayList: List<SubwayData>): Map<String, String> {
        val result = mutableMapOf<String, String>()

        subwayList.forEach { subwayData ->
            val trainLineNm = subwayData.trainLineNm ?: return@forEach
            // 정규 표현식: 괄호와 그 안의 내용을 제거
            val regex = "\\(.*?\\)".toRegex()
            val stationName = trainLineNm.split("-").getOrNull(1)?.trim()?.replace(regex, "")

            if (stationName != null) {
                // updnLine을 key로 사용하고 stationName을 value로 추가
                result[subwayData.updnLine] = stationName
            }
        }

        return result
    }
}