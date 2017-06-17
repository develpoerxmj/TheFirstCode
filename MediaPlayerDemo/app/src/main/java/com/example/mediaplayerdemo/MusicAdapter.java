package com.example.mediaplayerdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sz132 on 2017/6/15.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private List<Music> list;
    private ClickListener listener;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public List<Music> getList() {
        return list;
    }

    public void setList(List<Music> list) {
        this.list = list;
    }

    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(MusicAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(list.get(position).getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout ll;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
        }
    }

    interface ClickListener{
        void click(String path);
    }
}
