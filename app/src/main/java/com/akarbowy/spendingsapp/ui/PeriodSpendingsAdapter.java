package com.akarbowy.spendingsapp.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akarbowy.spendingsapp.helpers.AmountFormatUtil;
import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.entities.PeriodSpendingsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeriodSpendingsAdapter extends RecyclerView.Adapter<PeriodSpendingsAdapter.ViewHolder> {

    private List<PeriodSpendingsData> spendings = new ArrayList<>();

    public void setItems(List<PeriodSpendingsData> items) {
        spendings.clear();
        spendings.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.home_item_spendings, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PeriodSpendingsData item = spendings.get(position);

        holder.valueView.setText(String.format("%1$s %2$s", AmountFormatUtil.format(item.total), item.symbol));
    }

    @Override
    public int getItemCount() {
        return spendings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.spendings_card_value)
        TextView valueView;

        /*TODO feature-1
        @BindView(R.id.spendings_card_change_icon)
        ImageView changeIconView;
        @BindView(R.id.spendings_card_change_value)
        TextView changeValueView;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
