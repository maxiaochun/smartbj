package com.xiao.smartbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiao.smartbj.R;

/**
 * 下拉刷新listview
 * Created by hasee on 2016/5/31.
 */
public class PullToRefreshListView extends ListView {

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

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    /**
     * 初始化头布局(下拉刷新)
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
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
    }

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

                if (mCurrentState == STATE_RELEASE_TO_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    //完整展示头布局
                    mHeaderView.setPadding(0,0,0,0);
                }else if(mCurrentState == STATE_PULL_TO_REFRESH){
                    //隐藏头布局
                    mHeaderView.setPadding(0,-mHeaderViewdHeight,0,0);
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
        animDown = new RotateAnimation(-180,0,
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
}