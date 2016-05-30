package com.xiao.smartbj.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * Created by hasee on 2016/5/26.
 */
public  abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }
    //初始化布局，必须子类实现
    public abstract View initView();
    //初始化数据
    public void initData(){

    }
}
