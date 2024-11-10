package com.example.reto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reto.database.DBHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextDni, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Inicio Sesion");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de los componentes de la UI
        editTextDni = findViewById(R.id.editTextDni);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Crear una instancia del DBHelper
        dbHelper = new DBHelper(this);

        // Acción para el botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = editTextDni.getText().toString().trim();  // Usamos el valor ingresado en el EditText
                String password = editTextPassword.getText().toString().trim();

                // Mostrar el DNI y la contraseña en el log para ver qué valores se están ingresando
                Log.d("LoginActivity", "DNI ingresado: " + dni);
                Log.d("LoginActivity", "Contraseña ingresada: " + password);

                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(password)) {
                    // Si algún campo está vacío, mostramos un mensaje
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    // Verificar si las credenciales son correctas llamando a verificarUsuario()
                    boolean isValidUser = dbHelper.verificarUsuario(dni, password);

                    Log.d("LoginActivity", "¿Es usuario válido?: " + isValidUser);

                    if (isValidUser) {
                        // Si las credenciales son correctas, guarda el DNI en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userDni", dni);  // Guardamos el DNI
                        editor.apply();

                        // Iniciar la siguiente actividad (en este caso, TiendaActivity)
                        Intent intent = new Intent(LoginActivity.this, TiendaActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad de login para evitar que el usuario vuelva al login
                    } else {
                        // Si las credenciales son incorrectas, mostramos un mensaje
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_invalid_credentials), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // Acción para el botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir al usuario a la actividad de registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

