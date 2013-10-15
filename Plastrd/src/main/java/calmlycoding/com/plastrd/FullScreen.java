package calmlycoding.com.plastrd;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class FullScreen extends Activity {
    private AsyncHttpClient client = new AsyncHttpClient();
    private ImageView image;
    private Bitmap bmp;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.full_screen_view);

        Intent intent = getIntent();
        image = (ImageView) findViewById(R.id.fullScreenImage);

        //Gets screen size to retrieve image that matches screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        String path = "http://getPlastrd.com/serveImage.php?id=" + intent.getExtras().getString("id") + "&height=" + height + "&width=" + width * 2;

        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
        client.get(path, new BinaryHttpResponseHandler(allowedContentTypes) {
            public void onSuccess(byte[] fileData) {
                bmp = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                image.setImageBitmap(bmp);
            }
        });

    }

    public void saveDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FullScreen.this);
        builder.setMessage("Set this image as your wallpaper")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WallpaperManager WPMan = WallpaperManager.getInstance(FullScreen.this);
                        try {
                            WPMan.setBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}