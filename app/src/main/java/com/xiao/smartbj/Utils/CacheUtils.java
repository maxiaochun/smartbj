package com.xiao.smartbj.Utils;

import android.content.Context;

/**
 * 网络缓存工具类
 * Created by hasee on 2016/5/26.
 */
public class CacheUtils {
    /**
     * 以url为key,以json为value，保存在本地
     * @param url
     * @param json
     */
    public static void setCache(String url,String json,Context ctx){
        PrefUtils.setString(ctx,url,json);
    }

    /**
     * 获取缓存
     * @param url
     * @param ctx
     * @return
     */
    public static String getCache(String url,Context ctx){
       return PrefUtils.getString(ctx,url,null);
    }
}
