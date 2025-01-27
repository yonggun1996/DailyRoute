package com.example.dailyroute.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailyroute.listcode.CustomListItem
import com.example.dailyroute.repo.DeviceStationSelection
import com.example.dailyroute.repo.StationData
import com.example.dailyroute.repo.SubwayArriveRepo
import com.example.dailyroute.repo.SupabaseRepo
import com.example.dailyroute.viewmodel.SubwayArriveViewModel
import com.example.dailyroute.viewmodel.SupabaseViewModel

object CommonUI {

    /*
     * 기본적으로 자주 사용하고 있는 ui를 공통적으로 사용하기 위해 해당 object에 생성
     */

    private val subwayArriveViewModel = SubwayArriveViewModel(SubwayArriveRepo())
    private val supabaseViewModel = SupabaseViewModel(SupabaseRepo())

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppHeader() {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = com.example.dailyroute.R.drawable.signatureicon), // mipmap 아이콘 리소스 ID
                        contentDescription = "App Icon",
                        modifier = Modifier.size(24.dp) // 아이콘 크기 조정
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이 간격
                    Text(
                        text = stringResource(id = com.example.dailyroute.R.string.app_name),
                    )
                }
            },
//            backgroundColor = MaterialTheme.colors.primary, // 앱 바 배경색 설정
//            contentColor = Color.White // 텍스트와 아이콘 색상 설정
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar(defalutText: String) {
        var query by remember { mutableStateOf("") } // 검색어 관리 변수
        var active by remember { mutableStateOf(false) } // 검색 바 활성화 상태
        val searchResults by supabaseViewModel.searchResults.collectAsState() // 상태를 관찰하여 값이 변경되면 UI를 업데이트
        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        // material 디자인에서 제공하는 검색바 사용
        androidx.compose.material3.SearchBar(
            query = query,
            onQueryChange = {
                // 검색하려는 데이터가 바뀔때 마다 호출
                query = it
                selectedIndex = null
                supabaseViewModel.onSearchQueryChanged(query)
            }, // 검색어가 변경될 때 마다 호출
            onSearch = { /* 검색 로직 추가 */ }, // 검색이 실행될 때 호출
            active = active, // 검색 바의 활성화 상태를 나타냅니다.
            onActiveChange = { active = it }, // 검색 바의 활성화 상태가 변경될 때 호출됩니다.
            placeholder = { Text(text = defalutText) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "검색") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(searchResults) {index, item ->
                    SearchResultItem(
                        item = item,
                        isSelected = selectedIndex == index, // 선택 상태 전달
                        onClick = { clickedStation ->
                            // 클릭 시 선택 상태 토글
                            Log.d("DailyRoot", "item: $item")

                            // 11.11 응암역이 업데잍트 되었음
                            if (item.STATN_NM == "응암") item.STATN_NM = "응암순환(상선)"
//                            subwayArriveViewModel.fetchData(item.STATN_ID, item.STATN_NM)

                            selectedIndex = if (selectedIndex == index) null else index
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun SearchResultItem(
        item: StationData,
        isSelected: Boolean, // 선택 상태
        onClick: (StationData) -> Unit // 클릭 이벤트 핸들러
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(item) }
                .background(Color.White)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이미지
            Image(
                painter = painterResource(id = getLineImageRes(item.LINE_NM)),
                contentDescription = "Station Image",
                modifier = Modifier
                    .size(48.dp) // 이미지 크기를 고정
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) // 배경 추가 (선택사항)
            )

            Spacer(modifier = Modifier.width(8.dp)) // 이미지와 텍스트 간격

            // 전철역 이름
            Text(
                text = item.STATN_NM,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            // 호선 이름
            Text(
                text = item.LINE_NM,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp),
                textAlign = TextAlign.End
            )
        }
        // 선택된 경우 버튼 추가
        if (isSelected) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.UP_STATN_NM != null) {
                    Button(
                        onClick = {
                            val myChoiceSubwayData = DeviceStationSelection(
                                null,
                                item.STATN_ID,
                                getUUID(context),
                                item.STATN_NM,
                                item.LINE_NM,
                                "상행",
                                item.SUBWAY_ID
                            )

                            supabaseViewModel.insertMyChoiceSubwayData(myChoiceSubwayData)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(4.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getColorForRoute(item.LINE_NM),
                            contentColor = Color.White
                        )
                    ) {
                        Text("${item.UP_STATN_NM}방면")
                    }
                }

                if (item.DOWN_STATN_NM != null) {
                    Button(
                        onClick = {
                            val myChoiceSubwayData = DeviceStationSelection(
                                null,
                                item.STATN_ID,
                                getUUID(context),
                                item.STATN_NM,
                                item.LINE_NM,
                                "하행",
                                item.SUBWAY_ID
                            )

                            supabaseViewModel.insertMyChoiceSubwayData(myChoiceSubwayData)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(4.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = getColorForRoute(item.LINE_NM),
                            contentColor = Color.White
                        )
                    ) {
                        Text("${item.DOWN_STATN_NM}방면")
                    }
                }
            }
        }
    }

    // 본문이 될 List
    @Composable
    fun MainContent() {
        // 1. supabase에서 내가 선택한 데이터만 select
        val searchResults by supabaseViewModel.selectionList.collectAsState() // 상태를 관찰하여 값이 변경되면 UI를 업데이트
        supabaseViewModel.onSearchSelectionList(getUUID(LocalContext.current))
        Log.d("DailyRoot", "searchResults: $searchResults")

        // 2. 내가 선택한 전철역의 실시간 정보를 리스트에 저장
        searchResults.forEach { it ->
            Log.d("DailyRoot", "searchResults.it: ${it}")
            subwayArriveViewModel.fetchData(it)
        }

        val myChoiceSubwayList = subwayArriveViewModel.subwayArriveDataList.collectAsState()
        Log.d("DailyRoot", "myChoiceSubwayList: ${myChoiceSubwayList.value}")
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp) // 항목 간 간격 설정
//        ) {
//            items(items) {it
//                CustomListItem(item = it) // 지하철 도착 정보 전달
//            }
//        }
    }
}