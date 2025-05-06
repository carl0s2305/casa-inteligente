package com.example.casainteligente;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class PuertaPrincipalActivity extends AppCompatActivity {

    private Switch switchPuerta;
    private TextView txtEstadoPuerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puerta_principal); // Asegúrate de que el nombre coincida con tu layout XML

        switchPuerta = findViewById(R.id.switchPuerta);
        txtEstadoPuerta = findViewById(R.id.txtEstadoPuerta);
        FloatingActionButton fabBack = findViewById(R.id.fabRegresar);

        fabBack.setOnClickListener(v -> finish());

        switchPuerta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoPuerta.setText("Puerta abierta");
                enviarBluetooth("PP"); // Comando para abrir la puerta principal
            } else {
                txtEstadoPuerta.setText("Puerta cerrada");
                enviarBluetooth("pp"); // Comando para cerrar la puerta principal
            }
        });
    }

    private void enviarBluetooth(String comando) {
        try {
            if (BluetoothController.socket != null && BluetoothController.socket.isConnected()) {
                OutputStream outputStream = BluetoothController.socket.getOutputStream();
                outputStream.write((comando + "\n").getBytes()); // Se recomienda enviar \n si el Arduino espera comandos por línea
            } else {
                Toast.makeText(this, "Bluetooth no conectado", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al enviar comando", Toast.LENGTH_SHORT).show();
        }
    }
}
