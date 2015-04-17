package com.xmyunyou.wcd.ui.CityList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.City;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 95 on 2015/4/3.
 */
public class CityListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<City> list;

    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母

    public CityListAdapter(Context context ,List<City> list){

        this.inflater = LayoutInflater.from(context);
        this.list = list;
        alphaIndexer = new HashMap<String ,Integer>();

        for (int i = 0; i < list.size(); i++)
        {
            // 当前汉语拼音首字母
            String currentStr = list.get(i).getZiMu();
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = (i - 1) >= 0 ? list.get(i - 1).getZiMu() : " ";
            if (!previewStr.equals(currentStr))
            {
                String name = list.get(i).getZiMu();
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.adapter_city_list, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        String currentStr = list.get(position).getZiMu();
        String previewStr = (position - 1) >= 0 ? list.get(position - 1).getZiMu() : " ";
        if (!previewStr.equals(currentStr))
        {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else
        {
            holder.alpha.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder
    {
        TextView alpha;
        TextView name;
    }
}
