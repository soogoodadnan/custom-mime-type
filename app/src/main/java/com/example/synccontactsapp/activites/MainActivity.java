package com.example.synccontactsapp.activites;

import android.Manifest;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.synccontactsapp.other.AccountGeneral;
import com.example.synccontactsapp.other.ContactsManager;
import com.example.synccontactsapp.other.Log;
import com.example.synccontactsapp.other.MyContact;
import com.example.synccontactsapp.R;
import com.example.synccontactsapp.other.Contact;
import com.example.synccontactsapp.other.ContactFetcher;
import com.example.synccontactsapp.adaptors.ContactsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Contact> listContacts = new ArrayList<>();
    ListView lvContacts;
    ContactsAdapter adapterContacts;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Doing something, please wait.");
        dialog.setCancelable(false);

        lvContacts = (ListView) findViewById(R.id.lvContacts);
        adapterContacts = new ContactsAdapter(this, listContacts);
        lvContacts.setAdapter(adapterContacts);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermisions();
            }
        });


    }

    private void checkPermisions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
//                showContactsList();
                addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

            }
        } else {
//            showContactsList();
            addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

        }

    }

    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = AccountManager.get(this).addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    Log.i("Account was created");
//                    ContactsManager.addContact(MainActivity.this, new MyContact("0","Jhon Doe","123456","Jhondoe@test.com" ));
                    showContactsList();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }


    private void showContactsList() {

        dialog.show();

        listContacts.clear();
        listContacts.addAll(new ContactFetcher(MainActivity.this).fetchAll());
//         ContactsManager.addContact(MainActivity.this, new MyContact("Test", "Test"));

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for ( int i = 0; i < listContacts.size();i++) {
//            ContactsManager.updateMyContact(MainActivity.this,data.name);

                    String phone = "";
                    String name = listContacts.get(i).name;
                    String id = listContacts.get(i).id;
                    String email ="";

                    if (listContacts.get(i).numbers.size() > 0 && listContacts.get(i).numbers.get(0) != null) {
                        phone =   listContacts.get(i).numbers.get(0).number;
                    }

                    if (listContacts.get(i).emails.size() > 0 && listContacts.get(i).emails.get(0) != null) {
                        email =   listContacts.get(i).emails.get(0).address;

                    }


                    ContactsManager.addContact(MainActivity.this, new MyContact(id,name,phone, email));
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        adapterContacts.notifyDataSetChanged();
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }
                });
            }
        });

    }



    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.WRITE_SYNC_SETTINGS


        }, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted

//                    showContactsList();
                    addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);

                } else {
                    //not granted
                    Toast.makeText(MainActivity.this,"Permission is Required For this Operation.",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
