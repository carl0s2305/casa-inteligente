package com.example.casainteligente;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class GarajeActivity extends AppCompatActivity {

    private Switch switchLuz, switchPuerta;
    private TextView txtEstadoLuz, txtEstadoPuerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage); // Asegúrate que el nombre coincida con tu archivo XML

        switchLuz = findViewById(R.id.switchLuz);
        switchPuerta = findViewById(R.id.switchPuerta);
        txtEstadoLuz = findViewById(R.id.txtEstadoLuz);
        txtEstadoPuerta = findViewById(R.id.txtEstadoPuerta);
        FloatingActionButton fabBack = findViewById(R.id.fabRegresar);

        fabBack.setOnClickListener(v -> finish());

        switchLuz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoLuz.setText("Luz encendida");
                enviarBluetooth("LG");
            } else {
                txtEstadoLuz.setText("Luz apagada");
                enviarBluetooth("lg");
            }
        });

        switchPuerta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoPuerta.setText("Puerta abierta");
                enviarBluetooth("PG");
            } else {
                txtEstadoPuerta.setText("Puerta cerrada");
                enviarBluetooth("pg");
            }
        });
    }

    private void enviarBluetooth(String comando) {
        try {
            if (BluetoothController.socket != null && BluetoothController.socket.isConnected()) {
                OutputStream outputStream = BluetoothController.socket.getOutputStream();
                outputStream.write((comando + "\n").getBytes()); // Recomendado enviar con salto de línea
            } else {
                Toast.makeText(this, "Bluetooth no conectado", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al enviar comando", Toast.LENGTH_SHORT).show();
        }
    }
}
