package libericc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Whole program configuration
 */
public class Config {
	public enum OutputFormat { none, jimple, apk; }
	public enum Instrument { none, prune, aggressive, pre_evaluate;}

	static JSONObject iccLogs;
	public static Instrument instrument = Instrument.prune;
	public static OutputFormat outputFormat = OutputFormat.apk;
	public static String icclogPath;
	public static String apkPath;
	public static String androidjars;
	public static String adblogPath = null;
	public static int iccNo = 0;
	
	// load icc log from file to string
	static public void loadIccLogs(String filename){
		String content = null;
		Scanner s = null;
		try {
			s = new Scanner(new File(filename));
			content = s.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		iccLogs = new JSONObject(content);
		iccPreProcess();
	}
	
	// add new map with the "key" field in mExtras(bundle)
	static void iccPreProcess() {
		for (String key: iccLogs.keySet()) {
			try {
				JSONObject map = new JSONObject();
				JSONObject intent = Utility.iccToIntent(iccLogs.getJSONObject(key));
				JSONObject bundle = intent.getJSONObject("mExtras");
				
				JSONArray arr = bundle.getJSONArray("ArrayMap");
				for (int i=0; i<arr.length(); i++) {
					JSONObject a = arr.getJSONObject(i);
					map.put(a.getString("key"), a);
				}
				bundle.put("map", map);
			}
			catch (Exception e) {}
		}
	}
	
	static public JSONObject getIccLogs(){
		return iccLogs;
	}
}
