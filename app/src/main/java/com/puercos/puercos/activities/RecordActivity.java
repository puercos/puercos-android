package com.puercos.puercos.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.puercos.puercos.R;
import com.puercos.puercos.components.timerView.TimerView;

import org.firezenk.audiowaves.Visualizer;

public class RecordActivity extends AppCompatActivity {

    // region Attributes
    private static final int TIMER_LENGTH = 5;
    // endregion

    // region Views
    private TimerView mTimerView;
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
        this.audioVisualizer = (Visualizer) findViewById(R.id.record_screen_audio_visualizer);
    }
    // endregion
}
