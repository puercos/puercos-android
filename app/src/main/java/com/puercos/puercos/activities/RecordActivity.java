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

import org.firezenk.audiowaves.Visualizer;

public class RecordActivity extends AppCompatActivity {

    // region Attributes
    private static final int TIMER_LENGTH = 6;
    private int mInterval = 1000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            Log.d("RecordActivity", "Modifying remaining time " + mTxtTimer.getText().toString());
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
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
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
    // endregion
}
