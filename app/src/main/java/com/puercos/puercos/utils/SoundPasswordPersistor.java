package com.puercos.puercos.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.*;
import android.util.Log;

import com.puercos.puercos.model.SoundPassword;

/**
 * Created by germancampagno on 29/10/16.
 */

/**
 * Clase que maneja la persistencia de la ultima contraseña sonora generada por el usuario
 * */
public class SoundPasswordPersistor {

    // region Attributes
    private Activity activity;

    // MY_PREFS_NAME - a static String variable like:
    //public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String SOUND_PASSWORD_MY_PREFERENCES_KEY = "soundPasswordFile";
    public static final String SOUND_PASSWORD_KEY = "soundPassword";

    // endregion

    public SoundPasswordPersistor(Activity activity) {
        this.activity = activity;
    }

    public void setSoundPassword(SoundPassword soundPassword) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(SOUND_PASSWORD_MY_PREFERENCES_KEY, 0).edit();
        editor.putString(SOUND_PASSWORD_KEY, soundPassword.toString());
        editor.commit();
        Log.d("SoundPasswordPersistor","SetSoundPasswordOK");
    }

    public SoundPassword getSoundPassword() {
        SharedPreferences prefs = activity.getSharedPreferences(SOUND_PASSWORD_MY_PREFERENCES_KEY, 0);
        String soundPassword = prefs.getString(SOUND_PASSWORD_KEY, "");//"No name defined" is the default value.
        return new SoundPassword(soundPassword);
    }
}
