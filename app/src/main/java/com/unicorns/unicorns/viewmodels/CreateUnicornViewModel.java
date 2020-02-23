package com.unicorns.unicorns.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unicorns.unicorns.database.Unicorn;

public class CreateUnicornViewModel extends ViewModel {

    private MutableLiveData<Unicorn> mUnicorn;

    public void init() {
        if(mUnicorn != null)
            return;
        mUnicorn = new MutableLiveData<>();
        mUnicorn.setValue(new Unicorn("", "", 0, ""));
    }

    public MutableLiveData<Unicorn> getUnicorn() {
        return mUnicorn;
    }

    public void setName(String name) {
        if(mUnicorn == null) return;
        Unicorn unicorn = mUnicorn.getValue();
        if(unicorn == null) return;
        unicorn.setName(name);
        mUnicorn.setValue(unicorn);
    }

    public void setAge(int age) {
        if(mUnicorn == null) return;
        Unicorn unicorn = mUnicorn.getValue();
        if(unicorn == null) return;
        unicorn.setAge(age);
        mUnicorn.setValue(unicorn);
    }

    public void setColor(String color) {
        if(mUnicorn == null) return;
        Unicorn unicorn = mUnicorn.getValue();
        if(unicorn == null) return;
        unicorn.setColour(color);
        mUnicorn.setValue(unicorn);
    }
}
