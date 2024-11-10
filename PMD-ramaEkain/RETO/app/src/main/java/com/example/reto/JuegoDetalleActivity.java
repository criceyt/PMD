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

    // Instancias de vistas
    private DBHelper dbHelper;
    private TextView textViewNombreJuego, textViewDescripcionJuego, textViewPrecioo;
    private ImageView imagenJuego;
    private Button logOut, comprarBotton;
    private LinearLayout btnTienda, botonBiblioteca;

    // Para la reproducción de videos
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Activa la funcionalidad de borde a borde en la actividad
        setContentView(R.layout.activity_juego_detalle);  // Establece el layout para esta actividad

        // Inicializa el DBHelper para acceder a la base de datos
        dbHelper = new DBHelper(this);

        // Obtiene el ID del juego desde el Intent que inició la actividad
        int juegoId = getIntent().getIntExtra("COLUMN_JUEGO_ID", -1);
        String usuarioId = getIntent().getStringExtra("usuario_id");
        Log.d("JuegoDetalleActivity", "juegoId: " + juegoId + ", usuario_id: " + usuarioId);

        // Verifica que el juegoId sea válido
        if (juegoId == -1) {
            Log.e("JuegoDetalleActivity", "ID de juego no encontrado");
            finish();  // Termina la actividad si no se encuentra el juego
            return;
        }

        // Inicializa las vistas para mostrar los detalles del juego
        textViewNombreJuego = findViewById(R.id.textViewNombreJuego);
        textViewDescripcionJuego = findViewById(R.id.textViewDescripcionJuego);
        textViewPrecioo = findViewById(R.id.textViewPrecio);
        comprarBotton = findViewById(R.id.btnComprar);
        btnTienda = findViewById(R.id.btnTienda);
        imagenJuego = findViewById(R.id.imagenJuegoView);
        videoView = findViewById(R.id.videoView);  // Inicializa el VideoView
        mediaController = new MediaController(this); // Inicializa el controlador de medios para el video

        // Configura el VideoView con el MediaController
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);  // Asocia el controlador de medios al VideoView

        // Llama a la función para cargar los detalles del juego
        cargarDetallesJuego(juegoId);

        // Configura el botón de logout para redirigir al usuario a la pantalla de registro
        logOut = findViewById(R.id.btnLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JuegoDetalleActivity.this, RegisterActivity.class);
                startActivity(intent);  // Inicia la actividad de registro
            }
        });

        // Configura el botón de compra para agregar el juego a la biblioteca del usuario
        comprarBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera el ID del usuario y el juego
                String userId = getIntent().getStringExtra("usuario_id");
                int juegoId = getIntent().getIntExtra("COLUMN_JUEGO_ID", -1);

                // Intenta agregar el juego a la biblioteca del usuario
                boolean agregado = dbHelper.agregarJuegoABiblioteca(userId, juegoId);

                // Muestra un mensaje dependiendo si se agregó correctamente o no
                if (agregado) {
                    Toast.makeText(JuegoDetalleActivity.this, "Juego agregado a tu biblioteca", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JuegoDetalleActivity.this, "Error al agregar el juego", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configura el botón de tienda para ir a la actividad de la tienda
        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir la actividad de la tienda
                Intent intent = new Intent(JuegoDetalleActivity.this, TiendaActivity.class);
                startActivity(intent);  // Inicia la actividad de la tienda
            }
        });

        // Configura el botón de biblioteca para ir a la actividad de la biblioteca
        botonBiblioteca = findViewById(R.id.btnBiblioteca);
        botonBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JuegoDetalleActivity", "Botón Biblioteca presionado");
                // Crea un Intent para abrir la actividad de la biblioteca
                Intent intent = new Intent(JuegoDetalleActivity.this, BibliotecaActivity.class);
                intent.putExtra("bibliotecaId", usuarioId);  // Pasa el usuarioId como el ID de la biblioteca
                startActivity(intent);  // Inicia la actividad de la biblioteca
            }
        });
    }

    // Método para cargar los detalles del juego desde la base de datos
    private void cargarDetallesJuego(int juegoId) {
        // Obtiene el juego desde la base de datos
        Juego juego = dbHelper.obtenerJuegoPorId(juegoId);

        if (juego != null) {
            // Muestra los detalles del juego en las vistas correspondientes
            textViewNombreJuego.setText(juego.getNombre());
            textViewDescripcionJuego.setText(juego.getDescripcion());
            textViewPrecioo.setText(juego.getPrecio());

            // Carga la imagen del juego usando su nombre (sin extensión)
            String imagenNombre = juego.getImagen();
            Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + imagenNombre);
            imagenJuego.setImageURI(imageUri);

            // Carga el video asociado con el juego (usa el nombre de la imagen para buscar el video)
            String videoBuscado = juego.getImagen();  // Este valor parece un poco extraño, deberías revisar si es el nombre correcto
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + videoBuscado);
            videoView.setVideoURI(videoUri);  // Asigna la URI del video al VideoView

            // Configura el listener para cuando el video esté listo para reproducirse
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.show();  // Muestra el controlador de medios de forma persistente
                    videoView.start();  // Inicia la reproducción del video
                    Toast.makeText(getBaseContext(), "El video está preparado", Toast.LENGTH_SHORT).show();
                }
            });

            // Configura el listener para cuando el video termine de reproducirse
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
