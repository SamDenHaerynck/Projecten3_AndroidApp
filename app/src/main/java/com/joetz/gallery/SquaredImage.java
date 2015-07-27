package com.joetz.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * This class extends the ImageView so every image's the same width and heigth.
 */

public class SquaredImage extends ImageView {
    public SquaredImage(Context context) {
        super(context);
    }

    public SquaredImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}

