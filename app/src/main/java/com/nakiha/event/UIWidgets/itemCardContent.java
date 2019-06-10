package com.nakiha.event.UIWidgets;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.nakiha.event.R;

public class itemCardContent extends ConstraintLayout {
    public itemCardContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.list_item_item, this);
    }
}
