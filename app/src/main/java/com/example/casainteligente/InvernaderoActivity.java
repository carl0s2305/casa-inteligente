package com.example.casainteligente;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class InvernaderoActivity extends AppCompatActivity {

    private Switch switchRiego;
    private TextView txtEstadoRiego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invernadero); // Asegúrate que coincida con el nombre del XML

        switchRiego = findViewById(R.id.switchRiego);
        txtEstadoRiego = findViewById(R.id.txtEstadoRiego);
        FloatingActionButton fabBack = findViewById(R.id.fabRegresar);

        fabBack.setOnClickListener(v -> finish());

        switchRiego.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoRiego.setText("Riego activado");
                enviarBluetooth("RI");
            } else {
                txtEstadoRiego.setText("Riego desactivado");
                enviarBluetooth("ri");
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
