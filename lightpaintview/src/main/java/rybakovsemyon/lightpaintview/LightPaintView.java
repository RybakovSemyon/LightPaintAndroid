package rybakovsemyon.lightpaintview;

import java.util.List;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

public class LightPaintView extends View {

    enum DrawerMode {
        PEN,
        LINE,
        RECTANGLE,
        OVAL
    }

    @Nullable
    private LightPaintViewState mLightPaintViewState;
    @NonNull
    private List<Path> mPaths = new ArrayList<>();
    @NonNull
    private List<Paint> mPaints = new ArrayList<>();
    @NonNull
    private DrawerMode mDrawerMode = DrawerMode.PEN;
    @NonNull
    private Paint.Style mPaintStyle = Paint.Style.STROKE;
    @NonNull
    private Paint.Cap mLineCap = Paint.Cap.ROUND;

    private int mPaintStrokeColor = Color.BLACK;
    private int mPaintFillColor = Color.BLACK;
    private float mPaintStrokeWidth = 10F;
    private int mOpacity = 255;
    private float mBlur = 0F;
    private boolean mIsDown = false;
    private float mStartX = 0F;
    private float mStartY = 0F;
    private int mBaseColor = Color.WHITE;
    private int mHistoryPointer = 0;

    public LightPaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setup();
    }

    public LightPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }

    public LightPaintView(Context context) {
        super(context);
        this.setup();
    }

    private void setup() {
        this.mPaths.add(new Path());
        this.mPaints.add(this.createPaint());
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
        if (this.mHistoryPointer == this.mPaths.size()) {
            this.mPaths.add(path);
            this.mPaints.add(this.createPaint());
            this.mHistoryPointer++;
        } else {
            this.mPaths.set(this.mHistoryPointer, path);
            this.mPaints.set(this.mHistoryPointer, this.createPaint());
            this.mHistoryPointer++;

            for (int i = this.mHistoryPointer, size = this.mPaints.size(); i < size; i++) {
                this.mPaths.remove(this.mHistoryPointer);
                this.mPaints.remove(this.mHistoryPointer);
            }
        }
    }

    private Path getCurrentPath() {
        return this.mPaths.get(this.mHistoryPointer - 1);
    }

    private void onActionDown(MotionEvent event) {
        this.updateHistory(this.createPath(event));
        this.mIsDown = true;
    }

    private void onActionMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (!mIsDown) {
            return;
        }

        Path path = this.getCurrentPath();

        switch (this.mDrawerMode) {
            case PEN :
                path.lineTo(x, y);
                break;
            case LINE :
                path.reset();
                path.moveTo(this.mStartX, this.mStartY);
                path.lineTo(x, y);
                break;
            case RECTANGLE :
                path.reset();
                if (y < mStartY + mPaintStrokeWidth)
                    y = mStartY + mPaintStrokeWidth;

                if (x < mStartX + mPaintStrokeWidth)
                    x = mStartX + mPaintStrokeWidth;

                path.addRect(this.mStartX, this.mStartY, x, y, Path.Direction.CW);
                break;
            case OVAL:
                RectF rect = new RectF(this.mStartX, this.mStartY, x, y);
                path.reset();
                path.addOval(rect, Path.Direction.CCW);
                break;
            default :
                break;
        }
    }

    private void onActionUp() {
        if (mIsDown) {
            this.mStartX = 0F;
            this.mStartY = 0F;
            this.mIsDown = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(this.mBaseColor);

        for (int i = 0; i < this.mHistoryPointer; i++) {
            Path path   = this.mPaths.get(i);
            Paint paint = this.mPaints.get(i);

            canvas.drawPath(path, paint);
        }
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

        this.invalidate();

        return true;
    }

    void setDrawerMode(@NonNull DrawerMode drawerMode) {
        this.mDrawerMode = drawerMode;
    }

    void undo() {
        if (this.mHistoryPointer > 1) {
            this.mHistoryPointer--;
            this.invalidate();
        }
    }

    void redo() {
        if (this.mHistoryPointer < this.mPaths.size()) {
            this.mHistoryPointer++;
            this.invalidate();
        }
    }

    void clear() {
        mHistoryPointer = 0;
        mPaints = new ArrayList<>();
        mPaths = new ArrayList<>();
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
        mPaths = object.paths;
        mPaints = object.paints;
        mBaseColor = object.baseColor;
        mHistoryPointer = object.historyPointer;
        mDrawerMode = object.drawerMode;
        mIsDown = object.isDown;
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

        private final int width;
        private final int height;
        private final List<Path> paths;
        private final List<Paint> paints;
        private final int baseColor;
        private final int historyPointer;
        private final DrawerMode drawerMode;
        private final boolean isDown;
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
            width = object.getWidth();
            height = object.getHeight();
            System.out.println(width + " " + height);
            paints = object.mPaints;
            paths = object.mPaths;
            baseColor = object.mBaseColor;
            historyPointer = object.mHistoryPointer;
            drawerMode = object.mDrawerMode;
            isDown = object.mIsDown;
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

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public List<Path> getPaths() {
            return paths;
        }

        public List<Paint> getPaints() {
            return paints;
        }

        public int getBaseColor() {
            return baseColor;
        }

        public int getHistoryPointer() {
            return historyPointer;
        }

        public DrawerMode getDrawerMode() {
            return drawerMode;
        }

        public boolean isDown() {
            return isDown;
        }

        public Paint.Style getPaintStyle() {
            return paintStyle;
        }

        public int getPaintStrokeColor() {
            return paintStrokeColor;
        }

        public int getPaintFillColor() {
            return paintFillColor;
        }

        public float getPaintStrokeWidth() {
            return paintStrokeWidth;
        }

        public int getOpacity() {
            return opacity;
        }

        public float getBlur() {
            return blur;
        }

        public Paint.Cap getLineCap() {
            return lineCap;
        }

        public float getStartX() {
            return startX;
        }

        public float getStartY() {
            return startY;
        }
    }
}