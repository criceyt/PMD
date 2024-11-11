package com.example.reto;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Juego;
import com.example.reto.database.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class TiendaActivity extends AppCompatActivity implements JuegoAdapter.OnJuegoClickListener {

    // Definición de las vistas y variables
    private RecyclerView recyclerViewJuegos;
    private JuegoAdapter juegoAdapter;
    private List<Juego> juegosList;
    private DBHelper dbHelper;
    private Button buttonLogout;
    private Button buttonAyuda;
    private Usuario usuario;
    private String userId;
    private View btnEditarPerfil;
    private MediaPlayer mediaPlayer; // Variable para manejar el audio de fondo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Recuperar los datos enviados a la actividad a través del Intent
        int juegoId = getIntent().getIntExtra("COLUMN_JUEGO_ID", -1);
        String usuarioId = getIntent().getStringExtra("usuario_id");
        Log.d("JuegoDetalleActivity", "juegoId: " + juegoId + ", usuario_id: " + usuarioId);

        setTitle("Tienda");  // Título de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);  // Asigna el layout de la tienda

        // Configuración del MediaPlayer para reproducir un sonido de fondo
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido); // Sonido de fondo en bucle
        mediaPlayer.setLooping(true);
        mediaPlayer.start(); // Inicia la reproducción

        // Inicialización de las vistas
        recyclerViewJuegos = findViewById(R.id.recyclerViewJuegos);  // RecyclerView donde se mostrarán los juegos
        recyclerViewJuegos.setLayoutManager(new LinearLayoutManager(this));  // Configura el layout del RecyclerView

        juegosList = new ArrayList<>();
        dbHelper = new DBHelper(this);  // Inicializa el DBHelper para manejar la base de datos
        cargarJuegos();  // Cargar los juegos desde la base de datos

        // Configuración del adaptador para mostrar los juegos en el RecyclerView
        juegoAdapter = new JuegoAdapter(juegosList, this, this);
        recyclerViewJuegos.setAdapter(juegoAdapter);

        // Inicialización de las vistas del perfil y botones
        btnEditarPerfil = findViewById(R.id.btnPerfil);

        // Obtención de los datos del usuario enviados a través del Intent
        String userId = getIntent().getStringExtra("usuario_id");
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");

        usuario = new Usuario(userId, userName, userEmail, "");  // Crea el objeto Usuario

        // Configuración del botón de Logout
        buttonLogout = findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();  // Llama al método para realizar logout
            }
        });

        // Configuración del botón de Biblioteca
        LinearLayout btnBiblioteca = findViewById(R.id.btnBiblioteca);
        btnBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JuegoDetalleActivity", "Botón Biblioteca presionado");
                Intent intent = new Intent(TiendaActivity.this, BibliotecaActivity.class);
                intent.putExtra("bibliotecaId", usuarioId);  // Pasa el ID del usuario como parámetro
                startActivity(intent);  // Abre la actividad Biblioteca
            }
        });

        // Configuración del botón de Editar Perfil
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPerfil();  // Llama al método para editar perfil
            }
        });

        // Configuración del botón de Ayuda
        buttonAyuda = findViewById(R.id.btnAyuda);
        buttonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAyuda();  // Llama al método para abrir la ayuda
            }
        });
    }

    // Método para cargar los juegos desde la base de datos
    private void cargarJuegos() {
        juegosList.clear();  // Limpiar la lista antes de cargar los nuevos juegos
        juegosList.addAll(dbHelper.obtenerJuegos());  // Obtiene los juegos desde la base de datos
    }

    // Método para hacer logout
    private void logout() {
        Intent intent = new Intent(TiendaActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Limpia la pila de actividades
        startActivity(intent);  // Inicia la actividad de login
        finish();  // Finaliza la actividad actual
    }

    // Método llamado cuando se hace clic en un juego en el RecyclerView
    @Override
    public void onJuegoClick(Juego juego) {
        Intent intent = new Intent(TiendaActivity.this, JuegoDetalleActivity.class);
        intent.putExtra("COLUMN_JUEGO_ID", juego.getId());  // Pasa el ID del juego
        intent.putExtra("usuario_id", usuario.getId());  // Pasa el ID del usuario
        startActivity(intent);  // Inicia la actividad de detalles del juego
    }

    // Método para abrir la pantalla de ayuda
    private void abrirAyuda() {
        Intent intent = new Intent(TiendaActivity.this, AyudaActivity.class);
        startActivity(intent);  // Inicia la actividad de ayuda
    }

    // Método para editar el perfil
    private void editarPerfil(){
        Intent intent = new Intent(TiendaActivity.this, PerfilActivity.class);
        intent.putExtra("usuario_id", usuario.getId());  // Pasa el ID del usuario
        startActivity(intent);  // Inicia la actividad de perfil
    }

    // Detener el sonido al salir de la actividad
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();  // Pausa el sonido
        }
    }

    // Reanudar el sonido cuando la actividad es visible nuevamente
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();  // Reanuda la reproducción del sonido
        }
    }

    // Liberar los recursos del MediaPlayer cuando la actividad es destruida
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Libera los recursos del MediaPlayer
            mediaPlayer = null;  // Elimina la referencia del MediaPlayer
        }
    }
}
