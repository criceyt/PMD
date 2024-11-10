package com.example.reto;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Juego;

public class JuegoDetalleActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView textViewNombreJuego;
    private TextView textViewDescripcionJuego;
    private TextView textViewPrecioo;
    private ImageView imagenJuego;
    private Button logOut, comprarBotton;
    private LinearLayout btnTienda;

    // Para el video
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
        textViewPrecioo = findViewById(R.id.textViewPrecio);
        comprarBotton = findViewById(R.id.btnComprar);
        btnTienda = findViewById(R.id.btnTienda);
        imagenJuego = findViewById(R.id.imagenJuegoView);
        videoView = findViewById(R.id.videoView);  // Inicializar el VideoView
        mediaController = new MediaController(this); // Inicializar el MediaController

        // Configurar el VideoView
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);  // Configura el MediaController para anclarse al VideoView

        // Cargar los detalles del juego con el ID
        cargarDetallesJuego(juegoId);

        // Manejar el logout
        logOut = findViewById(R.id.btnLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JuegoDetalleActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        comprarBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JuegoDetalleActivity.this, BibliotecaActivity.class);
                startActivity(intent);
            }
        });

        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir el TiendaActivity
                Intent intent = new Intent(JuegoDetalleActivity.this, TiendaActivity.class);
                // Iniciar el TiendaActivity
                startActivity(intent);
            }
        });
    }

    private void cargarDetallesJuego(int juegoId) {
        // Obtener los detalles del juego desde la base de datos
        Juego juego = dbHelper.obtenerJuegoPorId(juegoId);

        if (juego != null) {
            // Mostrar los detalles del juego en las vistas
            textViewNombreJuego.setText(juego.getNombre());
            textViewDescripcionJuego.setText(juego.getDescripcion());
            textViewPrecioo.setText(juego.getPrecio());

            // Cargar la imagen
            String imagenNombre = juego.getImagen(); // nombre de la imagen (sin la extensión .jpg o .png)
            Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + imagenNombre);
            imagenJuego.setImageURI(imageUri);

            // Cargar el video
            String videoBusacado = juego.getImagen();  // Aquí estás usando el nombre de la imagen, tal vez quieras algo diferente
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + videoBusacado);  // Asegúrate de tener el nombre correcto del archivo
            videoView.setVideoURI(videoUri); // Asignar URI del video al VideoView

            // Callback para cuando el video está listo para reproducirse
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // El MediaController se mostrará de forma persistente
                    mediaController.show();  // Mostrar el controlador de medios de forma persistente
                    videoView.start();  // Iniciar la reproducción del video
                    Toast.makeText(getBaseContext(), "El video está preparado", Toast.LENGTH_SHORT).show();
                }
            });

            // Callback para cuando el video ha terminado de reproducirse
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getBaseContext(), "El video ha terminado", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("JuegoDetalleActivity", "Juego no encontrado");
        }
    }
}
