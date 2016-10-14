package io.github.vitovalov.tabbedcoordinator;


import android.content.res.Configuration;
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
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Attendance extends Fragment {
    private Attendance_adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View view;
    protected Handler handler;
    private ArrayList<SignUp> userList = new ArrayList<SignUp>();
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public Attendance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_attendance, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_attendance);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_attendance);

        userList.add(null);

        getUsersForEvent();

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(l);

        if(!EventBusService.getInstance().isRegistered(this))
            EventBusService.getInstance().register(this);

        adapter = new Attendance_adapter(recyclerView,(ArrayList<SignUp>) userList);

        handler = new Handler();

        recyclerView.setAdapter(adapter);
        //  bindAdapter(commentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(view.getContext(), R.drawable.listitem_divider)));

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsersForEvent();
                bindAdapter(userList);
            }
        });

        return view;
    }


    public void setLoaded() {
        loading = false;
    }

    public int getItemCount() {
        return userList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public void getUsersForEvent()
    {

        String url = "https://eventfy.herokuapp.com/webapi/comments/getuserforevent";
        Log.e("url: ", ""+url);
        GetUsersForEvent getUsersForEvent = new GetUsersForEvent(url, "2");
        getUsersForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Subscribe
    public void getUsersForEvent(List<SignUp> userListTemp)
    {
        Log.e("item received out : ", "" + userListTemp.size());

        if(userListTemp.get(0) instanceof SignUp) {
            if( userList!=null && userList.size()>0 && userList.get(0) == null)
                userList.remove(0);

            this.userList.addAll(userListTemp);
            this.userList.add(null);
            Log.e("item received : ", "" + this.userList.size());

            displayComments();
        }
    }


    public void displayComments()
    {

        //add null , so the adapter will check view_type and show progress bar at bottom

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                userList.remove(userList.size() - 1);
                adapter.notifyItemRemoved(userList.size());

                adapter.clear();
                adapter.addAll(userList);
                adapter.notifyDataSetChanged();
                adapter.setLoaded();

            }
        }, 5000);

    }


    private void bindAdapter(List<SignUp> commentList){
        swipeRefreshLayout.setRefreshing(false);
        refreshData(userList);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
        super.onConfigurationChanged(newConfig);
    }

    @UiThread
    private void refreshData(List<SignUp> userList){
        if (adapter != null){
            adapter.clear();
            adapter.addAll(userList);
            adapter.notifyDataSetChanged();
        }
    }

}
