package com.akarbowy.spendingsapp.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeriodSpendingsAdapter extends RecyclerView.Adapter<PeriodSpendingsAdapter.ViewHolder> {

    private List<PeriodSpendings> spendings = new ArrayList<>();

    public void setItems(List<PeriodSpendings> items){
        spendings.clear();
        spendings.addAll(items);
        notifyDataSetChanged();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_home_spendings, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        PeriodSpendings item = spendings.get(position);
        holder.valueView.setText(String.format("%s", item.total));
        holder.changeValueView.setText("todo%");
    }

    @Override public int getItemCount() {
        return spendings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.spendings_card_value) TextView valueView;
        @BindView(R.id.spendings_card_change_icon) ImageView changeIconView;
        @BindView(R.id.spendings_card_change_value) TextView changeValueView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
