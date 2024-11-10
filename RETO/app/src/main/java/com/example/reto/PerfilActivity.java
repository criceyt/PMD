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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto.database.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    private EditText editTextDni, editTextEmail, editTextNombre, editTextPassword;
    private DBHelper dbHelper;
    private ImageView imageViewPerfil, imagenGaleria, imagenFoto;
    private LinearLayout btnTienda, btnCargarImagen, btnHacerFoto, btnBiblioteca;

    // Declarar el ActivityResultLaunchers para cámara y galería
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<Intent> galleryResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Perfil");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar los campos de vista
        editTextDni = findViewById(R.id.editTextDNI);
        editTextEmail = findViewById(R.id.editTextCorreo);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextPassword = findViewById(R.id.editTextContrasena);
        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        imagenGaleria = findViewById(R.id.imagenGaleria);
        imagenFoto = findViewById(R.id.imagenFoto);
        btnTienda = findViewById(R.id.btnTienda);
        btnBiblioteca = findViewById(R.id.btnBiblioteca);


        // Establecer la imagen predeterminada
        imageViewPerfil.setImageResource(R.drawable.icono);
        imagenGaleria.setImageResource(R.drawable.galeria);
        imagenFoto.setImageResource(R.drawable.camera);

        // Hacer que los campos sean solo de lectura
        editTextDni.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextNombre.setEnabled(false);
        editTextPassword.setEnabled(false);

        // Inicializar el DBHelper
        dbHelper = new DBHelper(this);

        // Obtener el DNI del usuario de las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userDni = sharedPreferences.getString("userDni", "");

        // Llamamos para mostrar los datos del usuario
        mostrarDatosUsuario(userDni);

        // Mostrar la imagen del usuario al iniciar la actividad
        mostrarImagenDePerfil(userDni);

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

        btnTienda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                abrirTienda();
            }
        });

        btnBiblioteca.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                abrirBiblioteca();
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

    private void abrirBiblioteca() {
    //    Intent intent = new Intent(PerfilActivity.this, BibliotecaActivity.class);
    //    startActivity(intent);
    }

    private void abrirTienda() {
        Intent intent = new Intent(PerfilActivity.this, TiendaActivity.class);
        startActivity(intent);
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
    public void mostrarImagenDePerfil(String dni) {  // Acepta `dni` como parámetro
        byte[] imagenBytes = dbHelper.recuperarImagenDeUsuario(dni);

        if (imagenBytes != null) {
            // Convertir el array de bytes a Bitmap y mostrarlo en un ImageView
            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
            imageViewPerfil.setImageBitmap(imagenBitmap);
        } else {
            Log.d("PerfilActivity", "No se encontró ninguna imagen para el usuario");
            // Opcional: Muestra una imagen por defecto si `imagenBytes` es null
            imageViewPerfil.setImageResource(R.drawable.icono);
        }
    }

    // Método para recuperar la imagen como byte array y convertirla a Bitmap
    private Bitmap recuperarImagenDeBaseDeDatos(String dni) {
        byte[] imagenBytes = dbHelper.recuperarImagenDeUsuario(dni);
        if (imagenBytes != null) {
            return BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
        }
        return null;
    }

    // Método para tomar una foto con la cámara
    private void hacerFotoConCamara() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResultLauncher.launch(cameraIntent); // Lanzar la actividad de la cámara
    }

    // Método para cargar la imagen desde la galería
    private void cargarImagenDesdeGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryResultLauncher.launch(galleryIntent);  // Lanzar la galería para seleccionar una imagen
    }

    // Método para mostrar los datos del usuario
    private void mostrarDatosUsuario(String dni) {
        Cursor cursor = dbHelper.obtenerDatosUsuario(dni);
        if (cursor != null && cursor.moveToFirst()) {
            // Asignar los valores a los campos si se encuentran
            asignarValorEditText(cursor.getColumnIndex("dni"), cursor, editTextDni, "dni");
            asignarValorEditText(cursor.getColumnIndex("nombre"), cursor, editTextNombre, "nombre");
            asignarValorEditText(cursor.getColumnIndex("email"), cursor, editTextEmail, "email");
            asignarValorEditText(cursor.getColumnIndex("password"), cursor, editTextPassword, "password");
            cursor.close();
        } else {
            mostrarError("No se encontraron datos para el DNI: " + dni);
        }
    }

    // Método para asignar valores a los EditText
    private void asignarValorEditText(int columnIndex, Cursor cursor, EditText editText, String columna) {
        if (columnIndex != -1) {
            String valor = cursor.getString(columnIndex);
            if (valor != null && !valor.isEmpty()) {
                editText.setText(valor);
            } else {
                mostrarError("El valor de '" + columna + "' está vacío.");
            }
        } else {
            mostrarError("Columna '" + columna + "' no encontrada");
        }
    }

    // Método para mostrar mensajes de error
    private void mostrarError(String mensaje) {
        Toast.makeText(PerfilActivity.this, mensaje, Toast.LENGTH_LONG).show();
        Log.d("PerfilActivity", mensaje);
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


