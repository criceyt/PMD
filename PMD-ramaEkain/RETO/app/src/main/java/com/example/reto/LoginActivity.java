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

    // Definición de los elementos de la interfaz de usuario
    private EditText editTextDni, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private DBHelper dbHelper;  // Instancia para acceder a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Inicio Sesion");  // Establece el título de la actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Establece el layout de la actividad

        // Inicializa los componentes de la interfaz de usuario
        editTextDni = findViewById(R.id.editTextDni);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Inicializa el DBHelper, que proporciona acceso a la base de datos
        dbHelper = new DBHelper(this);

        // Configura el evento del botón de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera los datos ingresados por el usuario en los campos de texto
                String dni = editTextDni.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Verifica si los campos de DNI o contraseña están vacíos
                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(password)) {
                    // Muestra un mensaje si los campos están vacíos
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    // Llama al método para verificar las credenciales del usuario
                    Usuario usuario = dbHelper.verificarUsuario(dni, password);

                    // Si el usuario es encontrado en la base de datos
                    if (usuario != null) {
                        // Crea un Intent para pasar al siguiente Activity (TiendaActivity)
                        Intent intent = new Intent(LoginActivity.this, TiendaActivity.class);
                        intent.putExtra("usuario_id", usuario.getId());  // Pasa el ID del usuario a TiendaActivity
                        startActivity(intent);  // Inicia la actividad
                        finish();  // Finaliza la actividad de login
                    } else {
                        // Si las credenciales no son correctas, muestra un mensaje
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_invalid_credentials), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Configura el evento del botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir la actividad de registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);  // Inicia la actividad de registro
            }
        });
    }
}
