// JuegoDetalleActivity.java
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

    // Declaración de variables para acceder a las vistas
    private DBHelper dbHelper;  // Instancia del helper para interactuar con la base de datos
    private TextView textViewNombreJuego;  // Vista para mostrar el nombre del juego
    private TextView textViewDescripcionJuego;  // Vista para mostrar la descripción del juego
    private TextView textViewPrecioo;  // Vista para mostrar el precio del juego
    private ImageView imagenJuego;  // Vista para mostrar la imagen del juego
    private Button logOut, comprarBotton;  // Botones para cerrar sesión y comprar el juego
    private String usuarioId;  // ID del usuario, obtenido de las preferencias compartidas (sesión)
    private LinearLayout btnTienda, btnPerfil;  // Botones para navegar a la tienda y perfil del usuario

    // Declaración de vistas para el manejo del video
    private VideoView videoView;  // Vista para mostrar el video del juego
    private MediaController mediaController;  // Controlador de medios para el VideoView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Habilita la compatibilidad con pantalla completa o "Edge-to-Edge"
        setContentView(R.layout.activity_juego_detalle);  // Configura el layout de la actividad

        // Inicializar el DBHelper para acceder a la base de datos
        dbHelper = new DBHelper(this);

        // Obtener el ID del usuario desde las preferencias compartidas (sesión activa)
        usuarioId = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("usuario_dni", null);

        // Si el usuario no está registrado en la sesión, cerrar la actividad
        if (usuarioId == null) {
            Log.e("JuegoDetalleActivity", "usuarioId es null, terminando actividad");
            Toast.makeText(this, "Usuario no encontrado en la sesión", Toast.LENGTH_SHORT).show();
            finish();  // Termina la actividad si no hay usuario
            return;
        }

        // Obtener el ID del juego desde el Intent que se pasó al abrir esta actividad
        int juegoId = getIntent().getIntExtra("COLUMN_JUEGO_ID", -1);
        if (juegoId == -1) {
            Log.e("JuegoDetalleActivity", "ID de juego no encontrado");
            finish();  // Si el ID del juego no está presente, cerrar la actividad
            return;
        }

        // Inicializar las vistas (TextViews, ImageView, Button, etc.)
        textViewNombreJuego = findViewById(R.id.textViewNombreJuego);  // Asignar la vista para el nombre del juego
        textViewDescripcionJuego = findViewById(R.id.textViewDescripcionJuego);  // Asignar la vista para la descripción del juego
        textViewPrecioo = findViewById(R.id.textViewPrecio);  // Asignar la vista para el precio del juego
        comprarBotton = findViewById(R.id.btnComprar);  // Asignar el botón de comprar
        btnTienda = findViewById(R.id.btnTienda);  // Asignar el botón para ir a la tienda
        btnPerfil = findViewById(R.id.btnPerfil);  // Asignar el botón para ir al perfil
        imagenJuego = findViewById(R.id.imagenJuegoView);  // Asignar el ImageView para mostrar la imagen del juego
        videoView = findViewById(R.id.videoView);  // Inicializar el VideoView para mostrar el video
        mediaController = new MediaController(this);  // Crear un controlador de medios para el VideoView

        // Configurar el VideoView con el MediaController para permitir reproducir el video
        videoView.setMediaController(mediaController);  // Asocia el MediaController al VideoView
        mediaController.setAnchorView(videoView);  // Ancla el controlador de medios al VideoView

        // Cargar los detalles del juego desde la base de datos
        cargarDetallesJuego(juegoId);

        // Manejo del clic en el botón de logout para cerrar sesión y redirigir a la pantalla de registro
        logOut = findViewById(R.id.btnLogout);  // Asignar el botón de logout
        logOut.setOnClickListener(new View.OnClickListener() {  // Configurar el evento de clic
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JuegoDetalleActivity.this, RegisterActivity.class);  // Crear un Intent para ir a RegisterActivity
                startActivity(intent);  // Iniciar la actividad de registro
                finish();  // Terminar la actividad actual (JuegoDetalleActivity)
            }
        });

        // Acción para el botón "Comprar" que agrega el juego a la biblioteca
        comprarBotton.setOnClickListener(new View.OnClickListener() {  // Configurar el evento de clic
            @Override
            public void onClick(View v) {
                // Agregar el juego a la biblioteca del usuario en la base de datos
                boolean agregado = dbHelper.agregarJuegoABiblioteca(usuarioId, juegoId);

                // Mostrar un mensaje de confirmación o error al usuario
                if (agregado) {
                    Toast.makeText(JuegoDetalleActivity.this, "Juego agregado a tu biblioteca", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JuegoDetalleActivity.this, "Error al agregar el juego", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Acción para navegar a la tienda
        btnTienda.setOnClickListener(new View.OnClickListener() {  // Configurar el evento de clic
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JuegoDetalleActivity.this, TiendaActivity.class);  // Crear un Intent para ir a TiendaActivity
                startActivity(intent);  // Iniciar la actividad de la tienda
            }
        });

        // Acción para navegar al perfil del usuario
        btnPerfil.setOnClickListener(new View.OnClickListener() {  // Configurar el evento de clic
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JuegoDetalleActivity.this, PerfilActivity.class);  // Crear un Intent para ir a PerfilActivity
                startActivity(intent);  // Iniciar la actividad del perfil
            }
        });

        // Acción para navegar a la biblioteca del usuario
        LinearLayout btnBiblioteca = findViewById(R.id.btnBiblioteca);  // Asignar el botón para la biblioteca
        btnBiblioteca.setOnClickListener(new View.OnClickListener() {  // Configurar el evento de clic
            @Override
            public void onClick(View v) {
                Log.d("JuegoDetalleActivity", "Botón Biblioteca presionado");  // Registrar en log el clic en Biblioteca
                Intent intent = new Intent(JuegoDetalleActivity.this, BibliotecaActivity.class);  // Crear un Intent para ir a BibliotecaActivity
                intent.putExtra("bibliotecaId", usuarioId);  // Pasar el ID del usuario para mostrar su biblioteca
                startActivity(intent);  // Iniciar la actividad de la biblioteca
            }
        });
    }

    // Método para cargar los detalles del juego usando su ID
    private void cargarDetallesJuego(int juegoId) {
        // Obtener los detalles del juego desde la base de datos usando el ID
        Juego juego = dbHelper.obtenerJuegoPorId(juegoId);

        if (juego != null) {
            // Si el juego fue encontrado, establecer los valores en las vistas correspondientes
            textViewNombreJuego.setText(juego.getNombre());  // Establecer el nombre del juego
            textViewDescripcionJuego.setText(juego.getDescripcion());  // Establecer la descripción del juego
            textViewPrecioo.setText(juego.getPrecio());  // Establecer el precio del juego

            // Cargar la imagen del juego
            String imagenNombre = juego.getImagen();  // Obtener el nombre de la imagen desde el juego
            Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + imagenNombre);  // Crear URI para la imagen
            imagenJuego.setImageURI(imageUri);  // Establecer la imagen en el ImageView

            // Cargar el video relacionado con el juego
            String videoBuscado = juego.getImagen();  // Usar el nombre de la imagen como nombre del video
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + videoBuscado);  // Crear URI para el video
            videoView.setVideoURI(videoUri);  // Establecer el video en el VideoView

            // Configurar el listener para cuando el video esté listo para reproducirse
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  // Callback cuando el video está listo
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.show();  // Mostrar los controles de video de manera persistente
                    videoView.start();  // Iniciar la reproducción del video
                    Toast.makeText(getBaseContext(), "El video está preparado", Toast.LENGTH_SHORT).show();  // Mostrar mensaje de confirmación
                }
            });

            // Configurar el listener para cuando el video termine de reproducirse
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  // Callback cuando el video ha terminado
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(getBaseContext(), "El video ha terminado", Toast.LENGTH_SHORT).show();  // Mostrar mensaje al terminar
                }
            });
        } else {
            Log.e("JuegoDetalleActivity", "Juego no encontrado");  // Registrar error si el juego no fue encontrado
        }
    }
}
