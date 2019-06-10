package com.nakiha.event.UIWidgets;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.nakiha.event.R;

public class eventCardContent extends ConstraintLayout {
    public eventCardContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.card_content_event, this);
    }
}
