package com.unicorns.unicorns.viewmodels;

import android.app.Application;
import android.content.Context;

import com.unicorns.unicorns.UnicornRepository;
import com.unicorns.unicorns.database.Unicorn;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * UnicornViewModel created by Andreas Pribitzer 13.01.2020
 */

public class UnicornViewModel extends AndroidViewModel {

    private MutableLiveData<List<Unicorn>> mUnicorns;
    private UnicornRepository mRepo;
    private Context mContext;

    public UnicornViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
    }

    public void init() {
        if(mUnicorns != null) {
            return;
        }
        mRepo = UnicornRepository.getInstance(mContext);
        mUnicorns = new MutableLiveData<>();
        mUnicorns.setValue(mRepo.getUnicorns());
    }

    public void addNewUnicorn(Unicorn unicorn) {
        mRepo.addUnicorn(unicorn);
        mUnicorns.setValue(mRepo.getUnicorns());
    }

    public void deleteUnicorn(Unicorn unicorn) {
        mRepo.deleteUnicorn(unicorn);
        mUnicorns.setValue(mRepo.getUnicorns());
    }

    public LiveData<List<Unicorn>> getUnicorns() {
        return mUnicorns;
    }
}
