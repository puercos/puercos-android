package com.puercos.puercos.utils;

import android.app.Activity;
import android.util.Log;

import com.puercos.puercos.R;
import com.puercos.puercos.model.SoundPassword;

/**
 * Created by fernandoortiz on 10/22/16.
 */

public class PasswordSoundPlayer extends SoundPlayer {

    // Atributos
    private static final String TAG = "PasswordSoundPlayer";
    private SoundPassword password;

    // Constructores
    public PasswordSoundPlayer(Activity context, SoundPassword password) {
        super(context);

        this.password = password;
        super.loadSound(context, R.raw.punch);
    }

    // Metodos publicos

    @Override
    public void play() {
        Log.d(TAG, "play: ");
        super.play();
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause: ");
        super.pause();
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop: ");
        super.stop();
    }
}
