package com.example.praktikum9databasemysql.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.praktikum9databasemysql.R
import com.example.praktikum9databasemysql.model.Mahasiswa
import com.example.praktikum9databasemysql.ui.customwidget.CostumeTopAppBar
import com.example.praktikum9databasemysql.ui.navigation.DestinasiNavigasi
import com.example.praktikum9databasemysql.ui.viewModel.HomeUiState
import com.example.praktikum9databasemysql.ui.viewModel.HomeViewModel
import com.example.praktikum9databasemysql.ui.viewModel.PenyediaViewModel


object DestinasiHome: DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Home Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeViewModel =viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showDialog by remember { mutableStateOf(false) }
    var mahasiswaToDelete by remember { mutableStateOf<Mahasiswa?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(horizontal = 10.dp),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiHome.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getMhs()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add Mahasiswa")
            }
        },
    ) {  innerPadding ->
        HomeStatus(
            homeUiState = viewModel.mhsUiState,
            retryAction = { viewModel.getMhs()},
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { mahasiswa ->
                mahasiswaToDelete = mahasiswa
                showDialog = true
            }
        )

        if (showDialog && mahasiswaToDelete != null) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    mahasiswaToDelete?.let {
                        viewModel.deleteMhs(it.nim)
                        viewModel.getMhs()
                    }
                    showDialog = false
                    mahasiswaToDelete = null
                },
                onDeleteCancel = {
                    showDialog = false
                    mahasiswaToDelete = null
                }
            )
        }
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Delete Data") },
        text = { Text("Apakah Anda yakin ingin menghapus data ini?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Batal")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Ya", color = Color.Red)
            }
        }
    )
}


@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onDetailClick: (String) -> Unit
){
    when(homeUiState){
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success ->
            if(homeUiState.mahasiswa.isEmpty()){
                return Box(modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    Text(text = "Tidak ada data Mahasiswa")
                }
            }
        else{
            MhsLayout(
                mahasiswa = homeUiState.mahasiswa,
                modifier = modifier.fillMaxWidth(),
                onDetailClick = {
                    onDetailClick(it.nim)
                },
                onDeleteClick = {
                    onDeleteClick(it)
                }
            )
        }
        is HomeUiState.Error -> OnError(retryAction,
            modifier = modifier.fillMaxSize())
    }
}

//home screen menampilkan pesan loading
@Composable
fun OnLoading(
    modifier: Modifier = Modifier
){
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

//home screen menampilkan pesan error dengan tombol coba lagi
@Composable
fun OnError(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.connection_error),
            contentDescription = "")
        Text(text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction, modifier = Modifier.padding(horizontal = 60.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.retry),
                    fontSize = 16.sp)

            }
        }
    }
}

@Composable
fun MhsLayout(
    mahasiswa: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onDetailClick: (Mahasiswa) -> Unit,
    onDeleteClick: (Mahasiswa) -> Unit = {}
){
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mahasiswa){
            mhs ->
            MhsCard(
                mahasiswa = mhs,
                modifier = Modifier.fillMaxWidth()
                    .clickable { onDetailClick(mhs) },
                onDeleteClick = {onDeleteClick(mhs)}
            )
        }
    }
}

@Composable
fun MhsCard(
    mahasiswa: Mahasiswa,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {}
){
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mahasiswa.nama,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {onDeleteClick(mahasiswa)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null, tint = Color.Red
                    )
                }
            }
            Text(
                text = mahasiswa.nim,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = mahasiswa.alamat,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}