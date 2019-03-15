package com.xrd.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WJ on 2019/3/15.
 */

public class MyAdapter extends RecyclerView.Adapter {
    private Context context;
    private MessagePicturesLayout.Callback mCallBack;
    private final LayoutInflater inflater;
    List<Uri> mlist=new ArrayList<>();
    List<Uri> list1= Arrays.asList(Uri.parse("http://www.pptbz.com/pptpic/UploadFiles_6909/201203/2012031220134655.jpg"));
    List<Uri> list2= Arrays.asList(Uri.parse("http://www.pptbz.com/pptpic/UploadFiles_6909/201203/2012031220134655.jpg"));

    public MyAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHoler holder1 = (ViewHoler) holder;
        List<Data> data = Data.get();
        Data data1 = data.get(position);
        holder1.mpl.set(data1.getPictureThumbList(),data1.getPictureList());
        holder1.mpl.setCallback(mCallBack);
    }
    public MyAdapter setCallBack(MessagePicturesLayout.Callback callBack){
        this.mCallBack=callBack;
        return this;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
    static class ViewHoler extends RecyclerView.ViewHolder {
        private MessagePicturesLayout mpl;
        public ViewHoler(View itemView) {
            super(itemView);
            mpl=itemView.findViewById(R.id.mpl);
        }
    }
}
