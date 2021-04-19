package ca.nait.zli.homebookkeeping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static SQLiteDatabase db;
    /* 初始化数据库对象*/
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  //得到帮助类对象
        db = helper.getWritableDatabase();      //得到数据库对象
    }


    public static List<TypeBean> getTypeList(int category){
        List<TypeBean> list = new ArrayList<>();
        //read table type
        String sql = "select * from type where category = "+ category;
        Cursor cursor = db.rawQuery(sql, null);
//        循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int selectedImageId = cursor.getInt(cursor.getColumnIndex("selectedImageId"));
            int categorySelected = cursor.getInt(cursor.getColumnIndex("category"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, selectedImageId, categorySelected);
            list.add(typeBean);
        }
        return list;
    }


    public static void insertItemToRecord(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypeName());
        values.put("selectedImageId",bean.getSelectedImageId());
        values.put("note",bean.getNote());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("category",bean.getCategory());
        db.insert("record",null,values);
        Log.i("DBManager.java", "insert item table successfully");
    }
    /*
    * 获取记账表当中某一天的所有支出或者收入情况
    * */
    public static List<AccountBean> getAccountListOneDayFromRecord(int year, int month, int day){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from record where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int selectedImageId = cursor.getInt(cursor.getColumnIndex("selectedImageId"));
            int category = cursor.getInt(cursor.getColumnIndex("category"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, typename, selectedImageId, note, money, time, year, month, day, category);
            list.add(accountBean);
        }
        Log.i("DBManager", "getAccountListOneDay successfully");
        return list;

    }

    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    public static List<AccountBean> getAccountListOneMonthFromRecord(int year, int month){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from record where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * 获取某一天的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int category){
        float total = 0.0f;
        String sql = "select sum(money) from record where year=? and month=? and day=? and category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", category + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
     * 获取某一月的支出或者收入的总金额   kind：支出==0    收入===1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int category){
        float total = 0.0f;
        String sql = "select sum(money) from record where year=? and month=? and category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", category + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /** 统计某月份支出或者收入情况有多少条  收入-1   支出-0*/
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from record where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    public static float getSumMoneyOneYear(int year,int category){
        float total = 0.0f;
        String sql = "select sum(money) from record where year=? and category=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", category + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
    * 根据传入的id，删除record表当中的一条数据
    * */
    public static int deleteItemFromRecordById(int id){
        int i = db.delete("record", "id=?", new String[]{id + ""});
        return i;
    }
    /**
     * 根据备注搜索收入或者支出的情况列表
     * */
    public static List<AccountBean> getAccountListByNoteFromRecord(String note){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from record where note like '%"+note+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String noteData = cursor.getString(cursor.getColumnIndex("note"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int selectedImageId = cursor.getInt(cursor.getColumnIndex("selectedImageId"));
            int category = cursor.getInt(cursor.getColumnIndex("category"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, selectedImageId, noteData, money, time, year, month, day, category);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 查询记账的表当中有几个年份信息
     * */
    public static List<Integer> getYearListFromrecord(){
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from record order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    /*
    * 删除record表格当中的所有数据
    * */
    public static void deleteAllAccount(){
        String sql = "delete from record";
        db.execSQL(sql);
    }

    /**
     * 查询指定年份和月份的收入或者支出每一种类型的总钱数
     * */
/*    public static List<ChartItemBean> getChartListFromrecord(int year, int month, int kind){
        List<ChartItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);  //求出支出或者收入总钱数
        String sql = "select typename,sImageId,sum(money)as total from record where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }*/

    /**
    * 获取这个月当中某一天收入支出最大的金额，金额是多少
     * */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from record where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    /** 根据指定月份每一日收入或者支出的总钱数的集合*/
  /*  public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year, int month, int kind){
        String sql = "select day,sum(money) from record where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }*/

}
