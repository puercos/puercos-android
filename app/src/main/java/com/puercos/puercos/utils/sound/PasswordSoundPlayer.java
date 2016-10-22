package com.puercos.puercos.utils.sound;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.puercos.puercos.R;
import com.puercos.puercos.model.SoundPassword;

/**
 * Created by fernandoortiz on 10/22/16.
 */

public class PasswordSoundPlayer extends SoundPlayer {

    // Inner types
    public interface PasswordPlayingCompletionHandler {
        // Esta funcion la llamamos cuando se
        // termina de reproducir la contraseña completa.
        void hasFinishedPlayingPassword();
    }

    // Atributos
    private static final String TAG = "PasswordSoundPlayer";
    private SoundPassword password;
    private boolean isPlayingPassword = false;

    // Constructores
    public PasswordSoundPlayer(Activity context, SoundPassword password) {
        super(context);

        this.password = password;
        super.loadSound(context, R.raw.punch);
    }

    // Getters
    public boolean isPlayingPassword() {
        return this.isPlayingPassword;
    }

    // Metodos publicos

    public void playPassword(final PasswordPlayingCompletionHandler completionHandler) {
        Log.d(TAG, "play: ");
        this.isPlayingPassword = true;

        int pauseAcummulator = 0;
        int index = 0;
        boolean isLastPause = false;
        for(int pause : password.getPauses()) {
            if (index == password.getPauses().size() - 1) {
                isLastPause = true;
            }
            final boolean isThisTheLastPause = isLastPause;

            pauseAcummulator += pause;
            Log.d(TAG, "play: Ponemos play con una pausa de " + String.valueOf(pauseAcummulator));

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    PasswordSoundPlayer.super.play();
                    PasswordSoundPlayer.super.stop();
                    if(isThisTheLastPause) {
                        isPlayingPassword = false;
                        completionHandler.hasFinishedPlayingPassword();
                    }
                }
            }, pauseAcummulator);
            index++;
        }
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
