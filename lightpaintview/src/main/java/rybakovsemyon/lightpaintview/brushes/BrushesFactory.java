package rybakovsemyon.lightpaintview.brushes;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BrushesFactory {

    public enum Mode{
        ERASER,
        LINE,
        OVAL,
        PEN,
        RECT
    }

    public static CommonBrush createBrushFromMode(@NonNull Mode mode, @NonNull Paint paint, @Nullable Path path){
        switch (mode){
            case ERASER:
                return new EraserBrush(paint, path);
            case OVAL:
                return new OvalBrush(paint, path);
            case LINE:
                return new LineBrush(paint, path);
            case PEN:
                return new PenBrush(paint, path);
            case RECT:
                return new RectangleBrush(paint, path);
        }

        throw new RuntimeException("stub");
    }
}