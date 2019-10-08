package android.utils;
//package android.common;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.xml.sax.SAXException;
//import org.xmlpull.v1.XmlPullParserException;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.PixelFormat;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Environment;
//import android.view.WindowManager;
//
//public final class Util {
//
//	/**
//	 * 删除目录（文件夹）以及目录下的文件
//	 * 
//	 * @param sPath
//	 *            被删除目录的文件路径
//	 * @return 目录删除成功返回true，否则返回false
//	 */
//	public static boolean deleteDirectory(String sPath) {
//		boolean flag;
//		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
//		if (!sPath.endsWith(File.separator)) {
//			sPath = sPath + File.separator;
//		}
//		File dirFile = new File(sPath);
//		// 如果dir对应的文件不存在，或者不是一个目录，则退出
//		if (!dirFile.exists() || !dirFile.isDirectory()) {
//			return false;
//		}
//		flag = true;
//		// 删除文件夹下的所有文件(包括子目录)
//		File[] files = dirFile.listFiles();
//		for (int i = 0; i < files.length; i++) {
//			// 删除子文件
//			if (files[i].isFile()) {
//				flag = deleteFile(files[i].getAbsolutePath());
//				if (!flag)
//					break;
//			} // 删除子目录
//			else {
//				flag = deleteDirectory(files[i].getAbsolutePath());
//				if (!flag)
//					break;
//			}
//		}
//		if (!flag)
//			return false;
//		// 删除当前目录
//		if (dirFile.delete()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * 删除单个文件
//	 * 
//	 * @param sPath
//	 *            被删除文件的文件名
//	 * @return 单个文件删除成功返回true，否则返回false
//	 */
//	public static boolean deleteFile(String sPath) {
//		boolean flag = false;
//		File file = new File(sPath);
//		// 路径为文件且不为空则进行删除
//		if (file.isFile() && file.exists()) {
//			file.delete();
//			flag = true;
//		}
//		return flag;
//	}
//
//	// Drawable-->Bitmap
//	public static Bitmap drawableToBitmap(Drawable drawable) {
//		int width = drawable.getIntrinsicWidth();
//		int height = drawable.getIntrinsicHeight();
//		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
//				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//				: Bitmap.Config.RGB_565);
//		Canvas canvas = new Canvas(bitmap);
//		drawable.setBounds(0, 0, width, height);
//		drawable.draw(canvas);
//		return bitmap;
//
//	}
//
//	public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
//		Matcher m = p.matcher(mobiles);
//		System.out.println(m.matches() + "---");
//		return m.matches();
//	}
//
//	public static boolean isPhoneNumberValid(String phoneNumber) {
//		boolean isValid = false;
//
//		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
//
//		String expression2 = "^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
//
//		CharSequence inputStr = phoneNumber;
//
//		Pattern pattern = Pattern.compile(expression);
//
//		Matcher matcher = pattern.matcher(inputStr);
//
//		Pattern pattern2 = Pattern.compile(expression2);
//
//		Matcher matcher2 = pattern2.matcher(inputStr);
//		if (matcher.matches() || matcher2.matches()) {
//			isValid = true;
//		}
//		return isValid;
//	}
//
//	/*
//	 * public static void printLog(final Context context,Exception e,String
//	 * text){ BufferedWriter bw = null; try { String path =
//	 * Environment.getExternalStorageDirectory()+"/"
//	 * +context.getString(R.string.cache_path)+"/"; File f = new File(path);
//	 * if(!f.exists()){ f.mkdirs(); }
//	 * 
//	 * Calendar calendar = Calendar.getInstance(); final String hour =
//	 * String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)); final String minute =
//	 * String.valueOf(calendar.get(Calendar.MINUTE));
//	 * 
//	 * final String year = String.valueOf(calendar.get(Calendar.YEAR)); final
//	 * String mouth = String.valueOf(calendar.get(Calendar.MONTH)+1); final
//	 * String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)); final
//	 * String time = year+"/"+mouth+"/"+day+"/"+hour+":"+minute;
//	 * 
//	 * StringBuffer msg = new StringBuffer("----->"+time+"<----\n"); if(e !=
//	 * null){ msg.append(e.getMessage()+"\n");
//	 * msg.append(e.getLocalizedMessage()+"\n"); StackTraceElement[] elements =
//	 * e.getStackTrace(); for(int i=0;i<elements.length;i++){
//	 * 
//	 * if(elements[i].getClassName().startsWith("com.pcs")){
//	 * msg.append(elements[i].getClassName()
//	 * +"("+elements[i].getMethodName()+":" +elements[i].getLineNumber()+")\n");
//	 * } }
//	 * 
//	 * e.printStackTrace(); } msg.append(text).append("\n");
//	 * System.out.println("[exception]"+msg.toString());
//	 * 
//	 * final String error = msg.toString();
//	 * 
//	 * new Thread(new Runnable() { public void run() {
//	 * 
//	 * List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//	 * nameValuePairs.add(new BasicNameValuePair("error", error));
//	 * nameValuePairs.add(new BasicNameValuePair("time", time));
//	 * 
//	 * DataHttp data = new DataHttp(context, URLManager.getBugURL("", ""),
//	 * 10000); try { data.postMethod(nameValuePairs); } catch
//	 * (ClientProtocolException e) { e.printStackTrace(); } catch (IOException
//	 * e) { e.printStackTrace(); } } }).start();
//	 * 
//	 * bw = new BufferedWriter(new FileWriter(new File(path, "log.txt"),true));
//	 * /
//	 * /bw.write("[ERORR]"+o.getClass().getName()+"-->"+_methodName+"()"+":"+text
//	 * +"\n"); bw.write(error); bw.flush();
//	 * 
//	 * } catch (IOException e1) { e1.printStackTrace(); }finally{ if(bw!=null){
//	 * try { bw.close(); } catch (IOException e1) {} }
//	 * 
//	 * }
//	 * 
//	 * }
//	 */
//
//	public static void showDialog(Context context, String msg) {
//		new AlertDialog.Builder(context).setMessage(msg).setTitle("��ʾ")
//				.setNegativeButton("֪����", null).show();
//	}
//
//	public static String bitmaptoString(Bitmap bitmap) {
//
//		// 将Bitmap转换成字符串
//		String string = null;
//		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//		bitmap.compress(CompressFormat.JPEG, 100, bStream);
//		byte[] bytes = bStream.toByteArray();
//		// string = Base64.encodeToString(bytes);
//
//		return string;
//	}
//
//	/*
//	 * public static void showToast(Context c, String text, int drawableid) {
//	 * LayoutInflater inflater = LayoutInflater.from(c); View layout =
//	 * inflater.inflate(R.layout.mytoast, null); ImageView image = (ImageView)
//	 * layout.findViewById(R.id.iv_toast); if (0 != drawableid) {
//	 * image.setImageResource(drawableid); } else {
//	 * image.setVisibility(View.GONE); } TextView tvToast = (TextView)
//	 * layout.findViewById(R.id.tv_toast); tvToast.setText(text);
//	 * 
//	 * Toast toast = new Toast(c);
//	 * toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 100);
//	 * toast.setDuration(Toast.LENGTH_SHORT); toast.setView(layout);
//	 * toast.show(); }
//	 */
//
//	/*
//	 * public static int getImgID(String weatherID){ //int resID = -1; int
//	 * weather=0; int firstweather=0; try{ weather =
//	 * Integer.parseInt(weatherID); firstweather = R.drawable.pcsweathericon_00;
//	 * }catch (Exception e) { } return weather+firstweather; }
//	 */
//
//	/**
//	 * ��ȡ��Ļ�ĸߺͿ�
//	 * 
//	 * @param c
//	 * @return
//	 */
//	public static int getScreenHeight(Context c) {
//		WindowManager windowManager = (WindowManager) c
//				.getSystemService(Context.WINDOW_SERVICE);
//		return windowManager.getDefaultDisplay().getHeight();// ��ø߶ȣ���ÿ��Ҳ����
//	}
//
//	public static int getScreenWidth(Context c) {
//		WindowManager windowManager = (WindowManager) c
//				.getSystemService(Context.WINDOW_SERVICE);
//		return windowManager.getDefaultDisplay().getWidth();// ��ÿ�ȣ���ø߶�Ҳ����
//	}
//
//	// dipֵת����pxֵ
//	public static int Dip2Px(Context c, int dipValue) {
//		float scale = c.getResources().getDisplayMetrics().density;
//		if (scale == 0)
//			System.out.println("[scale] : 0");
//		System.out.println("[scale] :  " + scale);
//		System.out.println("[0.5f] :  " + 0.5f);
//		System.out.println("[dipValue * scale + 0.5f] :  " + dipValue * scale
//				+ 0.5f);
//		return (int) (dipValue * scale + 0.5f);
//	}
//
//	/******************************** ����XML ************************************/
//	public static Element parseXML(InputStream inStream)
//			throws XmlPullParserException, IOException {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder;
//		Element root = null;
//		try {
//			builder = factory.newDocumentBuilder();
//			Document dom = builder.parse(inStream);
//			root = dom.getDocumentElement();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return root;
//	}
//
//	/******************************** ��������� ************************************/
//	public static void SetValue(Activity activity, String XmlName, String key,
//			String value) {
//		Editor sharedata = activity.getSharedPreferences(XmlName,
//				Context.MODE_WORLD_READABLE).edit();
//		sharedata.putString(key, value);
//		sharedata.commit();
//	}
//
//	public final static void SetValue(Context context, String XmlName,
//			String key, String value) {
//		Editor sharedata = context.getSharedPreferences(XmlName,
//				Context.MODE_WORLD_READABLE).edit();
//		sharedata.putString(key, value);
//		sharedata.commit();
//	}
//
//	public final static String GetValue(Activity activity, String XmlName,
//			String key) {
//		String value = null;
//		SharedPreferences sharedata = activity.getSharedPreferences(XmlName,
//				Context.MODE_WORLD_READABLE);
//		value = sharedata.getString(key, "");
//		return value;
//	}
//
//	public final static String GetValue(Context context, String XmlName,
//			String key) {
//		String value = null;
//		SharedPreferences sharedata = context.getSharedPreferences(XmlName, 0);
//		value = sharedata.getString(key, "");
//		return value;
//	}
//
//	public final static void DeleteKey(Context context, String XmlName,
//			String key) {
//		Editor editor = context.getSharedPreferences(XmlName, 0).edit();
//		editor.remove(key);
//		editor.commit();
//	}
//
//	@SuppressWarnings("rawtypes")
//	public final static List<String> GetMultiValue(Activity activity,
//			String XmlName) {
//		List<String> values = new ArrayList<String>();
//		SharedPreferences sharedata = activity.getSharedPreferences(XmlName, 0);
//		java.util.Map<String, ?> m = sharedata.getAll();
//		for (Iterator i = m.values().iterator(); i.hasNext();) {
//			Object obj = i.next();
//			System.out.println(obj);// ѭ�����value
//			values.add(obj.toString());
//		}
//		return values;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public final static ArrayList<String> GetMultiKey(Context context,
//			String XmlName) {
//		ArrayList<String> keys = new ArrayList<String>();
//		SharedPreferences sharedata = context.getSharedPreferences(XmlName, 0);
//		java.util.Map<String, ?> m = sharedata.getAll();
//		for (Iterator i = m.keySet().iterator(); i.hasNext();) {
//			String key = i.next().toString();
//			keys.add(key);
//		}
//		return keys;
//	}
//
//	public final static int GetValueLength(Activity activity, String XmlName) {
//		SharedPreferences sharedata = activity.getSharedPreferences(XmlName, 0);
//		return sharedata.getAll().size();
//	}
//
//	// ȫ��ɾ��XML�ļ��е�����
//	public final static void DeleteAll(Context context, String XmlName) {
//		final Editor sharedata = context.getSharedPreferences(XmlName, 0)
//				.edit();
//		sharedata.clear();
//		sharedata.commit();
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static void DeleteValue(Activity activity, String XmlName,
//			String Value) {
//		SharedPreferences sharedata = activity.getSharedPreferences(XmlName, 0);
//		java.util.Map<String, ?> m = sharedata.getAll();
//		for (Iterator i = m.keySet().iterator(); i.hasNext();) {
//			String key = i.next().toString();
//			if (GetValue(activity, XmlName, key).equals(Value)) {
//				DeleteKey(activity, XmlName, key);
//				break;
//			}
//		}
//	}
//
//	public static int dip2px(Context context, float dipValue) {
//		final float scale = context.getResources().getDisplayMetrics().density;
//		System.out.println("dipValue * scale + 0.5f" + dipValue * scale + 0.5f);
//		return (int) (dipValue * scale + 0.5f);
//	}
//
//	public static int px2dip(Context context, float pxValue) {
//		final float scale = context.getResources().getDisplayMetrics().density;
//		return (int) (pxValue / scale + 0.5f);
//	}
//
//	public static boolean SDCardExists() {
//		return (Environment.getExternalStorageState()
//				.equals(Environment.MEDIA_MOUNTED));
//	}
//
//	public static Bitmap getBimap(File file) {
//		if (!Util.SDCardExists()) {
//			return null;
//		}
//		if (file.exists()) {
//			Bitmap img = BitmapFactory.decodeFile(file.getAbsolutePath());
//			if (img != null) {
//				return img;
//			}
//		}
//		return null;
//	}
//
//	public static boolean saveBitmap(Bitmap img, String path, String name)
//			throws FileNotFoundException {
//
//		if (!Util.SDCardExists()) {
//			return false;
//		}
//
//		if (img != null) {
//			File f = new File(path);
//			if (!f.exists()) {
//				f.mkdirs();
//			}
//			FileOutputStream iStream = new FileOutputStream(
//					new File(path, name));
//			img.compress(CompressFormat.JPEG, 100, iStream);
//			return true;
//		}
//		return false;
//	}
//
//	/*********** �޸�Activity������ *************/
//	public static void setBrightness(Activity activity, float value) {
//		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//		lp.screenBrightness = value;
//		activity.getWindow().setAttributes(lp);
//	}
//
//	/************ ���ַ��� ************/
//	public final static void sendShare4Text(Activity activity, String content) {
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_SUBJECT, "����");
//		intent.putExtra(Intent.EXTRA_TEXT, content);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
//	}
//
//	public final static void sendShare4Image(Activity activity, Bitmap content) {
//		Intent intent = new Intent(Intent.ACTION_SEND);
//		intent.setType("image/*");
//		intent.putExtra(Intent.EXTRA_SUBJECT, "����");
//		intent.putExtra(Intent.EXTRA_STREAM, content);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
//	}
//
//	public final static void userLogin() {
//		System.out.println("onClick  btn_top_bar_user");
//	}
//
//	public final static void finishApp(Activity activity) {
//		activity.finish();
//	}
//
//	@SuppressWarnings({ "finally" })
//	public static int getAppVersionCode(Context context, String packageName) {
//		int versionCode = 0;
//		try {
//			PackageManager pm = context.getPackageManager();
//			PackageInfo pi = pm.getPackageInfo(packageName, 0);
//			versionCode = pi.versionCode;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			return versionCode;
//		}
//	}
//
//	@SuppressWarnings({ "finally" })
//	public static int getAppVersionCode(PackageInfo pi) {
//		int versionCode = 0;
//		try {
//			versionCode = pi.versionCode;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			return versionCode;
//		}
//	}
//
//	public static void showImage(Context context, String path) {
//
//		try {
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			String type = "image/*";
//			Uri name = Uri.parse("file://" + path);
//			intent.setDataAndType(name, type);
//			context.startActivity(intent);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * ��ת������wifi�������
//	 * 
//	 * @param fromPage
//	 *            ���ý���
//	 */
//	public static void goToSetNetwork(final Context fromPage) {
//		final Intent intent = new Intent(
//				android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//		fromPage.startActivity(intent);
//	}
// }
