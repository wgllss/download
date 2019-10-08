package android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.application.CommonApplication;

public class JsonTestDataManager {
	private static JsonTestDataManager mInstance;
	private List<String> lstFile;
	private Map<String, String> mapJsonContent = new HashMap<String, String>();

	public static JsonTestDataManager getInstance() {
		if (mInstance == null) {
			mInstance = new JsonTestDataManager();
			mInstance.Init();
		}
		return mInstance;
	}

	private void Init() {
		// 加载所有图片路径

		try {
			lstFile = Arrays.asList(CommonApplication.getContext().getAssets()
					.list("jsonTest"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 从assets 文件夹中获取文件并读取数据
	private String getFromAssets(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					CommonApplication.getContext().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public String GetJsonByFile(String strFile) {
		if (mapJsonContent.containsKey(strFile)) {
			return mapJsonContent.get(strFile);
		}

		if (lstFile.contains(strFile)) {
			return getFromAssets("jsonTest/" + strFile);
		}

		return "";
	}
}
