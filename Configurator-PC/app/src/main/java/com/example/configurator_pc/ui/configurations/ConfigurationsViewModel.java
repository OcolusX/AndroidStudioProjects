package com.example.configurator_pc.ui.configurations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigurationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ConfigurationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}