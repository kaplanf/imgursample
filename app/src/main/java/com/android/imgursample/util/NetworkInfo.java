package com.android.imgursample.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkInfo {

	public static boolean isInternetAvailable(Context c) {
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
	}
}
