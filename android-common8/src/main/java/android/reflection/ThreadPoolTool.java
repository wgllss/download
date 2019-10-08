/**
 *
 */
package android.reflection;

import android.app.Activity;
import android.application.CommonApplication;
import android.common.CommonHandler;
import android.http.HttpRequest;
import android.interfaces.CommonNetWorkExceptionToast;
import android.interfaces.NetWorkCallListener;
import android.reflection.ExceptionEnum.GsonJsonParserException;
import android.reflection.ExceptionEnum.HttpIOException;
import android.reflection.ExceptionEnum.HttpProtocolException;
import android.reflection.ExceptionEnum.HttpRequestFalse400;
import android.reflection.ExceptionEnum.HttpRequestFalse403;
import android.reflection.ExceptionEnum.HttpRequestFalse404;
import android.reflection.ExceptionEnum.HttpRequestFalse405;
import android.reflection.ExceptionEnum.HttpRequestFalse500;
import android.reflection.ExceptionEnum.HttpRequestFalse502;
import android.reflection.ExceptionEnum.HttpRequestFalse503;
import android.reflection.ExceptionEnum.HttpRequestFalse504;
import android.reflection.ExceptionEnum.RefelectException;
import android.reflection.ExceptionEnum.ReflectionActivityFinished;
import android.reflection.ExceptionEnum.ReflectionClassNotFoundException;
import android.reflection.ExceptionEnum.ReflectionIllegalAccessException;
import android.reflection.ExceptionEnum.ReflectionIllegalArgumentException;
import android.reflection.ExceptionEnum.ReflectionNoSuchMethodErrorException;
import android.reflection.ExceptionEnum.ReflectionParamHasNullException;
import android.reflection.ExceptionEnum.ReflectionSecurityException;
import android.reflection.ExceptionEnum.ReflectionTimeOutException;
import android.reflection.ExceptionEnum.ReflectionUnknownHostException;
import android.reflection.ExceptionEnum.ReflectionUnknownServiceException;
import android.reflection.ExceptionEnum.ReflectionUnsupportedEncodingException;
import android.reflection.ExceptionEnum.XmlIOException;
import android.reflection.ExceptionEnum.XmlParserException;
import android.utils.ShowLog;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * *****************************************************************************************
 *
 * @className:多线程异步http请求类
 * @author: Atar
 * @createTime:2014-5-18下午11:34:10
 * @modifyTime:
 * @version: 1.0.0
 * @description:利用多线程进行异步操作网络请求，此类使用单利模式 *****************************************************************************************
 */
public class ThreadPoolTool {

    private static final String TAG = ThreadPoolTool.class.getSimpleName();
    private static ThreadPoolTool mInstance;// 异步请求单例模式
    private final CommonNetWorkExceptionToast mCommonNetWorkExceptionToast = new CommonNetWorkExceptionToast();

    public static ThreadPoolTool getInstance() {
        if (mInstance == null) {
            mInstance = new ThreadPoolTool();
        }
        return mInstance;
    }

    // 线程池
    private final ExecutorService exec = Executors.newCachedThreadPool();
    private Gson gson = new Gson();

    /**
     * 执行异步任务
     *
     * @param mRunnable
     * @author :Atar
     * @createTime:2015-9-22下午4:24:05
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void execute(Runnable mRunnable) {
        if (exec != null) {
            exec.execute(mRunnable);
        }
    }

    /**
     * 多线程多异步请求 带Toast提示
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param mNetWorkCallListener
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, NetWorkCallListener mNetWorkCallListener, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, -1, -1, ErrorMsgEnum.NetWorkMsgWhithToast, mNetWorkCallListener, null, className, methodName, params, typeOfT);
    }

    /**
     * 多线程多异步请求 带Toast提示 区分线程1
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param mNetWorkCallListener
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, int which1, NetWorkCallListener mNetWorkCallListener, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, -1, ErrorMsgEnum.NetWorkMsgWhithToast, mNetWorkCallListener, null, className, methodName, params, typeOfT);
    }

    /**
     * 多线程多异步请求 带Toast提示 区分线程1,线程2,
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param which2               同一请求Url 但请求中参数2不相同-------> 对应http返回时handler 中msg.arg2
     * @param mNetWorkCallListener
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, int which1, int which2, NetWorkCallListener mNetWorkCallListener, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, which2, ErrorMsgEnum.NetWorkMsgWhithToast, mNetWorkCallListener, null, className, methodName, params, typeOfT);
    }

    /**
     * 多线程多异步请求 带Toast提示  加入activity 生命周期
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param mNetWorkCallListener 回调对象
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, -1, -1, ErrorMsgEnum.NetWorkMsgWhithToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
    }

    /**
     * 多线程多异步请求 带Toast提示 区分线程1, 加入activity 生命周期
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param mNetWorkCallListener 回调对象
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, int which1, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, -1, ErrorMsgEnum.NetWorkMsgWhithToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
    }

    /**
     * 多线程多异步请求 带Toast提示 区分线程1,线程2, 加入activity 生命周期
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param which2               同一请求Url 但请求中参数2不相同-------> 对应http返回时handler 中msg.arg2
     * @param mNetWorkCallListener 回调对象
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, int which1, int which2, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, which2, ErrorMsgEnum.NetWorkMsgWhithToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
    }

    /**
     * 异步处理http请求，不显示toast异常提示
     *
     * @param what                 代表哪一个请求Url 对应http返回时handler -------> 中msg.what
     * @param mNetWorkCallListener
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-10-8上午11:06:22
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:如ListView 中多个item中有按钮请求，点击一个再点击一个，然后再点击一个。。。。list中的position 可用于 whichThread
     * 不显示toast异常提示应用场景，如一些后台循环请求，但此请求就是出问题，在当前界面又不需要给用户展示
     */
    public void setAsyncTaskWhitoutToast(int what, NetWorkCallListener mNetWorkCallListener, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, -1, -1, ErrorMsgEnum.NetWorkMsgWhithoutToast, mNetWorkCallListener, null, className, methodName, params, typeOfT);
    }

    /**
     * 异步处理http请求，不显示toast异常提示 区分线程1
     *
     * @param what                 代表哪一个请求Url 对应http返回时handler -------> 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param mNetWorkCallListener
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-10-8上午11:06:22
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:如ListView 中多个item中有按钮请求，点击一个再点击一个，然后再点击一个。。。。list中的position 可用于 whichThread
     * 不显示toast异常提示应用场景，如一些后台循环请求，但此请求就是出问题，在当前界面又不需要给用户展示
     */
    public void setAsyncTaskWhitoutToast(int what, int which1, NetWorkCallListener mNetWorkCallListener, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, -1, ErrorMsgEnum.NetWorkMsgWhithoutToast, mNetWorkCallListener, null, className, methodName, params, typeOfT);
    }

    /**
     * 异步处理http请求，不显示toast异常提示 区分线程1，线程2
     *
     * @param what                 代表哪一个请求Url 对应http返回时handler -------> 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param which2               同一请求Url 但请求中参数2不相同-------> 对应http返回时handler 中msg.arg2
     * @param mNetWorkCallListener
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-10-8上午11:06:22
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:如ListView 中多个item中有按钮请求，点击一个再点击一个，然后再点击一个。。。。list中的position 可用于 whichThread
     * 不显示toast异常提示应用场景，如一些后台循环请求，但此请求就是出问题，在当前界面又不需要给用户展示
     */
    public void setAsyncTaskWhitoutToast(int what, int which1, int which2, NetWorkCallListener mNetWorkCallListener, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, which2, ErrorMsgEnum.NetWorkMsgWhithoutToast, mNetWorkCallListener, null, className, methodName, params, typeOfT);
    }

    /**
     * 异步处理http请求，不显示toast异常提示  加入activity 生命周期
     *
     * @param what                 代表哪一个请求Url 对应http返回时handler -------> 中msg.what
     * @param mNetWorkCallListener
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-10-8上午11:06:22
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:如ListView 中多个item中有按钮请求，点击一个再点击一个，然后再点击一个。。。。list中的position 可用于 whichThread
     * 不显示toast异常提示应用场景，如一些后台循环请求，但此请求就是出问题，在当前界面又不需要给用户展示
     */
    public void setAsyncTaskWhitoutToast(int what, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, -1, -1, ErrorMsgEnum.NetWorkMsgWhithoutToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
    }

    /**
     * 异步处理http请求，不显示toast异常提示 区分线程1, 加入activity 生命周期
     *
     * @param what                 代表哪一个请求Url 对应http返回时handler -------> 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param mNetWorkCallListener
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-10-8上午11:06:22
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:如ListView 中多个item中有按钮请求，点击一个再点击一个，然后再点击一个。。。。list中的position 可用于 whichThread
     * 不显示toast异常提示应用场景，如一些后台循环请求，但此请求就是出问题，在当前界面又不需要给用户展示
     */
    public void setAsyncTaskWhitoutToast(int what, int which1, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params, Type typeOfT) {
        setAsyncTask(what, which1, -1, ErrorMsgEnum.NetWorkMsgWhithoutToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
    }

    /**
     * 异步处理http请求，不显示toast异常提示 区分线程1，线程2, 加入activity 生命周期
     *
     * @param what                 代表哪一个请求Url 对应http返回时handler -------> 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param which2               同一请求Url 但请求中参数2不相同-------> 对应http返回时handler 中msg.arg2
     * @param mNetWorkCallListener
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-10-8上午11:06:22
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:如ListView 中多个item中有按钮请求，点击一个再点击一个，然后再点击一个。。。。list中的position 可用于 whichThread
     * 不显示toast异常提示应用场景，如一些后台循环请求，但此请求就是出问题，在当前界面又不需要给用户展示
     */
    public void setAsyncTaskWhitoutToast(int what, int which1, int which2, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params,
                                         Type typeOfT) {
        setAsyncTask(what, which1, which2, ErrorMsgEnum.NetWorkMsgWhithoutToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
    }

    /**
     * 多线程多异步请求  总方法
     *
     * @param what                 代表哪一个请求Url------->对应http返回时handler 中msg.what
     * @param which1               同一请求Url 但请求中参数1不相同-------> 对应http返回时handler 中msg.arg1
     * @param which2               同一请求Url 但请求中参数2不相同-------> 对应http返回时handler 中msg.arg2
     * @param showToast            是否显示toast提示
     * @param mNetWorkCallListener 回调对象
     * @param activity             用于判断activity是否关闭情况下用到
     * @param className
     * @param methodName
     * @param params
     * @param typeOfT              Gson解析对象
     * @author :Atar
     * @createTime:2014-12-12上午10:44:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:应用场景，在同一界面，在网络不好情况下 同一请求地址 反回数据结构一样 请求参数1换了两个值请求 在没有返回情况下，用户又去操作，
     * 请求参数2换了两个值，那这4个请求 下次回来时 页面需要展示的数据为最后一次请求的数据，此时前几次请求回来的数据 不是用户想要的如果不区分导致数据错乱
     * 怎么区分时会用到
     */
    public void setAsyncTask(int what, int which1, int which2, int showToast, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] params,
                             Type typeOfT) {
        HttpImplementTask task = new HttpImplementTask(what, which1, which2, showToast, mNetWorkCallListener, activity, className, methodName, params, typeOfT);
        if (exec != null) {
            exec.execute(task);
        }
    }

    /**
     * HttpImplementTask:实现http异步加载Runnable
     *
     * @author: Atar
     * @createTime:2014-5-19上午12:34:55
     * @modifyTime:
     * @version: 1.0.0
     * @description:
     */
    class HttpImplementTask implements Runnable {
        private NetWorkCallListener mNetWorkCallListener;
        private String reflectClassName;
        private String ReflectMethodName;
        private Object[] Reflectargs;
        private int msgWhat = ErrorMsgEnum.ENotDefine_Msg;// 默认未知错误异常
        private NetWorkMsg msg;// = new NetWorkMsg(msgWhat, msg1, msg2, msg3, null);// 返回对像

        private Type typeOfT;// 返回解析Gson用到

        private boolean hasActivity;
        private SoftReference<Activity> mSoftReference;// 弱引用 用于判断activity是否关闭情况下用到

        public HttpImplementTask(int msg, int msg1, int msg2, int showToast, NetWorkCallListener mNetWorkCallListener, Activity activity, String className, String methodName, Object[] args,
                                 Type typeOfT) {
            this.msgWhat = msg;
            this.msg = new NetWorkMsg(msg, msg1, msg2, showToast, null);// 返回对像

            this.mNetWorkCallListener = mNetWorkCallListener;
            reflectClassName = className;
            ReflectMethodName = methodName;
            Reflectargs = args;
            this.typeOfT = typeOfT;
            if (activity != null) {
                hasActivity = true;
                mSoftReference = new SoftReference<Activity>(activity);
            }
        }

        public void run() {
            if (!HttpRequest.IsUsableNetWork(CommonApplication.getContext())) {
                msg.what = ErrorMsgEnum.EMobileNetUseless_Msg;
                msg.arg3 = ErrorMsgEnum.NetWorkMsgWhithToast;
                CommonHandler.getInstatnce().NetWorkCall(mNetWorkCallListener, msg);
                return;
            }
            try {
                Object objReutrn = Reflection.invokeStaticMethod(reflectClassName, ReflectMethodName, Reflectargs);
                if (hasActivity && mSoftReference != null && mSoftReference.get() != null && mSoftReference.get().isFinishing()) {
                    ShowLog.i(TAG, "---activity已经关闭---异步线程执行到此结束-------->");
                    return;
                }
                if (typeOfT != null && objReutrn instanceof String) {
                    ShowLog.i(TAG, Reflectargs[0] + "---->" + typeOfT.toString() + "------>" + objReutrn);
                    if (typeOfT != String.class) {
                        objReutrn = gson.fromJson((String) objReutrn, typeOfT);
                    }
                }
                msg.obj = objReutrn;
            } catch (RefelectException e) {
                // 22个异常捕获
                if (e != null) {
                    if (e instanceof HttpProtocolException) {
                        msgWhat = ErrorMsgEnum.EHttpProtocol_Msg;
                    } else if (e instanceof HttpIOException) {
                        msgWhat = ErrorMsgEnum.EHttpIO_Msg;
                    } else if (e instanceof ReflectionTimeOutException) {
                        msgWhat = ErrorMsgEnum.EConnectTimeout_Msg;
                    } else if (e instanceof ReflectionClassNotFoundException) {
                        msgWhat = ErrorMsgEnum.EClassNotFound_Msg;
                    } else if (e instanceof ReflectionParamHasNullException) {
                        msgWhat = ErrorMsgEnum.EParamHasNull_Msg;
                    } else if (e instanceof GsonJsonParserException) {
                        msgWhat = ErrorMsgEnum.EJsonParser_Msg;
                    } else if (e instanceof ReflectionSecurityException) {
                        msgWhat = ErrorMsgEnum.ESecurity_Msg;
                    } else if (e instanceof ReflectionIllegalAccessException) {
                        msgWhat = ErrorMsgEnum.EIllegalAccess_Msg;
                    } else if (e instanceof ReflectionNoSuchMethodErrorException) {
                        msgWhat = ErrorMsgEnum.ENotFoundMethods_Msg;
                    } else if (e instanceof ReflectionIllegalArgumentException) {
                        msgWhat = ErrorMsgEnum.EParamUnInvalid_Msg;
                    } else if (e instanceof XmlParserException) {
                        msgWhat = ErrorMsgEnum.EXmlParser_Msg;
                    } else if (e instanceof XmlIOException) {
                        msgWhat = ErrorMsgEnum.EXmlIO_Msg;
                    } else if (e instanceof HttpRequestFalse400) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail400;
                    } else if (e instanceof HttpRequestFalse403) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail403;
                    } else if (e instanceof HttpRequestFalse404) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail404;
                    } else if (e instanceof HttpRequestFalse405) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail405;
                    } else if (e instanceof HttpRequestFalse502) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail502;
                    } else if (e instanceof HttpRequestFalse503) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail503;
                    } else if (e instanceof HttpRequestFalse504) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail504;
                    } else if (e instanceof HttpRequestFalse500) {
                        msgWhat = ErrorMsgEnum.EHttpRequestFail500;
                    } else if (e instanceof ReflectionUnknownHostException) {
                        msgWhat = ErrorMsgEnum.EUnknownHost_msg;
                    } else if (e instanceof ReflectionUnknownServiceException) {
                        msgWhat = ErrorMsgEnum.EUnknownService_msg;
                    } else if (e instanceof ReflectionUnsupportedEncodingException) {
                        msgWhat = ErrorMsgEnum.EUnsupportedEncoding_msg;
                    } else if (e instanceof ReflectionActivityFinished) {
                        ShowLog.i(TAG, "---ReflectionActivityFinished------activity已经关闭---异步线程执行到此结束-------->");
                        return;
                    } else {
                        msgWhat = ErrorMsgEnum.ENotDefine_Msg;
                    }
                }
                msg.obj = e.getMessage();
            } catch (Exception e) {
                if (e instanceof JsonSyntaxException) {
                    msgWhat = ErrorMsgEnum.EJsonParser_Msg;
                    msg.obj = e.getMessage();
                } else {
                    msgWhat = ErrorMsgEnum.ENotDefine_Msg;
                    msg.obj = "ENotDefine_Msg";
                }
            } finally {

            }
            if (hasActivity && mSoftReference != null && mSoftReference.get() != null && mSoftReference.get().isFinishing()) {
                ShowLog.i(TAG, "---activity已经关闭---异步线程执行到此结束-------->");
                return;
            }
            msg.what = msgWhat;
            CommonHandler.getInstatnce().NetWorkCall(mNetWorkCallListener, msg);
        }
    }

    /**
     * 通用网络错误提示 供扩展所用
     *
     * @param errorMsgWhat
     * @author :Atar
     * @createTime:2017-3-16上午9:40:08
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void toastException(int errorMsgWhat) {
        final NetWorkMsg msg = new NetWorkMsg(errorMsgWhat, 0, 0, ErrorMsgEnum.NetWorkMsgWhithToast, null);
        if (CommonHandler.getInstatnce().getHandler() != null) {
            CommonHandler.getInstatnce().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mCommonNetWorkExceptionToast.NetWorkCall(msg, null);
                }
            });
        }
    }
}
