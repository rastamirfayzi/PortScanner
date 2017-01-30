package com.portscanner.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.portscanner.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationConstant {

	public static final String TERMS_OF_USE = "By Registering account or signing in you agree to our Terms of use";
	public static final String TERMS_OF_USE_LINK = "Terms of use";

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";

	public static final String PASSWORD_PATTERN = "((?!\\s)\\A)(\\s|(?<!\\s)\\S){4,20}\\Z";// "^[A-Za-z0-9]{4,20}$"

	public static final String INPUTPORTPATTERN="^(?!([ \\d]*-){2})\\d+(?: *[-,] *\\d+)*$";
	public static String internetMessage = "Please check internet connection";

	private static final String IPADDRESS_PATTERN =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	//^(\\s*\\d+\\s*\\-\\s*\\d+\\s*,?|\\s*\\d+\\s*,?)+$
	// "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"

	public static void hideKeyBoard(Context ct, View v) {
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) ct
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
	public static final void showToast(Context mContext, String msg) {
		Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
	}
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	public static void hideKeyBoard(Context ct, EditText ed) {
		InputMethodManager imm = (InputMethodManager) ct
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
	}
	public static boolean isNetworkAvailable(Context context) {

		NetworkInfo localNetworkInfo = ((ConnectivityManager) context
				.getSystemService("connectivity")).getActiveNetworkInfo();
		return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
	}


	public static boolean isStringNullOrBlank(String str) {

		try{

			if (str == null) {
				return true;
			} else if (str.equals("null") || str.equals("")) {
				return true;
			}

		}catch(Exception e){

			e.printStackTrace();
		}
		return false;
	}
	public static boolean isValidPortValidation(String port)
	{
	    Pattern pattern = Pattern.compile(INPUTPORTPATTERN);
		Matcher matcher = pattern.matcher(port);
		return matcher.matches();
	}
	public static boolean isvalidIPAddress( String ip){
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	public static void showValidationDialog(Context context,String detail) {

		try {
			final Dialog dialog = new Dialog(context);
			LayoutInflater li = (LayoutInflater)context.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vi = li.inflate(R.layout.validation_popup_view, null, false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(vi);
			dialog.setCanceledOnTouchOutside(false);
			TextView tv_message = (TextView) vi.findViewById(R.id.tv_message);
			tv_message.setText(detail);

			TextView ok_bt = (TextView) vi.findViewById(R.id.tv_ok);

			ok_bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String  getCurrentDate() {
		String today=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		try {
			today = sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return today;
	}
	/*public static Date  getCurrentDate() {
		Date today=null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH);
		try {
			today = new Date();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return today;
	}*/
	public static String getCurrentTime() {
		String today=null;
		SimpleDateFormat stf = new SimpleDateFormat("h:mma", Locale.ENGLISH);
		try {
			today = stf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return today;
	}
	public static String decreaseOneDay(String StrDate){

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date dateNew=null;
		try {
			Date date = sdf.parse(StrDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);  // number of days to add
			dateNew = calendar.getTime();
		}catch (Exception e){
			e.printStackTrace();
		}
		String reportDate = sdf.format(dateNew);
		return  reportDate;
	}


}
