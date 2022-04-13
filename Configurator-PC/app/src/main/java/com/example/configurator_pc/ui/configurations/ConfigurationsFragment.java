package com.example.configurator_pc.ui.configurations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.configurator_pc.R;
import com.example.configurator_pc.databinding.FragmentConfigurationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConfigurationsFragment extends Fragment {

    private FragmentConfigurationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentConfigurationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        ServerConnection serverConnection = new ServerConnection(root.getContext());
//        List<Component> componentList = new ArrayList<>(serverConnection.getComponentList(ComponentType.CPU, 0, 49));
        

//        RecyclerView recyclerView = root.findViewById(R.id.recycler);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        ConfigurationAdapter adapter = new ConfigurationAdapter(componentList);
//        recyclerView.setAdapter(adapter);

        FloatingActionButton addConfigurationButton = root.findViewById(R.id.addConfigurationButton);
        addConfigurationButton.setOnClickListener(v -> {
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}