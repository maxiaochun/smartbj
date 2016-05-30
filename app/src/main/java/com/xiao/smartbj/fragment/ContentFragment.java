package com.xiao.smartbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xiao.smartbj.MainActivity;
import com.xiao.smartbj.R;
import com.xiao.smartbj.base.BasePager;
import com.xiao.smartbj.base.impl.GovAffairsPager;
import com.xiao.smartbj.base.impl.HomePager;
import com.xiao.smartbj.base.impl.NewsCenterPager;
import com.xiao.smartbj.base.impl.SettingPager;
import com.xiao.smartbj.base.impl.SmartServicePager;
import com.xiao.smartbj.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/5/23.
 * 主页面fragment
 */
public class ContentFragment extends BaseFragment {

    public NoScrollViewPager vp_content;
    private ArrayList<BasePager> mPagers;
    private RadioGroup rg_group;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rg_group = (RadioGroup) view.findViewById(R.id.rg_group);

        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();

        //添加五个标签页
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        vp_content.setAdapter(new ContentAdapter());
        //底栏标签切换监听
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        vp_content.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        vp_content.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        vp_content.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });

        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();

                if (position == 0 || position == mPagers.size() - 1) {
                    //首页和设置页要禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    //其他页开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动加载第一页数据
        mPagers.get(0).initData();
        //首页禁用侧边栏
        setSlidingMenuEnable(false);
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

    class ContentAdapter extends PagerAdapter {
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
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;//获取当前页面对象的布局
//            pager.initData();

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    //获取新闻中心页面
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
