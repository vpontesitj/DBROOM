package com.example.db_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.db_room.roomDb.Pessoa
import com.example.db_room.roomDb.PessoaDataBase
import com.example.db_room.ui.theme.Db_roomTheme
import com.example.db_room.viewModel.PessoaViewModel
import com.example.db_room.viewModel.Repository

class MainActivity : ComponentActivity() {


    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PessoaDataBase::class.java,
            "pessoa.db"
        ).build()
    }

    private val viewModel by viewModels<PessoaViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T{
                    return PessoaViewModel(Repository(db)) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Db_roomTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) { innerPadding ->
                    App(Modifier, viewModel, this)
                }
            }
        }
    }
}

@Composable
fun App(

    modifier: Modifier = Modifier,
    viewModel: PessoaViewModel,
    mainActivity: MainActivity

) {
    var nome by remember {
        mutableStateOf("")
    }
    var telefone by remember {
        mutableStateOf("")
    }

    val pessoa = Pessoa(
        nome,
        telefone
    )

    var pessoaList by remember {
        mutableStateOf(listOf<Pessoa>())
    }

    viewModel.getPessoa().observe(mainActivity) {
        pessoaList = it
    }

    Column(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .padding(20.dp)
        ) {
        }

        Row(
            Modifier
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(
                text = "Cadastro e Consulta",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            Arrangement.Start
        ) {
            Text(
                text = "Nome: ",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Digite o nome") },
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            Arrangement.Start
        ) {
            Text(
                text = "Telefone: ",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        TextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Digite o telefone") },
            modifier = Modifier.padding(20.dp)
        )

        // Espaçamento entre o campo "Telefone" e o botão "Cadastrar"
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            Modifier
                .padding(20.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            Arrangement.Start
        ) {
            Button(
                onClick = {
                    viewModel.upsertPessoa(pessoa)
                    nome = ""
                    telefone = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            ) {
                Text(
                    text = "Cadastrar",
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(
                text = "Clientes cadastrados",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
        Row(
            Modifier
                .padding(40.dp, 0.dp, 0.dp, 10.dp)
                .fillMaxWidth(),
            Arrangement.Start
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.5f),
                Arrangement.Center
            ) {
                Text(
                    text = "Nome",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Column(
                Modifier
                    .padding(20.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth(0.5f),
                Arrangement.Center
            ) {
                Text(
                    text = "Telefone",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        }
        HorizontalDivider()

        LazyColumn {
            items(pessoaList) { pessoa ->
                Row(
                    Modifier
                        .fillMaxWidth(),
                    Arrangement.Center
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth(0.5f),
                        Arrangement.Center
                    ) {
                        Text(
                            text = "${pessoa.nome}",
                            color = Color.Black
                        )
                    }

                    Column(
                        Modifier
                            .fillMaxWidth(0.5f),
                        Arrangement.Center
                    ) {
                        Text(
                            text = "${pessoa.telefone}",
                            color = Color.Black
                        )
                    }
                }

                HorizontalDivider()
            }

        }
        Row(
            Modifier
                .padding(20.dp)
        ) {
        }
    }
}