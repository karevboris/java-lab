package com.example.boris.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Boris on 17.05.2017.
 */

public class SceneView extends View {

    MoveShip ms;
    RectF rect;

    public SceneView(Context context) {
        super(context);
        init();
    }

    public SceneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SceneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init(){
        rect = new RectF(25,50, 50, 75);
    }

    void start(){
        if(ms == null)
        {
            ms = new MoveShip(this);
            ms.execute();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(100, 255, 255, 255);

        Paint p = new Paint();
        p.setColor(Color.BLACK);

        for (int i = 1; i < 60; i += 4)
            for (int j = 1; j < 60; j += 2) {
                RectF r = new RectF(25 * i, 25 * j, 25+25*i, 25+25*j);
                canvas.drawRect(r, p);
            }
        for (int i = 3; i < 60; i += 4)
            for (int j = 2; j < 60; j += 2) {
                RectF r = new RectF(25 * i, 25 * j, 25+25*i, 25+25*j);
                canvas.drawRect(r, p);
            }

        for (int j = 0; j < ms.numClients; j++) {
            if (j==0) p.setColor(Color.RED);
            if (j==1) p.setColor(Color.GREEN);
            if (j==2) p.setColor(Color.YELLOW);
            if (j==4) p.setColor(Color.BLUE);
            RectF r = new RectF(25 * ms.Coord[j * 4], 25 * ms.Coord[j * 4 + 1], 25 * ms.Coord[j * 4]+25, 25 * ms.Coord[j * 4+1]+25);
            canvas.drawRect(r, p);
            Rect r2 = new Rect(25 * ms.Coord[j * 4 + 2], 25 * ms.Coord[j * 4 + 3], 25 * ms.Coord[j * 4 + 2]+25, 25 * ms.Coord[j * 4 + 3]+25);
            canvas.drawRect(r2, p);
        }
    }
}
