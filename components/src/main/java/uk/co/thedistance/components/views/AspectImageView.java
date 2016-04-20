package uk.co.thedistance.components.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import uk.co.thedistance.components.R;


/**
 * {@link ImageView} allowing a custom aspect ratio, cropping and max width
 *
 * @attr ref R.styleable#AspectImageView_cropType
 * @attr ref R.styleable#AspectImageView_ratio
 * @attr ref R.styleable#AspectImageView_ratioValue
 * @attr ref R.styleable#AspectImageView_maxWidth
 * <p>
 * Created by benbaggley on 05/08/15.
 */
public class AspectImageView extends ImageView {

    private static final float RATIO_WIDE = 16f / 9;
    private static final float RATIO_43 = 4f / 3;
    private static final float RATIO_32 = 3f / 2;
    private static final float RATIO_SQUARE = 1;
    private int cropType;
    private final int ratio;
    private int maxWidth;
    private float ratioValue;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CROP_TOP, CROP_BOTTOM, CROP_LEFT, CROP_RIGHT})
    public @interface CropType {
    }

    public static final int CROP_TOP = 1;
    public static final int CROP_BOTTOM = 2;
    public static final int CROP_LEFT = 4;
    public static final int CROP_RIGHT = 8;

    /**
     * Set custom cropping for the image.
     *
     * @param cropType Must be one of {@link uk.co.thedistance.components.views.AspectImageView.CropType}
     */
    public void setCropType(@CropType int cropType) {
        this.cropType = cropType;
    }

    /**
     * Set the maximum width the view will occupy, even when using match_parent
     *
     * @param maxWidth The maximum width of the view
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Set a custom aspect ratio, width / height
     *
     * @param ratio The aspect ratio to display the image at
     */
    public void setRatio(float ratio) {
        this.ratioValue = ratio;
    }

    public AspectImageView(Context context) {
        super(context);
        cropType = -1;
        ratio = -1;
        maxWidth = -1;
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AspectImageView,
                defStyleAttr, defStyleRes);

        cropType = a.getInteger(R.styleable.AspectImageView_cropType, -1);
        ratio = a.getInteger(R.styleable.AspectImageView_ratio, -1);
        ratioValue = a.getFloat(R.styleable.AspectImageView_ratioValue, -1);
        maxWidth = a.getDimensionPixelSize(R.styleable.AspectImageView_maxWidth, -1);

        if (ratioValue == -1) {
            switch (ratio) {
                case 0:
                    ratioValue = RATIO_WIDE;
                    break;
                case 1:
                    ratioValue = RATIO_43;
                    break;
                case 2:
                    ratioValue = RATIO_32;
                    break;
                case 3:
                    ratioValue = RATIO_SQUARE;
            }
        }

        if (cropType != -1) {
            setScaleType(ScaleType.MATRIX);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (maxWidth > 0 && specWidth > maxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        specWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (ratioValue != -1) {
            int height = (int) (specWidth / ratioValue);
            setMeasuredDimension(specWidth, height);
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {

        Matrix matrix = getImageMatrix();

        float scale;
        int viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int drawableWidth = 0, drawableHeight = 0;
        // Allow for setting the drawable later in code by guarding ourselves here.
        if (getDrawable() != null) {
            drawableWidth = getDrawable().getIntrinsicWidth();
            drawableHeight = getDrawable().getIntrinsicHeight();
        }

        // Get the scale.
        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            // Drawable is flatter than view. Scale it to fill the view height.
            // A Top/Bottom crop here should be identical in this case.
            scale = (float) viewHeight / (float) drawableHeight;
        } else {
            // Drawable is taller than view. Scale it to fill the view width.
            // Left/Right crop here should be identical in this case.
            scale = (float) viewWidth / (float) drawableWidth;
        }

        float viewToDrawableWidth = viewWidth / scale;
        float viewToDrawableHeight = viewHeight / scale;
        float xOffset = (((cropType & 8) == 8) ? 1 : 0) * (drawableWidth - viewToDrawableWidth);
        float yOffset = (((cropType & 2) == 2) ? 1 : 0) * (drawableHeight - viewToDrawableHeight);

        // Define the rect from which to take the image portion.
        RectF drawableRect = new RectF(xOffset, yOffset, xOffset + viewToDrawableWidth,
                yOffset + viewToDrawableHeight);
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

        setImageMatrix(matrix);

        return super.setFrame(l, t, r, b);
    }
}
