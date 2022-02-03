package com.example.trafficlights;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout redLight;
    private LinearLayout yellowLight;
    private LinearLayout greenLight;
    private Button startButton;
    private int counter = 0;
    private boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redLight = findViewById(R.id.redLight);
        yellowLight = findViewById(R.id.yellowLight);
        greenLight = findViewById(R.id.greenLight);
        startButton = findViewById(R.id.button_start);
        startButton.setBackgroundColor(getResources().getColor(R.color.green));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        start = false;
    }

    public void onClickStart(View view) {
        if (start) {
            start = false;
            startButton.setText(R.string.button_start);
            startButton.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            start = true;
            startButton.setText(R.string.button_stop);
            startButton.setBackgroundColor(getResources().getColor(R.color.red));
            new Thread(() -> {
                while (start) {
                    try {
                        if (++counter == 1) {
                            greenLight.setBackgroundColor(getResources().getColor(R.color.grey));
                            redLight.setBackgroundColor(getResources().getColor(R.color.red));
                        } else if (counter == 2) {
                            redLight.setBackgroundColor(getResources().getColor(R.color.grey));
                            yellowLight.setBackgroundColor(getResources().getColor(R.color.yellow));
                        } else if (counter == 3) {
                            yellowLight.setBackgroundColor(getResources().getColor(R.color.grey));
                            greenLight.setBackgroundColor(getResources().getColor(R.color.green));
                            counter = 0;
                        }
                        if(start)
                            Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}