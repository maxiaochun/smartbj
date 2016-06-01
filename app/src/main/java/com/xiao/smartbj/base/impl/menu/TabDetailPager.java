package com.xiao.smartbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.CirclePageIndicator;
import com.xiao.smartbj.R;
import com.xiao.smartbj.Utils.CacheUtils;
import com.xiao.smartbj.base.BaseMenuDetailPager;
import com.xiao.smartbj.domain.NewsMenu;
import com.xiao.smartbj.domain.NewsTabBean;
import com.xiao.smartbj.globle.GlobalConstants;
import com.xiao.smartbj.view.PullToRefreshListView;
import com.xiao.smartbj.view.TopNewsViewPager;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * 页签页面对象
 * Created by hasee on 2016/5/27.
 */


public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
    //    private TextView view;
    private  String mUrl;//网络链接
    private ArrayList<NewsTabBean.TopNews> mTopNews;//头条新闻数据
    private String cache;
    private static final String TAG = "TabDetailPager";
    private ArrayList<NewsTabBean.NewsData> mNewsList;//列表新闻数据
    private NewsAdapter mNewsAdapter;
    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager vp_top_news;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.lv_list)
    private PullToRefreshListView lv_list;
    private String mMoreUrl;//下一页的数据链接
    private Object moreDataFromServer;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstants.SERVER_URL + mTabData.url;

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        // 给listview添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header,
                null);
        ViewUtils.inject(this, mHeaderView);// 此处必须将头布局也注入
        lv_list.addHeaderView(mHeaderView);

        //5.前端界面设置回调
        lv_list.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                //判断是否有下一页数据
                if (mMoreUrl != null) {
                    //有下一页
                    getMoreDataFromServer();
                } else {
                    //没有下一页
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        cache = CacheUtils.getCache(mUrl, mActivity);
       if (!TextUtils.isEmpty(cache)){
           processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(mUrl,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String result = response.toString();
                processData(result);
                Log.e(TAG, "murl: "+mUrl);
                CacheUtils.setCache(mUrl,result,mActivity);

                //刷新结束，收起下拉刷新控件
                lv_list.onRefreshComplete(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                //刷新结束，收起下拉刷新控件
                lv_list.onRefreshComplete(false);
            }
        });
    }
    protected void processData(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);

       String moreUrl =  newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)){
            mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
        }else {
            mMoreUrl = null;
        }


        //头条新闻填充数据
        mTopNews = newsTabBean.data.topnews;
        Log.e(TAG, "头条: "+mTopNews);
        //头条新闻填充数据
        if (mTopNews!= null){
            vp_top_news.setAdapter(new TopNewsAdapter());
            mIndicator.setViewPager(vp_top_news);
            mIndicator.setSnap(true);//快照方式展示
            //viewpager和indicator同时存在，事件要设置给indicator
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //更新头条新闻标题
                    NewsTabBean.TopNews topNews = mTopNews.get(position);
                    tv_title.setText(topNews.title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //更新第一个头条新闻标题
            tv_title.setText(mTopNews.get(0).title);
            mIndicator.onPageSelected(0);//默认让第一个选中(解决bug)
        }
        //列表新闻填充数据
        mNewsList = newsTabBean.data.news;
        if (mNewsList!= null){
            mNewsAdapter = new NewsAdapter();
            lv_list.setAdapter(mNewsAdapter);
        }

    }

    /**
     * 加载下一页数据
     * @return
     */
    protected void getMoreDataFromServer() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(mUrl,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String result = response.toString();
                processData(result);

                //刷新结束，收起下拉刷新控件
                lv_list.onRefreshComplete(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                //刷新结束，收起下拉刷新控件
                lv_list.onRefreshComplete(false);
            }
        });
    }

    //头条新闻数据适配器
        class TopNewsAdapter extends PagerAdapter {

        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils
                    .configDefaultLoadingImage(R.mipmap.topnews_item_default);// 设置加载中的默认图片
        }

            @Override
            public int getCount() {
                return mTopNews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = new ImageView(mActivity);
                // view.setImageResource(R.drawable.topnews_item_default);
                view.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片缩放方式, 宽高填充父控件

                String imageUrl = mTopNews.get(position).topimage;// 图片下载链接

                // 下载图片-将图片设置给imageview-避免内存溢出-缓存
                // BitmapUtils-XUtils
                mBitmapUtils.display(view, imageUrl);

                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        }
    //listview适配器
    class NewsAdapter extends BaseAdapter{
        //加载图片
        private  BitmapUtils mBitmapUtils;
        public NewsAdapter(){
       mBitmapUtils =  new BitmapUtils(mActivity);
        mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);//默认图片
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertview == null){
               convertview = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertview.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView)convertview.findViewById(R.id.tv_title);
                holder.tvDate = (TextView)convertview.findViewById(R.id.tv_date);

                convertview.setTag(holder);
            }else {
                holder = (ViewHolder)convertview.getTag();
            }

            NewsTabBean.NewsData news = getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);
//            mBitmapUtils.display(holder.ivIcon,news.listimage);
            return convertview;
        }
    }
    static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}