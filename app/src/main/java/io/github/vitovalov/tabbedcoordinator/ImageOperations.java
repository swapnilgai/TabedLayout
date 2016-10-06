package io.github.vitovalov.tabbedcoordinator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by swapnil on 10/5/16.
 */
public class ImageOperations {

    Context context;
    String CacheDir;
    Activity activity;
    private Bitmap bm;
    private static final int PICK_IMAGE_ID = 234;


    public ImageOperations()
    {

    }

    private Uri beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(CacheDir, "cropped"));
      //  Crop.of(source, destination).asSquare().start(context, activity, Crop.REQUEST_CROP);
        return  destination;
    }

    private Bitmap handleCrop(int resultCode, Intent result, Uri destination) {


        if (resultCode == activity.RESULT_OK) {

            bm = decodeBitmap(activity, destination, 3);
            return bm;
            //   mImageView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(activity, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        Options options = new Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d("img : ", options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

}
