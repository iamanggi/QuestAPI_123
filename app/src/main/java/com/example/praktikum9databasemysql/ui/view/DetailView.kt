package com.example.praktikum9databasemysql.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.praktikum9databasemysql.model.Mahasiswa
import com.example.praktikum9databasemysql.ui.customwidget.CostumeTopAppBar
import com.example.praktikum9databasemysql.ui.navigation.DestinasiNavigasi
import com.example.praktikum9databasemysql.ui.viewModel.DetailViewModel
import com.example.praktikum9databasemysql.ui.viewModel.PenyediaViewModel

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail"
    override val titleRes = "Detail Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(
    nim: String,
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val mahasiswaState by viewModel.mahasiswa.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(nim) {
        viewModel.getMahasiswaById(nim)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(horizontal = 16.dp),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiDetail.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        },
        content = { innerPadding ->
            Box(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                when {
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Terjadi kesalahan",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    mahasiswaState != null -> {
                        DetailCard(mahasiswa = mahasiswaState!!)
                    }
                    else -> {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}


@Composable
fun DetailCard(mahasiswa: Mahasiswa) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailRow(label = "Nama", value = mahasiswa.nama)
            DetailRow(label = "Nim", value = mahasiswa.nim)
            DetailRow(label = "Kelas", value = mahasiswa.kelas)
            DetailRow(label = "Alamat", value = mahasiswa.alamat)
            DetailRow(label = "Angkatan", value = mahasiswa.angkatan)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(0.1f)
        )
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(2f)
        )
    }
}
