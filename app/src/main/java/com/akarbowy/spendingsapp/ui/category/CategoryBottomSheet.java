package com.akarbowy.spendingsapp.ui.category;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akarbowy.spendingsapp.R;
import com.akarbowy.spendingsapp.data.AppDatabase;
import com.akarbowy.spendingsapp.data.entities.CategoryEntity;
import com.akarbowy.spendingsapp.data.entities.GroupedCategories;
import com.akarbowy.spendingsapp.ui.ResourceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class CategoryBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.list)
    RecyclerView categoriesListView;

    private SectionedRecyclerViewAdapter sectionAdapter;

    private Callback callback;

    private CategoryViewModel viewModel;

    public static CategoryBottomSheet newInstance(Bundle bundle) {

        final CategoryBottomSheet fragment = new CategoryBottomSheet();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.transaction_category_content, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        initViewModel();

        initAdapter();

        bindCategories();
    }

    @NonNull
    private void initViewModel() {
        viewModel = ViewModelProviders.of(getActivity(),
                new CategoryViewModel.Factory(AppDatabase.getInstance(getActivity())))
                .get(CategoryViewModel.class);
    }

    private void initAdapter() {
        sectionAdapter = new SectionedRecyclerViewAdapter();

        final GridLayoutManager glm = new GridLayoutManager(getContext(), 4);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 4;
                    default:
                        return 1;
                }
            }
        });
        categoriesListView.setLayoutManager(glm);
        categoriesListView.setAdapter(sectionAdapter);
    }

    private void bindCategories() {
        viewModel.categoriesByGroup.observe(this, (groups) -> {
            for (GroupedCategories g : groups) {
                final CategorySection section = new CategorySection(g.group.groupName, g.categories);
                sectionAdapter.addSection(section);
            }
            sectionAdapter.notifyDataSetChanged();
        });
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    private void onCategoryChosen(CategoryEntity category) {
        if (callback != null) {
            callback.onCategoryChosen(getArguments(), category);
        }

        dismiss();
    }

    public interface Callback {
        void onCategoryChosen(Bundle bundle, CategoryEntity chosen);
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

    private class CategorySection extends StatelessSection {

        private final String title;
        private final List<CategoryEntity> items;
        private boolean expanded = false;

        CategorySection(String title, List<CategoryEntity> items) {
            super(new SectionParameters.Builder(R.layout.transaction_category_item)
                    .headerResourceId(R.layout.transaction_category_header)
                    .build());

            this.title = title;
            this.items = items;
        }

        @Override
        public int getContentItemsTotal() {
            return expanded ? items.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final CategoryEntity item = items.get(position);

            itemHolder.icon.setImageResource(ResourceUtil.getCategoryIconId(getContext(), title, item.categoryName));
            itemHolder.title.setText(item.categoryName);

            itemHolder.itemView.setOnClickListener(view -> {
                final int itemPosition = sectionAdapter.getPositionInSection(itemHolder.getLayoutPosition());
                onCategoryChosen(items.get(itemPosition));

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

            headerHolder.itemView.setOnClickListener(v -> {
                expanded = !expanded;
                headerHolder.arrow.setImageResource(
                        expanded ?
                                R.drawable.ic_arrow_drop_up_black_24dp
                                : R.drawable.ic_arrow_drop_down_black_24dp
                );
                sectionAdapter.notifyDataSetChanged();
            });
        }
    }
}
