package com.bloodliviykot.tools.DataBase;


import android.database.sqlite.SQLiteDatabase;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

public class SQLTransaction
{
  private I_Transaction trn_func_execute;
  public SQLTransaction(I_Transaction _trn_func_execute)
  {
    trn_func_execute = _trn_func_execute;
  }
  public boolean runTransaction()
  {
    SQLiteDatabase db = MySQLiteOpenHelper.getMySQLiteOpenHelper().db;
    boolean result = trn_func_execute != null;
    try
    {
      //Запускаем транзакцию
      db.beginTransaction();
      //Запускаем пользовательский обработчик транзакции
      if(result)
        result = trn_func_execute.trnFunc();
      if(result)
      {
        //Завершаем транзакцию
        //Помечаем что транзакцию нужно успешно завершить
        db.setTransactionSuccessful();
      }
      else
      {
        //Откатываем транзакцию
        //Не помечаем что транзакцию нужно успешно завершить
        //db.setTransactionSuccessful();
      }
    }
    finally
    {
      //Завершаем (применяем или откатываем см. выше) транзакцию
      db.endTransaction();
    }
    return result;
  }
}
