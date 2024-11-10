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

public class LoginActivity extends AppCompatActivity {
    private EditText editTextDni, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Inicio Sesion");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextDni = findViewById(R.id.editTextDni);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);


        dbHelper = new DBHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = editTextDni.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValidUser = dbHelper.verificarUsuario(dni, password);

                    if (isValidUser) {

                        Intent intent = new Intent(LoginActivity.this, TiendaActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.toast_invalid_credentials), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
