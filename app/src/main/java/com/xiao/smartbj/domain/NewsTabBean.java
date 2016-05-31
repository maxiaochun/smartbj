package com.xiao.smartbj.domain;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/5/30.
 */
public class NewsTabBean {

    public NewsTab data;

    public class NewsTab{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopNews> topnews;

    }
    //新闻列表对象
    public class NewsData{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
    //头条新闻
    public class TopNews{
        public int id;
        public String pubdate;
        public String title;
        public String type;
        public String url;
        public String topimage;
    }
}
