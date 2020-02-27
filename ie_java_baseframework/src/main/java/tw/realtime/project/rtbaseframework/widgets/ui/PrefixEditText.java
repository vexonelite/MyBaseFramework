package tw.realtime.project.rtbaseframework.widgets.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * [Adding a prefix to an EditText](https://medium.com/@ali.muzaffar/adding-a-prefix-to-an-edittext-2a17a62c77e1)
 * @see <a href="https://medium.com/@ali.muzaffar/adding-a-prefix-to-an-edittext-2a17a62c77e1">Adding a prefix to an EditText</a>
 */
public final class PrefixEditText extends AppCompatEditText {

    float mOriginalLeftPadding = -1;

    
    private String thePrefix = "";

    public void setPrefix(@NonNull String prefix) {
        this.thePrefix = prefix;
        this.mOriginalLeftPadding = -1;
    }

    public PrefixEditText(Context context) {
        super(context);
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefixEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePrefix();
    }

    private void calculatePrefix() {
        if (mOriginalLeftPadding == -1) {
            //String prefix = (String) getTag();
            final float[] widths = new float[thePrefix.length()];
            final TextPaint textPaint = getPaint();
            textPaint.getTextWidths(thePrefix, widths);
            float textWidth = 0;
            for (final float width : widths) {
                textWidth += width;
            }
            mOriginalLeftPadding = getCompoundPaddingLeft();
            setPadding((int) (
                    textWidth + mOriginalLeftPadding),
                    getPaddingTop(),
                    getPaddingRight(),
                    getPaddingBottom());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //String prefix = (String) getTag();
        canvas.drawText(
                thePrefix,
                mOriginalLeftPadding,
                getLineBounds(0, null),
                getPaint());
    }
}
