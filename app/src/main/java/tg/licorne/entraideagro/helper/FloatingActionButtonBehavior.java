package tg.licorne.entraideagro.helper;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 11/04/2018.
 */

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;// || dependency instanceof RecyclerView;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        params.bottomMargin = (int) UIUtils.convertDpToPixel(16, child.getContext()) + UIUtils.getNavigationBarHeight(child.getContext());
        child.setLayoutParams(params);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            float lastTranslationY = child.getTranslationY();
            int bottomMargin = ((ViewGroup.MarginLayoutParams) dependency.getLayoutParams()).bottomMargin;
            float translationY = Math.min(0, (dependency.getTranslationY() + Math.abs(bottomMargin)) - dependency.getHeight());
            child.setTranslationY(translationY);
            return lastTranslationY != translationY;
        }
        return false;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (child.getTranslationY() != 0.0F && dependency instanceof Snackbar.SnackbarLayout)
            child.animate().translationY(0.0F).start();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE)
            child.hide();
        else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE)
            child.show();
    }
}
