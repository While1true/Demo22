package com.kxjsj.doctorassistant.Glide;

import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kxjsj.doctorassistant.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by vange on 2017/9/22.
 */

public class MyOptions extends RequestOptions {
    /**
     * 基本options
     * @return
     */
    private static RequestOptions getRequestOptions() {
        return new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
    }

    /**
     * 圆型图片
     * @return
     */
    public static RequestOptions getCircleRequestOptions() {
        return getRequestOptions().circleCrop();
    }

    /**
     * 圆角图片
     * @param radius
     * @param margins
     * @return
     */
    public static RequestOptions getRoundRequestOptions(int radius, int margins) {
        return getRequestOptions().centerCrop()
                .transform(new RoundedCornersTransformation(radius, margins));
    }

    /**
     * 淡入淡出
     * @return
     */
    public static TransitionOptions getTransitionCrossFade() {
        return new BitmapTransitionOptions().crossFade(2000);
    }
}
