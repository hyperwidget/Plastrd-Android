package calmlycoding.com.plastrd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

    //how long until we go to the next activity
    protected int _splashTime = 2000;

    private Thread splashTread;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        // thread for displaying the SplashScreen
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        //wait 5 sec
                        wait(_splashTime);
                    }

                } catch (InterruptedException e) {
                } finally {
                    finish();
                    //start a new activity
                    Intent intent = new Intent(SplashScreen.this, InitialView.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        };

        splashTread.start();
    }

    //Function that will handle the touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (splashTread) {
                splashTread.notifyAll();
            }
        }
        return true;
    }

}