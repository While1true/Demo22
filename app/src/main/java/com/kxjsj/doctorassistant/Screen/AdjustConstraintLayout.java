package com.kxjsj.doctorassistant.Screen;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hupihuai on 2017/8/9.
 * Adjust by 不听话好孩子 on 2017/9/12
 */

public class AdjustConstraintLayout extends ConstraintLayout {

    public AdjustConstraintLayout(Context context) {
        this(context, null);
    }

    public AdjustConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdjustConstraintLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (!isInEditMode()) {
            AdjustUtils.adjustConstraintLayout(child,params);
        }
        super.addView(child, params);
    }


}
