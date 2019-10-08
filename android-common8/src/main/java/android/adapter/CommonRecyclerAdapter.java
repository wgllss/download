package android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> list;
    private Handler handler;
    private Context context;
    private int currentPostiotn = -1;
    private int condition = -1;
    private int skinType;

    public CommonRecyclerAdapter(List<?> list) {
        this.list = (List<T>) list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getCurrentPostiotn() {
        return currentPostiotn;
    }

    public void setCurrentPostiotn(int currentPostiotn) {
        this.currentPostiotn = currentPostiotn;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getSkinType() {
        return skinType;
    }

    public void setSkinType(int skinType) {
        this.skinType = skinType;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public ImageLoadingListener getAnimateFirstListener() {
        return animateFirstListener;
    }

    //
    // public void setAnimateFirstListener(ImageLoadingListener animateFirstListener) {
    // this.animateFirstListener = animateFirstListener;
    // }

    private ImageLoadingListener animateFirstListener = new CommonRecyclerAdapter.AnimateFirstDisplayListener();

    protected ImageLoadingListener getImageLoadingListener() {
        return animateFirstListener;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
