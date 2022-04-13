package com.example.configurator_pc.ui.store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.configurator_pc.R;
import com.example.configurator_pc.model.Component;
import com.example.configurator_pc.model.Price;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewRow> {

    private List<Component> componentList;

    public StoreAdapter(List<Component> componentList) {
        this.componentList = componentList;
    }

    @NonNull
    @Override
    public StoreViewRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item, parent, false);
        return new StoreViewRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewRow holder, int position) {
        // TODO : разработать вывод картинки по url и доработать вывод цен
        Component component = componentList.get(position);
        holder.name.setText(component.getName());
        List<Price> priceList = component.getPriceList();
        float price = 0f;
        if(priceList != null && priceList.size() >= 1) {
            price = priceList.get(0).getPrice();
        }
        holder.price.setText(price + " р.");
    }



    @Override
    public int getItemCount() {
        return componentList.size();
    }

    public void changeList(List<Component> componentList) {
        this.componentList = componentList;
        this.notifyItemRangeChanged(0, componentList.size());
    }

    public void removeList() {
        if(componentList != null && !componentList.isEmpty()) {
            int size = componentList.size();
            componentList.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }


    static class StoreViewRow extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView name;
        private final TextView price;

        public StoreViewRow(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.store_item_image);
            name = itemView.findViewById(R.id.store_item_name);
            price = itemView.findViewById(R.id.store_item_price);
        }
    }
}
