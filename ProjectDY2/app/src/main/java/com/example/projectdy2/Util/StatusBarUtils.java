package com.example.projectdy2.Util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

public class StatusBarUtils {

	public static int getHeight(Context context) {
		int statusBarHeight = 0 ;
		try {
			int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen","android");
			if (resourceId > 0) {
				statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	public static void setColor(@NonNull Window window, @ColorInt int color) {
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		window.setStatusBarColor(color);
//		setTextDark(window, !isDarkColor(color));
		setTextDark(window,true);
	}

	public static void setColor(Context context, @ColorInt int color) {
		if (context instanceof Activity)
			setColor(((Activity) context).getWindow(), color);
	}

	private static void setTextDark(Window window, boolean isDark) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			View decorView = window.getDecorView();
			int systemUiVisibility = decorView.getSystemUiVisibility();
			if (isDark) {
				decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			} else {
				decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			}
		}
	}
	public static void setTextDark(Context context, boolean isDark) {
		if (context instanceof Activity) {
			setTextDark(((Activity) context).getWindow(), isDark);
		}
	}
	public static boolean isDarkColor(@ColorInt int color) {
		return ColorUtils.calculateLuminance(color) < 0.5;
	}
}
