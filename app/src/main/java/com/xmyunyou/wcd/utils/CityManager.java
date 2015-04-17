package com.xmyunyou.wcd.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.City;
import com.xmyunyou.wcd.model.Province;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95 on 2015/3/19.
 */
public class CityManager {

    public static final int BUFFER_SIZE = 1024 * 10;
    private static final String PATH = Environment.getExternalStorageDirectory() + "/wanchezhijia";
    private static final String FILE_NAME = "wanchezhijia.db";

    public static final String PRROVINCE = "Province";
    public static final String CITY = "City";
    public static final String DISTRICT = "District";


    private static SQLiteDatabase mSQLiteDataBase;

    public void readDataBase(Context context) {
        File dbFile = null;
        //raw目录下db文件拷贝的sd卡目录
        try {
            String filePath = PATH + "/" + FILE_NAME;
            File dir = new File(PATH);
            //判断文件夹是否存在，不存在就新建一个
            if (!dir.exists()) {
                dir.mkdirs();
            }
            dbFile = new File(filePath);
            if (dbFile.exists()) {
                dbFile.delete();
            }
            FileOutputStream os = new FileOutputStream(filePath);//得到数据库文件的写入流
            InputStream is = context.getResources().openRawResource(R.raw.wanchezhijia);//得到数据库文件的数据流
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
            os.flush();
            os.close();
            is.close();
            Globals.log("copy success...");


            InputStream readIS = context.getResources().openRawResource(R.raw.wanchezhijia); //欲导入的数据库
            FileOutputStream fos = new FileOutputStream(dbFile);
            byte[] bufferLen = new byte[BUFFER_SIZE];
            int countLen = 0;
            while ((countLen = readIS.read(bufferLen)) > 0) {
                fos.write(bufferLen, 0, countLen);
            }
            is.close();
            fos.close();
            mSQLiteDataBase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Province> getProvivceList() {
        List<Province> plist = new ArrayList<>();
        Cursor cursor = mSQLiteDataBase.query("Province", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int pid = cursor.getColumnIndexOrThrow("ProvinceID");
            int name = cursor.getColumnIndexOrThrow("Name");

            while (cursor.moveToNext()) {
                Province p = new Province();
                p.setProvinceID(cursor.getInt(pid));
                p.setName(cursor.getString(name));

                plist.add(p);
            }

            cursor.close();
        }
        return plist;
    }


    /**
     * 从数据库获取城市数据
     *
     * @return
     */
    public ArrayList<City> getCityNames(){
        ArrayList<City> names = new ArrayList<City>();
        Cursor cursor = mSQLiteDataBase.rawQuery("SELECT * FROM City ORDER BY ZiMu", null);
        for (int i = 0; i < cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            City cityModel = new City();
            cityModel.setName(cursor.getString(cursor.getColumnIndex("Name")));
            cityModel.setZiMu(cursor.getString(cursor.getColumnIndex("ZiMu")));
            cityModel.setProvinceID(cursor.getInt(cursor.getColumnIndex("ProvinceID")));
            cityModel.setCityID(cursor.getInt(cursor.getColumnIndex("CityID")));
            names.add(cityModel);
        }
        return names;
    }

    /**
     * 查询名称
     *
     * @param tableName 表名
     *                  查询省份：tablename=Province,
     *                  查询城市：tableName = City
     *                  查询区 ： tableName = District
     * @param colName   字段名
     *                  相对于的字段名
     *                  省份：ProvinceID
     *                  城市：CityID
     *                  区：DistrictID
     * @param id        id值
     * @return
     */
    public String getCityName(String tableName, String colName, int id) {
        String name = "";
        String sql = "select Name from " + tableName + " where " + colName + " = " + id;
        Cursor cursor = mSQLiteDataBase.rawQuery(sql, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow("Name");
            cursor.moveToNext();
            name = cursor.getString(columnIndex);
        }
        return name;
    }

    /*public String getCityName(int cityID){
        String CityName = null;
        String sql = "select * from City where name like '%"+ cityID+"%";
        Cursor cursor = mSQLiteDataBase.rawQuery(sql,null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow("Name");
            cursor.moveToNext();
            CityName = cursor.getString(columnIndex);
        }
        return CityName;
    }*/


    public int getProvinceId(String params) {
        int ID = 0;
        String sql = "select * from Province where name like '%" + params + "%'";
        Cursor cursor = mSQLiteDataBase.rawQuery(sql,null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow("ProvinceID");
            cursor.moveToNext();
            ID = cursor.getInt(columnIndex);
        }
        return ID;
    }

    public int getCityId(String params){
        int ID = 0;
        String sql = "select * from City where name like '%" + params + "%'";
        Cursor cursor = mSQLiteDataBase.rawQuery(sql,null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow("CityID");
            cursor.moveToNext();
            ID = cursor.getInt(columnIndex);
        }

        return ID;
    }




}
