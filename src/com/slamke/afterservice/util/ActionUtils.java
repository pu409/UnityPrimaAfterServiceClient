package com.slamke.afterservice.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

import com.slamke.afterservice.R;

public class ActionUtils {
	private ActionUtils() {
	}

	public static SpannableString getDialAction(final Context mContext) {
		SpannableString s1 = new SpannableString(Const.ACITON_DIAL
				+ mContext.getResources().getString(R.string.dial_phone));
		s1.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View paramView) {
				Intent call = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:"+mContext.getResources().getString(R.string.dial_phone)));
				mContext.startActivity(call);
			}
		}, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s1;
	}
}
