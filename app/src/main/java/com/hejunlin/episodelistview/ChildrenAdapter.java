package com.hejunlin.episodelistview;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.MyViewHolder> {

    private static final int EPISODES_COLUMN_COUNT = 10;
    private static final int LONG_FOCUS_TIME = 2000;

    OnItemClickListener mClickListener;
    OnItemLongFocusListener mLongFocusListener;
    OnItemFocusListener mFocusListener;

    private List<String> mData;
    private List<Integer> mSelectedPositions;
    private int parentWidth, itemWidth;
    private int mCurrentPosition;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public ChildrenAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        parentWidth = parent.getMeasuredWidth();
        itemWidth = (parentWidth -
                (holder.textView.getPaddingLeft() + holder.textView.getPaddingRight()) * (EPISODES_COLUMN_COUNT))
                / EPISODES_COLUMN_COUNT + 1;
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (position < mData.size()) {
            holder.textView.setText(mData.get(position));
            holder.textView.setWidth(itemWidth);
            holder.textView.setFocusable(true);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setSelected(false);

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onEpisodesItemClick(v, position);
                }
            });

            final LongFocusRunnable longFocusRunnable = new LongFocusRunnable(holder.textView, position);
            holder.textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    if (hasFocus) {
                        mFocusListener.onEpisodesItemFocus(v, position, hasFocus);
                        mCurrentPosition = position;
                        mHandler.postDelayed(longFocusRunnable, LONG_FOCUS_TIME);
                    } else {
                        mHandler.removeCallbacks(longFocusRunnable);
                        mLongFocusListener.onEpisodesItemLongFocus(v,position,hasFocus);
                    }
                }
            });

            if (mSelectedPositions != null && mSelectedPositions.contains(position)) {
                holder.textView.setSelected(true);
            }

        } else {
            holder.textView.setText("");
            holder.textView.setWidth(itemWidth);
            holder.textView.setVisibility(View.INVISIBLE);
            holder.textView.setFocusable(false);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size() + EPISODES_COLUMN_COUNT;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public List<String> getData() {
        return mData;
    }

    public List<Integer> getSelectedPositions() {
        return mSelectedPositions;
    }

    public void setSelectedPositions(List<Integer> mPositions) {
        this.mSelectedPositions = mPositions;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.item);
            textView.setFocusable(true);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongFocusListener(OnItemLongFocusListener listener) {
        mLongFocusListener = listener;
    }

    public void setOnItemFocusListener(OnItemFocusListener listener) {
        mFocusListener = listener;
    }

    public interface OnItemClickListener {
        void onEpisodesItemClick(View view, int position);
    }

    public interface OnItemLongFocusListener {
        void onEpisodesItemLongFocus(View v, int position, boolean hasFocus);
    }

    public interface OnItemFocusListener {
        void onEpisodesItemFocus(View view, int position, boolean hasFocus);
    }

    class LongFocusRunnable implements Runnable {

        View v;
        int position;

        public LongFocusRunnable(View v, int position) {
            this.v = v;
            this.position = position;
        }

        @Override
        public void run() {
            mLongFocusListener.onEpisodesItemLongFocus(v, position,true);
        }
    }
}
