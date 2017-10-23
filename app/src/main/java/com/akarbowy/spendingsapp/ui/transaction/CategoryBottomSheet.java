package com.akarbowy.spendingsapp.ui.transaction;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class CategoryBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.list)
    RecyclerView categoriesListView;

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_category_content, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TransactionViewModel viewModel = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        GridLayoutManager glm = new GridLayoutManager(getContext(), 5);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 5;
                    default:
                        return 1;
                }
            }
        });
        categoriesListView.setLayoutManager(glm);
        categoriesListView.setAdapter(sectionAdapter);

        viewModel.categoriesWithSubs.observe(this, new Observer<Map<CategoryEntity, List<CategoryEntity>>>() {
            @Override
            public void onChanged(@Nullable Map<CategoryEntity, List<CategoryEntity>> categoriesWithSubs) {
                for (Map.Entry<CategoryEntity, List<CategoryEntity>> category : categoriesWithSubs.entrySet()) {
                    CategorySection section = new CategorySection(category.getKey().name, category.getValue());
                    sectionAdapter.addSection(section);
                }
            }
        });
    }

    private class CategorySection extends StatelessSection {

        String title;
        List<CategoryEntity> list;
        boolean expanded = false;

        CategorySection(String title, List<CategoryEntity> list) {
            super(new SectionParameters.Builder(R.layout.transaction_category_item)
                    .headerResourceId(R.layout.transaction_category_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? list.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            CategoryEntity item = list.get(position);

            itemHolder.title.setText(item.name);

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "klik", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.title.setText(title);

            headerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    headerHolder.arrow.setImageResource(
                            expanded ?
                                    R.drawable.ic_arrow_drop_up_black_24dp
                                    : R.drawable.ic_arrow_drop_down_black_24dp
                    );
                    sectionAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_title)
        TextView title;
        @BindView(R.id.header_arrow)
        ImageView arrow;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_icon)
        ImageView icon;
        @BindView(R.id.item_title)
        TextView title;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
