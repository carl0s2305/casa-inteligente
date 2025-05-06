package com.example.casainteligente;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.OutputStream;

public class SalaActivity extends AppCompatActivity {

    private Switch switchLuz, switchVentilador;
    private TextView txtEstadoLuz, txtEstadoVentilador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_activty); // Asegúrate de que el nombre coincida con tu archivo XML

        switchLuz = findViewById(R.id.switchLuz);
        switchVentilador = findViewById(R.id.switchVentilador);
        txtEstadoLuz = findViewById(R.id.txtEstadoLuz);
        txtEstadoVentilador = findViewById(R.id.txtEstadoVentilador);
        FloatingActionButton fabBack = findViewById(R.id.fabRegresar);

        fabBack.setOnClickListener(v -> finish());

        switchLuz.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoLuz.setText("Luz encendida");
                enviarBluetooth("LS");
            } else {
                txtEstadoLuz.setText("Luz apagada");
                enviarBluetooth("ls");
            }
        });

        switchVentilador.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtEstadoVentilador.setText("Ventilador encendido");
                enviarBluetooth("VS");
            } else {
                txtEstadoVentilador.setText("Ventilador apagado");
                enviarBluetooth("vs");
            }
        });
    }

    private void enviarBluetooth(String comando) {
        try {
            if (BluetoothController.socket != null && BluetoothController.socket.isConnected()) {
                OutputStream outputStream = BluetoothController.socket.getOutputStream();
                outputStream.write((comando + "\n").getBytes()); // Enviar comando con salto de línea
            } else {
                Toast.makeText(this, "Bluetooth no conectado", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al enviar comando", Toast.LENGTH_SHORT).show();
        }
    }
}
