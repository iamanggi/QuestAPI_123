package com.example.praktikum9databasemysql.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mahasiswa (
    val nim: String,
    val nama: String,
    val alamat: String,

    @SerialName("jenis_kelamin") //harus menyesuaikan dengan nama database mknnya namanya di serialkan
    val jenisKelamin: String,

    val kelas: String,
    val angkatan: String
)