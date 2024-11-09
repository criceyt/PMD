package com.example.reto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Juego;

public class JuegoDetalleActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView textViewNombreJuego;
    private TextView textViewDescripcionJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_detalle);

        // Inicializar el DBHelper
        dbHelper = new DBHelper(this);

        // Obtener el ID del juego pasado en el Intent
        int juegoId = getIntent().getIntExtra("COLUMN_JUEGO_ID", -1);

        if (juegoId == -1) {
            Log.e("JuegoDetalleActivity", "ID de juego no encontrado");
            finish(); // Terminar la actividad si el ID no es válido
            return;
        }

        // Inicializar las vistas
        textViewNombreJuego = findViewById(R.id.textViewNombreJuego);
        textViewDescripcionJuego = findViewById(R.id.textViewDescripcionJuego);

        // Cargar los detalles del juego con el ID
        cargarDetallesJuego(juegoId);
    }

    private void cargarDetallesJuego(int juegoId) {
        // Obtener los detalles del juego desde la base de datos
        Juego juego = dbHelper.obtenerJuegoPorId(juegoId);

        if (juego != null) {
            // Mostrar los detalles del juego en las vistas
            textViewNombreJuego.setText(juego.getNombre());
            textViewDescripcionJuego.setText(juego.getDescripcion());
        } else {
            Log.e("JuegoDetalleActivity", "Juego no encontrado");
        }
    }
}
