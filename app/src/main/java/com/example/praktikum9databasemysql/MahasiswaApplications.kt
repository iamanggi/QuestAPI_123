package com.example.praktikum9databasemysql

import android.app.Application
import com.example.praktikum9databasemysql.repository.AppContainer
import com.example.praktikum9databasemysql.repository.MahasiswaContainer

class MahasiswaApplications:Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container=MahasiswaContainer()
    }
}