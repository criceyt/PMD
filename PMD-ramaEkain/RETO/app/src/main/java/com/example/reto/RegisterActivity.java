package com.example.reto;

import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto.database.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    // Definición de las vistas de la actividad
    private EditText editTextDni, editTextEmail, editTextNombre, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister, buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Registrar");  // Establece el título de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // Asigna el layout correspondiente a la actividad

        // Inicializa los componentes de la interfaz de usuario
        editTextDni = findViewById(R.id.editTextDni);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Configura el evento de clic para el botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera los datos ingresados por el usuario
                String dni = editTextDni.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String nombre = editTextNombre.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Verifica que todos los campos estén llenos
                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(email) || TextUtils.isEmpty(nombre) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    // Muestra un mensaje de error si algún campo está vacío
                    Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    // Verifica que las contraseñas coincidan
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    // Si todos los campos son válidos, se intenta registrar el usuario en la base de datos
                    DBHelper dbHelper = new DBHelper(RegisterActivity.this);
                    boolean isRegistered = dbHelper.registrarUsuario(dni, email, nombre, password);

                    // Si el registro es exitoso
                    if (isRegistered) {
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        // Redirige al usuario a la pantalla de inicio de sesión
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);  // Inicia la actividad de login
                        finish();  // Finaliza la actividad de registro
                    } else {
                        // Si hay un error (por ejemplo, el DNI o el correo ya están en uso)
                        Toast.makeText(RegisterActivity.this, "Error al registrar. El DNI o el email ya están en uso.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Configura el evento de clic para el botón de ir a Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si el usuario ya tiene una cuenta, puede ir a la pantalla de inicio de sesión
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);  // Inicia la actividad de login
            }
        });
    }
}
