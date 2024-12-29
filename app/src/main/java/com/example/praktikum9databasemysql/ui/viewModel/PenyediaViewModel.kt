package com.example.praktikum9databasemysql.ui.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.example.praktikum9databasemysql.MahasiswaApplications
import com.example.praktikum9databasemysql.viewmodel.UpdateViewModel

object PenyediaViewModel{
    val Factory = viewModelFactory {
    initializer{HomeViewModel(MahasiswaApp().container.mahasiswaRepository)}
        initializer{InsertViewModel(MahasiswaApp().container.mahasiswaRepository)}
        initializer{DetailViewModel(MahasiswaApp().container.mahasiswaRepository)}
        initializer{ UpdateViewModel(MahasiswaApp().container.mahasiswaRepository) }
    }

    fun CreationExtras.MahasiswaApp(): MahasiswaApplications =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MahasiswaApplications)
}