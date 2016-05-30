package com.xiao.smartbj.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.xiao.smartbj.base.BaseMenuDetailPager;

/**
 * 主题详情页-新闻
 * Created by hasee on 2016/5/26.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {


    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-主题");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
