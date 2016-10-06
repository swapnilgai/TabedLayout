package io.github.vitovalov.tabbedcoordinator;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventInfo_Comment extends Fragment {
    private CommentAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout CommentPostImageLayout;
    private View view;
    private List<Comments> commentsList = new ArrayList<>();
    private List<Comments> commentsListTemp = new ArrayList<>();
    private EditText commentTextEditText;
    private ImageButton btnCommentSend;
    private ImageButton selectImageFromDevice;
    private ImageView slectedImageViewFromDevice;
    private ImageButton btnCommentImageDsicard;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    protected Handler handler;
    private Bitmap bm;
    private static final int PICK_IMAGE_ID = 234;
    private Uri dest = null;
    private Comments comments;
    private UploadImage uploadImage;
    private PostUsersComment postUsersComment;
    private String urlForComment;

    public EventInfo_Comment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_info__comment, container, false);
        commentsList.add(null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_comment);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_nearby);
        commentTextEditText = (EditText) view.findViewById(R.id.commentText);
        btnCommentSend = (ImageButton) view.findViewById(R.id.btnCommentSend);
        selectImageFromDevice = (ImageButton) view.findViewById(R.id.btnSelectImageFromDevice);
        CommentPostImageLayout = (LinearLayout) view.findViewById(R.id.CommentPostImageLayout);
        slectedImageViewFromDevice = (ImageView) view.findViewById(R.id.slectedImageViewFromDevice);
        btnCommentImageDsicard = (ImageButton) view.findViewById(R.id.btnCommentImageDsicard);

        //getNearbEventServerCall();

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        Log.e("layout manager : ",""+l);
        recyclerView.setLayoutManager(l);

        if(!EventBusService.getInstance().isRegistered(this))
            EventBusService.getInstance().register(this);

        adapter = new CommentAdapter(recyclerView, (ArrayList<Comments>) commentsList);

        handler = new Handler();

        recyclerView.setAdapter(adapter);
      //  bindAdapter(commentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(view.getContext(), R.drawable.listitem_divider)));

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNearbEventServerCall();
                bindAdapter(commentsList);

            }
        });

        btnCommentSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Comments commentTemp = new Comments();

                urlForComment = "https://eventfy.herokuapp.com/webapi/comments/addcomment";

                commentTemp.setUserName("Current user");

                String commentText = commentTextEditText.getText().toString();

                if(bm!=null) {
                    commentTemp.setCommentText(dest.toString());
                    commentTemp.setIsImage(true);
                    uploadImage = new UploadImage(commentTemp, bm, urlForComment);
                    uploadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else{
                    commentTemp.setCommentText(commentText);
                    commentTemp.setIsImage(false);
                    postUsersComment = new PostUsersComment(urlForComment, commentTemp);
                }


                commentsList.add(commentTemp);

                bindAdapter(commentsList);
            }
        });


        selectImageFromDevice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(view.getContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
            }
        });

        btnCommentImageDsicard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CommentPostImageLayout.setVisibility(View.INVISIBLE);
                bm = null;
            }
        });

        if (commentsList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
           // tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
          //  tvEmptyView.setVisibility(View.GONE);
        }

        adapter.setOnLoadMoreListener(new CommentAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if(!commentsList.isEmpty())
                    commentsList.remove(commentsList.size() - 1);
                adapter.notifyItemRemoved(commentsList.size());

                commentsList.add(null);

                adapter.notifyItemInserted(commentsList.size() - 1);

                getNearbEventServerCall();
            }
        });

        return view;
    }


    public void setLoaded() {
        loading = false;
    }

    public int getItemCount() {
        return commentsList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    private void bindAdapter(List<Comments> commentList){
        swipeRefreshLayout.setRefreshing(false);
        refreshData(commentList);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        super.onConfigurationChanged(newConfig);
    }

    @UiThread
    private void refreshData(List<Comments> commentList){
        if (adapter != null){
            adapter.clear();
            adapter.addAll(commentList);
            adapter.notifyDataSetChanged();
        }
    }


    public void getNearbEventServerCall()
    {
//        Comments c = new Comments();
//
//        c.setCommentText("gsa saggas agsga gga sdgagsdadggasdgha  hgasdgasgd gasgdg asgdhasghd gg asdg agsd ggjsadgj ghvdgvds basd vvsdasa dad asdasdadadasddsadas ddd");
//        c.setUserImage("https://res.cloudinary.com/eventfy/image/upload/v1471298728/ekkjvc76qf023s4gudgl.png");
//        c.setUserName("Mayurest Gupta");
//
//        Comments c1 = new Comments();
//
//        c1.setCommentText("https://res.cloudinary.com/eventfy/image/upload/v1471299043/hmm5tjvjlar6vtqljvss.png");
//        c1.setUserImage("https://res.cloudinary.com/eventfy/image/upload/v1471299043/hmm5tjvjlar6vtqljvss.png");
//        c1.setUserName("Dev Team");
//
//        c1.setIsImage(true);

        //for(int i=0;i<12;i++)
        {
         //   commentsList.add(c);
        //    commentsList.add(c1);
        }
      //  Log.e("size : ", ""+commentsList.size())



         String url = "https://eventfy.herokuapp.com/webapi/comments/geteventcomment";

         GetCommentsForEvent getCommentsForEvent = new GetCommentsForEvent(url, "2");
         getCommentsForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Subscribe
    public void getCommentForEvent(List<Comments> commentsList)
    {
        this.commentsListTemp.addAll(commentsList);

        displayComments();
    }

    @Subscribe
    public void getPostedComment(Comments comments)
    {
        commentsList.add(comments);
        adapter.notifyItemInserted(commentsList.size()-1);
    }

    public void setCommentSectionVisible()
    {
        CommentPostImageLayout.setVisibility(View.VISIBLE);
        slectedImageViewFromDevice.setImageBitmap(bm);

    }



    public void displayComments()
    {
        //add null , so the adapter will check view_type and show progress bar at bottom

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.e("Size : ", ""+commentsList.size());
                commentsList.remove(commentsList.size() - 1);
                adapter.notifyItemRemoved(commentsList.size());

               //  commentsList.addAll(commentsListTemp);

                for(Comments comments: commentsListTemp)
                {
                    commentsList.add(comments);
                    adapter.notifyItemInserted(commentsList.size()-1);
                }
               // adapter.clear();
            //    adapter.addAll(commentsList);
            //    adapter.notifyDataSetChanged();

                adapter.setLoaded();
            }
        }, 20000);
    }

    // Crop Image

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_ID) {
            Uri selectedImage = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
            dest = beginCrop(selectedImage);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data, dest);
        }

    }

    private Uri beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(300, 180).start(getContext(), EventInfo_Comment.this, Crop.REQUEST_CROP);
        return  destination;
    }

    private void handleCrop(int resultCode, Intent result, Uri destination) {


        if (resultCode == getActivity().RESULT_OK) {
            Log.e("crop : ", "" + getActivity().getCacheDir());

            bm = decodeBitmap(getActivity(), destination, 3);
            setCommentSectionVisible();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
    // Crop image end

}
