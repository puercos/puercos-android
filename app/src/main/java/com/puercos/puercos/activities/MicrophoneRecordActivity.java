package com.puercos.puercos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.puercos.puercos.R;
import com.puercos.puercos.model.AudioClipListener;
import com.puercos.puercos.model.AudioClipRecorder;
import com.puercos.puercos.model.SoundPassword;

import com.puercos.puercos.components.timerView.TimerView;

public class MicrophoneRecordActivity extends BaseActivity implements AudioClipListener {

    // region Views
    private TimerView mTimerView;
    private TextView mTxtTimer;
    // endregion

    // region Attributes
    private AudioClipRecorder audioClipRecorder; // Objeto que se encarga de la grabacion del audio
    private SoundPassword soundPassword;
//    private Visualizer audioVisualizer; // BUG: el visualizer accede al mic y rompe con el AudioClipRecorder

    private Handler mHandler; // Hanlder para el updateTimerThread
    private Runnable updateTimerThread; // Thread que maneja la logica del timer
    private Thread recordAudioThread; // Thread que maneja la grabacion de audio  (para no bloquear la UI)

    private static final int TIMER_LENGTH = 6;
    private int mInterval = 1000; // Intervalo para delay del thread. Equivale a 1 segundo
    private int index = 0;
    private int clapCounter = 0;

    private long lastClapTime = 0;
    private long pause = 0;

    private static final String SOUND_PASSWORD_DESCRIPTION_TAG = "sound_password_description";
    // endregion

    // region Activity life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initViews();
        initAttributes();
        initThreads();
    }
    // endregion

    // region Configuration
    private void initViews() {
        this.mTimerView = (TimerView) findViewById(R.id.record_screen_timer);
        this.mTxtTimer = (TextView) findViewById(R.id.record_screen_txt_remaining_time);
    }
    private void initAttributes() {
        audioClipRecorder = new AudioClipRecorder(this);
        soundPassword = new SoundPassword();
    }
    private void initThreads() {
        updateTimerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int remainingTime = Integer.parseInt(mTxtTimer.getText().toString());
                if (remainingTime > 0) {
                    // Decrements time and sets the new remaining time to the TextView
                    remainingTime -= 1;
                    Log.d("MicActivity", "Modifying remaining time " + mTxtTimer.getText().toString());
                    mTxtTimer.setText(String.valueOf(remainingTime));
                    mHandler.postDelayed(this, mInterval);
                } else {
                    // Remaining time is now zero.
                    // So we have to finish this activity and
                    // continue the execution flow.
                    finishRecording();
                }
            }
        });
        recordAudioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                audioClipRecorder.startRecording();
//            audioClipRecorder.startRecordingForTime(5000,AudioClipRecorder.RECORDER_SAMPLERATE_8000, AudioFormat.ENCODING_PCM_16BIT);
            }
        });

        recordAudioThread.start();
        mTimerView.start(TIMER_LENGTH);
        mHandler = new Handler();
        mHandler.postDelayed(updateTimerThread, 0);
    }
    // endregion

    // region Private Methods
    /**
     *
     * Metodo que debe ser implementado para recibir la informacion del audio grabado
     *
     * */
    @Override
    public boolean heard(final short[] audioData, int sampleRate) {

        for (index=0; index<audioData.length; index++) {
            if (audioData[index] > 20000) {

                clapCounter++;

                long now = System.currentTimeMillis();

                if (lastClapTime == 0) {
                    // Todavia no se setteo nada. Es el primer golpe
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(MicrophoneRecordActivity.this, "FIRST CLAP!" + clapCounter, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // No es el primer shake. Anteriormente ya se detectaron golpes
                    pause = now - lastClapTime;
                    soundPassword.addPauseInMilliseconds((int)pause);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(MicrophoneRecordActivity.this, "CLAP: " + pause, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                lastClapTime = now;

                break;
            }
        }

        return false;
    }

    /**
     *
     * Metodo que maneja la finalizacion del grabado del audio
     *
     * */
    public void finishRecording() {
        audioClipRecorder.stopRecording();

        Intent intent = new Intent(MicrophoneRecordActivity.this, FinishEdittingPasswordActivity.class);
        if (soundPassword.toString() != null && !soundPassword.toString().isEmpty()) {
            intent.putExtra(SOUND_PASSWORD_DESCRIPTION_TAG, soundPassword.toString());
        } else {
            // sound password es null
            // porque el user no grabo nada
        }
        startActivity(intent);
    }
    // endregion
}
