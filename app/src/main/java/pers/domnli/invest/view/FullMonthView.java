package pers.domnli.invest.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;

import java.util.List;

import androidx.core.content.ContextCompat;
import pers.domnli.invest.R;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class FullMonthView extends MonthView {

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public FullMonthView(Context context) {
        super(context);

        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(dipToPx(context, 0.5f));
        mRectPaint.setColor(0x88efefef);

        mCurDayTextPaint.setColor(QMUIResHelper.getAttrColor(getContext(),R.attr.app_primary_color));


        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID));
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        canvas.drawRect(x, y , x + mItemWidth, y + mItemHeight, mSelectedPaint);
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

        List<Calendar.Scheme> schemes = calendar.getSchemes();
        if (schemes == null || schemes.size() == 0) {
            return;
        }
        mSchemeTextPaint.setTextSize(QMUIDisplayHelper.sp2px(getContext(),9));
        float textHeight = mSchemeTextPaint.getFontMetrics().descent - mSchemeTextPaint.getFontMetrics().ascent;
        float indexY = y + mItemHeight/2;
        for (Calendar.Scheme scheme : schemes) {

            mSchemeTextPaint.setColor(scheme.getShcemeColor());
            canvas.drawText(scheme.getScheme(),x + mItemWidth / 2,indexY,mSchemeTextPaint);
            indexY = indexY + textHeight;
        }
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mRectPaint);
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 4;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, cy,
                    mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, cy,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
