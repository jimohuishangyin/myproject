package com.xmyunyou.wcd.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sanmee on 2014/12/10.
 */
public class FragmentViewPagerAdapter extends PagerAdapter {

    private List<Fragment> mList;
    private FragmentManager mFragmentManager;

    public FragmentViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> list){
        mFragmentManager = fragmentManager;
        mList = list;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position).getView());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = mList.get(position);

        if (!fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.add(fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }


        if(fragment.getView().getParent() == null){
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }
}
