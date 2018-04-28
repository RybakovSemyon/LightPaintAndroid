package ru.rybakov.lightpaintandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rybakovsemyon.lightpaintview.LightPaintView;
import rybakovsemyon.lightpaintview.PaintPanel;

public class MainActivity extends AppCompatActivity {

    LightPaintView lightPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightPaintView = findViewById(R.id.paintView);
        PaintPanel paintPanel = findViewById(R.id.paintPanel);
        paintPanel.attachPaintView(lightPaintView);
        lightPaintView.setLightPaintViewState(ExampleSaver.getInstance().lightPaintViewState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ExampleSaver.getInstance().lightPaintViewState = lightPaintView.getLightPaintViewState();
    }

}
