package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class CommonBrush {

    Paint mPaint;
    Path mPath;

    CommonBrush(Paint paint, @Nullable Path path) {
        mPaint = paint;
        mPath = path == null ? new Path() : path;
    }

    public void onDraw(Canvas canvas){
        canvas.drawPath(mPath, mPaint);
    }

    public abstract void onActionMove(float startX, float startY, float x, float y);
}