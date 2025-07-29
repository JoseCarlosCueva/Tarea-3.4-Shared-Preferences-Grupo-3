package com.example.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_REMEMBER_ME = "remember_me";
    
    private TextView tvWelcome, tvUserInfo;
    private MaterialButton btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        // Inicializar vistas
        initViews();
        
        // Mostrar información del usuario
        displayUserInfo();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void displayUserInfo() {
        String username = sharedPreferences.getString(KEY_USERNAME, "Usuario");
        
        tvWelcome.setText(getString(R.string.welcome_dashboard, username));
        tvUserInfo.setText(getString(R.string.user_info, username));
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        // Verificar si "Recordarme" está marcado
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        
        // Solo limpiar datos si "Recordarme" NO está marcado
        if (!rememberMe) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_USERNAME);
            editor.putBoolean(KEY_REMEMBER_ME, false);
            editor.apply();
        }
        // Si "Recordarme" está marcado, mantener los datos pero cerrar sesión
        
        // Regresar a la pantalla de login
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 