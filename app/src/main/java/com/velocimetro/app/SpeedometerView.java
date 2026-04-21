com.velocimetro.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SpeedometerView extends View {

    private Paint arcPaint, tickPaint, needlePaint, textPaint, centerPaint;
    private float currentSpeed = 0f;
    private float maxSpeed = 200f;
    private RectF arcRect;

    public SpeedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(20f);
        arcPaint.setColor(Color.parseColor("#333333"));

        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeWidth(4f);
        tickPaint.setColor(Color.WHITE);

        needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        needlePaint.setStyle(Paint.Style.FILL);
        needlePaint.setColor(Color.RED);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setColor(Color.WHITE);
    }

    public void setSpeed(float speed) {
        this.currentSpeed = Math.min(speed, maxSpeed);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float cx = width / 2f;
        float cy = height / 2f;
        float radius = Math.min(cx, cy) * 0.85f;

        // Arco de fondo
        arcRect = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        arcPaint.setColor(Color.parseColor("#333333"));
        arcPaint.setStrokeWidth(25f);
        canvas.drawArc(arcRect, 150, 240, false, arcPaint);

        // Arco de velocidad actual (verde → amarillo → rojo)
        float speedRatio = currentSpeed / maxSpeed;
        int arcColor;
        if (speedRatio < 0.5f) {
            arcColor = Color.parseColor("#00CC44");
        } else if (speedRatio < 0.75f) {
            arcColor = Color.parseColor("#FFAA00");
        } else {
            arcColor = Color.parseColor("#FF2200");
        }
        arcPaint.setColor(arcColor);
        arcPaint.setStrokeWidth(25f);
        canvas.drawArc(arcRect, 150, 240 * speedRatio, false, arcPaint);

        // Marcas y números
        for (int i = 0; i <= 200; i += 20) {
            float angle = 150 + (i / maxSpeed) * 240;
            float rad = (float) Math.toRadians(angle);

            float innerR = radius * 0.78f;
            float outerR = radius * 0.92f;

            float x1 = cx + innerR * (float) Math.cos(rad);
            float y1 = cy + innerR * (float) Math.sin(rad);
            float x2 = cx + outerR * (float) Math.cos(rad);
            float y2 = cy + outerR * (float) Math.sin(rad);

            tickPaint.setStrokeWidth(4f);
            canvas.drawLine(x1, y1, x2, y2, tickPaint);

            // Números
            float textR = radius * 0.65f;
            float tx = cx + textR * (float) Math.cos(rad);
            float ty = cy + textR * (float) Math.sin(rad) + 10f;
            textPaint.setTextSize(radius * 0.12f);
            canvas.drawText(String.valueOf(i), tx, ty, textPaint);
        }

        // Aguja
        float needleAngle = 150 + (currentSpeed / maxSpeed) * 240;
        float needleRad = (float) Math.toRadians(needleAngle);
        float needleLength = radius * 0.7f;

        Path needle = new Path();
        float backRad = (float) Math.toRadians(needleAngle + 180);
        needle.moveTo(
            cx + needleLength * (float) Math.cos(needleRad),
            cy + needleLength * (float) Math.sin(needleRad)
        );
        needle.lineTo(
            cx + 20 * (float) Math.cos((float) Math.toRadians(needleAngle - 90)),
            cy + 20 * (float) Math.sin((float) Math.toRadians(needleAngle - 90))
        );
        needle.lineTo(
            cx + radius * 0.15f * (float) Math.cos(backRad),
            cy + radius * 0.15f * (float) Math.sin(backRad)
        );
        needle.lineTo(
            cx + 20 * (float) Math.cos((float) Math.toRadians(needleAngle + 90)),
            cy + 20 * (float) Math.sin((float) Math.toRadians(needleAngle + 90))
        );
        needle.close();
        canvas.drawPath(needle, needlePaint);

        // Centro
        canvas.drawCircle(cx, cy, radius * 0.08f, centerPaint);

        // Velocidad en el centro
        textPaint.setTextSize(radius * 0.28f);
        textPaint.setColor(Color.WHITE);
        canvas.drawText((int) currentSpeed + "", cx, cy + radius * 0.35f, textPaint);

        // km/h
        textPaint.setTextSize(radius * 0.12f);
        textPaint.setColor(Color.parseColor("#AAAAAA"));
        canvas.drawText("km/h", cx, cy + radius * 0.52f, textPaint);
    }
}
