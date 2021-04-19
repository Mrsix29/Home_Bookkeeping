package ca.nait.zli.homebookkeeping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ca.nait.zli.homebookkeeping.R;


public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context,"home_bookkeeping.db" , null, 1);
    }

//    only for first run project
    @Override
    public void onCreate(SQLiteDatabase db) {
//        create type table
        String sql = "create table type(id integer primary key autoincrement,typename varchar(20),imageId integer,selectedImageId integer,category integer)";
        db.execSQL(sql);
        insertType(db);
        //create record  table
        sql = "create table record(id integer primary key autoincrement,typename varchar(10),selectedImageId integer,note varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,category integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        String sql = "insert into type (typename,imageId,selectedImageId,category) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"Shopping", R.mipmap.ic_shopping,R.mipmap.ic_shopping_s,0});
        db.execSQL(sql,new Object[]{"Restaurant", R.mipmap.ic_restaurant,R.mipmap.ic_restaurant_s,0});
        db.execSQL(sql,new Object[]{"Car", R.mipmap.ic_car,R.mipmap.ic_car_s,0});
        db.execSQL(sql,new Object[]{"Utilities", R.mipmap.ic_utilities,R.mipmap.ic_utilities_s,0});
        db.execSQL(sql,new Object[]{"Phone", R.mipmap.ic_phone,R.mipmap.ic_phone_s,0});
        db.execSQL(sql,new Object[]{"Gas", R.mipmap.ic_gas,R.mipmap.ic_gas_s,0});
        db.execSQL(sql,new Object[]{"Education", R.mipmap.ic_education,R.mipmap.ic_education_s,0});
        db.execSQL(sql,new Object[]{"Housing", R.mipmap.ic_housing,R.mipmap.ic_housing_s,0});
        db.execSQL(sql,new Object[]{"Movie", R.mipmap.ic_movie,R.mipmap.ic_movie_s,0});
        db.execSQL(sql,new Object[]{"Maintenance", R.mipmap.ic_maintenance,R.mipmap.ic_maintenance_s,0});
        db.execSQL(sql,new Object[]{"Liquor", R.mipmap.ic_liquor,R.mipmap.ic_liquor_s,0});
        db.execSQL(sql,new Object[]{"Child Care", R.mipmap.ic_kid,R.mipmap.ic_kid_s,0});
        db.execSQL(sql,new Object[]{"Internet", R.mipmap.ic_internet,R.mipmap.ic_internet_s,0});
        db.execSQL(sql,new Object[]{"Health Care", R.mipmap.ic_health,R.mipmap.ic_health_s,0});
        db.execSQL(sql,new Object[]{"Gift", R.mipmap.ic_gift,R.mipmap.ic_gift_s,0});
        db.execSQL(sql,new Object[]{"Amazon", R.mipmap.ic_amazon,R.mipmap.ic_amazon_s,0});
        db.execSQL(sql,new Object[]{"Netflix", R.mipmap.ic_netflix,R.mipmap.ic_netflix_s,0});
        db.execSQL(sql,new Object[]{"Others", R.mipmap.ic_others_expense,R.mipmap.ic_others_fs,0});


        db.execSQL(sql,new Object[]{"Salary", R.mipmap.ic_salary,R.mipmap.ic_salary_s,1});
        db.execSQL(sql,new Object[]{"Bonus", R.mipmap.ic_bonus,R.mipmap.ic_bonus_s,1});
        db.execSQL(sql,new Object[]{"Interest", R.mipmap.ic_interest,R.mipmap.ic_interest_s,1});
        db.execSQL(sql,new Object[]{"Trade", R.mipmap.ic_trade,R.mipmap.ic_trade_s,1});
        db.execSQL(sql,new Object[]{"Rent Out", R.mipmap.ic_rent_out,R.mipmap.ic_rent_out_s,1});
        db.execSQL(sql,new Object[]{"Gift Card", R.mipmap.ic_gift_card,R.mipmap.ic_gift_card_s,1});
        db.execSQL(sql,new Object[]{"Others", R.mipmap.ic_others_expense,R.mipmap.ic_others_fs,1});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
