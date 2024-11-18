package com.example.dailyroute.repo

import com.example.dailyroute.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

data class StationData (
    var STATN_ID: Int,
    var SUBWAY_ID: Int,
    var STATN_NM: String,
    var LINE_NM: String,
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
}