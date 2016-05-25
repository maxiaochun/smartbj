package com.xiao.smartbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.xiao.smartbj.Utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/5/19.
 */
public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    private int[] mImageIDs;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout ll_container;
    private Button btn_start;
    private ImageView iv_red_point;
    private int mPointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initDate();
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动过程中的回调
//                Log.e("test","当前位置："+position+";移动偏移百分比："+positionOffset);
                //更新小红点的距离
                int leftMargin = (int) (mPointDis * positionOffset + position * mPointDis);
                //获取布局参数
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                params.leftMargin = leftMargin;//修改左边距
                iv_red_point.setLayoutParams(params);//重新设置左边距

            }

            @Override
            public void onPageSelected(int position) {
                //某个页面被选中
                if (position == mImageViewList.size() - 1) {
                    btn_start.setVisibility(View.VISIBLE);
                } else {
                    btn_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态发生变化的回调
            }
        });
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //移除监听，避免重复回调
                        iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //layout方法执行结束的回调
                        // 小红点移动距离 计算两个圆点的距离，=第二个圆点left值- 第一个圆点left值
                        mPointDis = ll_container.getChildAt(1).getLeft()
                                - ll_container.getChildAt(0).getLeft();
                    }
                }
        );
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新sp
                PrefUtils.setBoolean(getApplicationContext(),"is_first_enter",false);
                //跳转到主页面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

    private void initView() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        btn_start = (Button) findViewById(R.id.btn_start);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
    }

    private void initDate() {
        //图片封装到集合
        mImageIDs = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        //得到图片view集合
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIDs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIDs[i]);
            mImageViewList.add(imageView);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            //设置左边距
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 20;
            }
            point.setLayoutParams(params);//设置布局参数
            ll_container.addView(point);//给容器添加圆点
        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        //销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
