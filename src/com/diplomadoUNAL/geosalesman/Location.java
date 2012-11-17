package com.diplomadoUNAL.geosalesman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class Location extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		Intent receivedIntent = getIntent();
		WebView myWebView;
		myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.loadUrl("https://maps.google.com/?q="
						+ receivedIntent.getStringExtra("latitude") + ","
						+ receivedIntent.getStringExtra("longitude"));
		myWebView.setWebViewClient(new MyWebViewClient());
		Button okButton = (Button) findViewById(R.id.buttonOk);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_location, menu);
		return true;
	}
}
