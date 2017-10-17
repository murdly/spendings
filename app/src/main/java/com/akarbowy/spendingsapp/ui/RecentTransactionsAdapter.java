package com.akarbowy.spendingsapp.ui;


import android.arch.paging.PagedListAdapter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentTransactionsAdapter extends PagedListAdapter<TransactionEntity,
        RecentTransactionsAdapter.TransactionViewHolder> {

    private static final DiffCallback<TransactionEntity> DIFF_CALLBACK =
            new DiffCallback<TransactionEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull TransactionEntity oldTransaction,
                                               @NonNull TransactionEntity newTransaction) {
                    return oldTransaction.id == newTransaction.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull TransactionEntity oldTransaction,
                                                  @NonNull TransactionEntity newTransaction) {
                    return oldTransaction.equals(newTransaction);
                }
            };
    private OnItemClickListener onItemClickListener;

    public RecentTransactionsAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_transaction_recent, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        final TransactionEntity transaction = getItem(position);
        if (transaction != null) {
            holder.bindTo(transaction, onItemClickListener);
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.clear();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(TransactionEntity item);
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTransactionName) TextView nameView;
        @BindView(R.id.itemTransactionDate) TextView dateView;
        @BindView(R.id.itemTransactionValue) TextView valueView;
        View rootView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.rootView = itemView;
        }

        public void bindTo(final TransactionEntity transaction, final OnItemClickListener onItemClickListener) {
            nameView.setText(transaction.title);
            nameView.setBackgroundColor(Color.TRANSPARENT);

            dateView.setText("12 Aug");

            valueView.setText("- " + transaction.value + " $");

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onItemClickListener.onItemClick(transaction);
                }
            });


        }

        public void clear() {
            nameView.setBackgroundColor(Color.GRAY);
        }
    }
}