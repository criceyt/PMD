// LoginActivity.java
package com.example.reto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reto.database.DBHelper;
import com.example.reto.database.modelo.Usuario;

public class LoginActivity extends AppCompatActivity {

    // Declaración de las vistas que se utilizarán
    private EditText editTextDni, editTextPassword;  // EditText para que el usuario ingrese su DNI y contraseña
    private Button buttonRegister, buttonLogin;  // Botones para registrarse y para iniciar sesión
    private DBHelper dbHelper;  // Instancia del DBHelper para acceder a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Inicio Sesión");  // Establece el título de la actividad
        setContentView(R.layout.activity_login);  // Asigna el layout de esta actividad

        // Inicialización de las vistas
        editTextDni = findViewById(R.id.editTextDni);  // Asignación del campo de texto para el DNI
        editTextPassword = findViewById(R.id.editTextPassword);  // Asignación del campo de texto para la contraseña
        buttonRegister = findViewById(R.id.buttonRegister);  // Asignación del botón para registrarse
        buttonLogin = findViewById(R.id.buttonLogin);  // Asignación del botón para iniciar sesión

        // Inicialización del DBHelper para interactuar con la base de datos
        dbHelper = new DBHelper(this);

        // Acción cuando se hace clic en el botón "Iniciar sesión"
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String dni = editTextDni.getText().toString().trim();  // El DNI ingresado por el usuario
                String password = editTextPassword.getText().toString().trim();  // La contraseña ingresada por el usuario

                // Verificar si alguno de los campos está vacío
                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(password)) {
                    // Si los campos están vacíos, mostrar un mensaje al usuario
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    // Si los campos no están vacíos, proceder a verificar el usuario y contraseña en la base de datos
                    Usuario usuario = dbHelper.verificarUsuario(dni, password);  // Verifica las credenciales del usuario en la base de datos

                    if (usuario != null) {  // Si el usuario es encontrado en la base de datos
                        // Guardar el DNI del usuario en SharedPreferences para mantener la sesión
                        getSharedPreferences("user_prefs", MODE_PRIVATE)  // Acceder a las preferencias compartidas
                                .edit()  // Iniciar la edición de las preferencias
                                .putString("usuario_dni", usuario.getId())  // Guardar el ID del usuario (DNI)
                                .apply();  // Aplicar los cambios

                        // Redirigir a la actividad TiendaActivity
                        Intent intent = new Intent(LoginActivity.this, TiendaActivity.class);
                        startActivity(intent);  // Iniciar la actividad de la tienda
                        finish();  // Finalizar la actividad de login para que el usuario no pueda regresar

                    } else {
                        // Si las credenciales son incorrectas, mostrar un mensaje de error
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_invalid_credentials), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Acción cuando se hace clic en el botón "Registrarse"
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir al usuario a la actividad de registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);  // Iniciar la actividad de registro
            }
        });
    }
}

