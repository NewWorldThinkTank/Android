package com.cesta.cesta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

	public static final String PREF = "com.cesta.cesta.PREF";
	public static final int REQUEST_CODE_GOOGLE_SIGN_IN = 0x1;
	public static final int REQUEST_CODE_GOOGLE_SIGN_OUT = 0x2;
	public static final int REQUEST_CODE_FB_LOG_IN = 0x3;
	public static final int REQUEST_CODE_FB_LOG_OUT = 0x4;
	public static final String ACCOUNT_FILE = "account";
	public static final String SIGNED_IN = "signedIn";
	private static final String TAG = "MainActivity";

	//private Fragment fragment;
	private Button fbBtn;
	private View googleBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(this);

		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		SharedPreferences p = getSharedPreferences(PREF, Context.MODE_PRIVATE);
		if (p.contains(SIGNED_IN) && p.getBoolean(SIGNED_IN, false)) {
			Intent intent = new Intent(this, MapsActivity.class);
			Serializable s = null;
			try {
				FileInputStream fis = openFileInput(ACCOUNT_FILE);
				ObjectInputStream is = new ObjectInputStream(fis);
				s = (Serializable) is.readObject();
				is.close();
				fis.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			intent.putExtra("ac", s);
			startActivity(intent);
			finish();
		}

		googleBtn = findViewById(R.id.sign_in);
		googleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if (isNetworkConnected()) {
                    Intent i = new Intent(MainActivity.this, GoogleSignActivity.class);
                    i.setAction(GoogleSignActivity.SIGN_IN);
                    startActivityForResult(i, REQUEST_CODE_GOOGLE_SIGN_IN);
                } else {
                    //Toast.makeText(MainActivity.this, "Internet not available.", Toast
                    //        .LENGTH_SHORT).show();
                    Snackbar.make(googleBtn, "Internet not available.", Snackbar.LENGTH_SHORT).show();
                }
			}
		});

		findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                if (isNetworkConnected()) {
                    Intent i = new Intent(MainActivity.this, GoogleSignActivity.class);
                    i.setAction(GoogleSignActivity.SIGN_OUT);
                    startActivityForResult(i, REQUEST_CODE_GOOGLE_SIGN_OUT);
                } else
                    Snackbar.make(googleBtn, "Internet not available.", Snackbar.LENGTH_SHORT).show();
			}
		});

		fbBtn = (Button) findViewById(R.id.login_button);
		fbBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//loginButton.registerCallback(callbackManager, callback);
                if (isNetworkConnected()) {
                    Intent i = new Intent(MainActivity.this, FBLoginActivity.class);
                    i.setAction(FBLoginActivity.LOG_IN);
                    startActivityForResult(i, REQUEST_CODE_FB_LOG_IN);
                } else
                    Snackbar.make(googleBtn, "Internet not available.", Snackbar.LENGTH_SHORT).show();
			}
		});
		/*fbBtn.setReadPermissions(permissions);
		fbBtn.registerCallback(callbackManager, callback);*/

		findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isNetworkConnected()) {
                    Intent i = new Intent(MainActivity.this, FBLoginActivity.class);
                    i.setAction(FBLoginActivity.LOG_OUT);
                    startActivityForResult(i, REQUEST_CODE_FB_LOG_OUT);
                } else
                    Snackbar.make(googleBtn, "Internet not available.", Snackbar.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//fragment.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(this, MapsActivity.class);
				Serializable account = data.getSerializableExtra("Ac");
				intent.putExtra("ac", account);

				try {
					FileOutputStream fout = openFileOutput("account", Context.MODE_PRIVATE);
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(account);
					fout.close();
					oos.close();
					Log.d(TAG, "Account saved.");
				} catch (IOException e) {
					e.printStackTrace();
				}

				SharedPreferences p = getSharedPreferences(PREF, Context.MODE_PRIVATE);
				SharedPreferences.Editor edit = p.edit();
				edit.putBoolean(SIGNED_IN, true);
				edit.apply();

				startActivity(intent);
				finish();
			} else {
				Toast.makeText(this, "Failed to login.", Toast.LENGTH_SHORT).show();
				SharedPreferences p = getSharedPreferences(PREF, Context.MODE_PRIVATE);
				SharedPreferences.Editor edit = p.edit();
				edit.putBoolean(SIGNED_IN, false);
				edit.apply();
			}
		} else if (requestCode == REQUEST_CODE_GOOGLE_SIGN_OUT) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Log out successful.", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "onActivityResult: Log out successful.");
				Snackbar.make(googleBtn, "Log out successful", Snackbar.LENGTH_SHORT).show();
			}
		} else if (requestCode == REQUEST_CODE_FB_LOG_IN) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(this, MapsActivity.class);
				Serializable account = data.getSerializableExtra("Ac");
				intent.putExtra("ac", account);

				try {
					FileOutputStream fout = openFileOutput("account", Context.MODE_PRIVATE);
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(account);
					fout.close();
					oos.close();
					Log.d(TAG, "Account saved.");
				} catch (IOException e) {
					e.printStackTrace();
				}

				SharedPreferences p = getSharedPreferences(PREF, Context.MODE_PRIVATE);
				SharedPreferences.Editor edit = p.edit();
				edit.putBoolean(SIGNED_IN, true);
				edit.apply();

				startActivity(intent);
				finish();
			} else {
				Toast.makeText(this, "Failed to login.", Toast.LENGTH_SHORT).show();
				SharedPreferences p = getSharedPreferences(PREF, Context.MODE_PRIVATE);
				SharedPreferences.Editor edit = p.edit();
				edit.putBoolean(SIGNED_IN, false);
				edit.apply();
			}
		} else if (requestCode == REQUEST_CODE_FB_LOG_OUT) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Log out successful.", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "onActivityResult: Log out successful.");
				Snackbar.make(fbBtn, "Log out successful", Snackbar.LENGTH_SHORT).show();
			}
		}
	}

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

	/*public void changeFragment(Fragment fragment) {
		this.fragment = fragment;

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragmentContainer, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*@Override
	protected void onPause() {
		super.onPause();

		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		AppEventsLogger.activateApp(this);
	}*/
}
