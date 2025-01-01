package com.example.dailyroute.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyroute.repo.StationData
import com.example.dailyroute.repo.StationSelectData
import com.example.dailyroute.repo.SupabaseRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
 * supabase에서 데이터를 읽어들인 후
 * UI화면을 변경시키기 위한 ViewModel
 */
class SupabaseViewModel(private val repository: SupabaseRepo): ViewModel() {
    // StateFlow를 통해서 데이터의 상태를 관리하고 UI에 최신 데이터를 전달
    // 검색 결과
    private val _searchResults = MutableStateFlow<List<StationData>>(emptyList())
    val searchResults: StateFlow<List<StationData>> get() = _searchResults

    // 내가 저장한 전철역 목록
    private val _selectionList = MutableStateFlow<List<StationSelectData>>(emptyList())
    val selectionList: StateFlow<List<StationSelectData>> get() = _selectionList

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        // 기존 작업 취소
        searchJob?.cancel()

        // 백그라운드에서 데이터를가져오기 위해 viewModelScope활용
        searchJob = viewModelScope.launch {
            delay(1000) // 1초 대기 (Debounce)
            val result = repository.fetchDataByQuery(query)
            _searchResults.value = result
        }
    }

    fun onSearchSelectionList(uid: String) {
        // 기존 작업 취소
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            val result = repository.selectMyChoiceStation(uid)
            _selectionList.value = result
        }
    }
}