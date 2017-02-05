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

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;
/**
 * Created by hejunlin on 2017/2/5.
 * blog: http://blog.csdn.net/hejjunlin
 */
public class MainActivity extends AppCompatActivity {

    private EpisodeListView mEpisodeListView;
    private LinearLayout mContentPanel;
    private SummaryPopupWindow mSummaryPopupWindow;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentPanel = (LinearLayout) findViewById(R.id.container);
        mEpisodeListView = (EpisodeListView) findViewById(R.id.episodelistview);

        final String[] episodes = {
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20",
        };

        final String[] groups = {"1-10", "11-20"};

        final Integer[] selectedPositions = {1, 2, 3, 4, 6};

        final EpisodeListViewAdapter<String> adapter = new EpisodeListViewAdapter<String>() {
            @Override
            public List<String> getChildrenList() {
                return Arrays.asList(episodes);
            }

            @Override
            public List<String> getParentList() {
                return Arrays.asList(groups);
            }

            @Override
            public int getChildrenPosition(int groupPosition) {
                return groupPosition * 10;
            }

            @Override
            public int getParentPosition(int episodesPosition) {
                return episodesPosition / 10;
            }
        };

        mEpisodeListView.setAdapter(adapter);
        mEpisodeListView.setLongFocusListener(new ChildrenAdapter.OnItemLongFocusListener() {
            @Override
            public void onEpisodesItemLongFocus(View v, int position, boolean hasFocus) {
                if (hasFocus) {
                    mSummaryPopupWindow = new SummaryPopupWindow(MainActivity.this, mContentPanel);
                    mSummaryPopupWindow.setText("敬安王遭诬陷,聘婷孤身引敌,楚北捷发动兵变")
                            .setLocationByTargetView(v).show();

                }else {
                    if (mSummaryPopupWindow != null) {
                        mSummaryPopupWindow.dismiss();
                    }
                }
            }
        });


        adapter.setSelectedPositions(Arrays.asList(selectedPositions));
        mEpisodeListView.setAdapter(adapter);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEpisodeListView.requestFocus();
            }
        }, 300);


    }
}