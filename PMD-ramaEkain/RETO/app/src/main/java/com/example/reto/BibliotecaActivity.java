package com.example.reto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Juego;

import java.util.List;

public class BibliotecaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;           // RecyclerView para mostrar la lista de juegos
    private BibliotecaAdapter adapter;            // Adaptador para gestionar la lista de juegos
    private DBHelper dbHelper;                   // Objeto para interactuar con la base de datos
    private List<Juego> juegos;                  // Lista de juegos a mostrar
    private String bibliotecaId;                 // ID de la biblioteca (recibido desde el Intent)
    private LinearLayout botonTienda;             // Layout que contiene el botón para ir a la tienda

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca); // Configura el layout de la actividad

        // Obtener el 'bibliotecaId' desde los extras del Intent que lanzó esta actividad
        bibliotecaId = getIntent().getStringExtra("bibliotecaId");
        Log.d("BibliotecaActivity", "bibliotecaId: " + bibliotecaId);

        // Verificar si se recibió el 'bibliotecaId', si no, cerrar la actividad
        if (bibliotecaId == null) {
            Log.e("BibliotecaActivity", "No se proporcionó bibliotecaId");
            finish(); // Cierra la actividad si no se proporcionó el ID
            return;
        }

        // Inicializar el DBHelper para interactuar con la base de datos
        dbHelper = new DBHelper(this);
        // Obtener los juegos asociados a la biblioteca con el ID 'bibliotecaId'
        juegos = dbHelper.obtenerJuegosPorBiblioteca(bibliotecaId);
        Log.d("BibliotecaActivity", "Número de juegos obtenidos: " + (juegos != null ? juegos.size() : "null"));

        // Verificar si la lista de juegos es nula o vacía
        if (juegos == null || juegos.isEmpty()) {
            Log.e("BibliotecaActivity", "No se encontraron juegos en esta biblioteca.");
            return; // Si no hay juegos, se termina el método
        }

        // Inicializar el RecyclerView y configurar su LayoutManager
        recyclerView = findViewById(R.id.recyclerViewJuegos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Se utiliza un LayoutManager lineal

        // Inicializar el adaptador con la lista de juegos y asignarlo al RecyclerView
        adapter = new BibliotecaAdapter(juegos, this);
        recyclerView.setAdapter(adapter);

        // Inicializar el LinearLayout que contiene el botón (esto estaba faltando en el código original)
        botonTienda = findViewById(R.id.btnTienda);  // Obtener el botón de la tienda
        botonTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JuegoDetalleActivity", "Botón Biblioteca presionado");
                // Crear un Intent para iniciar la actividad TiendaActivity
                Intent intent = new Intent(BibliotecaActivity.this, TiendaActivity.class);
                // Lanzar la nueva actividad
                startActivity(intent);
            }
        });
    }
}