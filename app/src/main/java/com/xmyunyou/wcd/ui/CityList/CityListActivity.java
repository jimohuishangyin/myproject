package com.xmyunyou.wcd.ui.CityList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.City;
import com.xmyunyou.wcd.ui.view.MyLetterListView;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.CityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 95 on 2015/4/3.
 */
public class CityListActivity extends BaseActivity {
    private BaseActivity mActivity;
    private BaseAdapter adapter;
    private ListView mCityListView;
    private TextView overlay;
    private MyLetterListView mLetterListView;
    private HashMap<String,Integer>alphaIndexer;
    private String[] sections;
    private Handler handler;
    private OverlayThread overlayThread;
    private ArrayList<City> mCityNames;
    private TextView LocationCityTextView;

    private  int PrivinceID;
    private int CityID;
    private String City;
    private String Province;
    //百度定位
    public LocationClient mLocationClient = null;
    private int PID;
    private int CID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citylist);
        mActivity = this;
        mCityListView = (ListView) findViewById(R.id.city_list);
        LocationCityTextView = (TextView) findViewById(R.id.location_city);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return;
                City = bdLocation.getCity();
                Province =bdLocation.getProvince();
                PID = mActivity.mCityManager.getProvinceId(Province.substring(0, Province.length() - 1));
                CID = mActivity.mCityManager.getCityId(City.substring(0, City.length() - 1));
                LocationCityTextView.setText(City);
                LocationCityTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("PrivinceID",PID);
                        intent.putExtra("CityID",CID);
                        intent.putExtra("City",City);
                        setResult(3,intent);
                        finish();
                    }
                });
            }
        });
        setLocationOption();
        mLocationClient.start();
        mLetterListView = (MyLetterListView) findViewById(R.id.cityLetterListView);
        mLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        mCityNames =mActivity.mCityManager.getCityNames();
        alphaIndexer = new HashMap<String ,Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();
        setAdapter(mCityNames);
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = (City)mCityListView.getAdapter().getItem(position);
                PrivinceID = city.getProvinceID();
                CityID = city.getCityID();
                City = city.getName()+"市";
                Intent intent = new Intent();
                intent.putExtra("PrivinceID",PrivinceID);
                intent.putExtra("CityID",CityID);
                intent.putExtra("City",City);
                setResult(2,intent);
                finish();
            }
        });

    }
    //百度定位设置相关参数
    public void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

    }

    private void setAdapter(List<City> list)
    {
        if (list != null)
        {
            adapter = new ListAdapter(this, list);
            mCityListView.setAdapter(adapter);
        }

    }

    /**
     * ListViewAdapter
     *
     * @author sy
     *
     */
    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<City> list;

        public ListAdapter(Context context, List<City> list) {

            this.inflater = LayoutInflater.from(context);
            this.list = list;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = list.get(i).getZiMu();
                // 上一个汉语拼音首字母，如果不存在为“ ”
                String previewStr = (i - 1) >= 0 ? list.get(i - 1).getZiMu() : " ";
                if (!previewStr.equals(currentStr)) {
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
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.adapter_city_list, null);
                holder = new ViewHolder();
                holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(list.get(position).getName());
            String currentStr = list.get(position).getZiMu();
            String previewStr = (position - 1) >= 0 ? list.get(position - 1).getZiMu() : " ";
            if (!previewStr.equals(currentStr)) {
                holder.alpha.setVisibility(View.VISIBLE);
                holder.alpha.setText(currentStr);
            } else {
                holder.alpha.setVisibility(View.GONE);
            }
            return convertView;

        }

        private class ViewHolder {
            TextView alpha;
            TextView name;
        }


    }

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class LetterListViewListener implements MyLetterListView.OnTouchingLetterChangedListener
    {

        @Override
        public void onTouchingLetterChanged(final String s)
        {
            if (alphaIndexer.get(s) != null)
            {
                int position = alphaIndexer.get(s);
                mCityListView.setSelection(position);
                overlay.setText(sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }

    }


    // 设置overlay不可见
    private class OverlayThread implements Runnable
    {

        @Override
        public void run()
        {
            overlay.setVisibility(View.GONE);
        }

    }
}

