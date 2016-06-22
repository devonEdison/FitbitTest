package com.wits.fitbittest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressLint("SetJavaScriptEnabled")
public class AuthenticationActivity extends Activity {

	private static final String TAG = "AuthenticationActivity";

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		final WebView wvAuthorise = (WebView) findViewById(R.id.wvAuthorise);
		wvAuthorise.getSettings().setJavaScriptEnabled(true);
		wvAuthorise.getSettings().setLoadWithOverviewMode(true);
		wvAuthorise.getSettings().setUseWideViewPort(true);
		wvAuthorise.getSettings().setBuiltInZoomControls(true);
		wvAuthorise.setWebChromeClient(new WebChromeClient());
		wvAuthorise.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d(TAG,"1111111111111111111111");
				Log.v(TAG, url);
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(AuthenticationActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
			public void onLoadResource(WebView view, String url) {
//				Toast.makeText(getApplicationContext(), wvAuthorise.getUrl(), Toast.LENGTH_SHORT).show();
			}
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d(TAG,"55555555555555555555555555");
				Log.v(TAG, url);
				wvAuthorise.loadUrl(url);
				if(url.contains("disonash.temboolive.com/callback/fitbit")){
					String access_token = getQueryString(url,"access_token");
					String user_id = getQueryString(url,"user_id");
					// remember to decide if you want the first or last parameter with the same name
					// If you want the first call setPreferFirstRepeatedParameter(true);
					Log.d(TAG, "devon check code = " + access_token);
					Log.d(TAG, "devon check user_id = " + user_id);
					Intent intent = new Intent();
					intent.putExtra("access_token",access_token);
					intent.putExtra("user_id",user_id);
					setResult(RESULT_OK,intent);
					finish();
				}
				return true;
			}
		});
		//TODO:please provide your personal client_id  here.

		wvAuthorise.loadUrl("https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=227TBR"+
				"&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight");//&expires_in=604800
	}

	public static String getQueryString(String url, String tag) {
		String[] params = url.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}

		Set<String> keys = map.keySet();
		for (String key : keys) {
			if(key.equals(tag)){
				return map.get(key);
			}
			System.out.println("Name=" + key);
			System.out.println("Value=" + map.get(key));
		}
		return "";
	}

}
