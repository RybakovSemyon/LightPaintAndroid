package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Paint;
import android.graphics.Path;

public class PenBrush extends CommonBrush {

    PenBrush(Paint paint, Path path) {
        super(paint, path);
    }

    @Override
    public void onActionMove(float startX, float startY, float x, float y) {
        mPath.lineTo(x, y);
    }
}