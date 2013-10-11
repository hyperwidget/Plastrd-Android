package calmlycoding.com.plastrd;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class RepeatingAlarm extends BroadcastReceiver {
    private AsyncHttpClient client = new AsyncHttpClient();
    private Bitmap bmp;
    private ImageView image;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //Gets screen size to retrieve image that matches screen size
        WindowManager winMan = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = winMan.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //Path to playlist(Will need to figure this out)
        String path = "http://www.calmlycoding.com:78/Plastrd/serveRandom.php?height=" + height + "&width=" + width * 2;

        //Init imageview, nullpointer error otherwise
        image = new ImageView(context);
        //Allow pngs or jpgs
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
        //Get image using Asynchttp
        client.get(path, new BinaryHttpResponseHandler(allowedContentTypes) {
            public void onSuccess(byte[] fileData) {
                bmp = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                image.setImageBitmap(bmp);

                WallpaperManager WPMan = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);
                try {
                    WPMan.setBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
