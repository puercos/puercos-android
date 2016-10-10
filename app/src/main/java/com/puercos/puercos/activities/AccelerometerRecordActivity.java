package com.puercos.puercos.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.puercos.puercos.R;
import com.puercos.puercos.components.timerView.TimerView;
import com.puercos.puercos.model.SoundPassword;
import com.puercos.puercos.utils.ShakeDetector;

import org.firezenk.audiowaves.Visualizer;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class AccelerometerRecordActivity extends BaseActivity {

    // Constants
    private static final int TIMER_LENGTH = 6;
    private static final int TIME_INTERVAL = 1000; // 5 seconds by default, can be changed later
    static final String SOUND_PASSWORD_DESCRIPTION_TAG = "sound_password_description"; // 5 seconds by default, can be changed later

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    long lastShakeTime = 0;
    SoundPassword soundPassword;

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
                mHandler.postDelayed(this, TIME_INTERVAL);
            } else {
                // Remaining time is now zero.
                // So we have to finish this activity and
                // continue the execution flow.
                mHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(AccelerometerRecordActivity.this, FinishEdittingPasswordActivity.class);
                if (soundPassword.toString() != null && !soundPassword.toString().isEmpty()) {
                    intent.putExtra(SOUND_PASSWORD_DESCRIPTION_TAG, soundPassword.toString());
                } else {
                    // sound password es null
                    // porque el user no grabo nada
                }
                startActivity(intent);
            }

        }
    };


    // Views
    private TimerView mTimerView;
    private TextView mTxtTimer;

    // Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_record);

        initViews();

        this.soundPassword = new SoundPassword();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                long now = System.currentTimeMillis();

                if (lastShakeTime == 0) {
                    // Todavia no se setteo nada.
                    // Es el primer shake
                } else {
                    // no es el primer shake,
                    // anteriormente ya se sacudio el dispositivo
                    long pause = now - lastShakeTime;
                    soundPassword.addPauseInMilliseconds((int)pause);
                    Toast.makeText(AccelerometerRecordActivity.this, "SHAKE!", Toast.LENGTH_SHORT).show();
                }

                Log.d("ACCELEROMETER_RECORD", "Device has shaked! Password => " + soundPassword.toString());

                lastShakeTime = now;
            }
        });

        mTimerView.start(TIMER_LENGTH);
        mHandler = new Handler();
        mHandler.postDelayed(updateTimerThread, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    // Configuration
    private void initViews() {
        this.mTimerView = (TimerView) findViewById(R.id.accelerometer_record_screen_timer);
        this.mTxtTimer = (TextView) findViewById(R.id.accelerometer_record_screen_txt_remaining_time);
    }
}
