package com.example.configurator_pc.ui.configurations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.configurator_pc.R;
import com.example.configurator_pc.databinding.FragmentConfigurationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationsFragment extends Fragment {

    private FragmentConfigurationsBinding binding;
    private LinearLayout configurationsLayout;
    private List<Button> configurationsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentConfigurationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        configurationsList = new ArrayList<>();
        configurationsLayout = root.findViewById(R.id.configurations_linear_layout);

        FloatingActionButton addConfigurationButton = root.findViewById(R.id.addConfigurationButton);
        addConfigurationButton.setOnClickListener(v -> {
            Button configuration = new Button(getActivity());
            configuration.setText("My configuration");
            configuration.setLayoutParams(configurationsLayout.getLayoutParams());
            configurationsList.add(configuration);
            configurationsLayout.addView(configuration);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}