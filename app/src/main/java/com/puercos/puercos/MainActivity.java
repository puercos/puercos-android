package com.puercos.puercos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // region Views
    private TextView txtTitle;
    private TextView txtSubtitle;
    private Button btnBegin;
    // endregion

    // region Life cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
    }
    // endregion

    // region Configuration
    private void initViews() {
        this.txtTitle = (TextView) findViewById(R.id.txt_main_title);
        this.txtSubtitle = (TextView) findViewById(R.id.txt_main_subtitle);
        this.btnBegin = (Button) findViewById(R.id.btn_main);
    }
    // endregion

    // region Actions
    public void handleBeginButton(View view) {

    }
    // endregion

}
