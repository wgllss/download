package android.utils;//package android.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import android.activity.R;
//import android.appconfig.AppConfigSetting;
//import android.content.Context;
//import android.enums.SkinMode;
//import android.text.Html;
//import android.view.View;
//import android.widget.TextView;
//
//public class UbbCode {
//
//	public static String ubbItem(String strContent, String re, String replayStr, Pattern pattern, Matcher matcher, boolean IgnoreCase) {
//		if (IgnoreCase) {
//			pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
//		} else {
//			pattern = Pattern.compile(re);
//		}
//		matcher = pattern.matcher(strContent);
//		strContent = matcher.replaceAll(replayStr);
//		return strContent;
//	}
//
//	private static String AtUbbItem(String strContent, String re, String replayStr, Pattern pattern, Matcher matcher, boolean IgnoreCase) {
//		if (IgnoreCase) {
//			pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
//		} else {
//			pattern = Pattern.compile(re);
//		}
//		matcher = pattern.matcher(strContent);
//		strContent = matcher.replaceAll(replayStr);
//		if (strContent.length() > 12) {
//			return strContent;
//		} else {
//			String n = strContent.replace("@", "");
//			return n;
//		}
//	}
//
//	public static String clearHtml(String strContent) {
//		Pattern pattern = Pattern.compile("<[^>]*>");
//		Matcher matcher = pattern.matcher(strContent);
//		StringBuffer sb = new StringBuffer();
//		boolean result1 = matcher.find();
//		while (result1) {
//			matcher.appendReplacement(sb, "");
//			result1 = matcher.find();
//		}
//		matcher.appendTail(sb);
//		return sb.toString().replaceAll("&nbsp;", "");
//	}
//
//	/**
//	 * 清除TAG
//	 * @author :Atar
//	 * @createTime:2014-11-28下午6:43:15
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strContent
//	 * @return
//	 * @description:
//	 */
//	public static String clearTAG(String strContent) {
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strContent = ubbItem(strContent, "\\[tag\\](.*?)\\[\\/tag\\]", "$1", pattern, matcher, true);
//		strContent = strContent.replace("[tag]", "");
//		strContent = strContent.replace("[/tag]", "");
//		return strContent;
//	}
//
//	public static String cleartGuBar(String strContent) {
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strContent = ubbItem(strContent, "\\[gguba\\](.*?)\\[\\/gguba\\]", "$1", pattern, matcher, true);
//		strContent = ubbItem(strContent, "\\[ggubar\\](.*?)\\[\\/ggubar\\]", "$1", pattern, matcher, true);
//		return strContent;
//	}
//
//	/*** 
//	* 获取ImageUrl地址 
//	*  
//	* @param HTML 
//	* @return 
//	*/
//	public static List<String> getImageUrl(String HTML) {
//		String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
//		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
//		List<String> listImgUrl = new ArrayList<String>();
//		while (matcher.find()) {
//			listImgUrl.add(matcher.group());
//		}
//		return listImgUrl;
//	}
//
//	/*** 
//	 * 获取ImageSrc地址 
//	 *  
//	 * @param listImageUrl 
//	 * @return 
//	 */
//	public static List<String> getImageSrc(List<String> listImageUrl) {
//		String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
//		List<String> listImgSrc = new ArrayList<String>();
//		for (String image : listImageUrl) {
//			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
//			while (matcher.find()) {
//				String imgUrl = matcher.group().substring(0, matcher.group().length() - 1);
//				listImgSrc.add(imgUrl);
//			}
//		}
//		return listImgSrc;
//	}
//
//	/**
//	 * 清除图片
//	 * @author :Atar
//	 * @createTime:2014-10-2上午9:42:37
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strContent
//	 * @return
//	 * @description:
//	 */
//	public static String clearImgsrc(String strContent) {
//		// String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
//		strContent = strContent.replace("<br><img", "<!--imageInfo--><br><img");
//		// strContent = strContent.replace("/><br/>", "/><br/><!--imageInfo-->");
//		// strContent = strContent.replace("/><br />", "/><br/><!--imageInfo-->");
//		// strContent = strContent.replace("><br/>", "/><br/><!--imageInfo-->");
//
//		strContent = strContent.replace("JPG\">", "JPG\"><!--imageInfo-->");
//		strContent = strContent.replace("jpg\">", "jpg\"><!--imageInfo-->");
//		strContent = strContent.replace("PNG\">", "PNG\"><!--imageInfo-->");
//		strContent = strContent.replace("png\">", "png\"><!--imageInfo-->");
//		strContent = strContent.replace("GIF\">", "GIF\"><!--imageInfo-->");
//		strContent = strContent.replace("gif\">", "gif\"><!--imageInfo-->");
//
//		strContent = strContent.replace("JPG\" >", "JPG\"><!--imageInfo-->");
//		strContent = strContent.replace("jpg\" >", "jpg\"><!--imageInfo-->");
//		strContent = strContent.replace("PNG\" >", "PNG\"><!--imageInfo-->");
//		strContent = strContent.replace("png\" >", "png\"><!--imageInfo-->");
//		strContent = strContent.replace("GIF\" >", "GIF\"><!--imageInfo-->");
//		strContent = strContent.replace("gif\" >", "gif\"><!--imageInfo-->");
//
//		strContent = strContent.replace("JPG\"/>", "JPG\"><!--imageInfo-->");
//		strContent = strContent.replace("jpg\"/>", "jpg\"><!--imageInfo-->");
//		strContent = strContent.replace("PNG\"/>", "PNG\"><!--imageInfo-->");
//		strContent = strContent.replace("png\"/>", "png\"><!--imageInfo-->");
//		strContent = strContent.replace("GIF\"/>", "GIF\"><!--imageInfo-->");
//		strContent = strContent.replace("gif\"/>", "gif\"><!--imageInfo-->");
//
//		strContent = strContent.replace("JPG\" />", "JPG\"><!--imageInfo-->");
//		strContent = strContent.replace("jpg\" />", "jpg\"><!--imageInfo-->");
//		strContent = strContent.replace("PNG\" />", "PNG\"><!--imageInfo-->");
//		strContent = strContent.replace("png\" />", "png\"><!--imageInfo-->");
//		strContent = strContent.replace("GIF\" />", "GIF\"><!--imageInfo-->");
//		strContent = strContent.replace("gif\" />", "gif\"><!--imageInfo-->");
//
//		strContent = strContent.replace(".jpg@!topic\">", ".jpg@!topic\"><!--imageInfo-->");
//
//		// strContent = strContent.replace("</span></b/><br/><!--imageInfo-->", "</span></b/><br/>");
//		strContent = strContent.replace("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<divstyle=", "");
//
//		String IMGURL_REG = "(\\<!--imageInfo-->)(.*?)(\\<!--imageInfo-->)";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strContent = ubbItem(strContent, IMGURL_REG, "", pattern, matcher, true);
//		return strContent;
//	}
//
//	/**
//	 * 清除图片
//	 * @author :Atar
//	 * @createTime:2014-10-2上午9:42:37
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strContent
//	 * @return
//	 * @description:
//	 */
//	public static String clearImgList(String strContent) {
//		String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strContent = ubbItem(strContent, IMGURL_REG, "", pattern, matcher, true);
//		return strContent;
//	}
//
//	/**
//	 * 得到语音
//	 * @author :Atar
//	 * @createTime:2014-10-2上午9:44:32
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @return
//	 * @description:
//	 */
//	public static List<String> getVoiceList(String html) {
//		String VOICE_REG = "\\[voice=(.+\\.mp3),([0-9]+\\.[0-9]*),(.+\\.spx)\\]";
//		List<String> voiceList = new ArrayList<String>();
//		Matcher matcher = Pattern.compile(VOICE_REG).matcher(html);
//		while (matcher.find()) {
//			String voice = matcher.group();
//			voice = voice.replaceAll("&quot;", "\"");
//			if (voice.length() > 7) {
//				if (voice.contains("][voice=")) {
//					voice = voice.replaceAll("\\]\\[voice=", "\\];\\[voice=");
//					String[] voices = voice.split(";");
//					for (int i = 0; i < voices.length; i++) {
//						voices[i] = voices[i].substring(7, voices[i].length() - 1);
//						voices[i] = voices[i].replace("“", "\"");
//						voiceList.add(voices[i]);
//					}
//				} else {
//					voice = voice.substring(7, voice.length() - 1);
//					voice = voice.replace("“", "\"");
//					voiceList.add(voice);
//				}
//			} else {
//				voice = voice.replace("“", "\"");
//				voiceList.add(voice);
//			}
//
//		}
//		return voiceList;
//	}
//
//	/**
//	 * 从贴子里面获取语音
//	 * @author :Atar
//	 * @createTime:2014-11-24下午4:11:09
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param html
//	 * @return
//	 * @description:
//	 */
//	public static List<String> getVoiceListFromTopic(String html) {
//		// String VOICE_REG = "\\<!--voice--><a style=\"display: none;\">(.+\\.mp3),([0-9]+\\.[0-9]*),(.+\\.spx)</a>\\<!--voice--> ";
//		String VOICE_REG = "(\\<!--voice-->)(.*?)(\\<!--voice-->)";
//		List<String> voiceList = new ArrayList<String>();
//		Matcher matcher = Pattern.compile(VOICE_REG).matcher(html);
//		while (matcher.find()) {
//			String voice = matcher.group();
//			if (voice.length() > 56) {
//				voice = voice.substring(38, voice.length() - 16);
//			}
//			voice = voice.replaceAll("&quot;", "\"");
//			voice = voice.replace("“", "\"");
//			voiceList.add(voice);
//		}
//		return voiceList;
//	}
//
//	/**
//	 * 清除视频
//	 * @author :Atar
//	 * @createTime:2015-1-21下午5:31:20
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String clearVidio(String strHtml) {
//		String VOICE_REG = "(\\<embed>)(.*?)(\\</embed>)";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strHtml = ubbItem(strHtml, VOICE_REG, "", pattern, matcher, true);
//		return strHtml;
//	}
//
//	/**
//	 * 清除贴子中语音
//	 * @author :Atar
//	 * @createTime:2014-11-24下午4:36:42
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String clearTopicVoice(String strHtml) {
//		String VOICE_REG = "(\\<!--voice-->)(.*?)(\\<!--voice-->)";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strHtml = ubbItem(strHtml, VOICE_REG, "", pattern, matcher, true);
//		String VOICE_REG2 = "(\\<!--voiceInfo-->)(.*?)(\\<!--voiceInfo-->)";
//		strHtml = ubbItem(strHtml, VOICE_REG2, "", pattern, matcher, true);
//		return strHtml;
//	}
//
//	/**
//	 * 清除语音
//	 * @author :Atar
//	 * @createTime:2014-10-2上午9:52:33
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String cleatVoice(String strHtml) {
//		String VOICE_REG = "\\[voice=(.+\\.mp3),([0-9]+\\.[0-9]*),(.+\\.spx)\\]";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strHtml = ubbItem(strHtml, VOICE_REG, "", pattern, matcher, true);
//		return strHtml;
//	}
//
//	/**
//	 * 得到说说标题
//	 * @author :Atar
//	 * @createTime:2014-10-2上午9:52:33
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String getShuoTitle(String strHtml) {
//		String VOICE_REG = "(【)(.*?)(】)";
//		Matcher matcher = Pattern.compile(VOICE_REG).matcher(strHtml);
//		String title = "";
//		while (matcher.find()) {
//			title = matcher.group();
//		}
//		return title;
//	}
//
//	/**
//	 * 清除说说标题
//	 * @author :Atar
//	 * @createTime:2014-10-16下午5:23:28
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String clearShuoTitle(String strHtml) {
//		String VOICE_REG = "(【)(.*?)(】)";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strHtml = ubbItem(strHtml, VOICE_REG, "", pattern, matcher, true);
//		return strHtml;
//	}
//
//	/**
//	 * 得到图片list
//	 * @author :Atar
//	 * @createTime:2014-10-14下午8:49:52
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param html
//	 * @return
//	 * @description:
//	 */
//	public static List<String> getImageList(String html) {
//		String VOICE_REG = "(\\[img]\\])(.*?)(\\[\\/img\\])";
//		List<String> voiceList = new ArrayList<String>();
//		Matcher matcher = Pattern.compile(VOICE_REG).matcher(html);
//		while (matcher.find()) {
//			String voice = matcher.group();
//			voice = voice.replaceAll("&quot;", "\"");
//			if (voice.length() > 7) {
//				voice = voice.substring(7, voice.length() - 8);
//			}
//			voiceList.add(voice);
//		}
//		return voiceList;
//	}
//
//	/**
//	 * 处理没有加点击事件的图片
//	 * @author :Atar
//	 * @createTime:2015-2-3上午9:33:37
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String replaceImg(String strHtml) {
//		String imgSrc = "<img src=(.*?)>";// 处理没有加点击事件的图片
//		Matcher matcher2 = Pattern.compile(imgSrc).matcher(strHtml);
//		while (matcher2.find()) {
//			String src = matcher2.group();
//			if (!src.contains("onclick") && !src.contains("class=\"lazy\"") && !src.contains("data-original")) {
//				if (src.length() > 9) {
//					src = src.substring(9, src.length() - 1);
//					src = src.replace("\"", "");// 多了引号去掉
//				}
//				strHtml = strHtml.replace(matcher2.group(), "<br/><img src=\"placeHolder.png\" onclick='window.injs.runOnAndroid(\"" + src + "\")' class=\"lazy\" data-original=\"" + src + "\">");
//			}
//		}
//		return strHtml;
//	}
//
//	// 替换说说中内容
//	public static String replace(String strHtml) {
//		Pattern pattern = null;
//		Matcher matcher = null;
//
//		// 图片转换1
//		strHtml = ubbItem(strHtml, "\\[img\\]\\s*[javascript]*(../|/)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]",
//				"<br/><img src=\"placeHolder.png\" onclick='window.injs.runOnAndroid(\"" + UrlParamCommon.IMAGE_HOST + "$2$3\")' class=\"lazy\" data-original=\"" + UrlParamCommon.IMAGE_HOST
//						+ "$2$3\">", pattern, matcher, true);
//		// 转换站外图
//		strHtml = ubbItem(strHtml, "\\[img\\]\\s*[javascript]*(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]",
//				"<br/><img src=\"placeHolder.png\" onclick='window.injs.runOnAndroid(\"" + UrlParamCommon.IMAGE_HOST + "$1$2\")' class=\"lazy\" data-original=\"" + UrlParamCommon.IMAGE_HOST
//						+ "$1$2\">", pattern, matcher, true);
//		// 图片转换2
//		strHtml = ubbItem(strHtml, "\\[img\\]\\s*[javascript]*(http://www..com.cn/|http://www..net)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]",
//				"<br/><img src=\"placeHolder.png\" onclick='window.injs.runOnAndroid(\"" + UrlParamCommon.IMAGE_HOST + "$2$3\")' class=\"lazy\" data-original=\"" + UrlParamCommon.IMAGE_HOST
//						+ "$2$3\">", pattern, matcher, true);
//		strHtml = replaceImg(strHtml);
//		// 超链接1
//		strHtml = ubbItem(strHtml, "\\[url\\](.*?)\\[\\/url\\]", "<a href=\"$1\">$1</a>", pattern, matcher, true);
//		// 超链接2
//		strHtml = getLinkName(strHtml, "\\[link\\](.*?)\\[\\/link\\]", "<a href=\"$1\">$1</a>", pattern, matcher, true);
//		// 表情
//		strHtml = ubbItem(strHtml, "\\[F\\](.*?)\\[\\/F\\]", "<img src=\"" + UrlParamCommon.getFacePath("$1") + "\">", pattern, matcher, true);
//		strHtml = ubbItem(strHtml, "\\[flash\\]\\s*[javascript]*(.[^\\]]*)(\\?|\\&|\\#)(.[^\\]]*)\\[\\/flash\\]", "<div align=center>不支持接动态链接<font color=\"red\">$1$2$3</font></div>", pattern,
//				matcher, true);
//		// FLASH
//		strHtml = ubbItem(strHtml, "(\\[flash\\])(.*?)(\\[\\/flash\\])", "[视频]", pattern, matcher, true);
//		// 视频
//		strHtml = ubbItem(strHtml, "\\[video\\](.*?)\\[\\/video\\]", "[视频]", pattern, matcher, true);
//
//		// 个股链接
//		strHtml = strHtml.replace("[stock]sz[gguba]", "[stock]");
//		strHtml = strHtml.replace("[stock]sh[gguba]", "[stock]");
//		strHtml = strHtml.replace("[stock]sz[gubar]", "[stock]");
//		strHtml = strHtml.replace("[stock]sh[gubar]", "[stock]");
//		strHtml = strHtml.replace("[/gguba][/stock]", "[/stock]");
//		strHtml = strHtml.replace("[/gubar][/stock]", "[/stock]");
//		strHtml = ubbItem(strHtml, "\\[gguba\\](.+?)\\[\\/gguba\\]", "<a href=\"mSearch?searchContent=$1&type=0\" style=\"color:#5193C7;\">$1</a>", pattern, matcher, true);
//		strHtml = ubbItem(strHtml, "\\[gubar\\](.+?)\\[\\/gubar\\]", "<a href=\"mSearch?searchContent=$1&type=0\" style=\"color:#5193C7;\">$1</a>", pattern, matcher, true);
//		strHtml = ubbItem(strHtml, "\\[ gubar\\](.+?)\\[\\/gubar\\]", "<a href=\"mSearch?searchContent=$1&type=0\" style=\"color:#5193C7;\">$1</a>", pattern, matcher, true);
//		/* 替换说说最终页里面主题标签为链接 */
//		strHtml = ubbItem(strHtml, "\\[tag\\](.+?)\\[\\/tag\\]", "<a href=\"mSearch?searchContent=$1&type=3\" style=\"color:#5193C7;\">$1</a>", pattern, matcher, true);
//		// strHtml = ubbItem(strHtml, "\\[tag\\](.*?)\\[\\/tag\\]", "$1", pattern, matcher, true);
//		// 解析行情
//		strHtml = ubbItem(strHtml, "\\[stock\\](.+?)\\[\\/stock\\]", "<a href=\"mSearch?searchContent=$1&type=0\" style=\"color:#5193C7;\">$1</a>", pattern, matcher, true);
//		strHtml = ubbItem(strHtml, "\\[stock\\](\\d{6})\\[\\/stock\\]", "<a href=\"mSearch?searchContent=$1&type=0\" style=\"color:#5193C7;\">$1</a>", pattern, matcher, true);
//		strHtml = replaceFaceHtml(strHtml);
//		return strHtml;
//	}
//
//	/**
//	 * 替换自定义表情 html
//	 * @author :Atar
//	 * @createTime:2015-3-13下午3:53:24
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String replaceFaceHtml(String strHtml) {
//		// strHtml = URLDecoder.decode(strHtml);
//		// strHtml = strHtml.replace("[傲慢]", "<img src=\"../img/express_aoman.png\" height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[鄙视]", "<img src=\"../img/express_bishi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[闭嘴]", "<img src=\"../img/express_bizui.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[不逊]", "<img src=\"../img/express_buxun.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[关灯吃面]", "<img src=\"../img/express_guandengchimian.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[耻笑]", "<img src=\"../img/express_chixiao.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[大笑]", "<img src=\"../img/express_daxiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[得瑟]", "<img src=\"../img/express_dese.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[饿了]", "<img src=\"../img/express_ele.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[鼓掌]", "<img src=\"../img/express_guzhang.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[海盗]", "<img src=\"../img/express_haidao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[害羞]", "<img src=\"../img/express_haixiu.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[好困惑]", "<img src=\"../img/express_haokunhuo.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[哈欠]", "<img src=\"../img/express_haqian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[加油]", "<img src=\"../img/express_jiayou.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[近视眼]", "<img src=\"../img/express_jinshiyan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[惊讶]", "<img src=\"../img/express_jingya.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[囧]", "<img src=\"../img/express_jiong.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[可怜]", "<img src=\"../img/express_kelian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[抠鼻]", "<img src=\"../img/express_koubi.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[亏大了]", "<img src=\"../img/express_kuidale.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[困]", "<img src=\"../img/express_kunle.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[流汗]", "<img src=\"../img/express_liuhan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[流泪]", "<img src=\"../img/express_liulei.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[流血]", "<img src=\"../img/express_liuxie.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[卖身]", "<img src=\"../img/express_maisheng.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[牛逼]", "<img src=\"../img/express_niubi.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[傻逼]", "<img src=\"../img/express_sb.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[色]", "<img src=\"../img/express_se.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[生气]", "<img src=\"../img/express_shengqi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[难过]", "<img src=\"../img/express_shiwangnanguo.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[睡觉]", "<img src=\"../img/express_shuijiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[叹气]", "<img src=\"../img/express_tanqi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[吐舌头]", "<img src=\"../img/express_tushetou.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[围观]", "<img src=\"../img/express_weiguan.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[猥琐]", "<img src=\"../img/express_weisuo.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[微笑]", "<img src=\"../img/express_weixiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[为什么]", "<img src=\"../img/express_why.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[思考]", "<img src=\"../img/express_xiangyixia.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[发财了]", "<img src=\"../img/express_zhuandale.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[心动]", "<img src=\"../img/express_aixin.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[抄底]", "<img src=\"../img/express_chaodi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[成交]", "<img src=\"../img/express_chengjiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[翔了]", "<img src=\"../img/express_dabian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[赞一个]", "<img src=\"../img/express_damuzhi_shang.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[弱爆了]", "<img src=\"../img/express_damuzhi_xia.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[多]", "<img src=\"../img/express_duo.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[护城河]", "<img src=\"../img/express_huchenghe.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[加]", "<img src=\"../img/express_jia.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[减]", "<img src=\"../img/express_jian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[空仓]", "<img src=\"../img/express_kongcang.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[满仓]", "<img src=\"../img/express_mancang.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[鲜花]", "<img src=\"../img/express_meigui.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[美元]", "<img src=\"../img/express_meiyuan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[空]", "<img src=\"../img/express_null.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[心碎]", "<img src=\"../img/express_xinsuile.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[复权]", "<img src=\"../img/express_fupan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[干杯]", "<img src=\"../img/express_ganbei.png\"height=\"35\" width=\"35\">");
//		return strHtml;
//	}
//
//	/**
//	 * 替换内容中的图片
//	 * @author :Atar
//	 * @createTime:2015-3-27下午2:07:28
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String replaceExpresstionContent(String strHtml) {
//		// strHtml = strHtml.replace("[傲慢]", "<img src=\"img/express_aoman.png\" height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[鄙视]", "<img src=\"img/express_bishi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[闭嘴]", "<img src=\"img/express_bizui.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[不逊]", "<img src=\"img/express_buxun.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[关灯吃面]", "<img src=\"img/express_guandengchimian.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[耻笑]", "<img src=\"img/express_chixiao.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[大笑]", "<img src=\"img/express_daxiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[得瑟]", "<img src=\"img/express_dese.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[饿了]", "<img src=\"img/express_ele.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[鼓掌]", "<img src=\"img/express_guzhang.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[海盗]", "<img src=\"img/express_haidao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[害羞]", "<img src=\"img/express_haixiu.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[好困惑]", "<img src=\"img/express_haokunhuo.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[哈欠]", "<img src=\"img/express_haqian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[加油]", "<img src=\"img/express_jiayou.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[近视眼]", "<img src=\"img/express_jinshiyan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[惊讶]", "<img src=\"img/express_jingya.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[囧]", "<img src=\"img/express_jiong.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[可怜]", "<img src=\"img/express_kelian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[抠鼻]", "<img src=\"img/express_koubi.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[亏大了]", "<img src=\"img/express_kuidale.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[困]", "<img src=\"img/express_kunle.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[流汗]", "<img src=\"img/express_liuhan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[流泪]", "<img src=\"img/express_liulei.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[流血]", "<img src=\"img/express_liuxie.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[卖身]", "<img src=\"img/express_maisheng.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[牛逼]", "<img src=\"img/express_niubi.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[傻逼]", "<img src=\"img/express_sb.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[色]", "<img src=\"img/express_se.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[生气]", "<img src=\"img/express_shengqi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[难过]", "<img src=\"img/express_shiwangnanguo.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[睡觉]", "<img src=\"img/express_shuijiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[叹气]", "<img src=\"img/express_tanqi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[吐舌头]", "<img src=\"img/express_tushetou.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[围观]", "<img src=\"img/express_weiguan.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[猥琐]", "<img src=\"img/express_weisuo.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[微笑]", "<img src=\"img/express_weixiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[为什么]", "<img src=\"img/express_why.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[思考]", "<img src=\"img/express_xiangyixia.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[发财了]", "<img src=\"img/express_zhuandale.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[心动]", "<img src=\"img/express_aixin.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[抄底]", "<img src=\"img/express_chaodi.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[成交]", "<img src=\"img/express_chengjiao.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[翔了]", "<img src=\"img/express_dabian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[赞一个]", "<img src=\"img/express_damuzhi_shang.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[弱爆了]", "<img src=\"img/express_damuzhi_xia.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[多]", "<img src=\"img/express_duo.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[护城河]", "<img src=\"img/express_huchenghe.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[加]", "<img src=\"img/express_jia.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[减]", "<img src=\"img/express_jian.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[空仓]", "<img src=\"img/express_kongcang.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[满仓]", "<img src=\"img/express_mancang.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[鲜花]", "<img src=\"img/express_meigui.png\"height=\"35\" width=\"35\">");
//		// strHtml = strHtml.replace("[美元]", "<img src=\"img/express_meiyuan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[空]", "<img src=\"img/express_null.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[心碎]", "<img src=\"img/express_xinsuile.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[复权]", "<img src=\"img/express_fupan.png\"height=\"35\" width=\"35\">");
//		strHtml = strHtml.replace("[干杯]", "<img src=\"img/express_ganbei.png\"height=\"35\" width=\"35\">");
//		return strHtml;
//	}
//
//	/**
//	 * 
//	 * @author :Atar
//	 * @createTime:2015-3-13下午3:13:14
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @return
//	 * @description:
//	 */
//	public static String clearExpresstion(String strContent) {
//		String expresstion_reg = "\\[E\\](.*?)\\[\\/E\\]";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strContent = ubbItem(strContent, expresstion_reg, "", pattern, matcher, true);
//		return strContent;
//	}
//
//	/**
//	 * 替换QQ表情
//	 */
//	public static String replaceEcpresstion(String strContent) {
//		Pattern pattern = null;
//		Matcher matcher = null;
//		// 表情
//		strContent = ubbItem(strContent, "\\[F\\](.*?)\\[\\/F\\]", " [表情 ]", pattern, matcher, true);
//		return strContent;
//	}
//
//	/**
//	 * 得到引用内容
//	 * @author :Atar
//	 * @createTime:2015-5-28下午2:04:26
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strContent
//	 * @return
//	 * @description:
//	 */
//	public static String replaceQuote(String strContent) {
//		strContent = strContent.replace("[quoe]", "");
//		strContent = strContent.replace("[/quoe]", "");
//		strContent = strContent.replace("[quote]", "");
//		strContent = strContent.replace("[/quote]", "");
//		strContent = strContent.replace(HtmlUtil.QUOTE_TOPIC_TIME_KONGGE, " ");
//		return strContent;
//	}
//
//	public static String replaceLab(String strContent) {
//		if (strContent == null || strContent.equals(""))
//			return "";
//		Pattern pattern = null; //
//		Matcher matcher = null;
//		strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(.+?)\\[\\/img\\]", " ", pattern, matcher, true);
//		strContent = ubbItem(strContent, "\\[img\\=(.+?)\\,(.+?)\\]\\s*[javascript]*(.+?)\\[\\/img\\]", " ", pattern, matcher, true);
//
//		/**
//		 * 个股吧（600000代码）
//		 */
//		strContent = ubbItem(strContent, "\\[gguba\\](.+?)\\[\\/gguba\\]", "$1", pattern, matcher, true);
//
//		/**
//		 * 个股吧（股票名称）
//		 */
//		strContent = ubbItem(strContent, "\\[gubar\\](.+?)\\[\\/gubar\\]", "$1", pattern, matcher, true);
//
//		strContent = ubbItem(strContent, "\\[atuser=(.+?)\\](.+?)\\[\\/atuser\\]", "@$2", pattern, matcher, true);
//		/**
//		 * 个股行情图片（与信龙合作）
//		 */
//		strContent = ubbItem(strContent, "\\[stock\\](.+?)\\[\\/stock\\]", "$1", pattern, matcher, true);
//		strContent = ubbItem(strContent, "\\[tag\\](.+?)\\[\\/tag\\]", "$1", pattern, matcher, true);
//
//		// 表格的替换
//		strContent = ubbItem(strContent, "(\\[table=(.[^\\[]*),(.*?)\\])(.*?)(\\[\\/table\\])", "$4", pattern, matcher, true);
//
//		strContent = ubbItem(strContent, "(\\[table=(.[^\\[]*)\\])(.*?)(\\[\\/table\\])", "$3", pattern, matcher, true);
//		strContent = ubbItem(strContent, "\\[mp\\](.*?)\\[\\/mp]", " ", pattern, matcher, true);
//
//		strContent = ubbItem(strContent, "\\[mp3\\](.*?)\\[\\/mp3]", " ", pattern, matcher, true);
//		// '表格UBB2
//		strContent = ubbItem(strContent, "(\\[td=([0-9]*),([0-9]*),(.*?)\\])(.*?)(\\[\\/td\\])", "$5", pattern, matcher, true);
//
//		strContent = ubbItem(strContent, "(\\[td=([0-9]*),([0-9]*)\\])(.*?)(\\[\\/td\\])",
//		/* "<td colspan=\"$2\" rowspan=\"$3\">$4</td>" */"$4", pattern, matcher, true);
//		strContent = ubbItem(strContent, "(\\[td\\])(.*?)(\\[\\/td\\])",
//		/* "<td>$2</td>" */"$2", pattern, matcher, true);
//		strContent = ubbItem(strContent, "(\\[tr\\])(.*?)(\\[\\/tr\\])",
//		/* "<tr>$2</tr>" */"$2", pattern, matcher, true);
//
//		strContent = ubbItem(strContent, "(\\[flash\\])(.*?)(\\[\\/flash\\])", " ", pattern, matcher, true);
//
//		strContent = strContent.replaceAll("\\[color=(.[^\\[]*)\\]", "");
//		strContent = strContent.replaceAll("\\[\\/color\\]", "");
//		// ALIGN
//		strContent = ubbItem(strContent, "(\\[align=(.[^\\[]*)\\])(.*?)(\\[\\/align\\])", "$3", pattern, matcher, true);
//		// ALIGN
//		strContent = ubbItem(strContent, "(\\[report=([0-9]*),([0-9]*)\\])(.*?)(\\[\\/report\\])", "", pattern, matcher, true);
//		// 字体格式
//		strContent = ubbItem(strContent, "(\\[i\\])(.*?)(\\[\\/i\\])",
//		/* "<i>$2</i>", */
//		"$2", pattern, matcher, true);
//		strContent = ubbItem(strContent, "(\\[u\\])(.*?)(\\[\\/u\\])",
//		/* "<u>$2</u>", */
//		"$2", pattern, matcher, true);
//		strContent = ubbItem(strContent, "(\\[b\\])(.*?)(\\[\\/b\\])",
//		/* "<b>$2</b>", */
//		"$2", pattern, matcher, true);
//		strContent = strContent.replaceAll("\\[size=(.[^\\[]*)\\]", "");
//		strContent = strContent.replaceAll("\\[\\/size\\]", "");
//
//		strContent = ubbItem(strContent, "(\\[list\\])(.+?)(\\[\\/list\\])", "$2", pattern, matcher, true);
//		strContent = ubbItem(strContent, "(\\[list=)(A|1)(\\])(.+?)(\\[\\/list\\])", "$4", pattern, matcher, true);
//		strContent = ubbItem(strContent, "(\\[center\\])(.*?)(\\[\\/center\\])", "$2", pattern, matcher, true);
//		strContent = ubbItem(strContent, "\\[font=(.*?)\\](.*?)(\\[\\/font\\])", "$2", pattern, matcher, true);
//		strContent = strContent.replaceAll("Æ", "");// 换行
//		strContent = removeHtml(strContent);
//		strContent = strContent.replace("[gubar]", "");
//		strContent = strContent.replace("[/gubar]", "");
//		strContent = strContent.replace("[tag]", "");
//		strContent = strContent.replace("[/tag]", "");
//		strContent = strContent.replace("[quoe]", "");
//		strContent = strContent.replace("[/b]", "");
//		strContent = strContent.replace("[b]", "");
//		strContent = strContent.replace("[/quoe]", "");
//		strContent = strContent.replace("[quote]", "");
//		strContent = strContent.replace("[/quote]", "");
//		strContent = strContent.replace("[/stock]", "");
//		strContent = strContent.replace("[stock]", "");
//		strContent = ubbItem(strContent, "(\\[img\\])(.*?)(\\[\\/img\\])", "[图片]", pattern, matcher, true);
//		strContent = strContent.replace("[img]", "");
//		strContent = strContent.replace("[/img]", "");
//		return strContent;
//	}
//
//	public static String removeHtml(String body) {
//		String regEx = "<.+?>"; // 表示标签
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(body);
//		String str = m.replaceAll("");
//		return str;
//	}
//
//	/**
//	 * 清除图片
//	 * @author :Atar
//	 * @createTime:2014-10-14下午8:50:36
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strHtml
//	 * @return
//	 * @description:
//	 */
//	public static String cleatImgList(String strHtml) {
//		String VOICE_REG = "(\\[img\\])(.*?)(\\[\\/img\\])";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strHtml = ubbItem(strHtml, VOICE_REG, "", pattern, matcher, true);
//		return strHtml;
//	}
//
//	// 说说内容过滤
//	public static String ShuoUBBCode(String strContent) {
//		if (strContent == null || strContent.equals("")) {
//
//		} else {
//			Pattern pattern = null;
//			Matcher matcher = null;
//			/**
//			 * @用户名 提醒
//			 */
//			strContent = AtUbbItem(strContent, "(\\@)(\\[a-zA-Z0-9\u4e00-\u9fa5\\]+)", "<font color=\"#5193C7\">$2</font>", pattern, matcher, true);
//			/**
//			 * 个股吧（600000代码）
//			 */
//			strContent = ubbItem(strContent, "\\[gguba\\](.+?)\\[\\/gguba\\]", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\[gubar\\](.+?)\\[\\/gubar\\]", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//
//			// 解析个股
//			strContent = ubbItem(strContent, "\\#(\\[^\\#|.\\]+)\\#", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//			// 解析行情
//			strContent = ubbItem(strContent, "\\[stock\\](\\d{6})\\[\\/stock\\]", " [$1]", pattern, matcher, true);
//
//			// 表情
//			strContent = ubbItem(strContent, "\\[F\\](.*?)\\[\\/F\\]", " [表情 ]", pattern, matcher, true);
//			// 转换站内图片(启用二级域名|)
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(../|http://www..com.cn/|http://www..net)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]",
//					"[图片]", // <img src=\"$1$2\" />
//					pattern, matcher, true);
//
//			// 转换站外图
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]", "[图片]", // <img src=\"$1$2\" />
//					pattern, matcher, true);
//		}
//		return strContent;
//	}
//
//	// 帖子内容过滤
//	public static String AppUBBCode(String strContent) {
//		if (strContent == null || strContent.equals("")) {
//
//		} else {
//			Pattern pattern = null;
//			Matcher matcher = null;
//			// COLOR[color=red]
//			strContent = ubbItem(strContent, "(\\[color=(.[^\\[]*)\\])(.*?)(\\[\\/color\\])", "<font color=$2>$3</font>", pattern, matcher, true);
//			// ALIGN
//			strContent = ubbItem(strContent, "(\\[align=(.[^\\[]*)\\])(.*?)(\\[\\/align\\])", "<div align=$2>$3</div>", pattern, matcher, true);
//
//			// 字体格式
//			strContent = ubbItem(strContent, "(\\[i\\])(.*?)(\\[\\/i\\])", "<i>$2</i>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "(\\[u\\])(.*?)(\\[\\/u\\])", "<u>$2</u>", pattern, matcher, true);
//			strContent = strContent.replace("[stock]sz[gguba]", "[stock]");
//			strContent = strContent.replace("[stock]sh[gguba]", "[stock]");
//			strContent = strContent.replace("[stock]sz[gubar]", "[stock]");
//			strContent = strContent.replace("[stock]sh[gubar]", "[stock]");
//			strContent = strContent.replace("[/gguba][/stock]", "[/stock]");
//			strContent = strContent.replace("[/gubar][/stock]", "[/stock]");
//			strContent = ubbItem(strContent, "\\[stock\\](\\d{6})\\[\\/stock\\]", " $1", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\[stock\\](.*?)\\[\\/stock\\]", "$1", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, // 加粗 ：text.getPaint().setFakeBoldText(true);
//					"(\\[b\\])(.*?)(\\[\\/b\\])", "$2", pattern, matcher, true);
//			strContent = ubbItem(strContent, "(\\[size=([0-9])\\])(.*?)(\\[\\/size\\])", "<font size=4>$3</font>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "(\\[center\\])(.*?)(\\[\\/center\\])", "<center>$2</center>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "\\[font=(.*?)\\](.*?)(\\[\\/font\\])", "<font style=\"font-family:$1\">$2</font>", pattern, matcher, true);
//
//			/**
//			 * @用户名 提醒
//			 */
//			strContent = ubbItem(strContent, "\\[atuser=([0-9]*)\\](.+?)\\[\\/atuser\\]", "<font color=\"#5193C7\">@$2</font>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "(\\@)(\\[a-zA-Z0-9\u4e00-\u9fa5\\]+)", "<font color=\"#5193C7\">$2</font>", pattern, matcher, true);
//			/**
//			 * 个股吧（600000代码）
//			 */
//			strContent = ubbItem(strContent, "\\[gguba\\](.+?)\\[\\/gguba\\]", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\[gubar\\](.+?)\\[\\/gubar\\]", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//
//			// 解析个股
//			strContent = ubbItem(strContent, "\\#(\\[^\\#|.\\]+)\\#", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//			// 解析行情
//			strContent = ubbItem(strContent, "\\[stock\\](\\d{6})\\[\\/stock\\]", " [$1]", pattern, matcher, true);
//
//			// 表情
//			strContent = ubbItem(strContent, "\\[F\\](.*?)\\[\\/F\\]", " [表情 ]", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(.[^\\[]*)(\\?|\\&|\\#)(.+?)\\[\\/img\\]", "<div align=center>不支持接动态链接<font color=\"red\">$1$2$3</font></div>", pattern,
//					matcher, true);
//
//			strContent = ubbItem(strContent, "\\[mp\\]\\s*[javascript]*(.[^\\[]*)(\\?|\\&|\\#)(.+?)\\[\\/mp\\]", "<div align=center>不支持接动态链接<font color=\"red\">$1$2$3</font></div>", pattern, matcher,
//					true);
//
//			strContent = ubbItem(strContent, "\\[mp3\\]\\s*[javascript]*(.[^\\[]*)(\\?|\\&|\\#)(.+?)\\[\\/mp3\\]", "<div align=center>不支持接动态链接<font color=\"red\">$1$2$3</font></div>", pattern,
//					matcher, true);
//			// //动态视频白名单
//			// strContent = ubbItem(strContent,
//			// "(\\[flash\\])(http:\\//(player.ku6.com|player.youku.com|www.tudou.com|v.ifeng.com|you.video.sina.com.cn|player.56.com){1}(.*?))(\\[\\/flash\\])",
//			// " 视频:<a style=\"color:#5193C7;text-decoration:underline;\" href=\"$2\" target=\"_blank\">$2</a>",
//			// pattern,matcher,true);
//
//			strContent = ubbItem(strContent, "\\[flash\\]\\s*[javascript]*(.[^\\]]*)(\\?|\\&|\\#)(.[^\\]]*)\\[\\/flash\\]", "<div align=center>不支持接动态链接<font color=\"red\">$1$2$3</font></div>",
//					pattern, matcher, true);
//
//			// FLASH
//			strContent = ubbItem(strContent, "(\\[flash\\])(.*?)(\\[\\/flash\\])", "[视频]", // 视频: <a href=\"$2\" ><font color=\"#5193C7\">$2</font></a>
//					pattern, matcher, true);
//			// REPORT
//			strContent = ubbItem(strContent, "(\\[report=([0-9]*),([0-9]*)\\])(.*?)(\\[\\/report\\])", " [报告 ] ", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "\\[mp\\](.*?)(.MP3|.mp3|.WMA|.wma|.WMV|.wmv|.AVI|.avi|.MPG|.mpg|.MPEG|.mpeg|.SWF|.swf|.WAV|.wav)\\[\\/mp]", "[音乐]", // 音乐: <a href=\"$1$2\" ><font
//																																									// color=\"#5193C7\">$1$2</font></a>
//					pattern, matcher, true);
//
//			// // 引用过滤(去掉引用内容)
//			// strContent = ubbItem(strContent, "(\\[quote\\])(.*?)(\\[\\/quote\\])", // .*\\[quote\\](.*?)\\[/quote\\].*
//			// "", pattern, matcher, true);
//
//			// 转换站内图片(启用二级域名|)
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(../|http://www..com.cn/|http://www..net)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]",
//					"[图片]", // <img src=\"$1$2\" />
//					pattern, matcher, true);
//
//			// 转换站外图
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]", "[图片]", // <img src=\"$1$2\" />
//					pattern, matcher, true);
//			// 视频
//			strContent = ubbItem(strContent, "\\[video\\](.*?)\\[\\/video\\]", "[视频]", pattern, matcher, true);
//			// 超链接
//			strContent = ubbItem(strContent, "\\[url\\](.*?)\\[\\/url\\]", "$1", pattern, matcher, true);
//			// 超链接_2
//			strContent = getLinkName(strContent, "\\[link\\](.*?)\\[\\/link\\]", "[链接]", pattern, matcher, true);
//			strContent = strContent.replaceAll("&quot;", "\"");
//			strContent = strContent.replaceAll("&#39;", "'");
//			strContent = strContent.replaceAll("&nbsp;", " ");
//			strContent = strContent.replaceAll("&gt;", ">");
//			strContent = strContent.replaceAll("&lt;", "<");
//			strContent = strContent.replaceAll("\r\n", "<br>");
//			strContent = strContent.replaceAll("\r", "<br>");
//			strContent = strContent.replaceAll("\n", "<br>");
//		}
//		return strContent;
//	}
//
//	// 获得引用的内容
//	public static String[] getQuoteString(String strContent) {
//		String[] quote = null;
//		String quoteReg = ".*\\[quote\\](.*?)\\[/quote\\].*";
//		// 提取引用内容
//		String getQuote = "";
//		Pattern patternQuote = Pattern.compile(quoteReg);
//		Matcher matcherQuote = patternQuote.matcher(strContent);
//		while (matcherQuote.matches()) {
//			getQuote = matcherQuote.group(1);
//			break;
//		}
//		if (!"".equals(getQuote)) {
//			String timeReg = ".*(([1-9]{1}[0-9]{3})-{1}(0?[1-9]{1}|1?[0-2]{1})-{1}(0?[1-9]|[12][0-9]|3[01])\\s?(0?[0-9]{1}|1?[0-9]{1}|2?[0-4]{1})\\:([0-5]{1}[0-9]{1}|60)){1}.*";
//			String time = "";
//			// 提取时间
//			Pattern pattern = Pattern.compile(timeReg);
//			Matcher matcher = pattern.matcher(getQuote);
//			while (matcher.matches()) {
//				time = matcher.group(1);
//				break;
//			}
//			if (!"".equals(time)) {
//				quote = new String[3];
//				quote[0] = getQuote.substring(3, getQuote.indexOf(time) - 1); // 姓名
//				quote[1] = time; // 时间
//				quote[2] = getQuote.substring(getQuote.indexOf(time) + time.length() + 2);// 内容
//			}
//		}
//		return quote;
//	}
//
//	// 获得内容中的图片链接
//	public static String AppUbbGetImg(String strContent) {
//		String getImg = "";
//		String imgReg = ".*\\[img\\]\\s*[javascript]*(../img/|http://www..com.cn/img/|http://www..net/img/)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\].*";
//		Pattern patternQuote = Pattern.compile(imgReg);
//		Matcher matcherQuote = patternQuote.matcher(strContent);
//		while (matcherQuote.matches()) {
//			getImg = matcherQuote.group(2) + matcherQuote.group(3);
//			break;
//		}
//		return getImg;
//	}
//
//	// 获得内容中的图片链接
//	public static String AppUbbGetShuoImg(String strContent) {
//		String getImg = "";
//		String imgReg = ".*\\[img\\]\\s*[javascript]*(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\].*";
//		Pattern patternQuote = Pattern.compile(imgReg);
//		Matcher matcherQuote = patternQuote.matcher(strContent);
//		while (matcherQuote.matches()) {
//			getImg = matcherQuote.group(1) + matcherQuote.group(2);
//			getImg = getImg.replaceAll("../img/", "");
//			break;
//		}
//		return getImg;
//	}
//
//	/**
//	 * 过滤新闻和公告的字符
//	 */
//	// 帖子内容过滤
//	public static String AppUBBCodeNews(String strContent) {
//		if (strContent == null || strContent.equals("")) {
//
//		} else {
//			Pattern pattern = null;
//			Matcher matcher = null;
//			// COLOR[color=red]
//			strContent = ubbItem(strContent, "(\\[color=(.[^\\[]*)\\])(.*?)(\\[\\/color\\])", "<font color=$2>$3</font>", pattern, matcher, true);
//			// ALIGN
//			strContent = ubbItem(strContent, "(\\[align=(.[^\\[]*)\\])(.*?)(\\[\\/align\\])", "<div align=$2>$3</div>", pattern, matcher, true);
//
//			// 字体格式
//			strContent = ubbItem(strContent, "(\\[i\\])(.*?)(\\[\\/i\\])", "<i>$2</i>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "(\\[u\\])(.*?)(\\[\\/u\\])", "<u>$2</u>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, // 加粗 ：text.getPaint().setFakeBoldText(true);
//					"(\\[b\\])(.*?)(\\[\\/b\\])", "$2", pattern, matcher, true);
//			strContent = ubbItem(strContent, "(\\[size=([0-9])\\])(.*?)(\\[\\/size\\])", "<font size=4>$3</font>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "(\\[center\\])(.*?)(\\[\\/center\\])", "<center>$2</center>", pattern, matcher, true);
//
//			strContent = ubbItem(strContent, "\\[font=(.*?)\\](.*?)(\\[\\/font\\])", "<font style=\"font-family:$1\">$2</font>", pattern, matcher, true);
//
//			strContent = strContent.replaceAll("&quot;", "\"");
//			strContent = strContent.replaceAll("&#39;", "'");
//			strContent = strContent.replaceAll("&nbsp;", " ");
//
//		}
//		return strContent;
//	}
//
//	// 说说内容过滤
//	public static String newUBBCode(String strContent) {
//		if (strContent == null || strContent.equals("")) {
//
//		} else {
//			Pattern pattern = null;
//			Matcher matcher = null;
//			/**
//			 * @用户名 提醒
//			 */
//			strContent = AtUbbItem(strContent, "(\\@)(\\[a-zA-Z0-9\u4e00-\u9fa5\\]+)", "<font color=\"#5193C7\">$2</font>", pattern, matcher, true);
//			/**
//			 * 个股吧（600000代码）
//			 */
//			strContent = strContent.replace("[stock]sz[gguba]", "[stock]");
//			strContent = strContent.replace("[stock]sh[gguba]", "[stock]");
//			strContent = strContent.replace("[stock]sz[gubar]", "[stock]");
//			strContent = strContent.replace("[stock]sh[gubar]", "[stock]");
//			strContent = strContent.replace("[/gguba][/stock]", "[/stock]");
//			strContent = strContent.replace("[/gubar][/stock]", "[/stock]");
//			strContent = ubbItem(strContent, "\\[gguba\\](.+?)\\[\\/gguba\\]", "$1", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\[gubar\\](.+?)\\[\\/gubar\\]", "$1", pattern, matcher, true);
//
//			// 解析个股
//			strContent = ubbItem(strContent, "\\#(\\[^\\#|.\\]+)\\#", " <font color=\"#5193C7\">$1</font>", pattern, matcher, true);
//			// 解析行情
//			strContent = getStockCode(strContent, "\\[stock\\](.*?)\\[\\/stock\\]", "$1", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\[stock\\](\\d{6})\\[\\/stock\\]", " $1", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\[stock\\](.*?)\\[\\/stock\\]", "$1", pattern, matcher, true);
//
//			// 表情
//			strContent = ubbItem(strContent, "\\[F\\](.*?)\\[\\/F\\]", " <img src=\"" + UrlParamCommon.IMAGE_HOST + "/images/$1\"/>", pattern, matcher, true);
//			// 转换站内图片(启用二级域名|)
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(http://www..com.cn/|http://www..net)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]",
//					"<img src=\"/$2$3\" />", // <img src=\"$1$2\" />
//					pattern, matcher, true);
//			// 转换站内图片(启用二级域名|)
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(../|/)(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]", "<img src=\"" + UrlParamCommon.IMAGE_HOST
//					+ "/$2$3\" />", pattern, matcher, true);
//			// 转换站外图
//			strContent = ubbItem(strContent, "\\[img\\]\\s*[javascript]*(.+?)(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG|.BMP|.bmp)\\[\\/img\\]", "<img src=\"/$1$2\" />", pattern, matcher, true);
//			strContent = ubbItem(strContent, "\\<span style=\\\"display:none;\\\"\\>\\[\\]\\<\\/span\\>", // \\Æ
//					"", pattern, matcher, true);
//			// 视频
//			strContent = ubbItem(strContent, "\\[video\\](.*?)\\[\\/video\\]", "[视频]", pattern, matcher, true);
//			// 超链接
//			strContent = ubbItem(strContent, "\\[url\\](.*?)\\[\\/url\\]", "$1", pattern, matcher, true);
//			// 超链接_2
//			strContent = getLinkName(strContent, "\\[link\\](.*?)\\[\\/link\\]", "", pattern, matcher, true);
//			// 懒加载的图片
//			strContent = ubbItem(strContent, "\\<img.*?loadImg\\(this,\"(.*?)\"\\).*?class=\"lazy\".*?data-original=\"(.*)\"(\\/>|>)", "<img src=\"$2\" />", pattern, matcher, true);
//
//			strContent = strContent.replaceAll("Æ", "");
//			strContent = strContent.replaceAll("\r\n", "<br>"); // 2015.01.15
//			strContent = strContent.replaceAll("\r", "<br>");
//			strContent = strContent.replaceAll("\n", "<br>");
//			// strContent = getHtml(strContent);// 10.01更改
//		}
//		return strContent;
//	}
//
//	/**
//	  * 清除空格换行等
//	 * @author :Atar
//	 * @createTime:2016-11-17上午9:44:45
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strContent
//	 * @param isUnClearQuote 是否不清除引用标签
//	 * @return
//	 * @description:
//	 */
//	public static String getTC(String strContent, boolean isUnClearQuote) {
//		if (strContent != null && strContent.length() > 0) {
//			strContent = strContent.replaceAll("\r\n\t", "");
//			strContent = strContent.replaceAll("\r\n", "");
//			strContent = strContent.replaceAll("&nbsp;", "");
//			strContent = strContent.replaceAll("  ", "");
//			if (!isUnClearQuote) {
//				strContent = strContent.replaceAll("\\[quote\\]", "");
//				strContent = strContent.replaceAll("\\[/quote\\]", "");
//			}
//			strContent = strContent.replaceAll("\r", "");
//			strContent = strContent.replaceAll("\n", "");
//			strContent = strContent.replaceAll("\\[图片\\]", "");
//			strContent = strContent.replaceAll("\\［图片\\]", "");
//			strContent = strContent.replaceAll("\\［图片\\］", "");
//			strContent = strContent.replaceAll("\\[图片\\]", "");
//			strContent = strContent.replaceAll("\\［语音\\]", "");
//			strContent = strContent.replaceAll("\\[语音\\]", "");
//			strContent = strContent.replaceAll("\\［语音\\］", "");
//			strContent = strContent.replaceAll("\\［color=Purple\\］", "");
//			strContent = strContent.replaceAll("\\［/color\\］", "");
//			strContent = strContent.replaceAll("\\［color=purple\\］", "");
//			strContent = strContent.replaceAll("\\［color=Red\\］", "");
//			strContent = strContent.replaceAll("\\［color=red\\］", "");
//			strContent = strContent.replaceAll("\\［color=blue\\］", "");
//			strContent = strContent.replaceAll("\\［color=Blue\\］", "");
//			strContent = strContent.replaceAll("\\［color=Green\\］", "");
//			strContent = strContent.replaceAll("\\［color=green\\］", "");
//
//			strContent = strContent.replaceAll("\\[color=Purple\\]", "");// [color=Purple]
//			strContent = strContent.replaceAll("\\[/color\\]", "");
//			strContent = strContent.replaceAll("\\[color=purple\\]", "");
//			strContent = strContent.replaceAll("\\[color=Red\\]", "");
//			strContent = strContent.replaceAll("\\[color=red\\]", "");
//			strContent = strContent.replaceAll("\\[color=blue\\]", "");
//			strContent = strContent.replaceAll("\\[color=Blue\\]", "");
//			strContent = strContent.replaceAll("\\[color=Green\\]", "");
//			strContent = strContent.replaceAll("\\[color=green\\]", "");
//		}
//		return strContent;
//	}
//
//	public static String ToDBC(String input) {
//		char[] c = input.toCharArray();
//		for (int i = 0; i < c.length; i++) {
//			if (c[i] == 12288) {
//				c[i] = (char) 32;
//				continue;
//			}
//			if (c[i] > 65280 && c[i] < 65375)
//				c[i] = (char) (c[i] - 65248);
//		}
//		return new String(c);
//	}
//
//	private static String getStockCode(String strContent, String re, String replayStr, Pattern pattern, Matcher matcher, boolean IgnoreCase) {
//		if (IgnoreCase) {
//			pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
//		} else {
//			pattern = Pattern.compile(re);
//		}
//		matcher = pattern.matcher(strContent);
//		StringBuffer sb = new StringBuffer();
//		while (matcher.find()) {
//			String getCode = matcher.group(1);
//			// String subStr = getCode.substring(0,2);
//			// if(subStr.equals("60") || subStr.equals("51") || subStr.equals("52") || subStr.equals("90")){
//			// getCode = "sh"+getCode;
//			// }else{
//			// getCode = "sz"+getCode;
//			// }
//			matcher.appendReplacement(sb, "<img src='http://image.sinajs.cn/newchart/daily/n/" + getCode + ".gif'/>");
//		}
//		matcher.appendTail(sb);
//		return sb.toString();
//	}
//
//	private static String getLinkName(String strContent, String re, String replayStr, Pattern pattern, Matcher matcher, boolean IgnoreCase) {
//		if (IgnoreCase) {
//			pattern = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
//		} else {
//			pattern = Pattern.compile(re);
//		}
//		matcher = pattern.matcher(strContent);
//		StringBuffer sb = new StringBuffer();
//		while (matcher.find()) {
//			String getStr = matcher.group(1);
//			if (getStr != null && !"".equals(getStr)) {
//				String[] str = getStr.split("\\|");
//				if (str.length > 1) {
//					matcher.appendReplacement(sb, "<a href=\"" + str[0] + "\">" + str[1] + "</a>");
//				}
//			}
//		}
//		matcher.appendTail(sb);
//		return sb.toString();
//	}
//
//	public static Boolean isContent(String str) {
//		Boolean result = false;
//		Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$");
//		Matcher matcher = pattern.matcher(str);
//		if (matcher.matches()) {
//			result = true;
//		}
//		return result;
//	}
//
//	public static Boolean isChina(String str) {
//		Boolean result = false;
//		Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
//		Matcher matcher = pattern.matcher(str);
//		if (matcher.matches()) {
//			result = true;
//		}
//		return result;
//	}
//
//	public static String getTopicQuote(String strContent, TextView topicView) {
//		String quote = "";
//		StringBuffer noQuoteContent = new StringBuffer();
//		try {
//			String quoteReg = ".*\\[quote\\](.*?)\\[/quote\\].*";
//			// 提取引用内容
//			Pattern patternQuote = Pattern.compile(quoteReg);
//			Matcher matcherQuote = patternQuote.matcher(strContent);
//			while (matcherQuote.find()) {
//				quote = matcherQuote.group(1);
//				noQuoteContent.append(strContent.replace(quote, ""));
//				// String reg = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"; 标准时间格式
//				quote = formateTopicQuote(topicView.getContext(), quote);
//				topicView.setText(Html.fromHtml(quote));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if ("".equals(quote)) {
//			topicView.setVisibility(View.GONE);
//		} else {
//			topicView.setVisibility(View.VISIBLE);
//		}
//		if ("".equals(quote)) {
//			return strContent;
//		}
//		return clearTopicQuote(noQuoteContent.toString());
//	}
//
//	/**
//	 * 改变帖子里面引用样式
//	 * @author :Atar
//	 * @createTime:2016-10-10上午10:17:06
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param strContent
//	 * @return
//	 * @description:
//	 */
//	public static String formateTopicQuote(Context context, String strContent) {
//		try {
//			String reg = "原[帖贴]由(.{1,12})[在]?\\d{0,4}[-]?\\d{2}-\\d{2}.*\\d{2}:\\d{2}[:]?\\d{0,2}[发][表]?";
//			// String reg = "[在]?\\d{0,4}[-]?\\d{2}-\\d{2}[\\s]?\\d{2}:\\d{2}[:]?\\d{0,2}[发][表]?";
//			Pattern patternQuote1 = Pattern.compile(reg);
//			Matcher matcherQuote1 = patternQuote1.matcher(strContent);
//			if (matcherQuote1.find()) {
//				String bluecolor = context.getResources().getStringArray(R.array.common_top_title_bar_bg_string)[AppConfigSetting.getInstance().getInt(SkinMode.SKIN_MODE_KEY, 0)];
//				String strMatcher = matcherQuote1.group(0);
//				strMatcher = strMatcher.replace("原帖由", "引用<font color=\"" + bluecolor + "\">");
//				strMatcher = strMatcher.replace("原贴由", "引用<font color=\"" + bluecolor + "\">");
//				strMatcher = strMatcher.replaceAll("[在]?\\d{0,4}[-]?\\d{2}-\\d{2}.*\\d{2}:\\d{2}[:]?\\d{0,2}[发][表]?", "</font>发表的：<br/>");
//				strContent = strContent.replace(matcherQuote1.group(0), strMatcher);
//				strContent = strContent.replace("</font>发表的：<br/><br/>", "</font>发表的：<br/>");
//				strContent = strContent.replace("</font>发表的：<br/><br />", "</font>发表的：<br/>");
//				strContent = strContent.replace("</font>发表的：<br /><br />", "</font>发表的：<br/>");
//				strContent = strContent.replace("</font>发表的：<br /><br/>", "</font>发表的：<br/>");
//			}
//		} catch (Exception e) {
//		}
//		return strContent;
//	}
//
//	/**
//	 * 匹配文字中正负百分比数字并加颜色
//	 * @author :Atar
//	 * @createTime:2016-9-20下午5:45:17
//	 * @version:1.0.0
//	 * @modifyTime:
//	 * @modifyAuthor:
//	 * @param str
//	 * @return
//	 * @description:如 333.08%  -243.98%
//	 */
//	public static String percent(String str) {
//		Pattern pattern = Pattern.compile("[-+]?\\d+(\\.\\d+)?%");
//		Matcher matcher = pattern.matcher(str);
//		StringBuffer sbr = new StringBuffer();
//		while (matcher.find()) {
//			String op = matcher.group();
//			if (op != null && op.length() > 0) {
//				if (Double.valueOf(op.replace("%", "")) > 0) {
//					matcher.appendReplacement(sbr, "<font color=\"#ff3e1e\">" + op + "</font>");
//				} else if (Double.valueOf(op.replace("%", "")) < 0) {
//					matcher.appendReplacement(sbr, "<font color=\"#34a95e\">" + op + "</font>");
//				}
//			}
//		}
//		matcher.appendTail(sbr);
//		return ToDBC(sbr.toString());
//	}
//
//	public static String clearTopicQuote(String strHtml) {
//		String VOICE_REG = "(\\[quote\\])(.*?)(\\[/quote\\])";
//		Pattern pattern = null;
//		Matcher matcher = null;
//		strHtml = ubbItem(strHtml, VOICE_REG, "", pattern, matcher, true);
//		return strHtml;
//	}
// }
