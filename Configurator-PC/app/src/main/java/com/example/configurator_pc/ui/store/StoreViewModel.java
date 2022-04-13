package com.example.configurator_pc.ui.store;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.configurator_pc.model.Component;
import com.example.configurator_pc.model.ComponentType;
import com.example.configurator_pc.repository.Repository;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {

    private MutableLiveData<List<Component>> data;
    private final Repository repository;
    private int tabPosition;

    private int lastIndex;                        // Индекс последнего загруженного компонента
    private static final int LOAD_ITEM_NUMBER = 50; // Кол-во загружаемых за раз компонентов

    public StoreViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<List<Component>> getData() {
        if(data == null) {
            data = repository.loadComponentList(ComponentType.MOTHERBOARD, 0, LOAD_ITEM_NUMBER - 1);
        }
        return data;
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public void saveTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
    }

    public MutableLiveData<List<Component>> loadData(ComponentType page) {
        repository.cancel();
        lastIndex = 0;
        data = repository.loadComponentList(page, lastIndex, LOAD_ITEM_NUMBER - 1);
        return data;
    }

    public MutableLiveData<List<Component>> subLoadData() {
        repository.cancel();
        lastIndex += LOAD_ITEM_NUMBER;
        return repository.loadComponentList(
                ComponentType.values()[tabPosition], lastIndex, LOAD_ITEM_NUMBER - 1
        );
    }

}

