package com.example.piotrek.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private static final int REQ_SIGNUP = 1;

    private AccountManager mAccountManager;
    private AuthPreferences mAuthPreferences;
    private String authToken;

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TextView text7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TEST", "TEST9");
        text1 = (TextView) findViewById(android.R.id.text1);
        text2 = (TextView) findViewById(android.R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        text6 = (TextView) findViewById(R.id.text6);
        text7 = (TextView) findViewById(R.id.text7);

        authToken = null;
        mAuthPreferences = new AuthPreferences(this);
        Log.d("TEST", "TEST17");
        mAccountManager = AccountManager.get(this);
        Log.d("TEST", "TEST10");
        mAccountManager.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, new GetAuthTokenCallback(), null);
        Log.d("TEST", "TEST11");
    }

    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            Bundle bundle;
            Log.d("TEST", "TEST12");
            try {
                bundle = result.getResult();

                final Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (null != intent) {
                    startActivityForResult(intent, REQ_SIGNUP);
                    Log.d("TEST", "TEST5");
                } else {
                    authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    final String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                    Log.d("TEST", "TEST8");
                    // Save session username & auth token
                    mAuthPreferences.setAuthToken(authToken);
                    mAuthPreferences.setUsername(accountName);

                    text1.setText("Retrieved auth token: " + authToken);
                    text2.setText("Saved account name: " + mAuthPreferences.getAccountName());
                    text3.setText("Saved auth token: " + mAuthPreferences.getAuthToken());
                    text4.setText("Account password: " + AccountManager.KEY_PASSWORD);
                    text5.setText("Test");
                    text6.setText("Email: " + bundle.getString("email"));
                    text7.setText("Status: " + AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
                    Log.d("TEST", "TEST6");
                    // If the logged account didn't exist, we need to create it on the device
                    Account account = AccountUtils.getAccount(MainActivity.this, accountName);
                    Log.d("TEST", "TEST18");
                    if (null == account) {
                        Log.d("TEST", "TEST7");
                        account = new Account(accountName, AccountUtils.ACCOUNT_TYPE);
                        Log.d("WBIJASZ DO IF'a?", "TAK");
                        mAccountManager.addAccountExplicitly(account, bundle.getString(LoginActivity.PARAM_USER_PASSWORD), null);
                        mAccountManager.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken);
                    }
                }
            } catch (OperationCanceledException e) {
                // If signup was cancelled, force activity termination
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close_session:

                // Clear session and ask for new auth token
                mAccountManager.invalidateAuthToken(AccountUtils.ACCOUNT_TYPE, authToken);
                mAuthPreferences.setAuthToken(null);
                mAuthPreferences.setUsername(null);
                mAccountManager.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, new GetAuthTokenCallback(), null);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
