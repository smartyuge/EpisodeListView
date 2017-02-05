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

import java.util.List;
/**
 * Created by hejunlin on 2017/2/5.
 * blog: http://blog.csdn.net/hejjunlin
 */
public abstract class EpisodeListViewAdapter<T>{

    private ChildrenAdapter mChildrenAdapter;
    private ParentAdapter mParentAdapter;

    public EpisodeListViewAdapter() {
        mChildrenAdapter = new ChildrenAdapter(getChildrenList());
        mParentAdapter = new ParentAdapter(getParentList());
    }

    public ChildrenAdapter getEpisodesAdapter() {
        return mChildrenAdapter;
    }

    public ParentAdapter getGroupAdapter() {
        return mParentAdapter;
    }

    public void setSelectedPositions(List<Integer> positions) {
        mChildrenAdapter.setSelectedPositions(positions);
    }

    public abstract List<String> getChildrenList();

    public abstract List<String> getParentList();

    public abstract int getChildrenPosition(int childPosition);

    public abstract int getParentPosition(int parentPosition);

}
