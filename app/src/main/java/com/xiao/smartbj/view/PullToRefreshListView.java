package com.xiao.smartbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiao.smartbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新listview
 * Created by hasee on 2016/5/31.
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;

    private View mHeaderView;
    private int startY = -1;
    private int endY;
    private int mHeaderViewdHeight;
    private TextView tv_title;
    private TextView tv_time;
    private ImageView iv_arrow;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar pb_loading;
    private View mFooterView;
    private int mFooterViewHeight;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局(下拉刷新)
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
        this.addHeaderView(mHeaderView);

        tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
        iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pb_loading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewdHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewdHeight, 0, 0);

        initAnim();
        setCurrentTime();
    }

    /**
     * 初始化脚布局
     */
    private  void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0,-mHeaderViewdHeight,0,0);

        this.setOnScrollListener(this);

    }

    //设置刷新时间
    private  void setCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tv_time.setText(time);

    }

    //下拉刷新
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {//当用户按住头条新闻的viewpager进行下拉时，ACTION_DOWN会被viewpager消费掉,
                    // 导致startY没有赋值,此处需要重新获取一下
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING) {
                    //如果是正在刷新，跳出循环
                    break;
                }
                endY = (int) ev.getY();
                int dy = endY - startY;//下拉距离

                int firstVisiblePosition = getFirstVisiblePosition();//当前显示的第一个item的位置（如果当前不是页面开头则没有必要刷新）
                // 必须下拉,并且当前显示的是第一个item
                if (dy > 0 && firstVisiblePosition == 0) {
                    int padding = dy - mHeaderViewdHeight;// 计算当前下拉控件的padding值
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        //改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:
                startY = -1;

                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    //完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);

                    //4.进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }

                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    //隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderViewdHeight, 0, 0);
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim() {
        //箭头顺时针旋转180度，变为向上
        animUp = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        //向下旋转180度
        animDown = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH://下拉刷新
                tv_title.setText("下拉刷新");
                pb_loading.setVisibility(View.INVISIBLE);
                iv_arrow.setVisibility(View.VISIBLE);
                iv_arrow.startAnimation(animDown);
                break;

            case STATE_RELEASE_TO_REFRESH://释放刷新
                tv_title.setText("松开刷新");
                pb_loading.setVisibility(View.INVISIBLE);
                iv_arrow.setVisibility(View.VISIBLE);
                iv_arrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING://刷新中
                tv_title.setText("刷新中...");

                iv_arrow.clearAnimation();//清除箭头动画，否则无法隐藏
                pb_loading.setVisibility(View.VISIBLE);
                iv_arrow.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     *刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success) {

        mHeaderView.setPadding(0, -mHeaderViewdHeight, 0, 0);

        mCurrentState = STATE_PULL_TO_REFRESH;
        tv_title.setText("下拉刷新");
        pb_loading.setVisibility(View.INVISIBLE);
        iv_arrow.setVisibility(View.VISIBLE);

        if (success) {//只有刷新成功之后才更新时间
            setCurrentTime();
        }
    }

    //3.定义成员变量，接收监听对象
    private OnRefreshListener mListener;

    /**
     * 2.暴露接口，设置监听
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 1.写回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();

        void onLoadMore();
    }


    private  boolean isLoadMore; //标记是否正在加载更多
    //滑动状态发生改变
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    if (scrollState == SCROLL_STATE_IDLE){//空闲状态
        int lastVisiblePosition = getLastVisiblePosition();

        if (lastVisiblePosition == getCount() - 1 && !isLoadMore){ //当前显示的是最后一个item并且没有正在加载更多
            // 到底了
           //显示加载更多的布局
            mFooterView.setPadding(0,0,0,0);

            setSelection(getCount() - 1);//将listview显示在最后一个item上,
                                        // 从而加载更多会直接展示出来, 无需手动滑动

            //通知主界面加载下一页数据
            if(mListener != null){
                mListener.onLoadMore();
            }

            
        }
    }
    }

    //滑动过程回调
    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }


}