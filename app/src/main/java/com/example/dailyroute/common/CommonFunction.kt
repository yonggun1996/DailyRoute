package com.example.dailyroute.common

import androidx.compose.ui.graphics.Color

/* 모든 화면에서 공통으로 사용할 것 같은 함수들 */
// 노선별 색상
fun getColorForRoute(routeType: String): androidx.compose.ui.graphics.Color {
    return when (routeType) {
        "1호선" -> Color(0xFF0052A4)
        "2호선" -> Color(0xFF00A84D)
        "3호선" -> Color(0xFFEF7C1C)
        "4호선" -> Color(0xFF00A5DE)
        "5호선" -> Color(0xFF996CAC)
        "6호선" -> Color(0xFFCD7C2F)
        "7호선" -> Color(0xFF747F00)
        "8호선" -> Color(0xFFE6186C)
        "9호선" -> Color(0xFFBB8336)
        "GTX-A" -> Color(0xFF9A6292)
        "경의중앙선" -> Color(0xFF77C4A3)
        "공항철도" -> Color(0xFF0065B3)
        "경춘선" -> Color(0xFF0C8E72)
        "수인분당선" -> Color(0xFFF5A200)
        "신분당선" -> Color(0xFFD4003B)
        "경강선" -> Color(0xFF003DA5)
        "우이신설선" -> Color(0xFFB0CE18)
        "서해선" -> Color(0xFF81A914)
        "신림선" -> Color(0xFF6789CA)
        else -> Color.Gray // 기본 색상
    }
}
