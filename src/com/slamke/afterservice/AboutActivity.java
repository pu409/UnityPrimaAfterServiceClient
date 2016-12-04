package com.slamke.afterservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 关于界面--无相关网络交互
 * @author sunke
 *
 */
public class AboutActivity extends Activity implements OnClickListener{
	private Button btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_about);
        
        btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
	}
	private void goBack() {
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goBack();
			return false;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			goBack();
			break;
		default:
			break;
		}
	}
}
