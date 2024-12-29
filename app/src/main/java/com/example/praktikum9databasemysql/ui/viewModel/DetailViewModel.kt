package com.example.praktikum9databasemysql.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktikum9databasemysql.model.Mahasiswa
import com.example.praktikum9databasemysql.repository.MahasiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState{
    data class  Success(val mahasiswa: List<Mahasiswa>): DetailUiState()
    object Error : DetailUiState()
    object Loading : DetailUiState()
}

class DetailViewModel(private val mhs: MahasiswaRepository) : ViewModel() {

    private val _mahasiswa = MutableStateFlow<Mahasiswa?>(null)
    val mahasiswa: StateFlow<Mahasiswa?> get() = _mahasiswa

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getMahasiswaById(nim: String) {
        viewModelScope.launch {
            try {
                val mahasiswaList = mhs.getMahasiswa()
                val mahasiswa = mahasiswaList.firstOrNull { it.nim == nim }

                if (mahasiswa != null) {
                    _mahasiswa.value = mahasiswa
                } else {
                    _errorMessage.value = "Mahasiswa dengan NIM $nim tidak ditemukan."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat data mahasiswa: ${e.message}"
            }
        }
    }
}
