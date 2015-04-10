package com.moon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moon.Account.R;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Moon
 * Date: 2015/4/10.
 */
public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;

    public MyListViewAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_detai,null);
            viewHolder = new ViewHolder();
            viewHolder.textView_detail_date = (TextView) view.findViewById(R.id.textView_detail_date);
            viewHolder.textView_detail_type = (TextView) view.findViewById(R.id.textView_detail_type);
            viewHolder.textView_detail_detail = (TextView) view.findViewById(R.id.textView_detail_detail);
            viewHolder.textView_detail_money = (TextView) view.findViewById(R.id.textView_detail_money);
            viewHolder.textView_detail_time = (TextView) view.findViewById(R.id.textView_detail_time);
            viewHolder.textView_detail_comment = (TextView) view.findViewById(R.id.textView_detail_comment);
            view.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();
        if (list != null) {
            viewHolder.textView_detail_date.setText(list.get(i).get("dates").toString());
            viewHolder.textView_detail_time.setText(list.get(i).get("time").toString());
            viewHolder.textView_detail_type.setText(list.get(i).get("type").toString());
            viewHolder.textView_detail_detail.setText(list.get(i).get("detail").toString());
            viewHolder.textView_detail_comment.setText(list.get(i).get("comments").toString());
            viewHolder.textView_detail_money.setText(list.get(i).get("money").toString());
        }
        return view;
    }

    class ViewHolder{
        TextView textView_detail_date;
        TextView textView_detail_time;
        TextView textView_detail_type;
        TextView textView_detail_detail;
        TextView textView_detail_money;
        TextView textView_detail_comment;
    }
}
