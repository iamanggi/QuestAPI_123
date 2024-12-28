package com.example.praktikum9databasemysql.ui.viewModel

import com.example.praktikum9databasemysql.model.Mahasiswa

sealed class HomeUiState{
    data class  Success(val mahasiswa: List<Mahasiswa>): HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}

