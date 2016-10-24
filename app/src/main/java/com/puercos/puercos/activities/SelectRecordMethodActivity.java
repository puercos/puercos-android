package com.puercos.puercos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.puercos.puercos.R;

public class SelectRecordMethodActivity extends AppCompatActivity {

    // region Views
    private Button btnAccelerometer;
    private Button btnMicrophone;
    // endregion

    // region Activity life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_record_method);

        initViews();
    }
    // endregion

    // region Configuration
    private void initViews() {
        this.btnAccelerometer = (Button) findViewById(R.id.btn_accelerometer);
        this.btnMicrophone = (Button) findViewById(R.id.btn_microphone);
    }
    // endregion

    // region Actions
    public void handleAccelerometerRecordActivity(View view) {
        Intent intent = new Intent(this, AccelerometerRecordActivity.class);
        startActivity(intent);
    }
    public void handleMicrophoneRecordActivity(View view) {
        Intent intent = new Intent(this, MicrophoneRecordActivity.class);
        startActivity(intent);
    }
    // endregion
}
