package com.slamke.afterservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableRow;

import com.slamke.afterservice.util.Message;
/**
 * 设置界面--无相关网络交互
 * @author sunke
 *
 */
public class SettingActivity extends Activity implements OnClickListener {
	private Dialog dialog;
	private EditText etxtContent;
	private int type = 0;
	private TableRow rowEmail;
	private TableRow rowPhone;
	private TableRow rowServer;
	private TableRow rowAbout;
	private TableRow rowExit;
	
	private Button btnBack;
	private CheckBox cbEnable;
	private boolean enableEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		rowEmail = (TableRow) findViewById(R.id.row_email);
		rowPhone = (TableRow) findViewById(R.id.row_phone);
		rowServer = (TableRow) findViewById(R.id.row_server);
		rowExit = (TableRow) findViewById(R.id.row_exit);
		rowAbout = (TableRow) findViewById(R.id.row_about);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		rowEmail.setOnClickListener(this);
		rowEmail.setVisibility(View.GONE);
		rowPhone.setOnClickListener(this);
		rowServer.setOnClickListener(this);
		rowAbout.setOnClickListener(this);
		rowExit.setOnClickListener(this);
		dialog = initMethodListDlg();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goBack();
			return false;
		}
		return false;
	}

	public Dialog initMethodListDlg() {
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.dialog_update, null);
		etxtContent = (EditText) view.findViewById(R.id.etxt_content);
		cbEnable = (CheckBox) view.findViewById(R.id.cb_enable);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Message.SETTING_UPDATE);
		builder.setView(view);
		builder.setCancelable(false); // �ǰ�ť
		builder.setNegativeButton(Message.DIALOG_CANCEL,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						type = 0;
						dialog.dismiss();
					}
				});
		builder.setPositiveButton(Message.DIALOG_CONFIRM,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setContent(type,etxtContent.getText().toString());
						type = 0;
						dialog.dismiss();
					}
				});
		Dialog mDeleteDlg = builder.create();
		return (mDeleteDlg);
	}

	private void goBack() {
		setResult(200);
		finish();
	}

	private String getContent(int index) {
		SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences
		String result = "";
		switch (index) {
		case 1:
			result = sp.getString(Message.PREFERENCE_EMAIL, "");
			enableEmail =sp.getBoolean(Message.PREFERENCE_EMAIL_ENABLE, false);
			cbEnable.setChecked(enableEmail);
			if (result.equals("")) {
				result = getResources().getString(R.string.default_email);
			}
			break;
		case 2:
			result = sp.getString(Message.PREFERENCE_PHONE_SERVER, "");
			Log.i("phone", "123:"+result);
			if (result.equals("")) {
				result = getResources().getString(R.string.default_phone_mobile);
			}
			break;
		case 3:
			result = sp.getString(Message.PREFERENCE_SERVER, "");
			if (result.equals("")) {
				result = getResources().getString(R.string.default_IPAddress);
			}
			break;

		default:
			break;
		}
		return result;
	}
	
	private void setContent(int index,String content) {
		SharedPreferences sp = getSharedPreferences(Message.PREFERENCE_NAME,
				MODE_PRIVATE); // 获得Preferences
		SharedPreferences.Editor editor = sp.edit(); // 获得Editor
		switch (index) {
		case 1:
			editor.putString(Message.PREFERENCE_EMAIL, content); // 将用户名存入Preferences
			if (cbEnable.isChecked()) {
				editor.putBoolean(Message.PREFERENCE_EMAIL_ENABLE, true); // 将用户名存入Preferences
			}else {
				editor.putBoolean(Message.PREFERENCE_EMAIL_ENABLE, false); // 将用户名存入Preferences
			}
			break;
		case 2:
			editor.putString(Message.PREFERENCE_PHONE_SERVER, content); // 将用户名存入Preferences
			break;
		case 3:
			editor.putString(Message.PREFERENCE_SERVER, content); // 将用户名存入Preferences
			break;

		default:
			break;
		}
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.row_email:
			cbEnable.setVisibility(View.VISIBLE);
			type = 1;
			dialog.show();
			etxtContent
					.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			etxtContent.setText(getContent(type));
			break;
		case R.id.row_phone:
			cbEnable.setVisibility(View.GONE);
			type = 2;
			dialog.show();
			etxtContent
					.setInputType(InputType.TYPE_CLASS_PHONE);
			etxtContent.setText(getContent(type));
			break;
		case R.id.row_server:
			cbEnable.setVisibility(View.GONE);
			type = 3;
			dialog.show();
			etxtContent
					.setInputType(InputType.TYPE_CLASS_TEXT);
			etxtContent.setText(getContent(type));
			break;
		case R.id.row_exit:
			SharedPreferences sp = getSharedPreferences(
					Message.PREFERENCE_NAME, MODE_PRIVATE); // 获得Preferences
			SharedPreferences.Editor editor = sp.edit(); // 获得Editor
			editor.putString(Message.PREFERENCE_USERNAME, ""); // 将用户名存入Preferences
			editor.putString(Message.PREFERENCE_PASSWORD, ""); // 将密码存入Preferences
			editor.commit();
			setResult(44);
			finish();
			break;
		case R.id.btn_back:
			goBack();
			break;
		case R.id.row_about:
			Intent intent = new Intent(this,AboutActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
