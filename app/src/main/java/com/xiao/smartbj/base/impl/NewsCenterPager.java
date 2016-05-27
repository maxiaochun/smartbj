package com.xiao.smartbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiao.smartbj.MainActivity;
import com.xiao.smartbj.Utils.CacheUtils;
import com.xiao.smartbj.base.BaseMenuDetailPager;
import com.xiao.smartbj.base.BasePager;
import com.xiao.smartbj.base.impl.menu.InteractMenuDetailPager;
import com.xiao.smartbj.base.impl.menu.NewsMenuDetailPager;
import com.xiao.smartbj.base.impl.menu.PhotoMenuDetailPager;
import com.xiao.smartbj.base.impl.menu.TopicMenuDetailPager;
import com.xiao.smartbj.domain.NewsMenu;
import com.xiao.smartbj.fragment.LeftMenuFragment;
import com.xiao.smartbj.globle.GlobalConstants;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * 新闻中心
 * Created by hasee on 2016/5/23.
 */
public class NewsCenterPager extends BasePager {


    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    private NewsMenu mNewsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.e("text", "news.....");
       /* TextView view = new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        fl_content.addView(view);*/
        //修改页面标题
        tv_title1.setText("新闻");

        //隐藏菜单按钮
        btn_menu.setVisibility(View.VISIBLE);

        //先判断有没有缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            Log.e(TAG, "initData: 有缓存。。。");
            processData(cache);
        } else {
            //请求服务器，获取数据
            //开源框架xutils
            getDataFromServer();

        }
    }

    /**
     * 从服务器获取数据
     * />
     */
    private void getDataFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams(); // 绑定参数
        client.get(GlobalConstants.CATEGORY_URL, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
//                Toast.makeText(mActivity, response.toString(), Toast.LENGTH_SHORT)
//                        .show();
                String result = response.toString();
                Log.e(TAG, "onSuccess: " + result);
                //gson解析
                processData(result);
                //写缓存
                CacheUtils.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 解析数据
     */
    private static final String TAG = "NewsCenterPager";

    private void processData(String json) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(json, NewsMenu.class);

        Log.e(TAG, "processData:" + mNewsData);

        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();

        //给侧边栏设置数据
        fragment.setMenuData(mNewsData.data);

        //初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotoMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);
    }

    //设置菜单详情页
    public void setCurrentDetailPager(int position) {
        //重新给framelayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        View view = pager.mRootView;//当前页面的布局

        //清除之前旧的布局
        fl_content.removeAllViews();

        fl_content.addView(view);

        //初始化页面数据
        pager.initData();

        //更新标题
        tv_title1.setText(mNewsData.data.get(position).title);


    }
}