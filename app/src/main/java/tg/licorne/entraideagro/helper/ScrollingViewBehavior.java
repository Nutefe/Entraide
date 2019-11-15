package tg.licorne.entraideagro.helper;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Admin on 11/04/2018.
 */

public class ScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    public ScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (child instanceof RecyclerView)
            child.setPadding(0, 0, 0, child.getPaddingBottom() != 0 ? child.getPaddingBottom() * 2 - child.getPaddingTop() : child.getPaddingTop());
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
