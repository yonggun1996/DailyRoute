package com.example.dailyroute.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dailyroute.repo.StationData
import com.example.dailyroute.viewmodel.SupabaseViewModel

object CommonUI {

    /*
     * 기본적으로 자주 사용하고 있는 ui를 공통적으로 사용하기 위해 해당 object에 생성
     */

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
    fun SearchBar(defalutText: String, viewModel: SupabaseViewModel) {
        var query by remember { mutableStateOf("") } // 검색어 관리 변수
        var active by remember { mutableStateOf(false) } // 검색 바 활성화 상태
        val searchResults by viewModel.searchResults.collectAsState() // 상태를 관찰하여 값이 변경되면 UI를 업데이트

        // material 디자인에서 제공하는 검색바 사용
        androidx.compose.material3.SearchBar(
            query = query,
            onQueryChange = {
                // 검색하려는 데이터가 바뀔때 마다 호출
                query = it
                viewModel.onSearchQueryChanged(query)
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
                items(searchResults) {item ->
                    SearchResultItem(item = item)
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(item: StationData) {
    Text(
        text = item.STATN_NM,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}