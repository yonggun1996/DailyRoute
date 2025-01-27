@file:Suppress("PLUGIN_IS_NOT_ENABLED")

package com.example.dailyroute.repo

import android.util.Log
import com.example.dailyroute.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.serializer.MoshiSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class StationData (
    var STATN_ID: Int,          // 전철역 id
    var SUBWAY_ID: Int,         // 노선 id
    var STATN_NM: String,       // 전철역 명
    var LINE_NM: String,        // 호선
    var UP_STATN_NM: String?,   // 상행역
    var REGION_CODE: String,    // 지역구분
    var DOWN_STATN_NM: String?  // 하행역
)

// json필터링을 위한 클래스
data class StationSelectData (
    var STATN_NM: String,       // 선택한 역명
    var LINE_NM: String,        // 호선
    var UPDN_LINE: String       // 상,하행 / 내,외선
)

// device_station_selection DAO
@Serializable
data class DeviceStationSelection(
//    @SerialName("IDX")
    val IDX: Long? = null, // IDX는 자동 생성되므로 null로 초기화
//    @SerialName("STATN_ID")
    val STATN_ID: Int,
//    @SerialName("DEVICE_ID")
    val DEVICE_ID: String,
//    @SerialName("STATN_NM")
    val STATN_NM: String = "", // 기본값 설정
//    @SerialName("LINE_NM")
    val LINE_NM: String? = "", // NULL 허용
//    @SerialName("UPDN_LINE")
    val UPDN_LINE: String = "", // 기본값 설정
//    @SerialName("SUBWAY_ID")
    val SUBWAY_ID: Int? = null // NULL 허용
)

/*
 * MVVM에서 Model역할
 * 서버와 통신하는 역할로 자원들을 응답받아 값을 반환
 */
class SupabaseRepo {
    val SUPABASE_URL = BuildConfig.SUPABASE_URL
    val SUPABASE_KEY = BuildConfig.SUPABASE_KEY
    val supabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        defaultSerializer = MoshiSerializer()
        install(Postgrest)
    }

    suspend fun fetchDataByQuery(query: String): List<StationData>{
        if (query == "") return emptyList()
        return withContext(Dispatchers.IO) {

            val fetchData = supabaseClient
                .from("station_list")
                .select {
                    filter {
                        like("STATN_NM", "$query%")
                    }
                }.data

            val gson = Gson()
            val type = object : TypeToken<List<StationData>>() {}.type
            val stationList: List<StationData> = gson.fromJson(fetchData, type)
            stationList
        }
    }

    // supabase를 통해 선택한 역의 정보들을 가져옴
    suspend fun selectMyChoiceStation(uid: String): List<StationSelectData>{
        return withContext(Dispatchers.IO) {

            val fetchData = supabaseClient
                .from("device_station_selection")
                .select(columns = Columns.list("STATN_NM, LINE_NM, UPDN_LINE")) {
                    filter {
                        eq("DEVICE_ID", uid)
                    }
                }.data

            val gson = Gson()
            val type = object : TypeToken<List<StationSelectData>>() {}.type
            val selectionStationList: List<StationSelectData> = gson.fromJson(fetchData, type)
            selectionStationList
        }
    }

    suspend fun insertDeviceStationSelection(deviceStation: DeviceStationSelection) {
        withContext(Dispatchers.IO) {
            Log.d("DailyRoot", "insertDeviceStationSelection deviceStation: $deviceStation")
            supabaseClient.from("device_station_selection").insert(deviceStation)
        }
    }
}