package com.puercos.puercos.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puercos.puercos.R;
import com.puercos.puercos.model.AudioClipListener;
import com.puercos.puercos.model.AudioClipRecorder;
import com.puercos.puercos.model.SoundPassword;

public class RecordActivity extends AppCompatActivity implements AudioClipListener {

    // Componentes visuales
    private RecordButton mRecordButton = null;
    private PlayButton mPlayButton = null;
    private TextView mTextView = null;

    // Sincronizacion de grabacion y reproduccion de audio
    private boolean mStartRecording = false;
    private boolean mStartPlaying = false;

    // Thread que maneja la grabacion de audio, para no bloquear la UI
    private Thread recordThread = new Thread(new Runnable() {
        @Override
        public void run() {
            audioClipRecorder.startRecording();
        }
    });

    // Objeto que se encarga de la grabacion del audio
    private AudioClipRecorder audioClipRecorder;

    // Array que contiene informacion sobre el audio obtenido
    private short[] audioData = new short[1024];
    private int index = 0;

    // Deteccion de sonidos
    private int clapCounter = 0;
    long lastClapTime = 0;
    long pause = 0;
    SoundPassword soundPassword;
    static final String SOUND_PASSWORD_DESCRIPTION_TAG = "sound_password_description"; // 5 seconds by default, can be changed later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos el objeto encargado de la grabacion del audio
        audioClipRecorder = new AudioClipRecorder(this);

        configurarComponentesVisuales();

        this.soundPassword = new SoundPassword();
    }

    /**
     *
     * Metodo que debe ser implementado para recibir la informacion del audio grabado
     *
     * */
    @Override
    public boolean heard(final short[] audioData, int sampleRate) {

        // Actualizo la informacion sobre el audio obtenido hasta el momento
        this.audioData = audioData;

        for (index=0; index<audioData.length; index++) {
            if (audioData[index] > 20000) {

                clapCounter++;

                long now = System.currentTimeMillis();

                if (lastClapTime == 0) {
                    // Todavia no se setteo nada.
                    // Es el primer shake
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(RecordActivity.this, "FIRST CLAP!" + clapCounter, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // no es el primer shake,
                    // anteriormente ya se sacudio el dispositivo
                    pause = now - lastClapTime;
                    soundPassword.addPauseInMilliseconds((int)pause);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(RecordActivity.this, "CLAP: " + pause, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                lastClapTime = now;

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        Toast.makeText(MainActivity.this, "CLAP!" + clapCounter, Toast.LENGTH_SHORT).show();
//                    }
//                });

                break;
            }
        }

        return false;
    }

    /**
     *
     * Metodo que configura los componentes visuales
     *
     * */
    private void configurarComponentesVisuales() {
        LinearLayout ll = new LinearLayout(this);

        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        mTextView = new TextView(this);
        mTextView.setText("TextView");
        ll.addView(mTextView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));

        setContentView(ll);
    }

    /**
     *
     * Botones personalizados
     *
     * */
    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    /**
     *
     * Manejo de los botones
     *
     * */
    public void onRecord(boolean start) {
        if (start) {

            recordThread.start();

//            audioClipRecorder.startRecordingForTime(5000,AudioClipRecorder.RECORDER_SAMPLERATE_8000, AudioFormat.ENCODING_PCM_16BIT);

//            new Thread(new Runnable() {
//                public void run() {
//                    audioClipRecorder.startRecording();
//                }
//            }).start();

        } else {

            audioClipRecorder.stopRecording();

            recordThread.stop();

            Intent intent = new Intent(RecordActivity.this, FinishEdittingPasswordActivity.class);
            if (soundPassword.toString() != null && !soundPassword.toString().isEmpty()) {
                intent.putExtra(SOUND_PASSWORD_DESCRIPTION_TAG, soundPassword.toString());
            } else {
                // sound password es null
                // porque el user no grabo nada
            }
            startActivity(intent);

//            String audioDataEncoded = audioData.toString();
//
//            if(audioDataEncoded.length() > 0) {
//                mTextView.setText("No data");
//            } else {
//                mTextView.setText("Data");
//            }
//            Log.d("Audio record data","Audio Data: " + audioData.length);
        }
    }
    public void onPlay(boolean start) {
        if (start) {
//            startPlaying();
        } else {
//            stopPlaying();
        }
    }
}
