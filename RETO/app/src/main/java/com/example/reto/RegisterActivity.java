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
    // Declaración de las vistas de la interfaz
    private EditText editTextDni, editTextEmail, editTextNombre, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister, buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Registrar");  // Establece el título de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // Asigna el layout XML correspondiente

        // Inicialización de las vistas de la interfaz
        editTextDni = findViewById(R.id.editTextDni);  // EditText para el DNI
        editTextEmail = findViewById(R.id.editTextEmail);  // EditText para el email
        editTextNombre = findViewById(R.id.editTextNombre);  // EditText para el nombre
        editTextPassword = findViewById(R.id.editTextPassword);  // EditText para la contraseña
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);  // EditText para confirmar la contraseña
        buttonRegister = findViewById(R.id.buttonRegister);  // Botón para registrar
        buttonLogin = findViewById(R.id.buttonLogin);  // Botón para ir a la pantalla de login

        // Acción del botón "Registrar"
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene los valores de los campos de texto
                String dni = editTextDni.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String nombre = editTextNombre.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Verificar que los campos no estén vacíos
                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(email) || TextUtils.isEmpty(nombre) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    // Si hay campos vacíos, muestra un mensaje de error
                    Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    // Si las contraseñas no coinciden, muestra un mensaje de error
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    // Si todo está correcto, realiza el registro en la base de datos
                    DBHelper dbHelper = new DBHelper(RegisterActivity.this);
                    boolean isRegistered = dbHelper.registrarUsuario(dni, email, nombre, password);

                    if (isRegistered) {
                        // Si el registro es exitoso, muestra un mensaje de éxito
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        // Redirige al LoginActivity después del registro
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();  // Finaliza la actividad de registro
                    } else {
                        // Si el registro falla (por ejemplo, si el DNI o el email ya están en uso), muestra un error
                        Toast.makeText(RegisterActivity.this, "Error al registrar. El DNI o el email ya están en uso.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Acción del botón "Login" (para ir al LoginActivity)
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia el LoginActivity si el usuario ya tiene cuenta
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
