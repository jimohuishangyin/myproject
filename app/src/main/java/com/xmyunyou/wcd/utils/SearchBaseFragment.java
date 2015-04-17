package com.xmyunyou.wcd.utils;

import android.support.v4.app.Fragment;

/**
 * Created by 95 on 2015/2/9.
 */
public abstract class SearchBaseFragment extends Fragment{

    public abstract void loadMoreData();

    public abstract void pullData(boolean load);

}
