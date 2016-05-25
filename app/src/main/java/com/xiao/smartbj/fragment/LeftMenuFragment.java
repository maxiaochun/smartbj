package com.xiao.smartbj.fragment;

import android.view.View;

import com.xiao.smartbj.R;


/**
 * Created by hasee on 2016/5/23.
 * 侧边栏fragment
 */

public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        return view;
    }

    @Override
    public void initData() {
    }
}
