package com.akarbowy.spendingsapp.ui;


import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.entities.TransactionEntity;
import com.akarbowy.spendingsapp.ui.transaction.Transaction;

import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentTransactionsAdapter extends PagedListAdapter<Transaction,
        RecentTransactionsAdapter.TransactionViewHolder> {

    private static final DiffCallback<Transaction> DIFF_CALLBACK =
            new DiffCallback<Transaction>() {
                @Override
                public boolean areItemsTheSame(@NonNull Transaction oldTransaction,
                                               @NonNull Transaction newTransaction) {
                    return oldTransaction.transaction.transactionId == newTransaction.transaction.transactionId;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Transaction oldTransaction,
                                                  @NonNull Transaction newTransaction) {
                    return oldTransaction.transaction.equals(newTransaction.transaction);
                }
            };
    private OnItemClickListener onItemClickListener;
    private Context context;

    public RecentTransactionsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_transaction_recent, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        final Transaction transaction = getItem(position);
        if (transaction != null) {
            holder.bindTo(context, transaction, onItemClickListener);
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
        void onItemClick(Transaction item);
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTransactionIcon)
        ImageView iconView;
        @BindView(R.id.itemTransactionTitle)
        TextView titleView;
        @BindView(R.id.itemTransactionSubtitle)
        TextView subtitleView;
        @BindView(R.id.itemTransactionValue)
        TextView valueView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(Context context, final Transaction t, final OnItemClickListener onItemClickListener) {
            TransactionEntity transaction = t.transaction;

            iconView.setImageResource(ResourceUtil.getCategoryIconId(context, t.categoryGroup.groupName, t.category.categoryName));

            titleView.setText(transaction.title);
            titleView.setBackgroundColor(Color.TRANSPARENT);

            String dateFormatted = transaction.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


            String valueFormatted = String.format("- %1$.2f %2$s", transaction.value, t.currency.symbol);
            valueView.setText(valueFormatted);

            if (transaction.deleted) {
                valueView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
                subtitleView.setText(String.format("%1$s, %2$s", dateFormatted, "deleted"));
                itemView.setOnClickListener(null);
            } else {
                valueView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText));
                subtitleView.setText(dateFormatted);
                itemView.setOnClickListener(v -> onItemClickListener.onItemClick(t));
            }
        }

        public void clear() {
        }
    }
}