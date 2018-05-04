package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class OvalBrush extends CommonBrush {

    OvalBrush(Paint paint, Path path) {
        super(paint, path);
    }

    @Override
    public void onActionMove(float startX, float startY, float x, float y) {
        RectF rect = new RectF(startX, startY, x, y);
        mPath.reset();
        mPath.addOval(rect, Path.Direction.CCW);
    }
}