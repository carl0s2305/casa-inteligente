package com.example.casainteligente;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class Recamara1Activity extends AppCompatActivity {

    private Switch switchLuz, switchPuerta;
    private TextView txtEstadoLuz, txtEstadoPuerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recamara1);

        switchLuz = findViewById(R.id.switchLuz);
        switchPuerta = findViewById(R.id.switchPuerta);
        txtEstadoLuz = findViewById(R.id.txtEstadoLuz);
        txtEstadoPuerta = findViewById(R.id.txtEstadoPuerta);
        FloatingActionButton fabBack = findViewById(R.id.fabRegresar);

        fabBack.setOnClickListener(v -> finish());

        switchLuz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoLuz.setText("Luz encendida");
                enviarBluetooth("L");
            } else {
                txtEstadoLuz.setText("Luz apagada");
                enviarBluetooth("l");
            }
        });

        switchPuerta.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoPuerta.setText("Puerta abierta");
                enviarBluetooth("P");
            } else {
                txtEstadoPuerta.setText("Puerta cerrada");
                enviarBluetooth("p");
            }
        });
    }

    private void enviarBluetooth(String comando) {
        try {
            if (BluetoothController.socket != null && BluetoothController.socket.isConnected()) {
                OutputStream outputStream = BluetoothController.socket.getOutputStream();
                outputStream.write((comando + "\n").getBytes());  // ← AGREGAR '\n'
            } else {
                Toast.makeText(this, "Bluetooth no conectado", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al enviar comando", Toast.LENGTH_SHORT).show();
        }
    }
}
