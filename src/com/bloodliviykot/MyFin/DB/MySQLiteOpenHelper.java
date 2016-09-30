package com.bloodliviykot.MyFin.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import com.bloodliviykot.MyFin.DB.entities.Currency;
import com.bloodliviykot.MyFin.GlobalWars;
import com.bloodliviykot.MyFin.R;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.DataBase.SQLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class MySQLiteOpenHelper
  extends SQLiteOpenHelper
{
  public static final int VersionDB = 1; //Версия БД
  public static final String DBName = "Client.db";

  public final SQLiteDatabase db;

  private SQLReader sql_reader;
  private static String requests[] = null;
  private static MySQLiteOpenHelper mySQLiteOpenHelper = null;
  private static boolean distributive = false;

  synchronized
  public static MySQLiteOpenHelper getMySQLiteOpenHelper()
  {
    if(mySQLiteOpenHelper == null)
    {
      mySQLiteOpenHelper = new MySQLiteOpenHelper();
      if(distributive)
        mySQLiteOpenHelper.fillDistr(mySQLiteOpenHelper.db);
    }
    return mySQLiteOpenHelper;
  }
  private MySQLiteOpenHelper()
  {
    //Здесь создается или открывается БД
    super(GlobalWars.application_context, DBName, null, VersionDB);
    sql_reader = new SQLReader(GlobalWars.application_context.getResources());
    db = getWritableDatabase();
  }
  //Создаем таблицы базы
  @Override
  public void onCreate(SQLiteDatabase db)
  {
    //Здесь создаются таблицы базы
    List<String> queris = sql_reader.getQueris(R.raw.v_001_tables);
    for(String query : queris)
      try
      {
        db.execSQL(query);
      }
      catch(SQLException e)
      {
        String err = e.getMessage();
        err = "";
      }
    distributive = true;
  }
  //Дистрибутивное наполнение таблиц базы
  private static final String SKIP_TAG = "distrib";
  private static final String USE_PARENT_ID = "ИД_родителя";
  private void fillDistr(SQLiteDatabase db)
  {
    try
    {
      //Заполним дистрибутивное содержание данных в БД из distrib_db.xml
      Stack<Long> stack_id = new Stack<Long>();
      ContentValues values = new ContentValues();
      long _id = 0;
      XmlPullParser xpp = GlobalWars.application_context.getResources().getXml(R.xml.distrib_db);
      while (xpp.getEventType() != XmlPullParser.END_DOCUMENT)
      {
        switch (xpp.getEventType())
        {
          // начало документа
          case XmlPullParser.START_DOCUMENT:
            break;
          // начало тэга
          case XmlPullParser.START_TAG:
            stack_id.push(_id);
            String  table_name = xpp.getName();
            if(table_name.compareTo(SKIP_TAG) == 0 )
              break;
            values.clear();
            for(int i = 0; i < xpp.getAttributeCount(); i++)
            {
              String attr_name = xpp.getAttributeName(i);
              String attr_value = xpp.getAttributeValue(i);
              if(attr_value.compareTo(USE_PARENT_ID) == 0)
              {
                if(_id == 0)
                  continue; //Отсекаем попытку вставить элемент верхнего уровня как дочерний
                attr_value = Long.toString(_id);
              }
              values.put(attr_name, attr_value);
            }
            _id = db.insert(table_name, null, values);
            break;
          // конец тэга
          case XmlPullParser.END_TAG:
            _id = stack_id.pop();
            break;
          // содержимое тэга
          case XmlPullParser.TEXT:
            //xpp.getText();
            break;

          default:
            break;
        }
        // следующий элемент
        xpp.next();
      }
    }
    catch(SQLException e)
    {
      String err = e.getMessage();
    }
    catch(XmlPullParserException e)
    {
      e.printStackTrace();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    //Добавим в справочник валют валюту для текущей локализации, если она уже не добавилась из distrib_db.xml
    java.util.Currency util_currency = java.util.Currency.getInstance(Locale.getDefault());
    Entity currency = (Entity)new Currency(util_currency.getCurrencyCode(), null);
    currency.insert();

//!!!! удалить
try
{
  Currency c = new Currency(2);
  c.getId();
}
catch(Entity.EntityException ee)
{
  int fdfdf=0;
  fdfdf++;
}

  }

  //Обновляем таблицы базы
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    //Здесь модифицируются таблицы базы

  }

  //Текст запросов из sql_querys.sql-------------------------------------------------------------------------
  public String getQuery(EQ eq)
  {
    if(requests == null)
    {
      requests = new String[EQ.EQ_COUNT.value];
      List<String> queris = sql_reader.getQueris(R.raw.sql_querys);
      int i = 0;
      for(String query : queris)
        requests[i++] = query;
    }
    return requests[eq.value];
  }

  //Исключительно для отладки -------------------------------------------------------------------------------
  public static void debugDeleteDB(Context context)
  {
    //Базы лежат тут
    //  /data/data/com.BloodliviyKot.OurBudget/databases/OurBudget.db
    //  можно посмотреть через Tools/android/ADM

    //Удалим базу для отладки !!!!!!!!!!!!!!!!!!!
    String dbfile_path = Environment.getDataDirectory().toString() +
      File.separator + "data" + File.separator +
      context.getPackageName() + File.separator + "databases";
    File MyDB1 = new File( dbfile_path + File.separator + DBName );
    MyDB1.delete();
    //Удалим базу для отладки ^^^^^^^^^^^^^^^^^^^
  }


}