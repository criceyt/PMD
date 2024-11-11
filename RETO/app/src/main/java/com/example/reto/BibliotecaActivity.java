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
import com.example.reto.database.modelo.Usuario;

import java.util.List;

public class BibliotecaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BibliotecaAdapter adapter;
    private DBHelper dbHelper;
    private List<Juego> juegos;
    private String bibliotecaId;  // Variable para almacenar el bibliotecaId (DNI)
    private LinearLayout btnTienda, btnPerfil;  // Variable para el LinearLayout que actúa como botón

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca); // Asegúrate de que este es el layout correcto

        // Obtener el bibliotecaId (DNI) desde SharedPreferences
        bibliotecaId = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("usuario_dni", null);
        Log.d("BibliotecaActivity", "bibliotecaId (DNI): " + bibliotecaId);

        // Verificar si se proporcionó el bibliotecaId (DNI)
        if (bibliotecaId == null) {
            Log.e("BibliotecaActivity", "No se proporcionó bibliotecaId (DNI)");
            finish(); // Cierra la actividad si no hay bibliotecaId (DNI)
            return;
        }

        // Inicializar el DBHelper y obtener la lista de juegos
        dbHelper = new DBHelper(this);
        juegos = dbHelper.obtenerJuegosPorBiblioteca(bibliotecaId); // Usar el DNI como bibliotecaId
        Log.d("BibliotecaActivity", "Número de juegos obtenidos: " + (juegos != null ? juegos.size() : "null"));

        // Verificar si la lista de juegos está vacía o es nula
        if (juegos == null || juegos.isEmpty()) {
            Log.e("BibliotecaActivity", "No se encontraron juegos en esta biblioteca.");
            return; // Salir si no hay juegos
        }

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewJuegos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Configurar el LayoutManager

        // Inicializar el adaptador con la lista de juegos y configurarlo en el RecyclerView
        adapter = new BibliotecaAdapter(juegos, this);
        recyclerView.setAdapter(adapter);

        // Inicializar el LinearLayout para el botón que va a TiendaActivity
        btnTienda = findViewById(R.id.btnTienda);
        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la TiendaActivity
                Intent intent = new Intent(BibliotecaActivity.this, TiendaActivity.class);
                startActivity(intent);
            }
        });
        btnPerfil = findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la TiendaActivity
                Intent intent = new Intent(BibliotecaActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }
}