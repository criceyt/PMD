// PerfilActivity.java
package com.example.reto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    // Declaración de vistas (ImageView, LinearLayout, etc.)
    private ImageView imageViewPerfil, imagenGaleria, imagenFoto;
    private LinearLayout btnTienda, btnCargarImagen, btnHacerFoto, btnBiblioteca;
    private DBHelper dbHelper;  // Instancia de la base de datos
    private String userId;  // Variable para almacenar el ID del usuario
    private Usuario usuario;  // Objeto de usuario que contiene los datos del usuario

    // Declarar el ActivityResultLauncher para manejar el resultado de la cámara y la galería
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<Intent> galleryResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Perfil");  // Establece el título de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);  // Asigna el layout XML de la actividad

        // Obtener el DNI del usuario pasado en el Intent
        String usuarioId = getIntent().getStringExtra("usuario_id");

        // Inicialización de las vistas (ImageView y botones)
        imageViewPerfil = findViewById(R.id.imageViewPerfil);  // Imagen de perfil
        imagenGaleria = findViewById(R.id.imagenGaleria);  // Icono de galería
        imagenFoto = findViewById(R.id.imagenFoto);  // Icono de cámara
        btnTienda = findViewById(R.id.btnTienda);  // Botón para acceder a la tienda
        btnBiblioteca = findViewById(R.id.btnBiblioteca);  // Botón para acceder a la biblioteca
        btnCargarImagen = findViewById(R.id.btnCargarImagen);  // Botón para cargar imagen desde la galería
        btnHacerFoto = findViewById(R.id.btnHacerFoto);  // Botón para tomar una foto con la cámara

        // Establecer iconos predeterminados para las imágenes
        imageViewPerfil.setImageResource(R.drawable.icono);
        imagenGaleria.setImageResource(R.drawable.galeria);
        imagenFoto.setImageResource(R.drawable.camera);

        // Inicializar la instancia del DBHelper para interactuar con la base de datos
        dbHelper = new DBHelper(this);

        // Llamar a la función para mostrar la imagen de perfil
        mostrarImagenDePerfil(usuarioId);

        // Acción del botón "Cargar Imagen" (desde la galería)
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagenDesdeGaleria();  // Inicia la carga de imagen desde la galería
            }
        });

        // Acción del botón "Hacer Foto" (con la cámara)
        btnHacerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerFotoConCamara();  // Inicia la captura de una foto con la cámara
            }
        });

        // Acción del botón "Ir a Tienda"
        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTienda();  // Abre la actividad de la tienda
            }
        });

        // Acción del botón "Ir a Biblioteca"
        btnBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad BibliotecaActivity
                Intent intent = new Intent(PerfilActivity.this, BibliotecaActivity.class);
                startActivity(intent);
            }
        });

        // Launcher para la cámara
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Si la foto fue tomada correctamente
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            imageViewPerfil.setImageBitmap(imageBitmap);  // Muestra la imagen capturada en el ImageView

                            // Guardar la imagen tomada en la base de datos
                            guardarImagenEnBaseDeDatos(imageBitmap);
                        }
                    }
                }
        );

        // Launcher para la galería
        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Si se seleccionó una imagen correctamente desde la galería
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            imageViewPerfil.setImageURI(selectedImageUri);  // Muestra la imagen seleccionada en el ImageView

                            // Guardar la imagen seleccionada en la base de datos
                            guardarImagenDesdeUri(selectedImageUri);  // Llama al método para guardar la imagen
                        }
                    }
                }
        );
    }

    // Método para abrir la tienda
    private void abrirTienda() {
        Intent intent = new Intent(PerfilActivity.this, TiendaActivity.class);
        intent.putExtra("usuario_id", usuario.getId());  // Pasa el ID del usuario a la tienda
        startActivity(intent);  // Inicia la actividad de la tienda
    }

    // Método para cargar una imagen desde la galería
    private void cargarImagenDesdeGaleria() {
        // Inicia la selección de una imagen desde la galería
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryResultLauncher.launch(intent);
    }

    // Método para tomar una foto con la cámara
    private void hacerFotoConCamara() {
        // Inicia la captura de una imagen con la cámara
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResultLauncher.launch(intent);
    }

    // Método para convertir un Bitmap en un arreglo de bytes (byte array)
    private byte[] convertirBitmapABytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);  // Comprime la imagen en formato PNG
        byte[] bytes = stream.toByteArray();  // Convierte la imagen comprimida a byte array
        Log.d("PerfilActivity", "Imagen convertida a bytes, tamaño: " + bytes.length);
        return bytes;  // Devuelve el byte array
    }

    // Método para guardar la imagen del perfil en la base de datos
    private void guardarImagenEnBaseDeDatos(Bitmap bitmap) {
        byte[] imagenBytes = convertirBitmapABytes(bitmap);  // Convierte la imagen a byte array
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userDni = sharedPreferences.getString("userDni", "");  // Obtiene el DNI del usuario desde las preferencias compartidas

        // Si el DNI no está vacío, guarda la imagen en la base de datos
        if (!userDni.isEmpty()) {
            dbHelper.guardarImagenUsuario(userDni, imagenBytes);  // Guarda la imagen en la base de datos
            imageViewPerfil.setImageBitmap(bitmap);  // Muestra la imagen guardada en el ImageView
        } else {
            mostrarError("No se encontró el DNI del usuario.");  // Muestra un error si el DNI no es encontrado
        }
    }

    // Método para recuperar y mostrar la imagen de perfil desde la base de datos
    public void mostrarImagenDePerfil(String dni) {
        if (dni == null || dni.isEmpty()) {
            mostrarError("El DNI no puede ser nulo o vacío");  // Muestra un error si el DNI es inválido
            return;
        }

        // Recupera la imagen del usuario desde la base de datos
        byte[] imageBytes = dbHelper.recuperarImagenDeUsuario(dni);
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageViewPerfil.setImageBitmap(bitmap);  // Muestra la imagen recuperada
        } else {
            mostrarError("No se encontró imagen para el usuario con DNI: " + dni);  // Muestra un error si no se encuentra la imagen
        }
    }

    // Método para mostrar errores en un Toast y loguearlos
    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();  // Muestra un Toast con el mensaje de error
        Log.e("PerfilActivity", mensaje);  // Registra el error en el log
    }

    // Método para guardar la imagen seleccionada desde la galería
    private void guardarImagenDesdeUri(Uri imageUri) {
        try {
            // Convierte el Uri de la imagen seleccionada en un Bitmap
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            // Guarda la imagen en la base de datos
            guardarImagenEnBaseDeDatos(bitmap);
        } catch (IOException e) {
            mostrarError("Error al cargar la imagen desde la galería.");  // Muestra un error si ocurre una excepción
        }
    }

}
