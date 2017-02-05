package com.hejunlin.episodelistview;

import java.util.List;

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
