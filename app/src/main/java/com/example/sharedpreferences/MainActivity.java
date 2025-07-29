package com.example.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REMEMBER_ME = "remember_me";
    
    private TextInputEditText etUsername, etPassword;
    private TextInputLayout tilUsername, tilPassword;
    private CheckBox cbRememberMe;
    private MaterialButton btnLogin;
    private TextView tvStatus;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        // Inicializar vistas
        initViews();
        
        // Cargar datos guardados
        loadSavedData();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvStatus = findViewById(R.id.tvStatus);
    }

    private void loadSavedData() {
        // Verificar si se debe recordar el usuario
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        cbRememberMe.setChecked(rememberMe);
        
        // Si está marcado "Recordarme", cargar el nombre de usuario
        if (rememberMe) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            if (!TextUtils.isEmpty(savedUsername)) {
                etUsername.setText(savedUsername);
            }
        }
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        
        // Listener para el checkbox "Recordarme"
        cbRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Si se desmarca "Recordarme", limpiar los datos guardados
            if (!isChecked) {
                clearSavedData();
            }
        });
    }

    private void performLogin() {
        // Obtener valores de los campos
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showError(getString(R.string.error_empty_fields));
            return;
        }
        
        // Simular validación de credenciales
        if (validateCredentials(username, password)) {
            // Guardar datos si "Recordarme" está marcado
            if (cbRememberMe.isChecked()) {
                saveUserData(username);
            } else {
                clearSavedData();
            }
            
            showSuccess(getString(R.string.success_login));
            
            // Mostrar mensaje de bienvenida
            String welcomeMessage = getString(R.string.welcome_message, username);
            Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show();
            
            // Navegar al Dashboard
            navigateToDashboard();
            
        } else {
            showError(getString(R.string.error_invalid_credentials));
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Simulación de validación básica
        // En una aplicación real, esto se conectaría a una base de datos
        return username.equals("jose") && password.equals("jose2001");
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish(); // Cerrar la actividad de login
    }

    private void saveUserData(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_REMEMBER_ME, true);
        editor.apply();
    }

    private void clearSavedData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.putBoolean(KEY_REMEMBER_ME, false);
        editor.apply();
    }

    private void showError(String message) {
        tvStatus.setText(message);
        tvStatus.setTextColor(getResources().getColor(R.color.red));
        tvStatus.setVisibility(View.VISIBLE);
        
        // Ocultar el mensaje después de 3 segundos
        tvStatus.postDelayed(() -> tvStatus.setVisibility(View.GONE), 3000);
    }

    private void showSuccess(String message) {
        tvStatus.setText(message);
        tvStatus.setTextColor(getResources().getColor(R.color.green));
        tvStatus.setVisibility(View.VISIBLE);
        
        // Ocultar el mensaje después de 3 segundos
        tvStatus.postDelayed(() -> tvStatus.setVisibility(View.GONE), 3000);
    }
}