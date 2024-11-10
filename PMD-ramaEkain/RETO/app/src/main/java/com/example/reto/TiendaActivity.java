package com.example.reto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Juego;
import com.example.reto.database.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class TiendaActivity extends AppCompatActivity implements JuegoAdapter.OnJuegoClickListener {

    // Declaración de las vistas y variables
    private RecyclerView recyclerViewJuegos; // RecyclerView para mostrar los juegos
    private JuegoAdapter juegoAdapter; // Adaptador del RecyclerView
    private List<Juego> juegosList; // Lista para almacenar los juegos
    private DBHelper dbHelper; // Helper para interactuar con la base de datos
    private Button buttonLogout; // Botón para cerrar sesión
    private Usuario usuario; // Objeto que contiene la información del usuario
    private String userId; // ID del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Tienda");  // Establece el título de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);  // Establece el layout correspondiente

        // Inicializar RecyclerView y configurar su layout manager
        recyclerViewJuegos = findViewById(R.id.recyclerViewJuegos);
        recyclerViewJuegos.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa la lista de juegos y el DBHelper
        juegosList = new ArrayList<>();
        dbHelper = new DBHelper(this);

        // Cargar los juegos desde la base de datos
        cargarJuegos();

        // Configurar el adaptador para el RecyclerView
        juegoAdapter = new JuegoAdapter(juegosList, this, this);  // Pasar la lista de juegos y el listener
        recyclerViewJuegos.setAdapter(juegoAdapter);  // Asignar el adaptador al RecyclerView

        // Obtener el ID, nombre y correo del usuario desde el Intent
        String userId = getIntent().getStringExtra("usuario_id");
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");

        // Crear el objeto usuario con la información obtenida
        usuario = new Usuario(userId, userName, userEmail, "");

        // Configuración del botón de logout (cerrar sesión)
        buttonLogout = findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();  // Llamar al método logout cuando el botón es presionado
            }
        });

        // Configuración del botón de Biblioteca
        findViewById(R.id.btnBiblioteca).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBiblioteca();  // Llamar al método abrirBiblioteca para acceder a la Biblioteca
            }
        });
    }

    // Método para cargar los juegos desde la base de datos
    private void cargarJuegos() {
        juegosList.clear();  // Limpiar la lista de juegos antes de cargar los nuevos
        juegosList.addAll(dbHelper.obtenerJuegos());  // Obtener todos los juegos de la base de datos
    }

    // Método para manejar el logout
    private void logout() {
        // Crear un Intent para redirigir al usuario a la pantalla de LoginActivity
        Intent intent = new Intent(TiendaActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Limpiar la pila de actividades
        startActivity(intent);  // Iniciar la actividad de login
        finish();  // Finalizar la actividad actual
    }

    // Método que se llama cuando el usuario hace clic en un juego
    @Override
    public void onJuegoClick(Juego juego) {
        // Crear un Intent para abrir la actividad de detalles del juego
        Intent intent = new Intent(TiendaActivity.this, JuegoDetalleActivity.class);

        // Pasar el ID del juego y el ID del usuario a la siguiente actividad
        intent.putExtra("COLUMN_JUEGO_ID", juego.getId());
        intent.putExtra("usuario_id", usuario.getId());

        // Iniciar la actividad de detalles del juego
        startActivity(intent);
    }

    // Método para abrir la actividad de la Biblioteca
    private void abrirBiblioteca() {
        // Crear un Intent para abrir la actividad BibliotecaActivity
        Intent intent = new Intent(TiendaActivity.this, BibliotecaActivity.class);
        intent.putExtra("userId", userId);  // Pasar el ID del usuario
        startActivity(intent);  // Iniciar la actividad BibliotecaActivity
    }
}



