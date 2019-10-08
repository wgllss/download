package android.utils;

import java.util.Map;

public class CommonStringUtil {
	public static String emptyIfNull(String strValue) {
		if (strValue == null) {
			strValue = "";
		} else {
			if ("null".equals(strValue)) {
				strValue = "";
			}
			strValue = strValue.replace("null", "");
		}

		return strValue;
	}

	public static void setMap(Map<String, String> map, String mapKey, String mapValue) {
		if (mapValue != null && mapValue.length() > 0) {
			map.put(mapKey, mapValue);
		}
	}
}
