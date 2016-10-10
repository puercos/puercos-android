package com.puercos.puercos.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.puercos.puercos.R;

public class FinishEdittingPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_editting_password);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    public void onNextButtonClick(View view) {
        Intent intent = new Intent(FinishEdittingPasswordActivity.this, DoorStatusActivity.class);
        startActivity(intent);
    }

    public void oButtonClick(View view) {
        Intent intent = new Intent(FinishEdittingPasswordActivity.this, RecordActivity.class);
        startActivity(intent);
    }
}
