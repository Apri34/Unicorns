package com.unicorns.unicorns;

import android.content.Context;

import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.database.UnicornDao;
import com.unicorns.unicorns.sync.SyncAdapterManager;

import java.util.List;

/**
 * Unicorn Repository created by Andreas Pribitzer 13.01.2020
 * Works as an interface between the database/webserver and the ViewModel
 */

public class UnicornRepository {

    private static UnicornRepository instance;

    private UnicornDao unicornDao;

    private UnicornRepository(Context application){
        unicornDao = ((UnicornsApp) application).getDaoSession().getUnicornDao();
    }

    public static UnicornRepository getInstance(Context application) {
        if (instance == null) {
            instance = new UnicornRepository(application);
            SyncAdapterManager.init(application);
        }
        return instance;
    }

    public List<Unicorn> getUnicorns() {
        return unicornDao.loadAll();
    }

    public void addUnicorn(Unicorn unicorn) {
        unicornDao.insert(unicorn);
        //Force sync everytime a Unicorn is added
        SyncAdapterManager.forceRefresh();
    }

    public void deleteUnicorn(Unicorn unicorn) {
        unicornDao.delete(unicorn);
        //Force sync everytime a Unicorn is added
        SyncAdapterManager.forceRefresh();
    }
}
