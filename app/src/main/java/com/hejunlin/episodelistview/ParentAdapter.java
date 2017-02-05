/*
 * Copyright (C) 2016-2017 hejunlin <hejunlin2013@gmail.com>
 * Github:https://github.com/hejunlin2013/EpisodeListView
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hejunlin.episodelistview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
/**
 * Created by hejunlin on 2017/2/5.
 * blog: http://blog.csdn.net/hejjunlin
 */
public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.MyViewHolder> {

    private static final int GROUPS_COLUMN_COUNT = 10;
    private OnItemClickListener mItemClickListener;
    private OnItemFocusListener mItemFocusListener;
    private List<String> mDatas;

    private int parentWidth;
    private int itemWidth;
    private int mCurrentPosition;

    public ParentAdapter(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        parentWidth = parent.getMeasuredWidth();
        itemWidth = (parentWidth -
                (holder.textView.getPaddingLeft() + holder.textView.getPaddingRight()) * (GROUPS_COLUMN_COUNT))
                / GROUPS_COLUMN_COUNT + 1;
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView.setText(mDatas.get(position));
        holder.textView.setWidth(itemWidth);
        holder.textView.setFocusable(true);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onGroupItemClick(v, position);
            }
        });

        holder.textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mItemFocusListener.onGroupItemFocus(v, position, hasFocus);
                    mCurrentPosition = position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public List<String> getDatas() {
        return mDatas;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }

    public void setOnItemFocusListener(OnItemFocusListener listener) {
        mItemFocusListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.item);
        }
    }

    public interface OnItemClickListener {
        void onGroupItemClick(View view, int position);
    }

    public interface OnItemFocusListener {
        void onGroupItemFocus(View view, int position, boolean hasFocus);
    }
}
