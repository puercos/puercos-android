package com.puercos.puercos.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.puercos.puercos.R;
import com.puercos.puercos.components.timerView.TimerView;
import com.puercos.puercos.networking.NetworkListener;
import com.puercos.puercos.networking.NetworkManager;

import org.firezenk.audiowaves.Visualizer;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class RecordActivity extends AppCompatActivity {

    // region Attributes
    private static final int TIMER_LENGTH = 6;
    private int mInterval = 1000; // 5 seconds by default, can be changed later

    private Handler uiThread = new Handler();
    private Thread listeningThread;

    private Handler mHandler;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            int remainingTime = Integer.parseInt(mTxtTimer.getText().toString());
            if (remainingTime > 0) {
                // Decrements time and sets the new
                // remaining time to the TextView
                remainingTime -= 1;
                Log.d("RecordActivity", "Modifying remaining time " + mTxtTimer.getText().toString());
                mTxtTimer.setText(String.valueOf(remainingTime));
                mHandler.postDelayed(this, mInterval);
            } else {
                // Remaining time is now zero.
                // So we have to finish this activity and
                // continue the execution flow.
                mHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(RecordActivity.this, FinishEdittingPasswordActivity.class);
                startActivity(intent);
            }

        }
    };
    // endregion

    // region Views
    private TimerView mTimerView;
    private TextView mTxtTimer;
    private Visualizer audioVisualizer;
    // endregion

    // region Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initViews();

        audioVisualizer.startListening();
        mTimerView.start(TIMER_LENGTH);

        mHandler = new Handler();
        mHandler.postDelayed(updateTimerThread, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        uiThread.removeCallbacksAndMessages(null);
        listeningThread.interrupt();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
        uiThread.removeCallbacksAndMessages(null);
        listeningThread.interrupt();
    }

    @Override
    public void onBackPressed() {
        // Do nothing :D

        new AlertDialog.Builder(this)
            .setTitle("Cancelar la grabación?")
            .setMessage("Estás seguro de que querés cancelar la grabación?")
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RecordActivity.super.onBackPressed();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
            .show();

    }

    // endregion

    // region Configuration
    private void initViews() {
        this.mTimerView = (TimerView) findViewById(R.id.record_screen_timer);
        this.mTxtTimer = (TextView) findViewById(R.id.record_screen_txt_remaining_time);
        this.audioVisualizer = (Visualizer) findViewById(R.id.record_screen_audio_visualizer);
    }

    private void prepare() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                uiThread.post(new Runnable() {
                    @Override
                    public void run() {
                        int pitch =  pitchInHz > 0 ? (int) pitchInHz : 1;

                        Toast.makeText(RecordActivity.this, "Pitch is now => " + String.valueOf(pitchInHz), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        AudioProcessor p = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        listeningThread = new Thread(dispatcher);
        listeningThread.start();
    }
    // endregion
}
