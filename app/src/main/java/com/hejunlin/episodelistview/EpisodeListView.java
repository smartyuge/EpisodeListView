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
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
/**
 * Created by hejunlin on 2017/2/5.
 * blog: http://blog.csdn.net/hejjunlin
 */
public class EpisodeListView extends RelativeLayout implements View.OnFocusChangeListener {

    public static final String TAG = EpisodeListView.class.getSimpleName();

    private Context mContext;
    private RelativeLayout mContentPanel;
    private RecyclerView mChildrenView;
    private RecyclerView mParentView;
    private LinearLayoutManager mEpisodesLayoutManager;
    private LinearLayoutManager mGroupLayoutManager;

    private EpisodeListViewAdapter mEpisodeListAdapter;
    private ChildrenAdapter mChildrenAdapter;
    private ParentAdapter mParentAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public EpisodeListView(Context context) {
        this(context, null);
    }

    public EpisodeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EpisodeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            mContext = context;
            init();
        }
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.episodelist_layout, this, true);

        mChildrenView = (RecyclerView) findViewById(R.id.episodes);
        mParentView = (RecyclerView) findViewById(R.id.groups);

        mEpisodesLayoutManager = new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false);
        mGroupLayoutManager = new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false);

        mChildrenView.setLayoutManager(mEpisodesLayoutManager);
        mParentView.setLayoutManager(mGroupLayoutManager);

        mChildrenView.setItemAnimator(new DefaultItemAnimator());
        mParentView.setItemAnimator(new DefaultItemAnimator());

        mChildrenView.setOnFocusChangeListener(this);
        mParentView.setOnFocusChangeListener(this);
        this.setOnFocusChangeListener(this);
    }


    public void setAdapter(final EpisodeListViewAdapter adapter) {
        mEpisodeListAdapter = adapter;
        mChildrenAdapter = adapter.getEpisodesAdapter();
        mParentAdapter = adapter.getGroupAdapter();
        mChildrenView.setAdapter(mChildrenAdapter);
        mParentView.setAdapter(mParentAdapter);

        mParentAdapter.setOnItemClickListener(new ParentAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClick(View view, int position) {
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getChildrenPosition(position), 0);
            }
        });

        mParentAdapter.setOnItemFocusListener(new ParentAdapter.OnItemFocusListener() {
            @Override
            public void onGroupItemFocus(View view, int position, boolean hasFocus) {
                int episodePosition = adapter.getChildrenPosition(position);
                mChildrenAdapter.setCurrentPosition(episodePosition);
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getChildrenPosition(position), 0);
            }
        });

        mChildrenAdapter.setOnItemFocusListener(new ChildrenAdapter.OnItemFocusListener() {
            @Override
            public void onEpisodesItemFocus(View view, int position, boolean hasFocus) {
                if (hasFocus) {
                    int groupPosition = adapter.getParentPosition(position);
                    mGroupLayoutManager.scrollToPositionWithOffset(groupPosition, 0);
                    mParentAdapter.setCurrentPosition(adapter.getParentPosition(groupPosition));
                }
            }
        });

        mChildrenAdapter.setOnItemClickListener(new ChildrenAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {

            }
        });

    }

    public void setLongFocusListener(ChildrenAdapter.OnItemLongFocusListener listener) {
        mChildrenAdapter.setOnItemLongFocusListener(listener);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (mParentView.hasFocus()) {
                        mChildrenView.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (mChildrenView.hasFocus()) {
                        mParentView.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mChildrenView.hasFocus()
                            && mChildrenAdapter.getCurrentPosition() >= mChildrenAdapter.getData().size() - 1) {
                        return true;
                    }
                    if (mParentView.hasFocus()
                            && mParentAdapter.getCurrentPosition() >= mParentAdapter.getDatas().size() - 1) {
                        return true;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus) {
            mChildrenView.requestFocus();
        } else if (v == mChildrenView && hasFocus) {
            View child = mChildrenView.getLayoutManager().findViewByPosition(mChildrenAdapter.getCurrentPosition());
            if (child != null) {
                child.requestFocus();
            } else {
                int lastPosition = mEpisodesLayoutManager.findLastVisibleItemPosition();
                child = mEpisodesLayoutManager.findViewByPosition(lastPosition);
                if (child != null)
                    child.requestFocus();
            }
        } else if (v == mParentView && hasFocus) {
            View child = mParentView.getLayoutManager().findViewByPosition(mParentAdapter.getCurrentPosition());
            if (child != null) {
                child.requestFocus();
            }
        }
    }
}
