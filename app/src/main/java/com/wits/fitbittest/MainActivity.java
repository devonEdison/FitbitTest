package com.wits.fitbittest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {
	static final int GET_PIN_REQUEST = 101;  // The request code
	private static final String TAG = "MainActivity";
	static OAuthService service;
	static Token requestToken;
	public static String access_token="";
	TextView textView;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_main);
		textView = (TextView)findViewById(R.id.tvOutput);
		button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnRetrieveData();
				textView.setText("");
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		startActivityForResult(new Intent(this,AuthenticationActivity.class),GET_PIN_REQUEST);
	}

	public void btnRetrieveData() {
		Log.d(TAG, "3devon check access_token = " + access_token);
		AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
		client.addHeader("Authorization", "Bearer "+access_token);
		client.get(MainActivity.this, "https://api.fitbit.com/1/user/-/profile.json", new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d(TAG, "4devon check responseBody = " + responseBody);
				textView.setText(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == GET_PIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                if(extras != null){
                	final String access_token = extras.getString("access_token");
					MainActivity.access_token = access_token;
					Log.d(TAG, "2devon check code = " + access_token);
					btnRetrieveData();
				}
            }
        }
    }
}