package com.example.telequiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.telequiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeleQuizActivity extends AppCompatActivity {

    private TextView textTematica, textPregunta, textPuntaje;
    private RadioGroup opcionesGroup;
    private RadioButton opcion1, opcion2, opcion3;
    private Button btnSiguiente;
    private Button btnAnterior;

    private List<Pregunta> listaPreguntas;
    private int preguntaActual = 0;
    private int puntaje = 0;
    private String tematica;
    private boolean respondido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telequiz);

        // Enlazar vistas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textTematica = findViewById(R.id.textTematica);
        textPregunta = findViewById(R.id.textPregunta);
        textPuntaje = findViewById(R.id.textPuntaje);
        opcionesGroup = findViewById(R.id.opcionesGroup);
        opcion1 = findViewById(R.id.opcion1);
        opcion2 = findViewById(R.id.opcion2);
        opcion3 = findViewById(R.id.opcion3);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnAnterior = findViewById(R.id.btnAnterior);

        // Obtener temática
        tematica = getIntent().getStringExtra("tematica");
        textTematica.setText("Temática: " + tematica);

        // Cargar preguntas y mostrar la primera
        listaPreguntas = obtenerPreguntasPorTematica(tematica);
        Collections.shuffle(listaPreguntas);
        mostrarPregunta();

        // Evento selección de opción
        opcionesGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (!respondido) {
                btnSiguiente.setEnabled(true);
            }
        });

        // Evento botón siguiente
        btnSiguiente.setOnClickListener(v -> {
            if (!respondido) {
                verificarRespuesta();
            } else {
                siguientePregunta();
            }
        });

        // Evento botón anterior
        btnAnterior.setOnClickListener(v -> {
            if (preguntaActual > 0) {
                preguntaActual--;
                mostrarPregunta();
                btnSiguiente.setEnabled(false);
                btnAnterior.setEnabled(preguntaActual > 0);
            }
        });
    }

    private void mostrarPregunta() {
        respondido = false;
        btnSiguiente.setEnabled(false);
        opcionesGroup.clearCheck();

        Pregunta pregunta = listaPreguntas.get(preguntaActual);
        textPregunta.setText(pregunta.getEnunciado());

        List<String> opciones = new ArrayList<>(pregunta.getOpciones());
        Collections.shuffle(opciones);

        opcion1.setText(opciones.get(0));
        opcion2.setText(opciones.get(1));
        opcion3.setText(opciones.get(2));

        // Verificar si la pregunta ya ha sido respondida
        if (preguntaRespondida()) {
            // Si la pregunta ya fue respondida, colorear las opciones
            marcarRespuesta();
        }
    }

    private boolean preguntaRespondida() {
        // Verificar si la pregunta ya ha sido respondida
        Pregunta pregunta = listaPreguntas.get(preguntaActual);
        String respuestaSeleccionada = getRespuestaSeleccionada();
        return respuestaSeleccionada != null;
    }

    private String getRespuestaSeleccionada() {
        // Obtener la respuesta seleccionada por el usuario
        int checkedId = opcionesGroup.getCheckedRadioButtonId();
        if (checkedId != -1) {
            RadioButton seleccionada = findViewById(checkedId);
            return seleccionada.getText().toString();
        }
        return null;
    }

    private void marcarRespuesta() {
        // Marcar las opciones con color verde o rojo dependiendo de la respuesta
        Pregunta pregunta = listaPreguntas.get(preguntaActual);
        String respuestaCorrecta = pregunta.getRespuestaCorrecta();
        String respuestaSeleccionada = getRespuestaSeleccionada();

        if (respuestaSeleccionada != null) {
            if (respuestaSeleccionada.equals(respuestaCorrecta)) {
                puntaje += 2;
                actualizarColorRespuesta(Color.GREEN);
            } else {
                puntaje -= 2;
                actualizarColorRespuesta(Color.RED);
            }
        }
        textPuntaje.setText("Puntaje: " + puntaje);
    }

    private void actualizarColorRespuesta(int color) {
        // Cambiar el color de la respuesta seleccionada
        int checkedId = opcionesGroup.getCheckedRadioButtonId();
        RadioButton seleccionada = findViewById(checkedId);
        seleccionada.setTextColor(color);
    }

    private void verificarRespuesta() {
        respondido = true;
        btnSiguiente.setText("Siguiente");

        int checkedId = opcionesGroup.getCheckedRadioButtonId();
        RadioButton seleccionada = findViewById(checkedId);

        String respuesta = seleccionada.getText().toString();
        String correcta = listaPreguntas.get(preguntaActual).getRespuestaCorrecta();

        if (respuesta.equals(correcta)) {
            puntaje += 2;
            seleccionada.setTextColor(Color.GREEN);
        } else {
            puntaje -= 2;
            seleccionada.setTextColor(Color.RED);
        }

        textPuntaje.setText("Puntaje: " + puntaje);
    }

    private void siguientePregunta() {
        preguntaActual++;
        if (preguntaActual < listaPreguntas.size()) {
            btnSiguiente.setText("Responder");
            opcion1.setTextColor(Color.BLACK);
            opcion2.setTextColor(Color.BLACK);
            opcion3.setTextColor(Color.BLACK);
            mostrarPregunta();
        } else {
            terminarJuego();
        }
        btnAnterior.setEnabled(preguntaActual > 0);
    }

    private void terminarJuego() {
        Toast.makeText(this, "Juego terminado. Puntaje final: " + puntaje, Toast.LENGTH_LONG).show();
        finish();
    }

    private List<Pregunta> obtenerPreguntasPorTematica(String tematica) {
        List<Pregunta> preguntas = new ArrayList<>();

        switch (tematica) {
            case "Redes":
                preguntas.add(new Pregunta("¿Qué protocolo se utiliza para cargar páginas web?",
                        List.of("HTTP", "FTP", "SSH"), "HTTP"));
                preguntas.add(new Pregunta("¿Cuál de estas es una dirección IP privada?",
                        List.of("192.168.1.1", "8.8.8.8", "172.217.0.0"), "192.168.1.1"));
                preguntas.add(new Pregunta("¿Qué dispositivo conecta redes diferentes y dirige el tráfico?",
                        List.of("Router", "Switch", "Hub"), "Router"));
                preguntas.add(new Pregunta("¿Qué significa DNS?",
                        List.of("Domain Name System", "Digital Network Service", "Data Node Server"), "Domain Name System"));
                preguntas.add(new Pregunta("¿Qué tipo de red cubre un área pequeña, como una oficina?",
                        List.of("LAN", "WAN", "MAN"), "LAN"));
                break;

            case "Ciberseguridad":
                preguntas.add(new Pregunta("¿Qué es un Ransomware?",
                        List.of("Un tipo de malware que pide rescate", "Una red segura", "Un antivirus"), "Un tipo de malware que pide rescate"));
                preguntas.add(new Pregunta("¿Cuál es el objetivo del phishing?",
                        List.of("Robar información", "Mejorar la red", "Acelerar descargas"), "Robar información"));
                preguntas.add(new Pregunta("¿Qué protocolo cifra las comunicaciones web?",
                        List.of("HTTPS", "FTP", "HTTP"), "HTTPS"));
                preguntas.add(new Pregunta("¿Qué algoritmo de cifrado es asimétrico y se usa para firmas digitales?",
                        List.of("RSA", "AES", "MD5"), "RSA"));
                preguntas.add(new Pregunta("¿Para qué sirve un firewall?",
                        List.of("Proteger la red", "Mejorar la velocidad", "Conectar computadoras"), "Proteger la red"));
                break;

            case "Microondas":
                preguntas.add(new Pregunta("¿En qué rango de frecuencias suelen operar las redes Wi-Fi?",
                        List.of("2.4 GHz y 5 GHz", "1 MHz", "60 GHz"), "2.4 GHz y 5 GHz"));
                preguntas.add(new Pregunta("¿Qué problema es común en enlaces de microondas?",
                        List.of("Interferencia por lluvia", "Velocidad lenta", "Latencia"), "Interferencia por lluvia"));
                preguntas.add(new Pregunta("¿Qué es la zona de Fresnel?",
                        List.of("Área de propagación de señal", "Nombre de satélite", "Tipo de conector"), "Área de propagación de señal"));
                preguntas.add(new Pregunta("¿Qué ventaja tienen las comunicaciones por microondas?",
                        List.of("Gran ancho de banda", "Barato", "Menor cobertura"), "Gran ancho de banda"));
                preguntas.add(new Pregunta("¿Qué dispositivo se usa para enfocar señales de microondas?",
                        List.of("Antena parabólica", "Router", "Switch"), "Antena parabólica"));
                break;
        }

        return preguntas;
    }
}


// Clase de apoyo
class Pregunta {
    private final String enunciado;
    private final List<String> opciones;
    private final String respuestaCorrecta;

    public Pregunta(String enunciado, List<String> opciones, String respuestaCorrecta) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}
