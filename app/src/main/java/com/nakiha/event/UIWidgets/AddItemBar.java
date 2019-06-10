package com.nakiha.event.UIWidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.nakiha.event.R;

public class AddItemBar extends ConstraintLayout {
    public AddItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bar_add_item, this);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AddItemBar,
                0, 0);
        try {
            if(!a.getBoolean(R.styleable.AddItemBar_is_button, true)){
                findViewById(R.id.add_item_fake_button).setVisibility(GONE);
                findViewById(R.id.add_item_edit_text).setVisibility(VISIBLE);
            }

        } finally {
            a.recycle();
        }
    }
}
