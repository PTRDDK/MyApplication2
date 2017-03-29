package com.example.piotrek.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Piotrek on 29.03.2017.
 */

public class AuthenticatorService extends Service {

    private static AccountAuthenticator sAccountAuthenticator;

    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = null;
        if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            binder = getAuthenticator().getIBinder();
        }
        return binder;
    }

    private AccountAuthenticator getAuthenticator() {
        if (null == AuthenticatorService.sAccountAuthenticator) {
            AuthenticatorService.sAccountAuthenticator = new AccountAuthenticator(this);
        }
        return AuthenticatorService.sAccountAuthenticator;
    }
}
