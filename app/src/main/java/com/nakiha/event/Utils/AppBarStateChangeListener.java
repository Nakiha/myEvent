package com.nakiha.event.Utils;

import android.support.design.widget.AppBarLayout;

public class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    public AppBarStateChangeListener(AppBarStateChangeListener.setOnStateChangeListener setOnStateChangeListener) {
        this.setOnStateChangeListener = setOnStateChangeListener;
    }

    private setOnStateChangeListener setOnStateChangeListener;

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                setOnStateChangeListener.onStateChange(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                setOnStateChangeListener.onStateChange(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                setOnStateChangeListener.onStateChange(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public interface setOnStateChangeListener {
        void onStateChange(AppBarLayout appBarLayout, State state);
    }
}
