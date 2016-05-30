package com.xiao.smartbj.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by hasee on 2016/5/29.
 */
public class TabPageIndicator1 extends TabPageIndicator {
    public TabPageIndicator1(Context context) {
        super(context);
    }

    public TabPageIndicator1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求所有父控件及祖控件不要拦截事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
