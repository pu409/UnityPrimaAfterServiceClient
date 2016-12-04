package com.slamke.afterservice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.slamke.afterservice.adapter.SpinnerAdapter;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
/**
 * 暂未启动的activity功能
 * @author sunke
 *
 */
public class EmailActivity extends Activity implements OnClickListener {
	private Button btnBack;
	private Button btnSubmit;
	private Spinner spSubject;
	private String subject;
	private EditText etxtContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		spSubject = (Spinner) findViewById(R.id.sp_subject);
		etxtContent = (EditText) findViewById(R.id.etxt_content);
		etxtContent.setText(GlobalObject.message);
		SpinnerAdapter adapterCategory = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.subjects));
		adapterCategory
				.setDropDownViewResource(android.R.layout.browser_link_context_header);
		spSubject.setAdapter(adapterCategory);
		spSubject.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				subject = getResources().getStringArray(R.array.subjects)[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				subject = null;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(20);
			finish();
			return false;
		}
		return false;
	}

	private void goHome() {
		finish();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			setResult(20);
			finish();
			break;
		case R.id.btn_submit:
			sendMailByIntent();
		default:
			break;
		}
	}

	public int sendMailByIntent() {
		SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences
		String email = sp.getString(Message.PREFERENCE_EMAIL, "");
		if (email.equals("")) {
			email = getResources().getString(R.string.default_email);
		}
		String[] reciver = new String[] { email };
		String myCc = "";
		String mybody = etxtContent.getText().toString();
		Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
		myIntent.setType("plain/text");
		myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
		myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
		myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
		startActivityForResult(Intent.createChooser(myIntent, "mail"), 100);
		return 1;
	}

	/**
	 * 复写onActivityResult，这个方法 是要等到SimpleTaskActivity点了提交过后才会执行的
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		// setResult(44);
		Toast.makeText(this, getResources().getString(R.string.email_tip),
				Toast.LENGTH_LONG).show();
		setResult(44);
		goHome();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
