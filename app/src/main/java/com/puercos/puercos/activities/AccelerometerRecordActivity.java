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
import com.puercos.puercos.utils.sound.PasswordSoundPlayer;

import org.firezenk.audiowaves.Visualizer;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class AccelerometerRecordActivity extends BaseActivity {

    // Constantes

    // Estas son constantes del TIMER

    // Cantidad de segundos que va a estar grabando
    private static final int TIMER_LENGTH = 6;
    private static final int TIME_INTERVAL = 1000; // 5 seconds by default, can be changed later
    static final String SOUND_PASSWORD_DESCRIPTION_TAG = "sound_password_description"; // 5 seconds by default, can be changed later

    // The following are used for the shake detection

    // Esto me parece que ya no lo usamos
    private SensorManager mSensorManager;

    // Me parece que quedo viejo de antes
    // ahora ya no lo usa
    private Sensor mAccelerometer;

    // Esta clase detecta cuando se sacude el dispositivo.
    // Es la clase intermedia entre el acelerometro
    // y esta activity, para que quede menos feo.
    private ShakeDetector mShakeDetector;
    // El tiempo del ultimo shake
    long lastShakeTime = 0;

    // Aca vamos a guardar la contrasenia que estamos grabando
    SoundPassword soundPassword;

    // Password sound player
    PasswordSoundPlayer passwordSoundPlayer;

    private Handler mHandler;
    private Runnable updateTimerThread = new Runnable() {
        // Este es un hilo que cada segundo decrementa el timer,
        // y cuando se termina y llega a 0, cambia de activity.
        public void run() {
            int remainingTime = Integer.parseInt(mTxtTimer.getText().toString());
            if (remainingTime > 0) {
                // Decrements time and sets the new
                // remaining time to the TextView
                remainingTime -= 1;
                Log.d("MicrophoneActivity", "Remaining time " + mTxtTimer.getText().toString());
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

        this.passwordSoundPlayer = new PasswordSoundPlayer(this);
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

                passwordSoundPlayer.play();
                passwordSoundPlayer.stop();

                if (lastShakeTime == 0) {
                    // Todavia no se setteo nada.
                    // Es el primer shake
                } else {
                    // no es el primer shake,
                    // anteriormente ya se sacudio el dispositivo
                    long pause = now - lastShakeTime;
                    soundPassword.addPauseInMilliseconds((int)pause);
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
