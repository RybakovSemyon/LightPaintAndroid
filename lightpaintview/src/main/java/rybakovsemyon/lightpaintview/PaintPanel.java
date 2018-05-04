package rybakovsemyon.lightpaintview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import rybakovsemyon.lightpaintview.brushes.BrushesFactory;

public class PaintPanel extends LinearLayout implements View.OnClickListener{

    private SeekBar mSizePxSeekBar, mAlphaSeekBar, mRedSeekBar, mGreenSeekBar, mBlueSeekBar;
    private LightPaintView mLightPaintView;
    private TextView mPxText, mRedText, mGreenText, mBlueText, mAlphaText, mColorText;
    private FrameLayout mColorFrame;
    private int mColor;

    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setTextAndValues(mAlphaSeekBar.getProgress(), mRedSeekBar.getProgress(),
                    mGreenSeekBar.getProgress(), mBlueSeekBar.getProgress(), mSizePxSeekBar.getProgress() + 1);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public PaintPanel(Context context) {
        this(context, null, 0);
    }

    public PaintPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void attachPaintView(@NonNull LightPaintView lightPaintView){
        this.mLightPaintView = lightPaintView;
    }

    private void init() {
        inflate(getContext(), R.layout.paint_panel, this);
        findViewById(R.id.paint_panel_btn_undo).setOnClickListener(this);
        findViewById(R.id.paint_panel_btn_redo).setOnClickListener(this);
        findViewById(R.id.paint_panel_btn_clear).setOnClickListener(this);
        findViewById(R.id.paint_panel_btn_fill).setOnClickListener(this);
        RadioGroup paintsGroup = findViewById(R.id.paint_panel_rg);
        mPxText = findViewById(R.id.paint_panel_text_size);
        mSizePxSeekBar = findViewById(R.id.paint_panel_seekbar_size);
        mPxText.setText(getTextPx(mSizePxSeekBar.getProgress() + 1));
        mSizePxSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mRedText = findViewById(R.id.paint_panel_text_red);
        mGreenText = findViewById(R.id.paint_panel_text_green);
        mBlueText = findViewById(R.id.paint_panel_text_blue);
        mAlphaText = findViewById(R.id.paint_panel_text_alpha);
        mRedSeekBar = findViewById(R.id.paint_panel_seek_red);
        mGreenSeekBar = findViewById(R.id.paint_panel_seek_green);
        mBlueSeekBar = findViewById(R.id.paint_panel_seek_blue);
        mAlphaSeekBar = findViewById(R.id.paint_panel_seek_alpha);
        mColorFrame = findViewById(R.id.paint_panel_fl_color);
        mColorText = findViewById(R.id.paint_panel_text_color);
        mRedSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mGreenSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mBlueSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mAlphaSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        paintsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                modeFromCheckedItem(checkedId);
            }
        });
        modeFromCheckedItem(paintsGroup.getCheckedRadioButtonId());
        setTextAndValues(mAlphaSeekBar.getProgress(), mRedSeekBar.getProgress(), mGreenSeekBar.getProgress(), mBlueSeekBar.getProgress(),
                mSizePxSeekBar.getProgress() + 1);
    }

    private void setDrawer(BrushesFactory.Mode drawerMode){
        if (mLightPaintView != null){
            mLightPaintView.setOpacity(mAlphaSeekBar.getProgress());
            mLightPaintView.setPaintStrokeColor(mColor);
            mLightPaintView.setMode(drawerMode);
        }
    }

    // 14 lines
    private void setTextAndValues(int a, int r, int g, int b, int strokeWidth){
        mColor = Color.argb(a,r,g,b);
        mPxText.setText(getTextPx(strokeWidth));
        if (mLightPaintView != null){
            mLightPaintView.setPaintStrokeWidth(strokeWidth);
            mLightPaintView.setPaintStrokeColor(mColor);
            mLightPaintView.setOpacity(a);
        }
        mRedText.setText(concat("R ", r));
        mGreenText.setText(concat("G ", g));
        mBlueText.setText(concat("B ", b));
        mAlphaText.setText(concat("A ", a));
        mColorText.setTextColor(getTextColorFromRGB(r, g, b));
        mColorText.setText(getTextFromRGB(r, g, b, a));
        mColorFrame.setBackgroundColor(mColor);
    }

    @NonNull
    private String concat(@NonNull String a, int b){
        return a + b;
    }

    @NonNull
    private String getTextFromRGB(int r, int g, int b, int a){
        return String.format("#%02x%02x%02x%02x", a, r, g, b);
    }

    private void modeFromCheckedItem(int id){
        if (id == R.id.paint_panel_rbtn_eraser){
           setDrawer(BrushesFactory.Mode.ERASER);
        } else if (id == R.id.paint_panel_rbtn_pen){
            setDrawer(BrushesFactory.Mode.PEN);
        } else if (id == R.id.paint_panel_rbtn_line){
            setDrawer(BrushesFactory.Mode.LINE);
        } else if (id == R.id.paint_panel_rbtn_oval){
            setDrawer(BrushesFactory.Mode.OVAL);
        } else if (id == R.id.paint_panel_rbtn_rectangle){
            setDrawer(BrushesFactory.Mode.RECT);
        }
    }

    private int getTextColorFromRGB(int r, int g, int b){
        return Color.rgb(Math.abs(r-255), Math.abs(g-255), Math.abs(b-255));
    }

    @NonNull
    private String getTextPx(int value){
        return value + " px";
    }

    @Override
    public void onClick(View v) {
        if (mLightPaintView != null){
            int id = v.getId();
            if (id == R.id.paint_panel_btn_undo){
                mLightPaintView.undo();
            } else if (id == R.id.paint_panel_btn_clear){
                mLightPaintView.clear();
            } else if (id == R.id.paint_panel_btn_fill){
                mLightPaintView.setBaseColor(mColor);
            } else if (id == R.id.paint_panel_btn_redo){
                mLightPaintView.redo();
            }
        }
    }
}