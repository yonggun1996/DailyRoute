package com.example.dailyroute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailyroute.common.CommonUI
import com.example.dailyroute.listcode.CustomListItem
import com.example.dailyroute.listcode.SubwayArriveData
import com.example.dailyroute.ui.theme.DailyRouteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 리스트뷰 샘플 데이터
        val sampleItems = List(10) { index ->
            SubwayArriveData(
                subwayName = "태릉입구역",
                terminus = "봉화산",
                arrivalTime = "5분",
                lineNum = "6호선"
            )
        }

        super.onCreate(savedInstanceState)
        setContent {
            DailyRouteTheme {
                // A surface container using the 'background' color from the theme
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CommonUI.AppHeader()
                    CommonUI.SearchBar(defalutText = "역 이름을 입력해주세요")
                    ItemList(items = sampleItems)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyRouteTheme {
        // A surface container using the 'background' color from the theme
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 리스트뷰 샘플 데이터
            val sampleItems = List(20) { index ->
                SubwayArriveData(
                    subwayName = "태릉입구역",
                    terminus = "봉화산",
                    arrivalTime = "5분",
                    lineNum = "6호선"
                )
            }

            CommonUI.AppHeader()
            CommonUI.SearchBar(defalutText = "역 이름을 입력해주세요")
            ItemList(items = sampleItems)
        }
    }
}

// 해당 페이지의 LazyColumn 설정
@Composable
fun ItemList(items: List<SubwayArriveData>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // 항목 간 간격 설정
    ) {
        items(items) {it
            CustomListItem(item = it) // 지하철 도착 정보 전달
        }
    }
}