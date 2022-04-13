package com.example.configurator_pc.ui.store;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class StoreViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public StoreViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) throws IllegalArgumentException{
        if(modelClass == StoreViewModel.class) {
            return (T) new StoreViewModel(application);
        }
        throw new IllegalArgumentException("Argument class is not StoreViewModel");
    }
}
