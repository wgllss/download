package android.utils;

import java.security.MessageDigest;

/**
 * <p>
 * Title:采用MD5加密
 * </p>
 * <p>
 * Description: MDPassword.java
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007-11-5
 * </p>
 * <p>
 * Company: FSFI
 * </p>
 *
 * @author 吴秋锦
 * @version v1.0
 * @param
 * @return
 * @创建时间 2007-11-5上午11:21:06
 * @修改历史 <li>版本号 修改日期 修改人 修改说明
 * <li>
 * <li>
 */
public class MDPassword
{

    /**
     * 得到MD5明文密码
     */
    public static String getPassword32(String s)
    {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try
        {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 向右移4位，左边补零
                // ,0xFF在C中是11111111,在JAVA中没试过
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 得到MD5明文密码
     */
    public static String getPassword16(String s)
    {
        return getPassword32(s).substring(8, 24);

    }

    public static String byte2hex(byte[] b) // 二行制转字符串
    {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
        }
        return hs.toUpperCase();
	}

}
