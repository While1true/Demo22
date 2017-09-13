package com.kxjsj.doctorassistant.View;

import android.content.Context;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by hupihuai on 2017/8/9.
 * Adjust by 不听话好孩子 on 2017/9/12
 */

public class AdjustConstraintLayout extends ConstraintLayout {
    private static final int DESIGN_WIDTH = 1080;
    private static final int DESIGN_HEIGHT = 1920;
    private static final float DESIGN_SCALE = 3.0f;
    private float mScale;
    private float mFontScale;
    private float mScaleX = 0;
    private float mScaleY = 0;

    public AdjustConstraintLayout(Context context) {
        this(context, null);
    }

    public AdjustConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdjustConstraintLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Point point = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        //横屏
        if (point.x > point.y) {
            mScaleX = (point.x * 1.0f / DESIGN_HEIGHT);
            mScaleY = (point.y * 1.0f / DESIGN_WIDTH);
        } else {//竖屏
            mScaleX = (point.x * 1.0f / DESIGN_WIDTH);
            mScaleY = (point.y * 1.0f / DESIGN_HEIGHT);
        }
        //等宽缩放
        mScaleY = mScaleX;

        float density = DESIGN_SCALE / getResources().getDisplayMetrics().density;
        float scaleDensity = DESIGN_SCALE / getResources().getDisplayMetrics().scaledDensity;
        float minScale = Math.min(mScaleX, mScaleY);
        mScale = minScale * density;
        mScaleX *= density;
        mScaleY *= density;
        mFontScale = minScale * scaleDensity;
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
            transformSize(child, (ConstraintLayout.LayoutParams) params);
            if (child instanceof ViewGroup && !(child instanceof ConstraintLayout))
                initChildViewGroup((ViewGroup) child);
        }
        super.addView(child, params);
    }

    /**
     * 遍历子类适配
     * @param groups
     */
    private void initChildViewGroup(ViewGroup groups) {
        for (int i = 0; i < groups.getChildCount(); i++) {
            View view = groups.getChildAt(i);
            if (!(view instanceof ConstraintLayout)) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                transformSize(view, (MarginLayoutParams) layoutParams);
            }
            if (view instanceof ViewGroup)
                initChildViewGroup((ViewGroup) view);
        }
    }

    private void transformSize(View child, MarginLayoutParams params) {
        if (params.width > 0 && params.height > 0) {//按比列
            params.width *= mScale;
            params.height *= mScale;
        } else {
            //width
            if (params.width > 0) {
                params.width *= mScaleX;
            }
            //height
            if (params.height > 0) {
                params.height *= mScaleY;
            }
        }

        //font size
        if (child instanceof AppCompatTextView) {
            final float textSize = ((AppCompatTextView) child).getTextSize();
            ((TextView) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * mFontScale);
        } else if (child instanceof AppCompatButton) {
            final float textSize = ((AppCompatButton) child).getTextSize();
            ((Button) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * mFontScale);
        } else if (child instanceof AppCompatEditText) {
            final float textSize = ((AppCompatEditText) child).getTextSize();
            ((EditText) child).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * mFontScale);
        }
        //margin
        params.leftMargin *= mScaleX;
        params.topMargin *= mScaleY;
        params.rightMargin *= mScaleX;
        params.bottomMargin *= mScaleY;
        //padding
        int paddingLeft = (int) (child.getPaddingLeft() * mScaleX);
        int paddingTop = (int) (child.getPaddingTop() * mScaleY);
        int paddingRight = (int) (child.getPaddingRight() * mScaleX);
        int paddingBottom = (int) (child.getPaddingBottom() * mScaleY);
        child.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

}
