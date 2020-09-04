package com.atar.download.adapter;

import android.adapter.CommonRecyclerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atar.download.bean.ExKeyBoardBean;
import com.atar.downloadapp.R;

import java.util.List;

/**
 * @author：atar
 * @date: 2020/9/4
 * @description:
 */
public class KeyAdapter extends CommonRecyclerAdapter<ExKeyBoardBean> {

    private OnKeyListener listener;

    public KeyAdapter(List list) {
        super(list);
    }

    public void setListener(OnKeyListener listener) {
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setContext(parent.getContext());
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_key_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (getList() != null && getList().size() > 0) {
            viewHolder.bind(getList().get(position), position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_key_name;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txt_key_name = itemView.findViewById(R.id.txt_key_name);
        }

        public void bind(final ExKeyBoardBean info, int position) {
            txt_key_name.setText(info.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onKey(info);
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onLongClickKey(info);
                    }
                    return false;
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onKey(info);
                    }
                }
            });

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (info != null && info.getType() == 1) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_OUTSIDE:
                                if (listener != null) {
                                    listener.onTouchUp(info);
                                }
                                break;
                            case MotionEvent.ACTION_DOWN:
                                if (listener != null) {
                                    listener.onTouchDown(info);
                                }
                                break;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public interface OnKeyListener {
        void onKey(ExKeyBoardBean info);

        void onLongClickKey(ExKeyBoardBean info);

        void onTouchDown(ExKeyBoardBean info);

        void onTouchUp(ExKeyBoardBean info);
    }
}
