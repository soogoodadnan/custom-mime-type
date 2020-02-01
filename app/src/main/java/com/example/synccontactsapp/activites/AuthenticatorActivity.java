package com.example.synccontactsapp.activites;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.synccontactsapp.other.AccountGeneral;
import com.example.synccontactsapp.other.Log;


public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private AccountManager mAccountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AuthenticatorActivity");
        Intent res = new Intent();
        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, AccountGeneral.ACCOUNT_NAME);
        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountGeneral.ACCOUNT_TYPE);
        res.putExtra(AccountManager.KEY_AUTHTOKEN, AccountGeneral.ACCOUNT_TOKEN);
        Account account = new Account(AccountGeneral.ACCOUNT_NAME, AccountGeneral.ACCOUNT_TYPE);
        mAccountManager = AccountManager.get(this);
        mAccountManager.addAccountExplicitly(account, null, null);
        //mAccountManager.setAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, AccountGeneral.ACCOUNT_TOKEN);
        ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        setAccountAuthenticatorResult(res.getExtras());
        setResult(RESULT_OK, res);
        finish();
    }
}
