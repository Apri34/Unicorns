package com.unicorns.unicorns.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * SyncAdapterManager created by Andreas Pribitzer 14.01.2020
 * Class to setup everything for the SyncAdapter
 */

public class SyncAdapterManager {
    private static final String TAG = "AccountManager";

    private static String AUTHORITY = "com.unicorns.unicorns.provider";
    private static String ACCOUNT = "dummyAccount";
    private static String ACCOUNT_TYPE = "com.unicorns.unicorns";
    private static Account mAccount;

    //Method to log in an Account
    public static void init(Context context) {
        mAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if(accountManager.addAccountExplicitly(mAccount, null, null)) {
            Log.i(TAG, "Account logged in");
        } else {
            boolean loggedIn = false;
            Account[] accounts = accountManager.getAccounts();
            for (Account account : accounts) {
                if (account == mAccount) {
                    loggedIn = true;
                    Log.i(TAG, "Account is already logged in");
                }
            }
            if(!loggedIn) {
                Log.i(TAG, "Something went wrong logging in the account");
            }
        }

        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
    }

    //Method to run onPerformSync() method
    public static void forceRefresh() {
         Bundle bundle = new Bundle();
         bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
         bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

         ContentResolver.requestSync(mAccount, AUTHORITY, bundle);
    }
}
