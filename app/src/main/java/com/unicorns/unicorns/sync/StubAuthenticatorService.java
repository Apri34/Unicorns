package com.unicorns.unicorns.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * StubAuthenticatorService created by Andreas Pribitzer 14.01.2020
 * This is needed for the SyncAdapter framework
 */

public class StubAuthenticatorService extends Service {

    private StubAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new StubAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       return mAuthenticator.getIBinder();
    }
}
