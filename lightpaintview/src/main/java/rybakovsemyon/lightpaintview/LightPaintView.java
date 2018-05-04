package rybakovsemyon.lightpaintview;

import java.util.List;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

import static rybakovsemyon.lightpaintview.brushes.BrushesFactory.Mode;
import static rybakovsemyon.lightpaintview.brushes.BrushesFactory.createBrushFromMode;
import rybakovsemyon.lightpaintview.brushes.BrushesFactory;
import rybakovsemyon.lightpaintview.brushes.CommonBrush;

public class LightPaintView extends View {

    @Nullable
    private LightPaintViewState mLightPaintViewState;
    @NonNull
    private List<CommonBrush> mBrushes = new ArrayList<>();
    @NonNull
    private Paint.Style mPaintStyle = Paint.Style.STROKE;
    @NonNull
    private Paint.Cap mLineCap = Paint.Cap.ROUND;
    private int mPaintStrokeColor = Color.BLACK;
    private int mPaintFillColor = Color.BLACK;
    private float mPaintStrokeWidth = 10F;
    private int mOpacity = 255;
    private float mBlur = 0F;
    private float mStartX = 0F;
    private float mStartY = 0F;
    private int mBaseColor = Color.TRANSPARENT;
    private int mHistoryPointer = 0;
    private BrushesFactory.Mode mMode = Mode.PEN;

    public LightPaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setup();
    }

    public LightPaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightPaintView(Context context) {
        this(context, null, 0);
    }

    private void setup() {
        this.mBrushes.add(createBrushFromMode(mMode, createPaint(), null));
        this.mHistoryPointer++;
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(this.mPaintStyle);
        paint.setStrokeWidth(this.mPaintStrokeWidth);
        paint.setStrokeCap(this.mLineCap);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setColor(this.mPaintStrokeColor);
        paint.setShadowLayer(this.mBlur, 0F, 0F, this.mPaintStrokeColor);
        paint.setAlpha(this.mOpacity);
        return paint;
    }

    private Path createPath(MotionEvent event) {
        Path path = new Path();

        this.mStartX = event.getX();
        this.mStartY = event.getY();

        path.moveTo(this.mStartX, this.mStartY);

        return path;
    }

    private void updateHistory(Path path) {
        if (this.mHistoryPointer == this.mBrushes.size()) {
            this.mBrushes.add(createBrushFromMode(mMode, createPaint(), path));
            this.mHistoryPointer++;
        } else {
            this.mBrushes.set(this.mHistoryPointer, createBrushFromMode(mMode, createPaint(), path));
            this.mHistoryPointer++;
            for (int i = this.mHistoryPointer, size = this.mBrushes.size(); i < size; i++) {
                this.mBrushes.remove(this.mHistoryPointer);
            }
        }
    }

    private CommonBrush getCurrentBrush() {
        return this.mBrushes.get(this.mHistoryPointer - 1);
    }

    private void onActionDown(MotionEvent event) {
        this.updateHistory(this.createPath(event));
    }

    private void onActionMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        CommonBrush brush = getCurrentBrush();
        brush.onActionMove(mStartX, mStartY, x, y);
    }

    private void onActionUp() {
        this.mStartX = 0F;
        this.mStartY = 0F;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(this.mBaseColor);
        for (int i = 0; i < this.mHistoryPointer; i++) {
            mBrushes.get(i).onDraw(canvas);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE :
                this.onActionMove(event);
                break;
            case MotionEvent.ACTION_UP :
                this.onActionUp();
                break;
            default :
                break;
        }

        invalidate();

        return true;
    }

    void undo() {
        if (this.mHistoryPointer > 1) {
            this.mHistoryPointer--;
            this.invalidate();
        }
    }

    void redo() {
        if (this.mHistoryPointer < this.mBrushes.size()) {
            this.mHistoryPointer++;
            this.invalidate();
        }
    }

    void clear() {
        mHistoryPointer = 0;
        mBrushes = new ArrayList<>();
        setup();
        this.invalidate();
    }

    void setBaseColor(int color) {
        this.mBaseColor = color;
        invalidate();
    }

    void setPaintStrokeColor(int color) {
        this.mPaintStrokeColor = color;
    }

    void setPaintStrokeWidth(float width) {
        if (width >= 0) {
            this.mPaintStrokeWidth = width;
        } else {
            this.mPaintStrokeWidth = 3F;
        }
    }

    void setOpacity(int opacity) {
        if ((opacity >= 0) && (opacity <= 255)) {
            this.mOpacity = opacity;
        } else {
            this.mOpacity = 255;
        }
    }

    public void setMode(Mode mode) {
        mMode = mode;
    }

    @NonNull
    public LightPaintViewState getLightPaintViewState(){
        return new LightPaintViewState(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mLightPaintViewState != null){
            setState(mLightPaintViewState);
        }
    }

    private void setState(@NonNull LightPaintViewState object){
        mBrushes = object.brushes;
        mMode = object.mode;
        mBaseColor = object.baseColor;
        mHistoryPointer = object.historyPointer;
        mPaintStyle = object.paintStyle;
        mPaintStrokeColor = object.paintStrokeColor;
        mPaintFillColor = object.paintFillColor;
        mPaintStrokeWidth = object.paintStrokeWidth;
        mOpacity = object.opacity;
        mBlur = object.blur;
        mLineCap = object.lineCap;
        mStartX = object.startX;
        mStartY = object.startY;
        mLightPaintViewState = null;
        invalidate();
    }

    public void setLightPaintViewState(@Nullable LightPaintViewState object){
        if (object == null)
            return;

        if (isInLayout()){
            setState(object);
        } else {
            mLightPaintViewState = object;
        }
    }

    public static class LightPaintViewState {

        private final List<CommonBrush> brushes;
        private final Mode mode;
        private final int baseColor;
        private final int historyPointer;
        private final Paint.Style paintStyle;
        private final int paintStrokeColor;
        private final int paintFillColor;
        private final float paintStrokeWidth;
        private final int opacity;
        private final float blur;
        private final Paint.Cap lineCap;
        private final float startX;
        private final float startY;

        LightPaintViewState(LightPaintView object){
            mode = object.mMode;
            brushes = object.mBrushes;
            baseColor = object.mBaseColor;
            historyPointer = object.mHistoryPointer;
            paintStyle = object.mPaintStyle;
            paintStrokeColor = object.mPaintStrokeColor;
            paintFillColor = object.mPaintFillColor;
            paintStrokeWidth = object.mPaintStrokeWidth;
            opacity = object.mOpacity;
            blur = object.mBlur;
            lineCap = object.mLineCap;
            startX = object.mStartX;
            startY = object.mStartY;
        }
    }
}