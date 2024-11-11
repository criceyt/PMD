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

    private RecyclerView recyclerViewJuegos;
    private JuegoAdapter juegoAdapter;
    private List<Juego> juegosList;
    private DBHelper dbHelper;
    private Button buttonLogout;
    private Button buttonAyuda;
    private Usuario usuario;
    private String userId;
    private View btnEditarPerfil;
    private MediaPlayer mediaPlayer; // Declaramos el MediaPlayer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int juegoId = getIntent().getIntExtra("COLUMN_JUEGO_ID", -1);
        String usuarioId = getIntent().getStringExtra("usuario_id");
        Log.d("JuegoDetalleActivity", "juegoId: " + juegoId + ", usuario_id: " + usuarioId);
        setTitle("Tienda");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.sonido); // Reemplaza 'tu_sonido' con el nombre de tu archivo
        mediaPlayer.setLooping(true); // Hace que el sonido se repita en bucle
        mediaPlayer.start(); // Inicia la reproducción

        // Configuración de RecyclerView
        recyclerViewJuegos = findViewById(R.id.recyclerViewJuegos);
        recyclerViewJuegos.setLayoutManager(new LinearLayoutManager(this));


        juegosList = new ArrayList<>();
        dbHelper = new DBHelper(this);
        cargarJuegos();

        // Configuración del adaptador para los juegos
        juegoAdapter = new JuegoAdapter(juegosList, this, this);
        recyclerViewJuegos.setAdapter(juegoAdapter);
        btnEditarPerfil = findViewById(R.id.btnPerfil);

        String userId = getIntent().getStringExtra("usuario_id");
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");

        usuario = new Usuario(userId, userName, userEmail, "");

        // Configuración del botón de logout
        buttonLogout = findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        LinearLayout btnBiblioteca = findViewById(R.id.btnBiblioteca);
        btnBiblioteca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("JuegoDetalleActivity", "Botón Biblioteca presionado");
                // Aquí, asegúrate de pasar un valor válido para bibliotecaId
                Intent intent = new Intent(TiendaActivity.this, BibliotecaActivity.class);
                intent.putExtra("bibliotecaId", usuarioId);  // Pasamos userId como ejemplo de bibliotecaId
                startActivity(intent);
            }
        });
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPerfil();
            }
        });

        // Configuración del botón de Ayuda
        buttonAyuda = findViewById(R.id.btnAyuda);
        buttonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAyuda();
            }
        });

    }

    private void cargarJuegos() {
        juegosList.clear();
        juegosList.addAll(dbHelper.obtenerJuegos());
    }

    private void logout() {
        Intent intent = new Intent(TiendaActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onJuegoClick(Juego juego) {
        Intent intent = new Intent(TiendaActivity.this, JuegoDetalleActivity.class);
        intent.putExtra("COLUMN_JUEGO_ID", juego.getId());
        intent.putExtra("usuario_id", usuario.getId());
        startActivity(intent);
    }



    private void abrirAyuda() {
        Intent intent = new Intent(TiendaActivity.this, AyudaActivity.class);
        startActivity(intent);
    }

    private void editarPerfil(){
        Intent intent = new Intent(TiendaActivity.this, PerfilActivity.class);

        intent.putExtra("usuario_id", usuario.getId());
        startActivity(intent);
    }

    // Detener el sonido al salir de la actividad
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Reanudar el sonido al regresar a la actividad
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Liberar recursos cuando la actividad se destruye
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

