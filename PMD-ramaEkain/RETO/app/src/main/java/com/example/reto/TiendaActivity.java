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

import java.util.ArrayList;
import java.util.List;

public class TiendaActivity extends AppCompatActivity implements JuegoAdapter.OnJuegoClickListener {

    private RecyclerView recyclerViewJuegos;
    private JuegoAdapter juegoAdapter;
    private List<Juego> juegosList;
    private DBHelper dbHelper;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Tienda");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Configuración de RecyclerView
        recyclerViewJuegos = findViewById(R.id.recyclerViewJuegos);
        recyclerViewJuegos.setLayoutManager(new LinearLayoutManager(this));

        juegosList = new ArrayList<>();
        dbHelper = new DBHelper(this);
        cargarJuegos();

        // Configuración del adaptador para los juegos
        juegoAdapter = new JuegoAdapter(juegosList, this, this);
        recyclerViewJuegos.setAdapter(juegoAdapter);

        // Configuración del botón de logout
        buttonLogout = findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Configuración del botón de Biblioteca
        findViewById(R.id.btnBiblioteca).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBiblioteca();
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

    // Método para manejar el clic en un juego
    @Override
    public void onJuegoClick(Juego juego) {

        Intent intent = new Intent(TiendaActivity.this, JuegoDetalleActivity.class);


        intent.putExtra("COLUMN_JUEGO_ID", juego.getId());


        startActivity(intent);
    }


    // Método para abrir la actividad de la Biblioteca
    private void abrirBiblioteca() {
        Intent intent = new Intent(TiendaActivity.this, BibliotecaActivity.class);
        startActivity(intent);
    }
}


