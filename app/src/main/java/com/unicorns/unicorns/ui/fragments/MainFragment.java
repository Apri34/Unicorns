package com.unicorns.unicorns.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorns.unicorns.utils.MarginItemDecoration;
import com.unicorns.unicorns.R;
import com.unicorns.unicorns.adapters.UnicornAdapter;
import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.databinding.FragmentMainBinding;
import com.unicorns.unicorns.interfaces.IMainActivity;
import com.unicorns.unicorns.viewmodels.UnicornViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MainFragment created by Andreas Pribitzer 12.01.2020
 */

public class MainFragment extends Fragment {

    private static final int RECYCLER_VIEW_COLUMNS = 2;

    private FragmentMainBinding mBinding;
    private UnicornViewModel mUnicornViewModel;
    private UnicornAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        mBinding.setIMainActivity((IMainActivity) requireActivity());

        initRecyclerView();

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUnicornViewModel = ViewModelProviders.of(requireActivity()).get(UnicornViewModel.class);
        mUnicornViewModel.init();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = mBinding.recyclerViewMainFragment;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), RECYCLER_VIEW_COLUMNS));
        recyclerView.addItemDecoration(new MarginItemDecoration(2, 20));

        List<Unicorn> unicorns = mUnicornViewModel.getUnicorns().getValue();
        mAdapter = new UnicornAdapter(unicorns, (IMainActivity) requireActivity());
        recyclerView.setAdapter(mAdapter);

        mUnicornViewModel.getUnicorns().observe(requireActivity(), new Observer<List<Unicorn>>() {
            @Override
            public void onChanged(List<Unicorn> unicorns) {
                mAdapter.setUnicorns(unicorns);
            }
        });
    }
}
