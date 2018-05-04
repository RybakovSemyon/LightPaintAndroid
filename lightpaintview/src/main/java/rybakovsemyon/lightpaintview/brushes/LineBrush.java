package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Paint;
import android.graphics.Path;

public class LineBrush extends CommonBrush {

    LineBrush(Paint paint, Path path) {
        super(paint, path);
    }

    @Override
    public void onActionMove(float startX, float startY, float x, float y) {
        mPath.reset();
        mPath.moveTo(startX, startY);
        mPath.lineTo(x, y);
    }
}