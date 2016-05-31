package com.xiao.smartbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.xiao.smartbj.MainActivity;
import com.xiao.smartbj.R;
import com.xiao.smartbj.base.BaseMenuDetailPager;
import com.xiao.smartbj.domain.NewsMenu;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by hasee on 2016/5/26.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {


    private ViewPager vp_news_menu_detail;//下面的内容
    private ArrayList<NewsMenu.NewsTabData> mTabData;//页签网络数据
    private ArrayList<TabDetailPager> mPagers;//网签页面集合
    private TabPageIndicator indicator;//上面网签栏
    private ImageButton btn_next;
    private static final String TAG = "text";
    public NewsMenuDetailPager(Activity activity,
                               ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData = children;
        Log.e(TAG, "数据: "+mTabData );
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        vp_news_menu_detail = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        btn_next = (ImageButton) view.findViewById(R.id.btn_next);
        return view;

    }
    @Override
    public void initData() {
        //初始化页签
        mPagers = new ArrayList<TabDetailPager>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mTabData.get(i));
            mPagers.add(pager);
        }
        vp_news_menu_detail.setAdapter(new NewsMenuDetailAdapter());
        indicator.setViewPager(vp_news_menu_detail);//将viewpager和指示器绑定在一起，必须在viewpager设定之后再绑定
        //设置页面滑动监听
//        vp_news_menu_detail.setOnPageChangeListener(this);
        indicator.setOnPageChangeListener(this);//必须给指示器设置页面监听，不能设置给viewpager
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳到下一个页面
                int currentItem = vp_news_menu_detail.getCurrentItem();
                currentItem++;
                vp_news_menu_detail.setCurrentItem(currentItem);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            //开启侧边栏
            setSlidingMenuEnable(true);
        } else {
            //禁用侧边栏
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    class NewsMenuDetailAdapter extends PagerAdapter {

        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData data = mTabData.get(position);//每条children的数据
            return data.title;//每个children的名字

        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);

            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    protected void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


}
