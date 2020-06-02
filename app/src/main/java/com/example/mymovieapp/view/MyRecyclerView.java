package com.example.mymovieapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView
{
    private View emptyView;

    public MyRecyclerView(@NonNull Context context)
    {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private AdapterDataObserver observer = new AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            initEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount)
        {
            super.onItemRangeInserted(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount)
        {
            super.onItemRangeRemoved(positionStart, itemCount);
            initEmptyView();
        }
    };

    @Override
    public void setAdapter(Adapter adapter)
    {
        Adapter oldAdapter = getAdapter();
        super.setAdapter(adapter);

        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(observer);
        if (adapter != null)
            adapter.registerAdapterDataObserver(observer);
    }
    public void setEmptyView(View view)
    {
        this.emptyView = view;
        initEmptyView();
    }

    private void initEmptyView()
    {
        if (emptyView != null)
        {
            final boolean hasAdapterData = getAdapter() == null || getAdapter().getItemCount() == 0;
            emptyView.setVisibility(hasAdapterData ? VISIBLE : GONE);
            this.setVisibility(hasAdapterData ? GONE : VISIBLE);
        }
    }
}