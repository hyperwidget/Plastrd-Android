package calmlycoding.com.plastrd;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class Plastrd extends Application {
    ArrayList<infoHolder> firstHolderArray, secondHolderArray;
    int width, height, thumbWidth, thumbHeight, horizontalSpacing, verticalSpacing;
    boolean connected = false;

    @Override
    public void onCreate() {
        super.onCreate();

        checkConnection();
        //Only try getting images if connection available
        if (connected) {
            //Gets screen size to retrieve image that matches screen size
            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;

            thumbWidth = (int) Math.round(width * .95 / 2);

            horizontalSpacing = (int) Math.round(width * .05);

            //System.out.println("Width: " + width + " thumbWidth: " + thumbWidth + " horizontalSpacing: " + horizontalSpacing);

            firstHolderArray = new ArrayList<infoHolder>();
            secondHolderArray = new ArrayList<infoHolder>();

            final AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://getPlastrd.com/initialFetch.php", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONArray json_data) {
                    JSONObject JSONContents;
                    try {
                        JSONContents = json_data.getJSONObject(0);
                        JSONArray temp = JSONContents.getJSONArray("firstArray");
                        for (int i = 0; i < temp.length(); i++) {
                            final infoHolder tempHolder = new infoHolder();
                            JSONObject tempObj = (JSONObject) temp.get(i);
                            String tempString = tempObj.getString("id");
                            tempHolder.setHolderid(tempString);
                            String path = "http://getPlastrd.com/serveImage.php?id=" +
                                    tempObj.getString("id") + "&width=" + thumbWidth + "&height=" + thumbWidth;
                            String[] allowedContentTypes = new String[]{"image/jpeg"};
                            client.get(path, new BinaryHttpResponseHandler(allowedContentTypes) {
                                public void onSuccess(byte[] fileData) {
                                    Bitmap bmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                                    tempHolder.setHolderBmap(bmap);
                                }
                            });
                            firstHolderArray.add(tempHolder);
                        }

                        JSONContents = json_data.getJSONObject(1);
                        temp = JSONContents.getJSONArray("singlePopular");
                        for (int i = 0; i < temp.length(); i++) {
                            final infoHolder tempHolder = new infoHolder();
                            JSONObject tempObj = (JSONObject) temp.get(i);
                            String tempString = tempObj.getString("id");
                            tempHolder.setHolderid(tempString);
                            String path = "http://getPlastrd.com/serveImage.php?id=" + tempObj.getString("id") + "&width=150&height=150";
                            String[] allowedContentTypes = new String[]{"image/jpeg"};
                            client.get(path, new BinaryHttpResponseHandler(allowedContentTypes) {
                                public void onSuccess(byte[] fileData) {
                                    Bitmap bmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                                    tempHolder.setHolderBmap(bmap);
                                }
                            });
                            secondHolderArray.add(tempHolder);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(Throwable e, String response) {
                    System.out.println("FAILED");
                    // Response failed :(
                }

                public void onFinish() {
                    // Completed the request (either success or failure)
                }
            });
        }
    }

    private void checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connected = true;
        }
        System.out.println(connected);
    }

    //Simple class to hold image attributes
    public class infoHolder {
        private String holderid;
        private Bitmap holderBmap;

        public Bitmap getHolderBmap() {
            return holderBmap;
        }

        public void setHolderBmap(Bitmap holderBmap) {
            this.holderBmap = holderBmap;
        }

        public String getHolderID() {
            return holderid;
        }

        public void setHolderid(String holderID) {
            this.holderid = holderID;
        }
    }

}
