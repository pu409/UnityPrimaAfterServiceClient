package com.slamke.afterservice.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slamke.afterservice.R;
import com.slamke.afterservice.util.Message;

public class ContentAdapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Activity mContext;

	public ContentAdapter() {

	}

	public ContentAdapter(List<Map<String, String>> list, Activity context) {
		this.list = list;
		mContext = context;
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
		View view = inflater.inflate(R.layout.content_item, null);
		return view.getMeasuredHeight();

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = mContext.getLayoutInflater();
		final View view = inflater.inflate(R.layout.content_item, null);
		final TextView txtType = (TextView) view.findViewById(R.id.txt_type);
		final TextView txtOrigin = (TextView) view.findViewById(R.id.txt_origin);
		txtType.setText(list.get(position).get(Message.ITEM_SPEC_TYPE));
		txtOrigin.setText(list.get(position).get(Message.ITEM_SPEC_CONTENT));
		return view;
	}

}