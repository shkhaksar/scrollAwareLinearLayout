package me.khaksar.scrollawarelinearlayoutbehavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;


/**
 * Created by Shayan on 2/26/2016.
 */
public class LinearLayoutBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;
    private float oldTranslationY;
    private float oldTranslationX;

    public LinearLayoutBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout ||
                dependency instanceof Snackbar.SnackbarLayout ||
                super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, final LinearLayout child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
            child.setTranslationY(translationY);
            return true;
        } else {
            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            float translationY = Math.min(0, parent.getBottom() - child.getBottom());
            child.setTranslationY(translationY);
            oldTranslationY = child.getTranslationY();
            oldTranslationX = child.getTranslationX();
        }
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final LinearLayout child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final LinearLayout child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
            // User scrolled down and the LinearLayout is currently visible -> hide the LinearLayout
            animateOut(child);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            // User scrolled up and the LinearLayout is currently not visible -> show the LinearLayout
            animateIn(child);
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private void animateOut(final LinearLayout linearLayout) {
        oldTranslationY = linearLayout.getTranslationY();
        oldTranslationX = linearLayout.getTranslationX();
        ViewCompat.animate(linearLayout).translationY(168F).alpha(0.0F).setDuration(400L).setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        LinearLayoutBehavior.this.mIsAnimatingOut = true;
                    }

                    public void onAnimationCancel(View view) {
                        LinearLayoutBehavior.this.mIsAnimatingOut = false;
                    }

                    public void onAnimationEnd(View view) {
                        LinearLayoutBehavior.this.mIsAnimatingOut = false;
                        view.setVisibility(View.INVISIBLE);
                    }
                }).start();
    }

    private void animateIn(LinearLayout linearLayout) {
        linearLayout.setVisibility(View.VISIBLE);
        ViewCompat.animate(linearLayout).translationY(oldTranslationY).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                .start();
    }
}