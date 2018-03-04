package com.akarbowy.spendingsapp.ui.importdata;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.external.ImportData;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportDataAdapter extends RecyclerView.Adapter<ImportDataAdapter.ViewHolder> {

    private final Context context;

    private List<ImportData> data = new ArrayList<>();

    private Callback callback;

    public ImportDataAdapter(Context context) {
        this.context = context;
    }

    public void refreshData(List<ImportData> items) {
        this.data.clear();
        this.data.addAll(items);

        notifyDataSetChanged();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ImportDataAdapter.ViewHolder(inflater.inflate(R.layout.import_item_data, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(callback, data, position);
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onSetCategoryClicked(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.number) TextView numberView;
        @BindView(R.id.title) TextView titleView;
        @BindView(R.id.subtitle) TextView subtitleView;
        @BindView(R.id.value) TextView valueView;
        @BindView(R.id.set_category_button) Button setCategoryButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Callback callback, List<ImportData> data, int position) {

            final ImportData item = data.get(position);

            bindNumber(position);

            bindTitle(item);

            bindSubtitle(item);

            bindValue(item);

            bindCategoryButton(item, callback);
        }

        private void bindNumber(int position) {
            numberView.setText(String.valueOf(1 + position));
        }

        private void bindTitle(ImportData item) {
            titleView.setText(item.getReference());
        }

        private void bindSubtitle(ImportData item) {

            final String date = LocalDate.parse(item.getDate(), DateTimeFormatter.ofPattern("d MMM uuuu")).toString();

            final String category = item.getChosenCategoryName();

            final String subtitle = category != null && !category.isEmpty() ? String.format("%s, %s", category, date) : date;

            subtitleView.setText(subtitle);
        }

        private void bindValue(ImportData item) {

            final String value = String.format("%1$.2f", item.getValue());

            valueView.setText(value);
        }

        private void bindCategoryButton(ImportData item, Callback callback) {

            setCategoryButton.setVisibility(item.isCategoryChosen() ? View.GONE : View.VISIBLE);

            setCategoryButton.setOnClickListener(v -> {
                if (callback != null) {
                    callback.onSetCategoryClicked(getAdapterPosition());
                }
            });
        }
    }
}