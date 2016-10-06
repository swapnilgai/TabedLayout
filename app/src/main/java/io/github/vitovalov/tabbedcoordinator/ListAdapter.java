package io.github.vitovalov.tabbedcoordinator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.*;

/**
 * Created by @vitovalov on 30/9/15.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    List<String> mCommentData;

    public ListAdapter(List<String> mCommentData) {
        this.mCommentData = new ArrayList<String>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    public void addAll(List<String> mCommentData)
    {
        this.mCommentData.addAll(mCommentData);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return mCommentData == null ? 0 : mCommentData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listitem_name);
        }
    }

}

