package com.example.dailyroute.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyroute.repo.StationData
import com.example.dailyroute.repo.StationSelectData
import com.example.dailyroute.repo.SubwayArriveData
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
    private val _subwayArriveDataList = MutableStateFlow<List<SubwayArriveData>>(emptyList())
    val subwayArriveDataList: StateFlow<List<SubwayArriveData>> get() = _subwayArriveDataList

    suspend fun addSubwayList(list: List<StationSelectData>) {
        list.forEach { it ->
            val stationArriveData = fetchData(it)
            if (stationArriveData != null) {
                Log.d("DailyRoot", "stationArriveData: ${stationArriveData}")
                _subwayArriveDataList.value += stationArriveData
            }
        }
    }

    suspend fun fetchData(selectStationData: StationSelectData): SubwayArriveData? {
        val subwayArriveJsonData = repository.getSubwayArriveData(selectStationData.STATN_NM)

        var subwayArriveData: SubwayArriveData? = null
        val status = subwayArriveJsonData?.getJSONObject("errorMessage")
        if (status?.get("code").toString() == "INFO-000") {
            val realtimeArrivalList = subwayArriveJsonData?.getJSONArray("realtimeArrivalList")

            if (realtimeArrivalList != null) {
                subwayArriveData = filteringJSON(realtimeArrivalList, selectStationData.SUBWAY_ID.toString(), selectStationData.UPDN_LINE)
            }
        }

        return subwayArriveData
    }

    fun filteringJSON(realtimeArrivalList: JSONArray, subwayId: String, updnLine:String): SubwayArriveData? {
        val resultList = mutableListOf<JSONObject>()
        val subwayArriveData: SubwayArriveData? = null

        for (i in 0 until realtimeArrivalList.length()) {
            val obj = realtimeArrivalList.getJSONObject(i)
            val subwayIdStr = obj.getString("subwayId")
            val updnLineStr = obj.getString("updnLine")

            // 조건: subwayId가 "1007"이고 updnLine이 "하행"
            if (subwayIdStr == subwayId && updnLineStr == updnLine) {
                resultList.add(obj)
            }
        }

        if (resultList.isEmpty()) {
            return subwayArriveData
        } else {
            return SubwayArriveData(
                subwayName = resultList[0].getString("statnNm"),    // 전철역 이름
                terminus = resultList[0].getString("bstatnNm"),    // 행선지
                arrivalTime = resultList[0].getString("arvlMsg2"), // 도착 시간
                lineNum = resultList[0].getString("subwayId")      // 호선
            )
        }
    }

}