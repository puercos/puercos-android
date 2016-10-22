package com.puercos.puercos.utils.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by fernandoortiz on 10/22/16.
 */

public abstract class SoundPlayer {

    // Constants
    private static final String TAG = "SoundPlayer";

    // Attributes
    private SoundPool soundPool;
    private AudioManager audioManager;
    private int soundID;

    // Volume attributes
    private float actualVolume;
    private float maxVolume;
    private float volume;

    // Acá guardamos si el sonido ya
    // se termino de cargar en memoria
    private boolean loaded = false;

    // Acá guardamos si el sonido se
    // está reproduciendo en este momento
    private boolean isPlaying = false;

    // En el ejemplo utilizan este contador
    // para identificar el stream que se esta utilizando
    private int counter = 0;

    // Constructors
    public SoundPlayer(Activity context) {
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume  / maxVolume;

        context.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        }
        else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
    }

    // Getters
    public boolean isPlaying() {
        return this.isPlaying;
    }

    // Protected methods
    /**
     * Carga un sonido. Como es asincronico,
     * tambien se requiere una interfaz con un metodo
     * que se ejecute cuando finalizó de cargar
     * */
    protected void loadSound(Context context, final int _soundID) {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundID = sampleId;
                loaded = true;
            }
        });
        this.soundPool.load(context, _soundID, 1);
    }

    // Public interface
    public void play() {
        Log.d(TAG, "play: ");
        if (loaded && !isPlaying) {
            Log.d(TAG, "play: Esta reproduciendo!");
            soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f);
            counter = counter++;
            isPlaying = true;
        }
    }
    public void pause() {
        Log.d(TAG, "pause: Esta dentro del metodo pause");
        if (isPlaying) {
            Log.d(TAG, "pause: Esta pausando");
            soundPool.pause(soundID);
            isPlaying = false;
        }
    }
    public void stop() {
        Log.d(TAG, "stop: Esta dentro del metodo stop");
        if (isPlaying) {
            Log.d(TAG, "stop: Esta parando la reproduccion");
            soundPool.stop(soundID);
            isPlaying = false;
        }
    }

}
