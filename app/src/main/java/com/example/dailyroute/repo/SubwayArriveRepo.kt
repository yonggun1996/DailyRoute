package com.example.dailyroute.repo

import android.util.Log
import com.example.dailyroute.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

data class SubwayData(
    val beginRow: Int? = null,
    val endRow: Int? = null,
    val curPage: Int? = null,
    val pageRow: Int? = null,
    val totalCount: Int,
    val rowNum: Int,
    val selectedCount: Int,
    val subwayId: String,
    val subwayNm: String? = null,
    val updnLine: String,
    val trainLineNm: String,
    val subwayHeading: String? = null,
    val statnFid: String,
    val statnTid: String,
    val statnId: String,
    val statnNm: String,
    val trainCo: String? = null,
    val trnsitCo: String,
    val ordkey: String,
    val subwayList: String,
    val statnList: String,
    val btrainSttus: String,
    val barvlDt: String,
    val btrainNo: String,
    val bstatnId: String,
    val bstatnNm: String,
    val recptnDt: String,
    val arvlMsg2: String,
    val arvlMsg3: String,
    val arvlCd: String,
    val lstcarAt: String
)

// 지하철 도착시간 data
data class SubwayArriveData (
    val subwayName: String,     // 내가 설정한 전철역
    val terminus: String,       // 해당 전철의 행선지
    val arrivalTime: String,    // 도착 시간
    val lineNum: String         // 호선
)


// 지하철 도착 관련 api호출 및 비즈니스로직 처리하는 Model 구현부
class SubwayArriveRepo {
    private val okHttpClient = OkHttpClient()

    // 지하철 실시간 도착api 호출 및 xml응답을 json으로 변환
    suspend fun getSubwayArriveData(statnNm: String): JSONObject? {
        val SUBWAY_API_KEY = BuildConfig.SUBWAY_API_KEY
        val requestURL = "http://swopenAPI.seoul.go.kr/api/subway/${SUBWAY_API_KEY}/json/realtimeStationArrival/0/99/${statnNm}"

        return withContext(Dispatchers.IO) {  // IO 스레드에서 네트워크 요청 실행
            val request = Request.Builder()
                .url(requestURL)  // 한글이 포함된 URL
                .build()

            // 요청 실행 후 응답 받기
            val response = okHttpClient.newCall(request).execute()

            // 응답이 성공적이면, 응답 본문을 JSON으로 변환
            if (response.isSuccessful) {
                response.body?.string()?.let {
                    // 응답 본문을 JSONObject로 변환하여 반환
                    Log.d("DailyRoot", "getSubwayArriveData: $it")
                    return@withContext JSONObject(it)
                }
            }
            return@withContext null  // 응답이 실패하면 null 반환
        }

    }
}