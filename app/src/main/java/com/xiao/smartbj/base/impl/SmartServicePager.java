package com.xiao.smartbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xiao.smartbj.base.BasePager;


/**
 * 智慧服务
 * Created by hasee on 2016/5/23.
 */
public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView view = new TextView(mActivity);
        view.setText("智慧服务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        fl_content.addView(view);
        //修改页面标题
        tv_title1.setText("生活");
        //隐藏菜单按钮
        btn_menu.setVisibility(View.VISIBLE);
    }
}
