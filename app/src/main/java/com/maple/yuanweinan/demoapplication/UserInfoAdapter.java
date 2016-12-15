package com.maple.yuanweinan.demoapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maple.yuanweinan.demoapplication.pojo.UserInfo;

import java.util.List;

/**
 * Created by yuanweinan on 16/12/15.
 */
public class UserInfoAdapter extends BaseAdapter {

    private List<UserInfo> mUserInfos;
    private LayoutInflater mLayoutInflater;

    public UserInfoAdapter(Context context, List<UserInfo> userInfos) {
        mUserInfos = userInfos;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mUserInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.listview_content, null);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.name_textView);
            viewHolder.mSex = (TextView) convertView.findViewById(R.id.sex_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserInfo userInfo = mUserInfos.get(position);
        viewHolder.mName.setText(userInfo.mName);
        viewHolder.mSex.setText(userInfo.mSex == 0 ? "man": "female");
        return convertView;
    }

    private static class ViewHolder {
        public TextView mName;
        public TextView mSex;
    }
}
