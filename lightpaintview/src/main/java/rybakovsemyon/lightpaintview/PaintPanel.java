package rybakovsemyon.lightpaintview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class PaintPanel extends LinearLayout{

    private TextView mPxText;

    private SeekBar mSizePxSeekBar;

    private LightPaintView mLightPaintView;

    private TextView mRedText;

    private TextView mGreenText;

    private TextView mBlueText;

    private TextView mAlphaText;

    private int mColor, mAlpha = 255;
    private int mPenId, mLineId, mOvalId, mEraserId, mRectangleId;

    public PaintPanel(Context context) {
        super(context);
        if (getOrientation() == HORIZONTAL){
            inflate(getContext(), R.layout.paint_panel_horizontal, this);
        } else {
            inflate(getContext(), R.layout.paint_panel_vertical, this);
        }
        init();
    }

    public PaintPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (getOrientation() == HORIZONTAL){
            inflate(getContext(), R.layout.paint_panel_horizontal, this);
        } else {
            inflate(getContext(), R.layout.paint_panel_vertical, this);
        }
        init();
    }

    public PaintPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (getOrientation() == HORIZONTAL){
            inflate(getContext(), R.layout.paint_panel_horizontal, this);
        } else {
            inflate(getContext(), R.layout.paint_panel_vertical, this);
        }
        init();
    }

    public void attachPaintView(@NonNull LightPaintView lightPaintView){
        this.mLightPaintView = lightPaintView;
    }

    private void init() {
        Button undoBtn = findViewById(R.id.paint_panel_btn_undo);
        Button redoBtn = findViewById(R.id.paint_panel_btn_redo);
        Button clearBtn = findViewById(R.id.paint_panel_btn_clear);
        Button fillBtn = findViewById(R.id.paint_panel_btn_fill);
        RadioGroup paintsGroup = findViewById(R.id.paint_panel_rg);
        mPxText = findViewById(R.id.paint_panel_text_size);
        mSizePxSeekBar = findViewById(R.id.paint_panel_seekbar_size);
        mPxText.setText(getTextPx(mSizePxSeekBar.getProgress()));
        mSizePxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0){
                    mSizePxSeekBar.setProgress(1);
                    return;
                }
                mPxText.setText(getTextPx(progress));
                if (mLightPaintView != null){
                    mLightPaintView.setPaintStrokeWidth(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        undoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLightPaintView != null){
                    mLightPaintView.undo();
                }
            }
        });

        redoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLightPaintView != null){
                    mLightPaintView.redo();
                }
            }
        });

        clearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLightPaintView != null){
                    mLightPaintView.clear();
                }
            }
        });

        fillBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLightPaintView != null){
                    mLightPaintView.setBaseColor(mColor);
                }
            }
        });

        mRectangleId = R.id.paint_panel_rbtn_rectangle;
        mPenId = R.id.paint_panel_rbtn_pen;
        mOvalId = R.id.paint_panel_rbtn_oval;
        mEraserId = R.id.paint_panel_rbtn_eraser;
        mLineId = R.id.paint_panel_rbtn_line;

        mRedText = findViewById(R.id.paint_panel_text_red);
        mGreenText = findViewById(R.id.paint_panel_text_green);
        mBlueText = findViewById(R.id.paint_panel_text_blue);
        mAlphaText = findViewById(R.id.paint_panel_text_alpha);
        final SeekBar redSeekBar = findViewById(R.id.paint_panel_seek_red);
        final SeekBar greenSeekBar = findViewById(R.id.paint_panel_seek_green);

        final SeekBar blueSeekBar = findViewById(R.id.paint_panel_seek_blue);
        final SeekBar alphaSeekBar = findViewById(R.id.paint_panel_seek_alpha);
        final FrameLayout colorFrame = findViewById(R.id.paint_panel_fl_color);
        final TextView colorText = findViewById(R.id.paint_panel_text_color);
        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTextAndValues(colorText, colorFrame, alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        redSeekBar.setProgress(Color.red(mColor));
        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTextAndValues(colorText, colorFrame, alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        greenSeekBar.setProgress(Color.green(mColor));
        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTextAndValues(colorText, colorFrame, alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blueSeekBar.setProgress(Color.blue(mColor));
        alphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTextAndValues(colorText, colorFrame, alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        alphaSeekBar.setProgress(mAlpha);

        paintsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                modeFromCheckedItem(checkedId);
            }
        });
        modeFromCheckedItem(paintsGroup.getCheckedRadioButtonId());
    }

    private void setDrawer(LightPaintView.DrawerMode drawerMode){
        if (mLightPaintView != null){
            mLightPaintView.setOpacity(mAlpha);
            mLightPaintView.setPaintStrokeColor(mColor);
            mLightPaintView.setDrawerMode(drawerMode);
        }
    }

    private void eraser(){
        if (mLightPaintView != null){
            mLightPaintView.setDrawerMode(LightPaintView.DrawerMode.PEN);
            mLightPaintView.setOpacity(255);
            mLightPaintView.setPaintStrokeColor(Color.WHITE);
        }
    }

    private void setTextAndValues(TextView textView, FrameLayout frameLayout, int a, int r, int g, int b){
        mAlpha = a;
        mColor = Color.argb(a,r,g,b);
        if (mLightPaintView != null){
            mLightPaintView.setPaintStrokeColor(mColor);
            mLightPaintView.setOpacity(mAlpha);
        }
        String s = "R " + r;
        mRedText.setText(s);
        s = "G " + g;
        mGreenText.setText(s);
        s = "B " + b;
        mBlueText.setText(s);
        s = "A " + a;
        mAlphaText.setText(s);
        textView.setTextColor(getTextColorFromRGB(r, g, b));
        textView.setText(getTextFromRGB(r, g, b, mAlpha));
        frameLayout.setBackgroundColor(Color.parseColor(getTextFromRGB(r, g, b, mAlpha)));
    }

    @NonNull
    private String getTextFromRGB(int r, int g, int b, int a){
        String alpha = Integer.toHexString(a);
        if (alpha.length() == 1){
            alpha = "0" + alpha;
        }
        String red = Integer.toHexString(r);
        if (red.length() == 1){
            red = "0" + red;
        }
        String green = Integer.toHexString(g);
        if (green.length() == 1){
            green = "0" + green;
        }
        String blue = Integer.toHexString(b);
        if (blue.length() == 1){
            blue = "0" + blue;
        }
        return "#" + alpha + red + green + blue;
    }

    private void modeFromCheckedItem(int id){
        if (id == mEraserId){
            eraser();
        } else if (id == mPenId){
            setDrawer(LightPaintView.DrawerMode.PEN);
        } else if (id == mLineId){
            setDrawer(LightPaintView.DrawerMode.LINE);
        } else if (id == mOvalId){
            setDrawer(LightPaintView.DrawerMode.OVAL);
        } else if (id == mRectangleId){
            setDrawer(LightPaintView.DrawerMode.RECTANGLE);
        }
    }

    private int getTextColorFromRGB(int r, int g, int b){
        return  Color.rgb(Math.abs(r-255), Math.abs(g-255), Math.abs(b-255));
    }

    @NonNull
    private String getTextPx(int value){
        return value + " px";
    }
}
