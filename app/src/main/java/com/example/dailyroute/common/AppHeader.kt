package com.example.dailyroute.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

object CommonUI {
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
}