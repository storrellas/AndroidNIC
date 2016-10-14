package com.icognos.util;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;



public class Logger {
	private TextView tv;
	private ScrollView sv;
	private ILoggerOutput outputView;
	
	// Static attributtes configuring the LOG methodology
	public static final int VISUAL_CONSOLE_ON    = 0x01;
	public static final int LOG_FILE_ON          = 0x10;
	public static final int ONLY_ANDROID_CONSOLE = 0x00;
	
	public static String logAppName = "com.icognos";
	
	// Logger public logger
	public static Logger logger = null;
	
	
	/**
	 * Constructor, assigns android UI components to local components
	 * 
	 */
	private Logger(ILoggerOutput _outputView) {
		this.outputView = _outputView;
		outputView.setFileLog("===== Program Start =====\n");
		outputView.setVisualLog("===== Program Start =====\n");	
	}
	
	
	
	/**
	 * 
	 * @return singleton logger
	 */
	public static Logger getInstance(ILoggerOutput _outputView) {
		if (logger == null) {
			logger = new Logger(_outputView);
		}
		return logger;
	}
	
	public static Logger getInstance() {
		if (logger == null) {
				return null;
		}
		return logger;
	}
		
	/**
	 * Prints text in white color
	 * 
	 * @param s Text to print
	 */
	public void info( String msg, int mode ) {
		
		String formattedMsg = format ( new Exception().getStackTrace()[1], msg );

		Log.w(logAppName, formattedMsg + "\n");
		if( (mode & 0xF0) != 0){
			outputView.setFileLog(formattedMsg+ "\r\n");		
		}		
		if( (mode & 0x0F) != 0){
			outputView.setVisualLog( formattedMsg + "\r\n");			
		}

	}
	
	/**
	 * Prints text in gray color
	 * 
	 * @param l Use: new Exception().getStackTrace()[0]
	 * @param s Text to print
	 */
	public void debug(StackTraceElement l, String s) {
		appendColoredText(tv, format(l, s) + "\n", Color.GRAY);
		sv.fullScroll(View.FOCUS_DOWN);
	}

	/**
	 * Prints text in red color
	 * 
	 * @param l Use: new Exception().getStackTrace()[0]
	 * @param s Text to print
	 */
	public void error(StackTraceElement l, String s) {
		appendColoredText(tv, format(l, s) + "\n", Color.RED);
		sv.fullScroll(View.FOCUS_DOWN);
	}

	/**
	 * Prints text in red color
	 * 
	 * @param l Use: new Exception().getStackTrace()[0]
	 * @param s Text to print
	 */
	public void error( String s) {
		appendColoredText(tv, format(new Exception().getStackTrace()[1], s) + "\n", Color.RED);
		sv.fullScroll(View.FOCUS_DOWN);
	}
	
	/**
	 * Taking the StackTraceElement and the String, it adds the date and formats it
	 * 
	 * @param l StackTraceElement
	 * @param msg String to print
	 * @return formated String
	 */
	private String format(StackTraceElement l, String msg) {
		StringBuffer sb = new StringBuffer();

		// Get current date & time
		Calendar cal = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH);
		String now = sdf.format(cal.getTime());

		String fullClassName = l.getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

		// the message
		sb.append(now  + " | ");
		sb.append(className +  "." + l.getMethodName() + " (" + l.getLineNumber() + ") | ");
		sb.append( msg);

		return sb.toString();
	}

	/**
	 * Appends a msg in a specified color to the TextView
	 * 
	 * @param tv TextView
	 * @param text Message to print
	 * @param color Color in which to print
	 */
	private static void appendColoredText(TextView tv, String text, int color) {
		int start = tv.getText().length();
		tv.append(text);
		int end = tv.getText().length();

		Spannable spannableText = (Spannable) tv.getText();
		spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
	}
	
	
	/*!
	 * Converts the stackTrace to a String
	 */
	public static String stack2string(Exception e) {
		try {
		    StringWriter sw = new StringWriter();
		    PrintWriter pw = new PrintWriter(sw);
		    e.printStackTrace(pw);
		    return "------\r\n" + sw.toString() + "------\r\n";
		}catch(Exception e2) {
		  return "bad stack2string";
		}
	 }
	
}
