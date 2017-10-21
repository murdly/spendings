package com.akarbowy.spendingsapp.ui.transaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.CurrencyDictionary;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arek.karbowy on 20/10/2017.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private List<CurrencyDictionary> items = new ArrayList<>();

    public void setItems(List<CurrencyDictionary> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CurrencyAdapter.ViewHolder(inflater.inflate(R.layout.item_transaction_currency, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CurrencyDictionary currency = items.get(position);
        holder.textView.setText(currency.name());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
