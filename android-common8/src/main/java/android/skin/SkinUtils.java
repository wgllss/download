/**
 *
 */
package android.skin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.utils.ShowLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ****************************************************************************************************************************************************************************
 * 设置 颜色 背景 图片
 *
 * @author :Atar
 * @createTime:2017-9-18上午11:38:59
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
@SuppressLint("Recycle")
public class SkinUtils {

    /**
     * 设置背景
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @param views
     * @author :Atar
     * @createTime:2017-9-18上午11:45:29
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setBackgroundColor(Context context, int resourcesName, int skinType, View... views) {
        if (context != null) {
            if (views != null && views.length > 0) {
                for (int i = 0; i < views.length; i++) {
                    try {
                        views[i].setBackgroundColor(getArrayColor(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置字体颜色
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @param textView
     * @author :Atar
     * @createTime:2017-9-18上午11:45:53
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setTextColor(Context context, int resourcesName, int skinType, TextView... textView) {
        if (context != null) {
            if (textView != null && textView.length > 0) {
                for (int i = 0; i < textView.length; i++) {
                    try {
                        textView[i].setTextColor(getArrayColor(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置背景
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @param views
     * @author :Atar
     * @createTime:2017-9-18上午11:45:29
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("deprecation")
    public static void setBackgroundDrawable(Context context, int resourcesName, int skinType, View... views) {
        if (context != null) {
            if (views != null && views.length > 0) {
                for (int i = 0; i < views.length; i++) {
                    try {
                        views[i].setBackgroundDrawable(getArrayDrawable(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置图片
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @author :Atar
     * @createTime:2017-9-18上午11:45:29
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setImageDrawable(Context context, int resourcesName, int skinType, ImageView... imageViews) {
        if (context != null) {
            if (imageViews != null && imageViews.length > 0) {
                for (int i = 0; i < imageViews.length; i++) {
                    try {
                        imageViews[i].setImageDrawable(getArrayDrawable(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置listView分隔线
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @param listView
     * @author :Atar
     * @createTime:2017-9-18上午11:47:01
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setDivider(Context context, int resourcesName, int skinType, ListView listView) {
        if (context != null && listView != null) {
            listView.setDivider(getArrayDrawable(context, resourcesName, skinType));
            listView.setDividerHeight(1);
        }
    }

    /**
     * 设置Array下文字
     *
     * @param context
     * @param resources
     * @param resourcesName
     * @param skinType
     * @param textView
     * @author :Atar
     * @createTime:2017-9-19下午4:18:28
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setText(Context context, Resources resources, int resourcesName, int skinType, TextView... textView) {
        if (context != null) {
            if (textView != null && textView.length > 0) {
                for (int i = 0; i < textView.length; i++) {
                    try {
                        textView[i].setText(getArrayString(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 得到Array下颜色值
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @return
     * @author :Atar
     * @createTime:2017-9-19下午2:53:24
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static int getArrayColor(Context context, int resourcesName, int skinType) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.obtainTypedArray(resources.getIdentifier(context.getResources().getString(resourcesName), "array", SkinResourcesManager.getInstance(context).getSkinPackName())).getColor(
                    skinType, 0);
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }

    /**
     * 得到Array下drawable
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @return
     * @author :Atar
     * @createTime:2017-9-19下午2:55:55
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static Drawable getArrayDrawable(Context context, int resourcesName, int skinType) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.obtainTypedArray(resources.getIdentifier(context.getResources().getString(resourcesName), "array", SkinResourcesManager.getInstance(context).getSkinPackName()))
                    .getDrawable(skinType);
        } catch (Exception e) {
            return new ColorDrawable(Color.TRANSPARENT);
        }
    }

    /**
     * 得到Array下String
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @return
     * @author :Atar
     * @createTime:2017-9-19下午4:14:54
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static String getArrayString(Context context, int resourcesName, int skinType) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.getStringArray(resources.getIdentifier(context.getResources().getString(resourcesName), "array", SkinResourcesManager.getInstance(context).getSkinPackName()))[skinType];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 得到颜色
     *
     * @param context
     * @param resourcesName
     * @return
     * @author :Atar
     * @createTime:2017-9-19下午4:26:01
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static int getColor(Context context, int resourcesName) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.getColor(resources.getIdentifier(context.getResources().getString(resourcesName), "color", SkinResourcesManager.getInstance(context).getSkinPackName()));
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getString(Context context, int resourcesName) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.getString(resources.getIdentifier(context.getResources().getString(resourcesName), "string", SkinResourcesManager.getInstance(context).getSkinPackName()));
        } catch (Exception e) {
            return "";
        }
    }

    public static String[] getStringArray(Context context, int resourcesName) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.getStringArray(resources.getIdentifier(context.getResources().getString(resourcesName), "array", SkinResourcesManager.getInstance(context).getSkinPackName()));
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int resourcesName) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.getDrawable(resources.getIdentifier(context.getResources().getString(resourcesName), "drawable", SkinResourcesManager.getInstance(context).getSkinPackName()));
        } catch (Exception e) {
            return new ColorDrawable(Color.TRANSPARENT);
        }
    }

    // public static int getStyleID(Context context, Resources resources, int resourcesName) {
    // try {
    // return resources.getIdentifier(context.getResources().getString(resourcesName), "style", SkinResourcesManager.getInstance(context).getSkinPackName());
    // } catch (Exception e) {
    // return 0;
    // }
    // }
    //
    // public static int getAnimID(Context context, Resources resources, int resourcesName) {
    // try {
    // return resources.getIdentifier(context.getResources().getString(resourcesName), "anim", SkinResourcesManager.getInstance(context).getSkinPackName());
    // } catch (Exception e) {
    // return 0;
    // }
    // }

    public static float getDimenID(Context context, int resourcesName) {
        try {
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            return resources.getDimension(resources.getIdentifier(context.getResources().getString(resourcesName), "dimen", SkinResourcesManager.getInstance(context).getSkinPackName()));
        } catch (Exception e) {
            return 0;
        }
    }

    //得到布局View
    public static View getLayout(Context context, int resourcesName, ViewGroup viewRoot, boolean attachToRoot) {
        try {
            if (context == null) {
                return null;
            }
            Resources resources = SkinResourcesManager.getInstance(context).getResources();
            int resID = resources.getIdentifier(context.getResources().getString(resourcesName), "layout", SkinResourcesManager.getInstance(context).getSkinPackName());
            XmlResourceParser mXmlResourceParser = resources.getLayout(resID);
            if (mXmlResourceParser != null) {
                View view = LayoutInflater.from(context).inflate(mXmlResourceParser, viewRoot, attachToRoot);
                return view;
            } else {
                int layoutID = context.getResources().getIdentifier(context.getResources().getString(resourcesName), "layout", context.getPackageName());
                View view = LayoutInflater.from(context).inflate(layoutID, viewRoot, attachToRoot);
                return view;
            }
        } catch (Exception e) {
            try {
                int layoutID = context.getResources().getIdentifier(context.getResources().getString(resourcesName), "layout", context.getPackageName());
                View view = LayoutInflater.from(context).inflate(layoutID, viewRoot, attachToRoot);
                return view;
            } catch (Exception ex) {

            }
        }
        return null;
    }

    /**
     * 设置text文字
     *
     * @param context
     * @param resourcesName
     * @param textView
     */
    public static void setText(Context context, int resourcesName, TextView... textView) {
        try {
            if (textView != null && context != null && textView.length > 0) {
                for (int i = 0; i < textView.length; i++) {
                    textView[i].setText(getString(context, resourcesName));
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 设置图片
     *
     * @param context
     * @param resourcesName
     * @param imageView
     */
    public static void setImageDrawable(Context context, int resourcesName, ImageView... imageView) {
        try {
            if (imageView != null && context != null && imageView.length > 0) {
                for (int i = 0; i < imageView.length; i++) {
                    imageView[i].setImageDrawable(getDrawable(context, resourcesName));
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 设置背景图片
     *
     * @param context
     * @param resourcesName
     * @param view
     */
    public static void setBackgroundDrawable(Context context, int resourcesName, View... view) {
        try {
            if (view != null && context != null && view.length > 0) {
                for (int i = 0; i < view.length; i++) {
                    view[i].setBackgroundDrawable(getDrawable(context, resourcesName));
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 设置EditText hint文字
     *
     * @param context
     * @param resourcesName
     * @param editTexts
     */
    public static void setHint(Context context, int resourcesName, EditText... editTexts) {
        try {
            if (editTexts != null && context != null && editTexts.length > 0) {
                for (int i = 0; i < editTexts.length; i++) {
                    editTexts[i].setHint(getString(context, resourcesName));
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 设置背景
     *
     * @param context
     * @param resourcesName
     * @param views
     * @author :Atar
     * @createTime:2017-9-18上午11:45:29
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setBackgroundColor(Context context, int resourcesName, View... views) {
        if (context != null) {
            if (views != null && views.length > 0) {
                for (int i = 0; i < views.length; i++) {
                    try {
                        views[i].setBackgroundColor(getColor(context, resourcesName));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置字体颜色
     *
     * @param context
     * @param resourcesName
     * @param textView
     * @author :Atar
     * @createTime:2017-9-18上午11:45:53
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void setTextColor(Context context, int resourcesName, TextView... textView) {
        if (context != null) {
            if (textView != null && textView.length > 0) {
                for (int i = 0; i < textView.length; i++) {
                    try {
                        textView[i].setTextColor(getColor(context, resourcesName));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置EditText hintColor
     *
     * @param context
     * @param resourcesName
     * @param editText
     */
    public static void setHintTextColor(Context context, int resourcesName, EditText... editText) {
        if (context != null) {
            if (editText != null && editText.length > 0) {
                for (int i = 0; i < editText.length; i++) {
                    try {
                        editText[i].setHintTextColor(getColor(context, resourcesName));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置EditText hintColor
     *
     * @param context
     * @param resourcesName
     * @param editText
     */
    public static void setHintTextColor(Context context, int resourcesName, int skinType, EditText... editText) {
        if (context != null) {
            if (editText != null && editText.length > 0) {
                for (int i = 0; i < editText.length; i++) {
                    try {
                        editText[i].setHintTextColor(getArrayColor(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * CompoundButton 设置ButtonDrawable
     *
     * @param context
     * @param resourcesName
     * @param skinType
     * @param compoundButton
     */
    public static void setButtonDrawable(Context context, int resourcesName, int skinType, CompoundButton... compoundButton) {
        if (context != null) {
            if (compoundButton != null && compoundButton.length > 0) {
                for (int i = 0; i < compoundButton.length; i++) {
                    try {
                        compoundButton[i].setButtonDrawable(getArrayDrawable(context, resourcesName, skinType));
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
