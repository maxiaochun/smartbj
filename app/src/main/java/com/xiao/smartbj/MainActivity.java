package com.xiao.smartbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xiao.smartbj.fragment.ContentFragment;
import com.xiao.smartbj.fragment.LeftMenuFragment;

/**
 * Created by hasee on 2016/5/19.
 */
public class MainActivity extends SlidingFragmentActivity {


    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(200);//屏幕预留200像素宽度
        initFragment();
    }

    /**
     * 初始化fragment
     */
    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开始事务
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.commit();
    }

    /**
     * 获取侧边栏fragment对象
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }
    /**
     * 获取主页fragment对象
     * @return
     */
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
