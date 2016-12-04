package com.slamke.afterservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.slamke.afterservice.adapter.AdapterFactory;
import com.slamke.afterservice.util.ConnectionDetector;
import com.slamke.afterservice.util.Message;
/**
 * 程序主页--进行功能选择前，出去保修和购买备件，其余功能首先进行网络状态验证
 * @author sunke
 *
 */
public class HomeActivity extends Activity implements OnClickListener {
	private GridView gvFirst;
	private ImageView imgSetting;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		imgSetting = (ImageView) findViewById(R.id.img_setting);
		imgSetting.setOnClickListener(this);
		gvFirst = (GridView) findViewById(R.id.gridViewFirst);
		gvFirst.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gvFirst.setVerticalSpacing(10);
		gvFirst.setHorizontalSpacing(10);
		setContent();
		gvFirst.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//******检测网络*****//
				ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
				Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
				if (!isInternetPresent && arg2 != 0 && arg2 != 3 && arg2 != 6) {
					Toast.makeText(HomeActivity.this, Message.INTERNET_ERROR_TRY_AGAIN, Toast.LENGTH_SHORT).show();
					return;
				}
				//******检测网络*****//
				Intent intent;
				switch (arg2) {
				//保修
				case 0:
					finish();
					intent = new Intent(HomeActivity.this,RepairFirstActivity.class);
					startActivity(intent);
					break;
				//进度查询
				case 1:
					finish();
					intent = new Intent(HomeActivity.this,ProgressActivity.class);
					startActivity(intent);
					break;
				//评论
				case 2:
					finish();
					intent = new Intent(HomeActivity.this,CommentActivity.class);
					startActivity(intent);
					break;
				//购买备件
				case 3:
					finish();
					intent = new Intent(HomeActivity.this,SellActivity.class);
					startActivity(intent);
					break;
				//投诉	
				case 4:
					finish();
					intent = new Intent(HomeActivity.this,ComplaintActivity.class);
					startActivity(intent);
					break;
				//建议
				case 5:
					finish();
					intent = new Intent(HomeActivity.this,AdviceActivity.class);
					startActivity(intent);
					break;
				//咨询
				case 6:
					finish();
					intent = new Intent(HomeActivity.this,ConsultActivity.class);
					startActivity(intent);
						break;
				default:
					break;
				}
			}

		});

	}
	@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }
    
    /** Save instance state.*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
	
	private void setContent() {
		String[] menuNameArray = {
				getResources().getString(R.string.item_repair),
				getResources().getString(R.string.item_progress),
				getResources().getString(R.string.item_comment),
				getResources().getString(R.string.item_sell),
				getResources().getString(R.string.item_complaint),
				getResources().getString(R.string.item_advice),
				getResources().getString(R.string.item_consult)};
		int[] imageResourceArray = {R.drawable.repair,R.drawable.check_status,R.drawable.evaluate,R.drawable.sales,R.drawable.complaint
				,R.drawable.advice,R.drawable.consult};
//		List<Map<String, String>> contents = new ArrayList<Map<String,String>>();
//		for (int i = 0; i < menuNameArray.length; i++) {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put(Message.MENU_ITEM_IMAGE, ""+imageResourceArray[i]);
//			map.put(Message.MENU_ITEM_TEXT, menuNameArray[i]);
//			contents.add(map);
//		}
		gvFirst.setPadding(5, 5, 5, 0);
		gvFirst.setNumColumns(3);
		//检测屏幕大小，设置列数
		DisplayMetrics dm = new DisplayMetrics();
		super.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		//MenuItemAdapter adapter = new MenuItemAdapter(contents, this, screenWidth);
		if(screenWidth > 320){
			gvFirst.setNumColumns(3);
		}else {
			gvFirst.setNumColumns(2);
		}
		gvFirst.setAdapter(AdapterFactory.getMenuAdapter(this,
				menuNameArray, imageResourceArray));
//		gvFirst.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.img_setting:
			//finish();
			Intent intent = new Intent(this,SettingActivity.class);
			startActivityForResult(intent, 200);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}
	 /** 
     * 复写onActivityResult，这个方法 
     * 是要等到SimpleTaskActivity点了提交过后才会执行的 
     */  
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        //可以根据多个请求代码来作相应的操作  
        if(44==resultCode)  
        {  
        	finish();
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    } 
}
