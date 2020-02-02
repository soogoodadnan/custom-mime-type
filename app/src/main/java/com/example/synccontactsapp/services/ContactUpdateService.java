package com.example.synccontactsapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.synccontactsapp.other.ContactsManager;
import com.example.synccontactsapp.other.MyContact;

public class ContactUpdateService extends Service {
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//ContactsManager.updateMyContact(this, "Sample");
		/*AccountManager manager = AccountManager.get(this);
		AccountManagerFuture<Bundle> future = manager.addAccount(ContactsManager.accountType, null, null, null, null, null, null);
		Intent intent = new Intent(this, AuthenticatorActivity.class);
		try {
			intent.putExtras(future.getResult());
			startActivity(intent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		ContactsManager.addContact(this, new MyContact("0","Jhon Doe","123456","Jhondoe@test.com" ));
	}

}
