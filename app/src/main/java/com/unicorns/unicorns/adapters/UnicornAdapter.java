package com.unicorns.unicorns.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.interfaces.IMainActivity;
import com.unicorns.unicorns.databinding.ViewUnicornBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter for RecyclerView created by Andreas Pribitzer 12.01.2020
 */

public class UnicornAdapter extends RecyclerView.Adapter<UnicornAdapter.UnicornViewHolder> {

    //Dataset for the Adapter
    private List<Unicorn> mDataset;
    //Listener for Delete-Function when item is long-clicked
    private IMainActivity listener;

    //Constructor
    public UnicornAdapter(List<Unicorn> unicorns, IMainActivity listener) {
        mDataset = unicorns;
        this.listener = listener;
    }

    //Update method for the adapter
    public void setUnicorns(List<Unicorn> unicorns) {
        mDataset = unicorns;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UnicornViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewUnicornBinding binding = ViewUnicornBinding.inflate(inflater, parent, false);
        return new UnicornViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UnicornViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }

    //ViewHolder class
    static class UnicornViewHolder extends RecyclerView.ViewHolder {

        private ViewUnicornBinding binding;
        private IMainActivity listener;

        UnicornViewHolder(@NonNull ViewUnicornBinding binding, IMainActivity listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        void bind(final Unicorn unicorn) {
            binding.setUnicorn(unicorn);
            binding.cardViewUnicorn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.showDeleteDialog(unicorn);
                    return true;
                }
            });
        }
    }
}
