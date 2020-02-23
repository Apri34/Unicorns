package com.unicorns.unicorns.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.unicorns.unicorns.R;
import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.databinding.FragmentCreateUnicornBinding;
import com.unicorns.unicorns.interfaces.IMainActivity;
import com.unicorns.unicorns.viewmodels.CreateUnicornViewModel;

public class CreateUnicornFragment extends Fragment {

    /**
     * CreateUnicornFragment created by Andreas Pribitzer 12.01.2020
     */

    private FragmentCreateUnicornBinding mBinding;

    private CreateUnicornViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_unicorn, container, false);
        mBinding.setIMainActivity((IMainActivity) requireActivity());
        setUpColorDropdown();
        setUpEditText();

        initViewModel();

        mBinding.setUnicorn(mViewModel.getUnicorn().getValue());
        mViewModel.getUnicorn().observe(this, new Observer<Unicorn>(){
            @Override
            public void onChanged(Unicorn unicorn) {
                mBinding.setUnicorn(unicorn);
            }
        });

        return mBinding.getRoot();
    }

    //Method to clear inputs
    //Gets called when Fragment is popped from backstack, so, when a new Unicorn gets created, the input fields are empty
    public void clearText() {
        mBinding.editTextCreateUnicornFragmentAge.setText("");
        mBinding.editTextCreateUnicornFragmentName.setText("");
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(requireActivity()).get(CreateUnicornViewModel.class);
        mViewModel.init();
        mViewModel.getUnicorn().observe(this, new Observer<Unicorn>() {
            @Override
            public void onChanged(Unicorn unicorn) {
                mBinding.setUnicorn(unicorn);
            }
        });
    }

    private void setUpColorDropdown() {
        final String[] colors = getResources().getStringArray(R.array.colors);
        ArrayAdapter adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                colors
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerCreateUnicornFragmentColor.setAdapter(adapter);
        mBinding.spinnerCreateUnicornFragmentColor.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position != -1)
                            mViewModel.setColor(colors[position]);
                        else
                            mViewModel.setColor(null);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    private void setUpEditText() {
        mBinding.editTextCreateUnicornFragmentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setName(s.toString());
            }
        });
        mBinding.editTextCreateUnicornFragmentAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) {
                    mViewModel.setAge(0);
                    return;
                }
                int age;
                try {
                    age = Integer.parseInt(s.toString());
                    mViewModel.setAge(age);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
