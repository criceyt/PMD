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
    private ImageView imageViewPerfil, imagenGaleria, imagenFoto;
    private LinearLayout btnTienda, btnCargarImagen, btnHacerFoto, btnBiblioteca;
    private DBHelper dbHelper;
    private String userId;
    private Usuario usuario;

    // Declarar el ActivityResultLaunchers para cámara y galería
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<Intent> galleryResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Perfil");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Obtener el DNI del usuario desde el Intent
        String usuarioId = getIntent().getStringExtra("usuario_id");

        // Inicializar los campos de vista
        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        imagenGaleria = findViewById(R.id.imagenGaleria);
        imagenFoto = findViewById(R.id.imagenFoto);
        btnTienda = findViewById(R.id.btnTienda);
        btnBiblioteca = findViewById(R.id.btnBiblioteca);
        btnCargarImagen = findViewById(R.id.btnCargarImagen);
        btnHacerFoto = findViewById(R.id.btnHacerFoto);

        // Establecer la imagen predeterminada
        imageViewPerfil.setImageResource(R.drawable.icono);
        imagenGaleria.setImageResource(R.drawable.galeria);
        imagenFoto.setImageResource(R.drawable.camera);

        // Inicializar el DBHelper
        dbHelper = new DBHelper(this);

        // Llamamos para mostrar los datos del usuario
        mostrarImagenDePerfil(usuarioId);

        // Botón para cargar imagen desde la galería
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagenDesdeGaleria();
            }
        });

        // Botón para hacer foto con la cámara
        btnHacerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerFotoConCamara();
            }
        });

        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTienda();
            }
        });

        btnBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, BibliotecaActivity.class);
                startActivity(intent);
            }
        });

        // Launcher para la cámara
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            imageViewPerfil.setImageBitmap(imageBitmap); // Set image to ImageView

                            // Guardar la imagen en la base de datos
                            guardarImagenEnBaseDeDatos(imageBitmap);
                        }
                    }
                }
        );

        // Launcher para la galería
        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            imageViewPerfil.setImageURI(selectedImageUri); // Set image to ImageView

                            // Guardar la imagen en la base de datos
                            guardarImagenDesdeUri(selectedImageUri); // Llamar al nuevo método
                        }
                    }
                }
        );
    }

    private void abrirTienda() {
        Intent intent = new Intent(PerfilActivity.this, TiendaActivity.class);
        intent.putExtra("usuario_id", usuario.getId());
        startActivity(intent);
    }

    private void cargarImagenDesdeGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryResultLauncher.launch(intent);
    }

    private void hacerFotoConCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResultLauncher.launch(intent);
    }

    // Método para convertir el bitmap a byte array
    private byte[] convertirBitmapABytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Comprime la imagen
        byte[] bytes = stream.toByteArray();
        Log.d("PerfilActivity", "Imagen convertida a bytes, tamaño: " + bytes.length);
        return bytes;  // Devuelve el byte array
    }

    // Método para guardar la imagen en la base de datos
    private void guardarImagenEnBaseDeDatos(Bitmap bitmap) {
        byte[] imagenBytes = convertirBitmapABytes(bitmap);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userDni = sharedPreferences.getString("userDni", "");

        if (!userDni.isEmpty()) {
            dbHelper.guardarImagenUsuario(userDni, imagenBytes);
            imageViewPerfil.setImageBitmap(bitmap);  // Actualiza el ImageView después de guardar la imagen
        } else {
            mostrarError("No se encontró el DNI del usuario.");
        }
    }

    // Método para recuperar la imagen de la base de datos
    public void mostrarImagenDePerfil(String dni) {
        if (dni == null || dni.isEmpty()) {
            mostrarError("El DNI no puede ser nulo o vacío");
            return;
        }

        byte[] imageBytes = dbHelper.recuperarImagenDeUsuario(dni);
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageViewPerfil.setImageBitmap(bitmap);
        } else {
            mostrarError("No se encontró imagen para el usuario con DNI: " + dni);
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Log.e("PerfilActivity", mensaje);
    }
    // Método para guardar una imagen seleccionada desde la galería
    private void guardarImagenDesdeUri(Uri imageUri) {
        try {
            // Convertir el Uri a un Bitmap
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            // Guardar la imagen en la base de datos
            guardarImagenEnBaseDeDatos(bitmap);
        } catch (IOException e) {
            mostrarError("Error al cargar la imagen desde la galería.");
        }
    }

}