package utils.funston.com.uiwidgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class FlipperCoin extends ImageButton {

    public FlipperCoin(Context context) {
        super(context);
    }

    public FlipperCoin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipperCoin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlipperCoin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
