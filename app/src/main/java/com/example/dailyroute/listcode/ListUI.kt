package com.example.dailyroute.listcode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailyroute.common.getColorForRoute

/* LazyColumn이나 LazyRow를 사용하기 위한 UI모음 */

// Main페이지에서 지하철 도착 시간 리스트UI
@Composable
fun CustomListItem(item: SubwayArriveData) {
    val backgroundColor = getColorForRoute(item.lineNum)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = backgroundColor)
            .border(1.dp, backgroundColor),
        verticalAlignment = Alignment.CenterVertically // 세로 정렬을 중앙으로 설정
    ) {
        // 왼쪽 아이콘
        Icon(
            painter = painterResource(id = com.example.dailyroute.R.drawable.signatureicon),
            contentDescription = null,
            modifier = Modifier.size(50.dp) // 아이콘 크기 설정
        )

        Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이의 간격

        // 중앙 제목 텍스트
        Text(
            text = "${item.subwayName} (${item.terminus}행)",
            modifier = Modifier.weight(1f), // 남은 공간을 차지하도록 설정
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )

        // 오른쪽 도착 시간 텍스트
        Text(
            text = item.arrivalTime,
            modifier = Modifier.padding(end = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}