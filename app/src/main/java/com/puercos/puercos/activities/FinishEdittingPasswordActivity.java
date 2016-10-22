package com.puercos.puercos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.puercos.puercos.R;
import com.puercos.puercos.model.SoundPassword;
import com.puercos.puercos.networking.NetworkListener;
import com.puercos.puercos.networking.NetworkManager;
import com.puercos.puercos.utils.sound.PasswordSoundPlayer;

public class FinishEdittingPasswordActivity extends BaseActivity {

    // Views
    private ImageButton playButton;

    // Attributes
    private NetworkManager manager;
    private SoundPassword soundPassword;
    private PasswordSoundPlayer passwordSoundPlayer;

    // Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_editting_password);

        this.playButton = (ImageButton) findViewById(R.id.finish_editting_password_play_button);

        String soundPasswordDescription = getIntent().getStringExtra(AccelerometerRecordActivity.SOUND_PASSWORD_DESCRIPTION_TAG);

        if (soundPasswordDescription != null) {
            this.soundPassword = new SoundPassword(soundPasswordDescription);
            this.passwordSoundPlayer = new PasswordSoundPlayer(this, this.soundPassword);
        }

        this.manager = NetworkManager.getInstance(this);

    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    // Actions

    public void onPlayButtonClick(View view) {

        // passwordSoundPlayer se inicializa cuando
        // se recibe un SoundPassword desde el intent que genera el activity
        // Si esto no se recibe, entonces va a ser nulo (igualmente no deberia pasar)
        if (this.passwordSoundPlayer == null) {
            // Si es nulo, entonces finalizamos el metodo aqui.
            return;
        }

        this.playButton.setImageResource(R.drawable.stop);
        this.passwordSoundPlayer.playPassword(new PasswordSoundPlayer.PasswordPlayingCompletionHandler() {
            @Override
            public void hasFinishedPlayingPassword() {
                playButton.setImageResource(R.drawable.play);
            }
        });
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
