package io.github.vitovalov.tabbedcoordinator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swapnil on 8/10/16.
 */
public class UploadImage  extends AsyncTask<Void, Void, Void> {

    private Bitmap bm;
    //Events event;
    private Comments comments;
    private Uri uri;
    private String Url;
    private String urlForComment;

//    public UploadImage(Events event, Bitmap bm) {
//        this.event = event;
//        this.bm = bm;
//        Log.e("upload image event: ",""+event);
//    }
    public UploadImage(Comments comments, Bitmap bm, String urlForComment) {
        this.comments = comments;
        this.bm = bm;
        this.urlForComment = urlForComment;
    }

    public UploadImage(boolean b) {
        super();

    }

    @Override
    protected Void doInBackground(Void... params) {
        Map config = new HashMap();
        config.put("cloud_name", "eventfy");
        config.put("api_key", "338234664624354");
        config.put("api_secret", "clA_O7equySs8LDK0hJNmmK62J8");
        Cloudinary cloudinary = new Cloudinary(config);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        try {
            Map uploadResult = cloudinary.uploader().upload(imageBytes, com.cloudinary.utils.ObjectUtils.emptyMap());
            Url = (String) uploadResult.get("secure_url");
            uri = Uri.parse(Url);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("image url : ", ""+uri);
        comments.setIsImage(true);
        comments.setCommentText(Url);
        PostUsersComment postUsersComment = new PostUsersComment(urlForComment, comments);
        postUsersComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}