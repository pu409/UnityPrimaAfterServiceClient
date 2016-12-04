package com.slamke.afterservice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {  
    Context context;  
    String[] items = new String[] {};  
  
    public SpinnerAdapter(final Context context,  
            final int textViewResourceId, final String[] objects) {  
        super(context, textViewResourceId, objects);  
        this.items = objects;  
        this.context = context;  
    }  
  
    @Override  
    public View getDropDownView(int position, View convertView,  
            ViewGroup parent) {  
  
        if (convertView == null) {  
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(  
                    android.R.layout.simple_spinner_item, parent, false);  
        }  
  
        TextView tv = (TextView) convertView  
                .findViewById(android.R.id.text1);  
        tv.setText(items[position]);  
        tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.BLACK);  
        tv.setTextSize(15);
        //tv.setHeight(50);
        parent.setMinimumHeight(50);
        tv.setPadding(0, 10, 0, 10);
        return convertView; 
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        if (convertView == null) {  
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(  
                    android.R.layout.simple_spinner_item, parent, false);  
        }  
  
        // android.R.id.text1 is default text view in resource of the android.  
        // android.R.layout.simple_spinner_item is default layout in resources of android.  
  
        TextView tv = (TextView) convertView  
                .findViewById(android.R.id.text1);  
        tv.setText(items[position]);  
        tv.setTextColor(Color.BLACK);  
        tv.setTextSize(15);
        tv.setPadding(5, 0, 0, 0);
        tv.setGravity(android.view.Gravity.LEFT);
        //tv.setHeight(40);
        tv.setWidth(LayoutParams.WRAP_CONTENT);
        return convertView;  
    } 
}