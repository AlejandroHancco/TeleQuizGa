package com.example.telequiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Button btnRedes, btnCiber, btnMicroondas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura la barra de herramientas (Toolbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cambia el texto del título en la barra de herramientas
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("TeleQuiz - Lab 2");

        // Encuentra los botones
        btnRedes = findViewById(R.id.btnRedes);
        btnCiber = findViewById(R.id.btnCiberseguridad);
        btnMicroondas = findViewById(R.id.btnMicroondas);

        // Asigna acciones a los botones
        btnRedes.setOnClickListener(v -> startGame("Redes"));
        btnCiber.setOnClickListener(v -> startGame("Ciberseguridad"));
        btnMicroondas.setOnClickListener(v -> startGame("Microondas"));
    }

    // Lanza la actividad del quiz con la temática seleccionada
    private void startGame(String tematica) {
        Intent intent = new Intent(MainActivity.this, TeleQuizActivity.class);
        intent.putExtra("tematica", tematica);
        startActivity(intent);
    }
}
