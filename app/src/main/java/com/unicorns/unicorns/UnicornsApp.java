package com.unicorns.unicorns;

import android.app.Application;

import com.unicorns.unicorns.database.DaoMaster;
import com.unicorns.unicorns.database.DaoSession;
import com.unicorns.unicorns.database.DbOpenHelper;

/**
 * UnicornsApp class created by Andreas Pribitzer 12.01.2020
 * Used to have only one DaoSession instance in the whole app
 */

public class UnicornsApp extends Application {

    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mDaoSession = new DaoMaster(new DbOpenHelper(this, "greendao.db").getWritableDb()).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
