package com.xiao.smartbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xiao.smartbj.MainActivity;
import com.xiao.smartbj.R;
import com.xiao.smartbj.base.impl.NewsCenterPager;
import com.xiao.smartbj.domain.NewsMenu;

import java.util.ArrayList;


/**
 * Created by hasee on 2016/5/23.
 * 侧边栏fragment
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView lv_list;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;
    private int mCurrentPos = 0;
    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        lv_list = (ListView) view.findViewById(R.id.lv_list);

        return view;
    }

    @Override
    public void initData() {
    }

    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        //更新页面
        mNewsMenuData = data;

        mAdapter = new LeftMenuAdapter();
        lv_list.setAdapter(mAdapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mCurrentPos = position;//更新被选中的位置
                mAdapter.notifyDataSetChanged();//刷新listview
                //收起侧边栏
                toggle();
                //侧边栏点击之后，要修改新闻中心的framelayout中的内容
                setCurrrentDetailPager(position);
            }
        });
    }

    /**
     * 设置当前菜单详情页
     * @param position
     */
    private void setCurrrentDetailPager(int position) {
    //获取新闻中心的对象
        MainActivity mainUI = (MainActivity) mActivity;
        //获取ContentFragment
        ContentFragment fragment = mainUI.getContentFragment();
       //获取NewsCenterPager
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        //修改新闻中心的framelayout布局
        newsCenterPager.setCurrentDetailPager(position);
    }

    /**
     * 打开或关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态是开，调用后就关，反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int posotion) {
            return mNewsMenuData.get(posotion);
        }

        @Override
        public long getItemId(int posotion) {
            return posotion;
        }

        @Override
        public View getView(int posotion, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(mActivity, R.layout.list_item_menu, null);
            TextView tv_menu = (TextView) view.findViewById(R.id.tv_menu);

            NewsMenu.NewsMenuData item = getItem(posotion);
            tv_menu.setText(item.title);

            if (posotion == mCurrentPos){
                tv_menu.setEnabled(true);
            }else {
                tv_menu.setEnabled(false);
            }
            return view;
        }
    }

}
