package com.example.reto;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Juego;

import java.util.List;

public class BibliotecaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JuegoBibliotecaAdapter adapter;
    private DBHelper dbHelper;
    private List<Juego> juegos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        // Inicializa el DBHelper
        dbHelper = new DBHelper(this);

        // Obtén la lista de juegos desde la base de datos
        juegos = dbHelper.obtenerJuegos();

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewJuegos);

        // Usa GridLayoutManager con 2 columnas
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerView.setLayoutManager(layoutManager);

        // Crea el adaptador y lo asigna al RecyclerView
        adapter = new JuegoBibliotecaAdapter(juegos, this);
        recyclerView.setAdapter(adapter);
    }
}
