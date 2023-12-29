package com.example.buoi1.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buoi1.DAO.ProductDAO;

public class SwipeItem extends ItemTouchHelper.SimpleCallback {

    private ProductAdapter mAdapter;

    public SwipeItem(ProductAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        int id = mAdapter.deleteItem(pos);
        new ProductDAO(mAdapter.getContext()).Delete(id);
        mAdapter.notifyItemRangeChanged(pos, mAdapter.getItemCount());
    }
}
