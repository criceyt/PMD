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

public class TiendaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewJuegos;
    private JuegoAdapter juegoAdapter;
    private List<Juego> juegosList;
    private DBHelper dbHelper;
    private Button buttonLogout;
    private View btnEditarPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Tienda");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        recyclerViewJuegos = findViewById(R.id.recyclerViewJuegos);
        recyclerViewJuegos.setLayoutManager(new LinearLayoutManager(this));

        juegosList = new ArrayList<>();

        dbHelper = new DBHelper(this);

        cargarJuegos();

        juegoAdapter = new JuegoAdapter(juegosList, this);
        recyclerViewJuegos.setAdapter(juegoAdapter);


        buttonLogout = findViewById(R.id.btnLogout);
        btnEditarPerfil = findViewById(R.id.btnPerfil);


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPerfil();
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

    private void editarPerfil(){
        Intent intent = new Intent(TiendaActivity.this, PerfilActivity.class);
        startActivity(intent);
    }
}
