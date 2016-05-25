package com.xiao.smartbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiao.smartbj.R;


/**
 * Created by hasee on 2016/5/23.
 * 五个标签页的基类
 */
public class BasePager {


    public Activity mActivity;
    public TextView tv_title;
    public ImageButton btn_menu;
    public TextView tv_title1;
    public FrameLayout fl_content;
    public  View mRootView; //当前页面的布局对象

    public  BasePager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }
    //初始化布局
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tv_title1 = (TextView) view.findViewById(R.id.tv_title);
        btn_menu = (ImageButton) view.findViewById(R.id.btn_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        return view;
    }
    //初始化数据
    public void initData(){

    }
}
