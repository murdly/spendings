package com.akarbowy.spendingsapp.ui.transaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.entities.CurrencyEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    public interface Callback {
        void onCurrencyChosen(CurrencyEntity chosen);
    }

    private List<CurrencyEntity> items = new ArrayList<>();
    private Callback callback;

    public void setItems(List<CurrencyEntity> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CurrencyAdapter.ViewHolder(inflater.inflate(R.layout.transaction_currency_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CurrencyEntity currency = items.get(position);
        holder.textView.setText(currency.isoCode);
        holder.itemView.setOnClickListener(view -> {
            if (callback != null) {
                CurrencyEntity c = items.get(holder.getLayoutPosition());
                callback.onCurrencyChosen(c);
            }
        });
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
