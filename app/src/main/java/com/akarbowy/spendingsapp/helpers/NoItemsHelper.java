package com.akarbowy.spendingsapp.helpers;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by arek.karbowy on 01/11/2017.
 */

public class NoItemsHelper {

    private RecyclerView recyclerView;
    private View emptyView;

    public NoItemsHelper(View emptyView) {
        this.emptyView = emptyView;
    }

    final private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            //NOTE: returns 0 when used with PagedListAdapter
            checkIfEmpty(recyclerView.getAdapter().getItemCount());
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIfEmpty(itemCount);

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIfEmpty(itemCount);
        }
    };

    private void checkIfEmpty(int count) {
        if (emptyView != null) {
            final boolean noItems = count == 0;
            emptyView.setVisibility(noItems ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(noItems ? View.GONE : View.VISIBLE);
        }
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (this.recyclerView == recyclerView) {
            return;
        }

        if (this.recyclerView != null) {
            destroyObservers();
        }

        this.recyclerView = recyclerView;

        if (this.recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();

            if (adapter == null) {
                throw new NullPointerException("Adapter must be present");
            }

            adapter.registerAdapterDataObserver(observer);
        }
    }

    private void destroyObservers() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
    }
}
