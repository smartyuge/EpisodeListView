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
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
/**
 * Created by hejunlin on 2017/2/5.
 * blog: http://blog.csdn.net/hejjunlin
 */
public class SummaryPopupWindow {

    private static final int POPUP_WINDOW_OFFSET_Y = 60;
    private static final int POPUP_WINDOW_PADDING_LEFT_RIGHT = 8 * 2;
    private static final int POPUP_TRIANGLE_WIDTH_HEIGHT = 24;
    private static final String TAG = SummaryPopupWindow.class.getSimpleName();
    private static int mScreenWidth = 1080;

    private Context mContext;
    private View mAttachView;
    private int[] mLocation;
    private PopupWindow mPopupWindow;
    private TextView mSummaryContent;
    private ImageView mTriangleImg;
    private View mPopupView;
    private float mWidth;

    public SummaryPopupWindow(Context context, View attachView) {
        this.mContext = context;
        this.mAttachView = attachView;
        init(context);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenPoint = new Point();
        windowManager.getDefaultDisplay().getSize(screenPoint);
        mScreenWidth = screenPoint.x;
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopupView = inflater.inflate(R.layout.summary_popup, null);
        mSummaryContent = (TextView) mPopupView.findViewById(R.id.text);
        mTriangleImg = (ImageView) mPopupView.findViewById(R.id.indicate);
        mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(mPopupView);
        this.setLocation(new int[]{0, 0});
    }

    public SummaryPopupWindow setLocation(int[] mLocation) {
        this.mLocation = mLocation;
        return this;
    }

    public SummaryPopupWindow setLocationByTargetView(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);

        location[0] -= (mWidth / 2 + POPUP_WINDOW_PADDING_LEFT_RIGHT - v.getWidth() / 2 - 63);
        location[1] -= POPUP_WINDOW_OFFSET_Y;
        Log.d(TAG , ">> mWidth=" + mWidth + ", v.getWidth()=" + v.getWidth());
        Log.d(TAG , ">> x=" + location[0] + ", y=" + location[1]);
        this.mLocation = location;
        return this;
    }

    public SummaryPopupWindow setText(String text) {
        mSummaryContent.setText(text);
        mWidth = mSummaryContent.getPaint().measureText(text);
        return this;
    }

    public SummaryPopupWindow show() {
        if (mPopupWindow != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, POPUP_TRIANGLE_WIDTH_HEIGHT);
            if (mLocation[0] < 0) {
                params.setMargins(
                        (int) ((mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT - POPUP_TRIANGLE_WIDTH_HEIGHT) / 2 + mLocation[0])
                        , 0, 0, 0);
            } else if (mLocation[0] + mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT > mScreenWidth) {
                params.setMargins(
                        (int) ((mLocation[0] + mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT - mScreenWidth)
                                + (mWidth + POPUP_WINDOW_PADDING_LEFT_RIGHT - POPUP_TRIANGLE_WIDTH_HEIGHT) / 2)
                        , 0, 0, 0);
            } else {
                params.gravity = Gravity.CENTER;
            }
            mTriangleImg.setLayoutParams(params);
            mPopupWindow.showAtLocation(mAttachView, Gravity.NO_GRAVITY, mLocation[0], mLocation[1]);
        }
        return this;
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
}
