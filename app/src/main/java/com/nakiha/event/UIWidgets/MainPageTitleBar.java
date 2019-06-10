package com.nakiha.event.UIWidgets;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.nakiha.event.R;

public class MainPageTitleBar extends ConstraintLayout {
    private Context mContext;

    public MainPageTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.bar_main_page, this);
        initUIWidgets();
    }

    private void initUIWidgets(){

    }
}
