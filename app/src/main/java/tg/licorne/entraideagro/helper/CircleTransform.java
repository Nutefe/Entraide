package tg.licorne.entraideagro.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.graphics.ColorUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Admin on 12/02/2018.
 */

public class CircleTransform extends BitmapTransformation {

    public CircleTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int borderColor = ColorUtils.setAlphaComponent(Color.WHITE, 0xFF);
        int borderRadius = 3;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        if (squared != source) {
            source.recycle();
        }

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r- borderRadius, paint);

        // Prepare the background
        Paint paintBg = new Paint();
        paintBg.setColor(borderColor);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(r, r, r- borderRadius, paintBg);

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - borderRadius, paint);

        squared.recycle();

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
