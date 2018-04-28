package ru.rybakov.lightpaintandroid;


import android.support.annotation.Nullable;

import rybakovsemyon.lightpaintview.LightPaintView;

public class ExampleSaver {

    private static ExampleSaver sMExampleSaver;

    @Nullable
    public LightPaintView.LightPaintViewState lightPaintViewState;

    public static ExampleSaver getInstance(){
        if (sMExampleSaver == null){
            sMExampleSaver = new ExampleSaver();
        }

        return sMExampleSaver;
    }
}
