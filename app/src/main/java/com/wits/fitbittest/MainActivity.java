package com.wits.fitbittest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {
	static final int GET_PIN_REQUEST = 101;  // The request code
	private static final String TAG = "MainActivity";
	static OAuthService service;
	static Token requestToken;
	public static String access_token="";
	public static String user_id="";
	TextView textView,textView2,textView3;
	Button button,button2,button3;
	EditText editText3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_main);
		button = (Button)findViewById(R.id.button1);
		textView = (TextView)findViewById(R.id.tvOutput);
		button2 = (Button)findViewById(R.id.button2);
		textView2 = (TextView)findViewById(R.id.tvOutput2);
		button3 = (Button)findViewById(R.id.button3);
		editText3 = (EditText)findViewById(R.id.editText3);
		textView3 = (TextView)findViewById(R.id.tvOutput3);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnRetrieveData();
				textView.setText("");
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getWaterLog();
			}
		});
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(editText3.getText().toString() != null){
					uploadWater(Float.parseFloat(editText3.getText().toString()));
				}
			}
		});
	}

	private void uploadWater(Float ff) {
		String Datetime;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Datetime = dateformat.format(c.getTime());
		Log.d(TAG,"check devon Datetime = " + Datetime);
		RequestParams params = new RequestParams();
		params.put("amount", String.format("%.1f", ff));
		params.put("date", Datetime);
		AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
		client.addHeader("Authorization", "Bearer "+access_token);
		client.post("https://api.fitbit.com/1/user/"+user_id+"/foods/log/water.json",params, new AsyncHttpResponseHandler(){


			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d(TAG, "textView3 devon check responseBody = " + response);
				textView3.setText(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		startActivityForResult(new Intent(this,AuthenticationActivity.class),GET_PIN_REQUEST);
	}

	public void getWaterLog(){
		String Datetime;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Datetime = dateformat.format(c.getTime());
		Log.d(TAG, "DatetimeDatetimeDatetime = " + Datetime);
		AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
		client.addHeader("Authorization", "Bearer "+access_token);
		client.get(MainActivity.this, "https://api.fitbit.com/1/user/"+user_id+"/foods/log/water/date/"+Datetime+".json", new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d(TAG, "textView2 devon check responseBody = " + response);
				textView2.setText(response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			}
		});
	}

	public void btnRetrieveData() {
		Log.d(TAG, "3devon check access_token = " + access_token);
		AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
		client.addHeader("Authorization", "Bearer "+access_token);
		client.get(MainActivity.this, "https://api.fitbit.com/1/user/"+user_id+"/profile.json", new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String response = new String(responseBody);
				Log.d(TAG, "4devon check responseBody = " + response);
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
					final String user_id = extras.getString("user_id");

					MainActivity.access_token = access_token;
					MainActivity.user_id = user_id;
					Log.d(TAG, "2devon check code = " + access_token);
					btnRetrieveData();
				}
            }
        }
    }
}