package com.velocimetro.app.auto;

import android.content.Intent;
import androidx.car.app.Screen;
import androidx.car.app.Session;
import androidx.annotation.NonNull;

public class VelocimetroSession extends Session {

    @NonNull
    @Override
    public Screen onCreateScreen(@NonNull Intent intent) {
        return new VelocimetroScreen(getCarContext());
    }
}
