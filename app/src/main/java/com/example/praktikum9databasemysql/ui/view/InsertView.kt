package com.example.praktikum9databasemysql.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.praktikum9databasemysql.ui.customwidget.CostumeTopAppBar
import com.example.praktikum9databasemysql.ui.navigation.DestinasiNavigasi
import com.example.praktikum9databasemysql.ui.viewModel.InsertUiEvent
import com.example.praktikum9databasemysql.ui.viewModel.InsertUiState
import com.example.praktikum9databasemysql.ui.viewModel.InsertViewModel
import com.example.praktikum9databasemysql.ui.viewModel.PenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiEntry: DestinasiNavigasi{
    override val route = "item_entry"
    override val titleRes = "Insert Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryMhsScreen(
    navigateBack:()-> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(horizontal = 10.dp),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ){innerPadding ->
        EntryBody(
            insertUiState = viewModel.uiState,
            onSiswaValueChange = viewModel::updateInsertMhsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertMhs()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}
@Composable
fun EntryBody(
    insertUiState: InsertUiState,
    onSiswaValueChange: (InsertUiEvent)-> Unit,
    onSaveClick:()->Unit,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInput(
            insertUiEvent = insertUiState.insertUIEvent,
            onValueChange = onSiswaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Save Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Simpan",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    insertUiEvent: InsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEvent)-> Unit ={},
    enabled: Boolean = true
){
    val kelas = listOf("A", "B", "C", "D")
    val JenisKelamin = listOf("Laki - Laki", "Perempuan")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.nama,
            onValueChange = {onValueChange(insertUiEvent.copy(nama = it))},
            label = { Text("Nama")},
            placeholder = { Text("Masukkan Nama") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.nim,
            onValueChange = {onValueChange(insertUiEvent.copy(nim = it))},
            label = { Text("Nim")},
            placeholder = { Text("Masukkan Nim") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Text("Jenis Kelamin", style = MaterialTheme.typography.bodyMedium)
        Row() {
            JenisKelamin.forEach { jK ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    androidx.compose.material3.RadioButton(
                        selected = insertUiEvent.jenisKelamin == jK,
                        onClick = { onValueChange(insertUiEvent.copy(jenisKelamin = jK)) }
                    )
                    Text(
                        text = jK,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        OutlinedTextField(
            value = insertUiEvent.alamat,
            onValueChange = {onValueChange(insertUiEvent.copy(alamat = it))},
            label = { Text("Alamat")},
            placeholder = { Text("Masukkan Alamat") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Text("Kelas", style = MaterialTheme.typography.bodyMedium)
        Row() {
            kelas.forEach { kelas ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    androidx.compose.material3.RadioButton(
                        selected = insertUiEvent.kelas == kelas,
                        onClick = { onValueChange(insertUiEvent.copy(kelas = kelas)) }
                    )
                    Text(
                        text = kelas,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        OutlinedTextField(
            value = insertUiEvent.angkatan,
            onValueChange = {onValueChange(insertUiEvent.copy(angkatan = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Angkatan")},
            placeholder = { Text("Masukkan Angkatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled){
            Text(
                text = "*Isi Semua Data!",
                modifier = Modifier.padding(10.dp),
                color = Color.Red
            )
        }
        Divider(
            thickness = 5.dp,
            modifier = Modifier.padding(5.dp)
        )
    }
}