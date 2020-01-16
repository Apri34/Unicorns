package com.unicorns.unicorns.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorns.unicorns.R;
import com.unicorns.unicorns.databinding.FragmentCreateUnicornBinding;
import com.unicorns.unicorns.interfaces.IMainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class CreateUnicornFragment extends Fragment {

    /**
     * CreateUnicornFragment created by Andreas Pribitzer 12.01.2020
     */

    private FragmentCreateUnicornBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_unicorn, container, false);
        mBinding.setIMainActivity((IMainActivity) requireActivity());
        mBinding.setEditTextName(mBinding.editTextCreateUnicornFragmentName);
        mBinding.setEditTextAge(mBinding.editTextCreateUnicornFragmentAge);
        mBinding.setSpinnerColor(mBinding.spinnerCreateUnicornFragmentColor);

        return mBinding.getRoot();
    }

    //Method to clear inputs
    //Gets called when Fragment is popped from backstack, so, when a new Unicorn gets created, the input fields are empty
    public void clearText() {
        mBinding.spinnerCreateUnicornFragmentColor.setSelection(0);
        mBinding.editTextCreateUnicornFragmentAge.setText("");
        mBinding.editTextCreateUnicornFragmentName.setText("");
    }
}
