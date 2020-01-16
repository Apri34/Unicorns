package com.unicorns.unicorns.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * DbOpenHelper created by Andreas Pribitzer 13.01.2020
 * This class is actually not needed in this example. It would be needed, if the database structure got updated.
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        //In case the database is changed in a newer version
    }
}
