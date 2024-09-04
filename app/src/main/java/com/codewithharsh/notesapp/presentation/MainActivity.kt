package com.codewithharsh.notesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codewithharsh.notesapp.domain.model.Note
import com.codewithharsh.notesapp.presentation.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val editNote = remember { mutableStateOf(Note(-1, "", "")) }
    val isEdit = remember { mutableStateOf(false) }


    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(3.dp),
                title = {
                    Text(
                        text = "Notes App",
                        fontSize = 15.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            editNote.value = Note(-1, "", "")
                            isEdit.value = false
                            showBottomSheet = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.value.data.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Notes Found",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = paddingValues,
            ) {
                items(uiState.value.data) {
                    Noteitem(
                        it,
                        modifier = Modifier.clickable {
                            editNote.value = it
                            isEdit.value = true
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Form(note = editNote.value) { title, desc ->
                if (isEdit.value) {
                    val note = Note(id = editNote.value.id, title = title, desc = desc)
                    viewModel.update(note)
                } else {
                    val note = Note(id = 0, title = title, desc = desc)
                    viewModel.insert(note)
                }
                showBottomSheet = false
            }
        }
    }
}

@Composable
fun Noteitem(
    note: Note,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = note.title,
//                text = "Harsh",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = note.desc,
//                text = "Harsh is a good boy",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun Form(
    note: Note, onClick: (String, String) -> Unit
) {

    val title = remember { mutableStateOf(note.title) }
    val description = remember { mutableStateOf(note.desc) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title.value,
            onValueChange = {
                title.value = it
            },
            label = {
                Text(text = "Title")
            },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        OutlinedTextField(
            value = description.value,
            onValueChange = {
                description.value = it
            },
            label = {
                Text(text = "Description")
            },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Button(
            onClick = { onClick(title.value, description.value) },
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = "Save Note"
            )
        }
    }
}


