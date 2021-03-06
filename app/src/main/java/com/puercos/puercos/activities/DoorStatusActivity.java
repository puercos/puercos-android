package com.puercos.puercos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.puercos.puercos.R;
import com.puercos.puercos.model.SoundPassword;
import com.puercos.puercos.networking.NetworkListener;
import com.puercos.puercos.networking.NetworkManager;

public class DoorStatusActivity extends BaseActivity {

    // Attributes
    static final String TAG = "DOOR_STATUS_ACTIVITY";
    private NetworkManager manager;

    // Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_status);

        this.manager = NetworkManager.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        // do nothing

        //super.onBackPressed();
    }

    // Handlers
    public void onOpenDoorClick(View view) {
        this.manager.openDoor(new NetworkListener() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String errorReason) {

            }
        });
    }
    public void onCloseDoorClick(View view) {
        this.manager.closeDoor(new NetworkListener() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String errorReason) {

            }
        });
    }

    // Este metodo se ejecuta cuando se toca el boton
    // "Escuchar contraseña"
    public void onCurrentPasswordClick(View view) {
        // Creamos un intent que tiene como objetivo CurrentPasswordActivity
        Intent intent = new Intent(this, CurrentPasswordActivity.class);
        // Y lo lanzamos
        startActivity(intent);
    }
}
