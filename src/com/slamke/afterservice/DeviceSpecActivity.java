package com.slamke.afterservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.slamke.afterservice.adapter.ContentAdapter;
import com.slamke.afterservice.util.GlobalObject;
import com.slamke.afterservice.util.Message;
/**
 * 显示咨询结果的详细信息---无相关网络交互
 * @author sunke
 *
 */
public class DeviceSpecActivity extends Activity implements OnClickListener{
	private Button btnBack;
	private ListView lv;
	
	private final static String PRODUCT_CLASS= "类型:";
	private final static String PRODUCT_NAME= "名称:";
	private final static String PRODUCT_MODEL= "型号:";
	private final static String PRODUCT_SPEC= "规格:";
	private final static String PRODUCT_REMARK= "备注:";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_spec);
        
        btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
        
        lv = (ListView)findViewById(R.id.lv);
        updatelistview();
	}
	
	  // 更新listview  
    public void updatelistview() {
    	List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> classMap = new HashMap<String, String>();
		classMap.put(Message.ITEM_SPEC_TYPE, PRODUCT_CLASS);
		classMap.put(Message.ITEM_SPEC_CONTENT, GlobalObject.theClass.getName());
		list.add(classMap);
		
		Map<String, String> nameMap = new HashMap<String, String>();
		nameMap.put(Message.ITEM_SPEC_TYPE, PRODUCT_NAME);
		nameMap.put(Message.ITEM_SPEC_CONTENT, GlobalObject.theType.getName());
		list.add(nameMap);
		
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put(Message.ITEM_SPEC_TYPE, PRODUCT_MODEL);
		modelMap.put(Message.ITEM_SPEC_CONTENT, GlobalObject.theType.getModel());
		list.add(modelMap);
		
		Map<String, String> specMap = new HashMap<String, String>();
		specMap.put(Message.ITEM_SPEC_TYPE, PRODUCT_SPEC);
		specMap.put(Message.ITEM_SPEC_CONTENT, GlobalObject.theType.getSpecification());
		list.add(specMap);
		
		Map<String, String> remarkMap = new HashMap<String, String>();
		remarkMap.put(Message.ITEM_SPEC_TYPE, PRODUCT_REMARK);
		remarkMap.put(Message.ITEM_SPEC_CONTENT, GlobalObject.theType.getRemark());
		list.add(remarkMap);
		
    	ContentAdapter adapter = new ContentAdapter(list,this);  
        // layout为listView的布局文件，包括三个TextView，用来显示三个列名所对应的值  
        // ColumnNames为数据库的表的列名  
        // 最后一个参数是int[]类型的，为view类型的id，用来显示ColumnNames列名所对应的值。view的类型为TextView  
        lv.setAdapter(adapter);
    } 
	private void goBack() {
		finish();
//		Intent intent = new Intent(this, ConsultActivity.class);
//		startActivity(intent);
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
