package com.example.piotrek.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by Piotrek on 29.03.2017.
 */

public class AccountUtils {

    public static final String ACCOUNT_TYPE = "com.example";
    public static final String AUTH_TOKEN_TYPE = "com.example.aaa";

    public static IServerAuthenticator mServerAuthenticator = (IServerAuthenticator) new MyServerAuthenticator();

    public static Account getAccount(Context context, String accountName) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }
}
