package com.example.configurator_pc.ui.store;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.configurator_pc.R;
import com.example.configurator_pc.databinding.FragmentStoreBinding;
import com.example.configurator_pc.model.Component;
import com.example.configurator_pc.model.ComponentType;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class StoreFragment extends Fragment {

    // TODO : реализовать вывод анимации значка загрузки

    private FragmentStoreBinding binding;
    private MutableLiveData<List<Component>> data;  // Список с компонентами
    private RecyclerView storeRecycler;             // Recycler для вывода списка на экран
    private TabLayout storeTabs;                    // Вкладки магазина
    private StoreViewModel storeViewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Инициализируем RecyclerView
        storeRecycler = root.findViewById(R.id.store_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        storeRecycler.setLayoutManager(layoutManager);

        // Запрашиваем ViewModel для нашего фрагмента
        storeViewModel = new ViewModelProvider(
                this, new StoreViewModelFactory(this.requireActivity().getApplication())
        ).get(StoreViewModel.class);

        // Инициализируем вкладки магазина
        storeTabs = root.findViewById(R.id.store_tabs);
        setupStoreTabs();

        // Загружаем данные из ViewModel (по умолчанию открыта вклада с материнскими платами)
        data = storeViewModel.getData();
        data.observe(getViewLifecycleOwner(), components ->
                storeRecycler.setAdapter(new StoreAdapter(components))
        );

        return root;
    }

    // Устанавливает иконки для вкладок магазина
    private void setupStoreTabs() {
        int[] tabIcons = {
                R.drawable.ic_motherboard,
                R.drawable.ic_cpu,
                R.drawable.ic_cooler,
                R.drawable.ic_graphics_card,
                R.drawable.ic_ram,
                R.drawable.ic_hdd,
                R.drawable.ic_hdd,
                R.drawable.ic_case,
                R.drawable.ic_power_supply
        };

        for (int i = 0; i < tabIcons.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.tab_item, null);
            TabLayout.Tab tab = storeTabs.getTabAt(i);
            if (tab != null) {
                ((ImageView) view.findViewById(R.id.tab_item_icon)).setImageResource(tabIcons[i]);
                ((TextView) view.findViewById(R.id.tab_item_text)).setText(tab.getText());
                tab.setCustomView(view);
                if (i == storeViewModel.getTabPosition()) {
                    storeTabs.selectTab(tab);
                }
            }
        }

        // Устаналиваем слушатели нажатия на вкладки
        storeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                data = storeViewModel.loadData(ComponentType.values()[tab.getPosition()]);
                data.observe(getViewLifecycleOwner(), components -> {
                    StoreAdapter adapter = (StoreAdapter) storeRecycler.getAdapter();
                    if (adapter == null) {
                        storeRecycler.setAdapter(new StoreAdapter(components));
                    } else {
                        adapter.changeList(components);
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                StoreAdapter adapter = (StoreAdapter) storeRecycler.getAdapter();
                if (adapter != null) {
                    adapter.removeList();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        storeViewModel.saveTabPosition(storeTabs.getSelectedTabPosition());
        binding = null;
    }

}