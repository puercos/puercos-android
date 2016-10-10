package com.puercos.puercos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.puercos.puercos.R;
import com.puercos.puercos.model.SoundPassword;
import com.puercos.puercos.networking.NetworkListener;
import com.puercos.puercos.networking.NetworkManager;

public class FinishEdittingPasswordActivity extends BaseActivity {

    private NetworkManager manager;
    private SoundPassword soundPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_editting_password);


        String soundPasswordDescription = getIntent().getStringExtra(AccelerometerRecordActivity.SOUND_PASSWORD_DESCRIPTION_TAG);

        if (soundPasswordDescription != null) {
            this.soundPassword = new SoundPassword(soundPasswordDescription);
        }

        this.manager = NetworkManager.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    public void onNextButtonClick(View view) {
        if (soundPassword == null || soundPassword.toString().isEmpty()) {
            Intent intent = new Intent(FinishEdittingPasswordActivity.this, DoorStatusActivity.class);
            startActivity(intent);
        } else {

            this.manager.changePassword(soundPassword, new NetworkListener() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(FinishEdittingPasswordActivity.this, DoorStatusActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(String errorReason) {
                    // Something went wrong...
                    Toast.makeText(FinishEdittingPasswordActivity.this, "Algo salio mal", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onRepeatButtonClick(View view) {
        Intent intent = new Intent(FinishEdittingPasswordActivity.this, RecordActivity.class);
        startActivity(intent);
    }
}
