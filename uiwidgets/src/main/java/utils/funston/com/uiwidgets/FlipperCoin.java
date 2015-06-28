package utils.funston.com.uiwidgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.TextView;

public class FlipperCoin extends TextView implements OnClickListener{

    AttributeSet mAttrs;
    Resources mResources;
    int mPixelDiameter;

    ShapeDrawable mCurShown;
    ShapeDrawable mCoinFront;
    ShapeDrawable mCoinBack;

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

        mCoinFront = new ShapeDrawable(new OvalShape());
        mCoinFront.getPaint().setColor(mResources.getColor(R.color.primary));
        mCoinFront.setBounds(0, 0, mPixelDiameter, mPixelDiameter);

        mCoinBack = new ShapeDrawable(new OvalShape());
        mCoinBack.getPaint().setColor(mResources.getColor(R.color.accent));
        mCoinBack.setBounds(0, 0, mPixelDiameter, mPixelDiameter);

        mCurShown = mCoinFront;

        setText("A");

        setOnClickListener(this);
    }

    /* Public */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCurShown.draw(canvas);
        super.onDraw(canvas);
    }

    public void setOnCheckedChangeListener(final OnCheckedChangeListener onCheckedChangeListener) {
        mCheckedChangeListener = onCheckedChangeListener;
    }

    /* Private */

    private int getCircleSize() {
        return getResources().getDimensionPixelSize(R.dimen.fab_size_mini);
    }


    private ShapeDrawable createCircleDrawable(int color) {
        final ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    @Override
    public void onClick(View v) {
        Log.v("FUCK", "onClick");


//        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(1000);
//        this.startAnimation(rotate);
        final ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
        objectAnimator1.setDuration(250);
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
        objectAnimator2.setDuration(250);
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator(getContext(), mAttrs));

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator1).before(objectAnimator2);
        animatorSet.start();

//        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.card_flip_left_out);
//        this.startAnimation(animation);
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
