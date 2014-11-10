package com.myapp.lilybbs;

import com.myapp.lilybbs.LoginActivity;
import com.myapp.lilybbs.R;
import com.myapp.lilybbs.TopActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button b = (Button) findViewById(R.id.loginbutton);

		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText username = (EditText) findViewById(R.id.login);
				EditText password = (EditText) findViewById(R.id.password);

				if (username.getText().toString().length() > 0
						&& password.getText().toString().length() > 0) {
					if (username.getText().toString().equals("test")
							&& password.getText().toString().equals("test")) {
						Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
