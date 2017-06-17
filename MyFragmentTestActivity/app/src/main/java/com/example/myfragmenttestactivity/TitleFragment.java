package com.example.myfragmenttestactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sz132 on 2017/6/7.
 */

public class TitleFragment extends Fragment {

    private boolean isTwo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<News> list = new ArrayList<>();
        for (int i=0; i<20; i++){
            News news = new News("title"+i+i+i, "content"+i+i+i);
            list.add(news);
        }
        recyclerView.setAdapter(new MyAdapter(list));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getFragmentManager().findFragmentById(R.id.content) != null){
            isTwo = true;
        }else {
            isTwo = false;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private List<News> list;

        public MyAdapter(List<News> list) {
            this.list = list;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.title.setText(list.get(position).getTitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTwo){
                        ((ContentFragment)getFragmentManager().findFragmentById(R.id.content)).refresh(list.get(position).getTitle(), list.get(position).getContent());
                    }else {
                        ContentActivity.startIntent(getActivity(), list.get(position).getTitle(), list.get(position).getContent());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }
    }
}
