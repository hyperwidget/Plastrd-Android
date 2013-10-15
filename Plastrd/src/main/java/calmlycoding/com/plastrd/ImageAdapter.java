package calmlycoding.com.plastrd;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Plastrd global;
    //Option tracks the selected option (newest, featured, popular)
    private int option;

    public ImageAdapter(Context c, Plastrd plastrd, int inOption) {
        mContext = c;
        global = plastrd;
        option = inOption;
    }

    /**
     * Gets the length of the array for the current option
     */
    public int getCount() {
        int count;
        switch (option) {
            case 1:
                count = global.firstHolderArray.size();
                break;
            case 2:
                count = global.secondHolderArray.size();
                break;
            case 3:
                count = global.secondHolderArray.size();
                break;
            default:
                count = 0;
                break;
        }

        return count;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (option) {
            case 1:
                final int tempPosition = position;
                final ViewGroup tempParent = parent;
                final PlastrdImageView imageView;

                if (convertView == null) {  // if it's not recycled, initialize some attributes
                    imageView = new PlastrdImageView(mContext);
                    imageView.setLayoutParams(new GridView.LayoutParams(global.thumbWidth, global.thumbWidth));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView = (PlastrdImageView) convertView;
                }
                imageView.id = global.firstHolderArray.get(tempPosition).getHolderID();
                imageView.setImageBitmap(global.firstHolderArray.get(tempPosition).getHolderBmap());

                OnClickListener clickListener = new OnClickListener() {
                    public void onClick(View v) {
                        if (v.equals(imageView)) {
                            Intent intent = new Intent(tempParent.getContext(), FullScreen.class);
                            intent.putExtra("id", imageView.id);
                            tempParent.getContext().startActivity(intent);
                        }
                    }
                };

                imageView.setOnClickListener(clickListener);

                return imageView;
            case 2:
                final int tempPosition2 = position;
                final ViewGroup tempParent2 = parent;
                final PlastrdImageView imageView2;
                if (convertView == null) {  // if it's not recycled, initialize some attributes
                    imageView2 = new PlastrdImageView(mContext);
                    imageView2.setLayoutParams(new GridView.LayoutParams(global.thumbWidth, global.thumbWidth));
                    imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView2 = (PlastrdImageView) convertView;
                }
                imageView2.id = global.secondHolderArray.get(tempPosition2).getHolderID();
                imageView2.setImageBitmap(global.secondHolderArray.get(tempPosition2).getHolderBmap());

                OnClickListener clickListener2 = new OnClickListener() {
                    public void onClick(View v) {
                        if (v.equals(imageView2)) {
                            Intent intent = new Intent(tempParent2.getContext(), FullScreen.class);
                            intent.putExtra("id", imageView2.id);
                            tempParent2.getContext().startActivity(intent);
                        }
                    }
                };

                imageView2.setOnClickListener(clickListener2);

                return imageView2;
            case 3:
                final int tempPosition3 = position;
                final ViewGroup tempParent3 = parent;
                final PlastrdImageView imageView3;
                if (convertView == null) {  // if it's not recycled, initialize some attributes
                    imageView3 = new PlastrdImageView(mContext);
                    imageView3.setLayoutParams(new GridView.LayoutParams(global.thumbWidth, global.thumbWidth));
                    imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView3 = (PlastrdImageView) convertView;
                }
                imageView3.id = global.secondHolderArray.get(tempPosition3).getHolderID();
                imageView3.setImageBitmap(global.secondHolderArray.get(tempPosition3).getHolderBmap());

                OnClickListener clickListener3 = new OnClickListener() {
                    public void onClick(View v) {
                        if (v.equals(imageView3)) {
                            Intent intent = new Intent(tempParent3.getContext(), FullScreen.class);
                            intent.putExtra("id", imageView3.id);
                            tempParent3.getContext().startActivity(intent);
                        }
                    }
                };

                imageView3.setOnClickListener(clickListener3);

                return imageView3;
            default:
                return null;
        }

    }
}