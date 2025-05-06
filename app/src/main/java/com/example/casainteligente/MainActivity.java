package com.example.casainteligente;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1001;

    private BluetoothAdapter bluetoothAdapter;
    private boolean isBluetoothConnected = false;

    private Button btnBluetooth;
    private LinearLayout btnRecamara1, btnRecamara2, btnSala, btnGaraje, btnInvernadero, btnPuerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias de botones
        btnBluetooth = findViewById(R.id.btnBluetooth);
        btnRecamara1 = findViewById(R.id.btnRecamara1);
        btnRecamara2 = findViewById(R.id.btnRecamara2);
        btnSala = findViewById(R.id.btnSala);
        btnGaraje = findViewById(R.id.btnGaraje);
        btnInvernadero = findViewById(R.id.btnInvernadero);
        btnPuerta = findViewById(R.id.btnPuerta);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Botón para conectar Bluetooth
        btnBluetooth.setOnClickListener(v -> {
            if (checkAndRequestPermissions()) {
                activarYConectarBluetooth();
            }
        });

        // Accesos a secciones
        btnRecamara1.setOnClickListener(v -> abrirSiConectado(Recamara1Activity.class));
        btnRecamara2.setOnClickListener(v -> abrirSiConectado(Recamara2Activity.class));
        btnSala.setOnClickListener(v -> abrirSiConectado(SalaActivity.class));
        btnGaraje.setOnClickListener(v -> abrirSiConectado(GarajeActivity.class));
        btnInvernadero.setOnClickListener(v -> abrirSiConectado(InvernaderoActivity.class));
        btnPuerta.setOnClickListener(v -> abrirSiConectado(PuertaPrincipalActivity.class));
    }

    private void abrirSiConectado(Class<?> destino) {
        if (isBluetoothConnected) {
            startActivity(new Intent(this, destino));
        } else {
            Toast.makeText(this, "Primero debes conectar al HC-05", Toast.LENGTH_SHORT).show();
        }
    }

    // Verificación de permisos para Android 12+
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        },
                        REQUEST_BLUETOOTH_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    // Activación del Bluetooth y conexión
    private void activarYConectarBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no soportado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_BLUETOOTH_PERMISSIONS);
                return;
            }

            bluetoothAdapter.enable();
            Toast.makeText(this, "Activando Bluetooth...", Toast.LENGTH_SHORT).show();
        }

        mostrarDispositivos();
    }

    // Muestra dispositivos emparejados
    private void mostrarDispositivos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
            return;
        }

        Set<BluetoothDevice> dispositivos = bluetoothAdapter.getBondedDevices();

        if (dispositivos.isEmpty()) {
            Toast.makeText(this, "No hay dispositivos emparejados", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] nombres = new String[dispositivos.size()];
        BluetoothDevice[] arreglo = new BluetoothDevice[dispositivos.size()];
        int i = 0;
        for (BluetoothDevice d : dispositivos) {
            nombres[i] = d.getName();
            arreglo[i] = d;
            i++;
        }

        new AlertDialog.Builder(this)
                .setTitle("Selecciona un dispositivo")
                .setItems(nombres, (dialog, which) -> conectarDispositivo(arreglo[which]))
                .show();
    }

    // Conexión al dispositivo
    private void conectarDispositivo(BluetoothDevice device) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
            return;
        }

        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(BTMODULEUUID);
            socket.connect();
            BluetoothController.socket = socket;
            isBluetoothConnected = true;
            Toast.makeText(this, "Conectado a HC-05", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al conectar: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            isBluetoothConnected = false;
        }
    }

    // Resultado de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activarYConectarBluetooth();
            } else {
                Toast.makeText(this, "Permisos de Bluetooth denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
