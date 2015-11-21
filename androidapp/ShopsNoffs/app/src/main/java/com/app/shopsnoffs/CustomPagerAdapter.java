package com.app.shopsnoffs;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import java.util.List;

/**
 * Created by stefan on 17.10.15.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private OnClickListener mListener;
    private LayoutInflater mLayoutInflater;
    private List<String> urls;
    private RequestManager glide;

    public CustomPagerAdapter(Context c, OnClickListener mListener, List<String> data, RequestManager glideReqManager) {
        this.mListener = mListener;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = data;
        this.glide = glideReqManager;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img);

        glide.load(urls.get(position)).error(R.drawable.burger).into(imageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
