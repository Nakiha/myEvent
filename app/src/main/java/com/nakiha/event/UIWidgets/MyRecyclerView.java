package com.nakiha.event.UIWidgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface onSizeBecomeSmall {
        void doSomething();
    }
    onSizeBecomeSmall mSizeSmall = null;
    public void onSizeSmall(onSizeBecomeSmall sizeSmall) {
        mSizeSmall = sizeSmall;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h){
            if (mSizeSmall !=null){
                mSizeSmall.doSomething();
            }
        }
    }
}
