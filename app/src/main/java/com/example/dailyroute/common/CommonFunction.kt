package com.example.dailyroute.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.dailyroute.R
import java.util.UUID

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

fun getLineImageRes(lineName: String): Int {
    return when (lineName) {
        "1호선" -> R.drawable.line1 // 1호선 이미지
        "2호선" -> R.drawable.line2 // 2호선 이미지
        "3호선" -> R.drawable.line3// 3호선 이미지
        "4호선" -> R.drawable.line4 // 4호선 이미지
        "5호선" -> R.drawable.line5 // 5호선 이미지
        "6호선" -> R.drawable.line6 // 6호선 이미지
        "7호선" -> R.drawable.line7 // 7호선 이미지
        "8호선" -> R.drawable.line8 // 8호선 이미지
        "9호선" -> R.drawable.line9 // 9호선 이미지
        "GTX-A" -> R.drawable.line_gtx_a
        "경의중앙선" -> R.drawable.line_kj
        "공항철도" -> R.drawable.line_gc
        "경춘선" -> R.drawable.line_gh
        "수인분당선" -> R.drawable.line_sb
        "신분당선" -> R.drawable.line_sbd
        "경강선" -> R.drawable.line_kk
        "우이신설선" -> R.drawable.line_us
        "서해선" -> R.drawable.line_ws
        "신림선" -> R.drawable.line19
        else -> R.drawable.signatureicon // 기본 이미지 (플레이스홀더)
    }
}

/* uuid가 저장되어있는지 확인하는 로직 */
fun getUUID (context: Context): String {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var uuid = sharedPreferences.getString("device_uuid", null)

    if (uuid == null) {
        uuid = UUID.randomUUID().toString()  // UUID 생성
        sharedPreferences.edit().putString("device_uuid", uuid).apply()  // 저장
    }

    return uuid
}