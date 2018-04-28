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
    private int penId, lineId, ovalId, eraserId, rectangleId;

    public PaintPanel(Context context) {
        super(context);
        init();
    }

    public PaintPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
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

        rectangleId = R.id.paint_panel_paint_item_rectangle;
        penId = R.id.paint_panel_paint_item_pen;
        ovalId = R.id.paint_panel_paint_item_oval;
        eraserId = R.id.paint_panel_paint_item_eraser;
        lineId = R.id.paint_panel_paint_item_line;

        mRedText = findViewById(R.id.paint_panel_r);
        mGreenText = findViewById(R.id.paint_panel_g);
        mBlueText = findViewById(R.id.paint_panel_b);
        mAlphaText = findViewById(R.id.paint_panel_a);
        final SeekBar redSeekBar = findViewById(R.id.paint_panel_seek_r);
        final SeekBar greenSeekBar = findViewById(R.id.paint_panel_seek_g);

        final SeekBar blueSeekBar = findViewById(R.id.paint_panel_seek_b);
        final SeekBar alphaSeekBar = findViewById(R.id.paint_panel_seek_a);
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
        if (id == eraserId){
            eraser();
        } else if (id == penId){
            setDrawer(LightPaintView.DrawerMode.PEN);
        } else if (id == lineId){
            setDrawer(LightPaintView.DrawerMode.LINE);
        } else if (id == ovalId){
            setDrawer(LightPaintView.DrawerMode.OVAL);
        } else if (id == rectangleId){
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
