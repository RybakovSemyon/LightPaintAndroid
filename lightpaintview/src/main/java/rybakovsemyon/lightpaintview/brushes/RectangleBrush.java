package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectangleBrush extends CommonBrush {

    RectangleBrush(Paint paint, Path path) {
        super(paint, path);
    }

    @Override
    public void onActionMove(float startX, float startY, float x, float y) {
        mPath.reset();
        RectF rect = new RectF();
        rect.set(startX, startY, x, y);
        rect.sort();
        mPath.addRect(rect, Path.Direction.CCW);
    }
}