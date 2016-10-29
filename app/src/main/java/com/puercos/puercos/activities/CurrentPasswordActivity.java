package com.puercos.puercos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.puercos.puercos.R;
import com.puercos.puercos.model.SoundPassword;
import com.puercos.puercos.utils.SoundPasswordPersistor;
import com.puercos.puercos.utils.sound.PasswordSoundPlayer;

public class CurrentPasswordActivity extends AppCompatActivity {

    private ImageButton playButton;

    private SoundPassword soundPassword;
    private PasswordSoundPlayer passwordSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_password);

        playButton = (ImageButton) findViewById(R.id.current_password_play_button);

        this.soundPassword = new SoundPasswordPersistor(this).getSoundPassword();
        if(!this.soundPassword.getPauses().isEmpty()) {
            this.passwordSoundPlayer = new PasswordSoundPlayer(this, this.soundPassword);
        }

//        playDefaultSound();
    }

    // Private methods
    public void playDefaultSound() {
        if (soundPassword == null || soundPassword.getPauses().isEmpty()) {
            return;
        }

        // passwordSoundPlayer se inicializa cuando
        // se recibe un SoundPassword desde el intent que genera el activity
        // Si esto no se recibe, entonces va a ser nulo (igualmente no deberia pasar)
        if (this.passwordSoundPlayer == null) {
            // Si es nulo, entonces finalizamos el metodo aqui.
            return;
        }

        // Verificamos que no este reproduciendo ya una contraseña.
        if (this.passwordSoundPlayer.isPlayingPassword()) {
            // Si es así y está reproduciendo una contraseña,
            // entonces no volvemos a reproducir otra más arriba.
            return;
        }

//        this.playButton.setImageResource(R.drawable.stop);
        this.passwordSoundPlayer.playPassword(new PasswordSoundPlayer.PasswordPlayingCompletionHandler() {
            @Override
            public void hasFinishedPlayingPassword() {
//                playButton.setImageResource(R.drawable.play);
            }
        });
    }

    // Selectors
    public void onPlayButtonClick(View view) {

        if (soundPassword == null || soundPassword.getPauses().isEmpty()) {
            return;
        }

        // passwordSoundPlayer se inicializa cuando
        // se recibe un SoundPassword desde el intent que genera el activity
        // Si esto no se recibe, entonces va a ser nulo (igualmente no deberia pasar)
        if (this.passwordSoundPlayer == null) {
            // Si es nulo, entonces finalizamos el metodo aqui.
            return;
        }

        // Verificamos que no este reproduciendo ya una contraseña.
        if (this.passwordSoundPlayer.isPlayingPassword()) {
            // Si es así y está reproduciendo una contraseña,
            // entonces no volvemos a reproducir otra más arriba.
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

    public void onModifyButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
