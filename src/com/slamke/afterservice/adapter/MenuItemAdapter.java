package com.slamke.afterservice.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.slamke.afterservice.R;
import com.slamke.afterservice.util.Message;

public class MenuItemAdapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Activity mContext;
	
	/**
	 * 屏幕宽度
	 */
	private int displayWeight;
	
	public MenuItemAdapter() {

	}

	public MenuItemAdapter(List<Map<String, String>> list, Activity context,int weight) {
		this.list = list;
		mContext = context;
		displayWeight = weight;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public int getItemHeight() {
		LayoutInflater inflater = mContext.getLayoutInflater();
		View view = inflater.inflate(R.layout.item_small, null);
		return view.getMeasuredHeight();

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = mContext.getLayoutInflater();
		final View view = inflater.inflate(R.layout.item_small, null);
		final ImageView image = (ImageView) view.findViewById(R.id.item_image);
		final TextView text = (TextView) view.findViewById(R.id.item_text);
		int width = (displayWeight-40)/3-40;
		Log.e("width", ""+width);
		image.setLayoutParams(new LayoutParams(width, width));
		image.setImageResource(Integer.parseInt(list.get(position).get(Message.MENU_ITEM_IMAGE)));
		text.setText(list.get(position).get(Message.MENU_ITEM_TEXT));
		return view;
	}

}