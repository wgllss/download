package android.reflection;

/**
 ******************************************************************************************
 * @className:错误消息枚举类
 * @author: Atar 
 * @createTime:2014-5-18下午11:46:39
 * @modifyTime:
 * @version: 1.0.0
 * @description:多个错误消息枚举
 ******************************************************************************************
 */
public class ErrorMsgEnum {
	// 错误消息
	public static final int ENotFoundMethods_Msg = 0x1001; // 没有找到反射方法
	public static final int EHttpProtocol_Msg = 0x1002; // HTTP协议错误
	public static final int EHttpIO_Msg = 0x1003; // HTTPSocket网络异常
	public static final int EXmlParser_Msg = 0x1004; // XML解析错误
	public static final int EXmlIO_Msg = 0x1005; // XML通讯错误
	public static final int EClassNotFound_Msg = 0x1006; // 没有找到反射类
	public static final int EIllegalAccess_Msg = 0x1007; // 反射调用时指针出错
	public static final int ESecurity_Msg = 0x1008; // 反射安全出错
	public static final int EMobileNetUseless_Msg = 0x1009; // 当前网络不可用
	public static final int EConnectTimeout_Msg = 0x1010; // 连接服务器超时
	public static final int EParamHasNull_Msg = 0x1011; // 反射方法中有传入为null的参数
	public static final int EParamUnInvalid_Msg = 0x1012; // 反射方法中传入参数不合法
	public static final int EJsonParser_Msg = 0x1013; // Json解析错误
	public static final int ENotDefine_Msg = 0x1014; // 未定义的错误
	public static final int NetWorkMsgWhithoutToast = -0x1015; // 不显示提示toast 默认值用负数 不显示
	public static final int NetWorkMsgWhithToast = -0x1016; // 显示提示toast 默认值用负数才显示
	public static final int NetWorkThreadMsg2 = -0x1017; // 用于多线程，代表哪一个线程 默认值用负数
	public static final int EHttpRequestFail400 = 0x1018; // 服务器异常400
	public static final int EHttpRequestFail403 = 0x1019; // ip被封了 服务器异常403
	public static final int EHttpRequestFail404 = 0x1020; // 服务器异常404
	public static final int EHttpRequestFail405 = 0x1021; // 服务器异常405
	public static final int EHttpRequestFail502 = 0x1022; // 服务器异常502
	public static final int EHttpRequestFail503 = 0x1023; // 服务器异常503
	public static final int EHttpRequestFail504 = 0x1024; // 服务器异常504
	public static final int EUnknownHost_msg = 0x1025; // 服务器异常找不到主机host
	public static final int EUnknownService_msg = 0x1026; // 服务端出错
	public static final int EUnsupportedEncoding_msg = 0x1027; // 通信编码错误
	public static final int EHttpRequestFail500 = 0x1028; // 服务器异常500
}
