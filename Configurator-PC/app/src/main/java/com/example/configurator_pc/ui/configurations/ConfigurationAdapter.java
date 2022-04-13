package com.example.configurator_pc.ui.configurations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.configurator_pc.R;
import com.example.configurator_pc.model.Component;

import java.util.List;

public class ConfigurationAdapter extends RecyclerView.Adapter<ConfigurationAdapter.ConfigurationViewRow>{

   private List<Component> configurationList;

    public ConfigurationAdapter(List<Component> configurationList) {
        this.configurationList = configurationList;
    }

    @NonNull
    @Override
    public ConfigurationViewRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.configuration_item, parent, false);
        return new ConfigurationViewRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigurationViewRow holder, int position) {
        holder.name.setText(configurationList.get(position).getName());
        holder.price.setText(configurationList.get(position).getType().name());
    }

    @Override
    public int getItemCount() {
        return configurationList.size();
    }

    static class ConfigurationViewRow extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView price;

        public ConfigurationViewRow(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.configuration_name);
            price = itemView.findViewById(R.id.configuration_price);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Click!", Toast.LENGTH_LONG).show();
        }
    }

}
