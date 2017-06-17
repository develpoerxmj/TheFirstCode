package com.example.myfragmenttestactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sz132 on 2017/6/7.
 */

public class ContentFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvContent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = (TextView) view.findViewById(R.id.title);
        tvContent = (TextView) view.findViewById(R.id.content);
    }

    public void refresh(String title, String content){
        tvTitle.setText(title);
        tvContent.setText(content);
    }
}
