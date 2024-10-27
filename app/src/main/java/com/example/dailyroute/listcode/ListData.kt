package com.example.dailyroute.listcode

/* LazyColumn이나 LazyRow를 사용하기 위한 Data모음 */

// 지하철 도착시간 data
data class SubwayArriveData (
    val subwayName: String,     // 내가 설정한 전철역
    val terminus: String,       // 해당 전철의 행선지
    val arrivalTime: String,    // 도착 시간
    val lineNum: String         // 호선
)