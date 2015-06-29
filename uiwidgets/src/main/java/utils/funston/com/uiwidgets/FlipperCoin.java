package utils.funston.com.uiwidgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

public class FlipperCoin extends View implements OnClickListener {

    AttributeSet mAttrs;
    Resources mResources;
    int mPixelDiameter;

    String mCurString = "20";
    int mAnimationDuration = 150;

    Paint mTextPaint;
    Paint mDebugPaint;

    ShapeDrawable mCurShown;
    ShapeDrawable mCoinFront;
    ShapeDrawable mCoinBack;
    ScaleDrawable mCheck;

    private OnCheckedChangeListener mCheckedChangeListener;

    public FlipperCoin(Context context) {
        super(context);
        init(context, null);
    }

    public FlipperCoin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlipperCoin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlipperCoin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        mAttrs = attrs;
        mResources = context.getResources();
        mPixelDiameter = (int) mResources.getDimension(R.dimen.fab_size_mini);
        Log.e("tag", mPixelDiameter + "");


        mCoinFront = new ShapeDrawable(new OvalShape());
        mCoinFront.getPaint().setColor(mResources.getColor(R.color.primary));
        mCoinFront.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mCoinFront.setBounds(0, 0, mPixelDiameter, mPixelDiameter);

        mCoinBack = new ShapeDrawable(new OvalShape());
        mCoinBack.getPaint().setColor(mResources.getColor(R.color.divider));
        mCoinBack.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mCoinBack.setBounds(0, 0, mPixelDiameter, mPixelDiameter);

        mCheck = new ScaleDrawable(context);

        mTextPaint = new Paint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mResources.getColor(android.R.color.white));
        mTextPaint.setTextSize(mResources.getDimension(R.dimen.labels_text_size));

        mDebugPaint = new Paint();
        mDebugPaint.setColor(mResources.getColor(android.R.color.holo_red_light));

        mCurShown = mCoinFront;

        setOnClickListener(this);
    }

    /* Public */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mPixelDiameter, mPixelDiameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCurShown.draw(canvas);
        if (mCurShown == mCoinFront) {
            drawText(canvas);
        } else {
            mCheck.draw(canvas);
        }
    }

    private void drawText(Canvas canvas) {
        final Rect textBounds = new Rect();
        mTextPaint.getTextBounds(mCurString, 0, mCurString.length(), textBounds);

        final int canvasHalf = getWidth() / 2;

        final int mTextWidth = (int) mTextPaint.measureText(mCurString);
        final int halfWidth = mTextWidth / 2;
        final int mTextHeight = textBounds.height();
        final int halfHeight = mTextHeight / 2;

        canvas.drawText(mCurString, canvasHalf - halfWidth, canvasHalf + halfHeight, mTextPaint);
    }

    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        mCheckedChangeListener = onCheckedChangeListener;
    }

    /* Private */

    public class ScaleDrawable {

        Drawable checkDrawable;
        float mScale = 1.0f;
        float intrisictWidth;

        ScaleDrawable(Context context) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                checkDrawable = context.getResources().getDrawable(R.drawable.ic_done_white_24dp, context.getTheme());
            } else {
                checkDrawable = context.getResources().getDrawable(R.drawable.ic_done_white_24dp);
            }

            intrisictWidth = checkDrawable.getIntrinsicWidth();
        }

        public void setScale(final float scale) {
            mScale = scale;
            invalidate();
        }

        public void draw(Canvas canvas) {
            final int viewWidth = getWidth() / 2;
            final int half = (int) ((intrisictWidth * mScale) / 2);
            checkDrawable.setBounds(viewWidth - half, viewWidth - half, viewWidth + half, viewWidth + half);
            checkDrawable.draw(canvas);
        }
    }

    @Override
    public void onClick(View v) {
        if (mCurShown == mCoinFront) {
            flipToBack();
        } else {
            flipToFront();
        }
    }

    public void flipToBack() {
        mCheck.setScale(0f);

        final ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
        objectAnimator1.setDuration(mAnimationDuration);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), mAttrs));
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurShown == mCoinFront) {
                    mCurShown = mCoinBack;
                } else {
                    mCurShown = mCoinFront;
                }

                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "rotationY", 90f, 0f);
        objectAnimator2.setDuration(mAnimationDuration);
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), mAttrs));

        final ObjectAnimator scaleAnimation = ObjectAnimator.ofFloat(mCheck, "scale", 0f, 1f);
        scaleAnimation.setDuration(mAnimationDuration + 100);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), mAttrs));

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator1).before(objectAnimator2).before(scaleAnimation);
        animatorSet.start();
    }

    public void flipToFront() {
        final ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
        objectAnimator1.setDuration(mAnimationDuration);
        objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), mAttrs));
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurShown == mCoinFront) {
                    mCurShown = mCoinBack;
                } else {
                    mCurShown = mCoinFront;
                }

                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "rotationY", 90f, 0f);
        objectAnimator2.setDuration(mAnimationDuration);
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), mAttrs));

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator1).before(objectAnimator2);
        animatorSet.start();
    }

    /* Inner Classes */

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a FlipperCoin changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */

        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }
}
