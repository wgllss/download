package android.interfaces;

import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class ScrollListListener extends RecyclerView.OnScrollListener {

    private ImageLoader imageLoader;

    public ScrollListListener(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (imageLoader != null) {
                    imageLoader.pause();
                }

                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                if (imageLoader != null) {
                    imageLoader.resume();
                }
                break;
        }
    }
}
