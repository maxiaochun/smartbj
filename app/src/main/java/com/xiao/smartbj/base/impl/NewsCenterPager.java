package com.xiao.smartbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xiao.smartbj.base.BasePager;


/**
 * 新闻中心
 * Created by hasee on 2016/5/23.
 */
public class NewsCenterPager extends BasePager {

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.e("text","news.....");
        TextView view = new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        fl_content.addView(view);
        //修改页面标题
        tv_title1.setText("新闻");

        //隐藏菜单按钮
        btn_menu.setVisibility(View.VISIBLE);

        //请求服务器，获取数据
        //开源框架xutils
    }
}
