package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class EraserBrush extends CommonBrush {

    EraserBrush(Paint paint, Path path) {
        super(paint, path);
    }

    @Override
    public void onActionMove(float startX, float startY, float x, float y) {
        mPaint.setAlpha(0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPath.lineTo(x, y);
    }
}