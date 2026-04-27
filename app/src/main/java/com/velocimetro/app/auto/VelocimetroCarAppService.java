package com.velocimetro.app.auto;

import androidx.car.app.CarAppService;
import androidx.car.app.Session;
import androidx.car.app.validation.HostValidator;
import androidx.annotation.NonNull;

public class VelocimetroCarAppService extends CarAppService {

    @NonNull
    @Override
    public Session onCreateSession() {
        return new VelocimetroSession();
    }

    @NonNull
    @Override
    public HostValidator createHostValidator() {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR;
    }
}
