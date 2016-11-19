package com.bloodliviykot.MyFin.DB.entities;

import android.database.Cursor;
import android.database.MatrixCursor;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 30.10.2016.
 */
//Объекты данного класса не хранятся в БД
public class Unit
{
  public long getId()
  {
    return _id;
  }
  public String getName()
  {
    return name;
  }
  public double getMultiplier()
  {
    return multiplier;
  }
  public long getIdGroup()
  {
    return _id_group;
  }

  private long _id;
  private String name;
  private double multiplier;
  private long _id_group; //группа например г-граммы в которую войдут Кг, г

  private static final String[] columns = new String[] { "_id", "name", "multiplier", "_id_group" };
  private static MatrixCursor cursor;

  public static Unit getUnitFromId(long _id) throws Entity.EntityException
  {
    return new Unit(_id);
  }
  private Unit(long _id) throws Entity.EntityException
  {
    //Ну как бы position всегда равно _id-1 в курсоре cursor
    if(cursor.moveToPosition((int)_id-1))
    {
      this._id        = cursor.getLong  (cursor.getColumnIndex("_id"));
      this.name       = cursor.getString(cursor.getColumnIndex("name"));
      this.multiplier = cursor.getDouble(cursor.getColumnIndex("multiplier"));
      this._id_group  = cursor.getLong  (cursor.getColumnIndex("_id_group"));
    }
    else
      throw new Entity.EntityException();
  }
  private Unit(int _id, String name, double multiplier, long _id_group)
  {
    this._id = _id;
    this.name = name;
    this.multiplier = multiplier;
    if(_id_group == 0)
      this._id_group = this._id;
    else
      this._id_group = _id_group;
  }
  public static Cursor getCursor() //Получить курсор по всем единицам измерения
  {
    MatrixCursor copy_cursor = new MatrixCursor(columns);
    for(boolean status=cursor.moveToFirst(); status; status = cursor.moveToNext())
    {
      Unit unit = null;
      try
      {
        unit = new Unit(cursor.getLong(cursor.getColumnIndex("_id")));
      } catch(Entity.EntityException e)
      {   }
      copy_cursor.addRow(new Object[]{unit._id, unit.name, unit.multiplier, unit._id_group});
    }
    return copy_cursor;
  }
  public static Cursor cursorForGroup(long _id_group) //Получить курсор по единицам измерения определенной группы
  {
    MatrixCursor g_cursor = new MatrixCursor(columns);
    for(boolean status=cursor.moveToFirst(); status; status = cursor.moveToNext())
    {
      Unit unit = null;
      try
      {
        unit = new Unit(cursor.getLong(cursor.getColumnIndex("_id")));
      } catch(Entity.EntityException e)
      {    }
      if(unit._id_group == _id_group)
        g_cursor.addRow(new Object[]{unit._id, unit.name, unit.multiplier, _id_group});
    }
    return g_cursor;
  }
  static
  {
    //Фиксированный набор единиц измерения

    cursor = new MatrixCursor(columns);

    long id = 1/*0 не используем*/, g_id;
    //штуки
    g_id = id;
    cursor.addRow(new Object[]{id++, "шт.", 1, g_id});
    cursor.addRow(new Object[]{id++, "дес.", 10, g_id}); //десяток //10шт в дес
    //граммы
    g_id = id;
    cursor.addRow(new Object[]{id++, "г", 0.001, g_id}); //1000г в Кг
    cursor.addRow(new Object[]{id++, "Кг", 1, g_id});
    //литры
    g_id = id;
    cursor.addRow(new Object[]{id++, "л", 1, g_id});
    cursor.addRow(new Object[]{id++, "мл", 0.001, g_id}); //1000мл в л
    //метры
    g_id = id;
    cursor.addRow(new Object[]{id++, "м", 1, g_id});
    cursor.addRow(new Object[]{id++, "см", 0.01, g_id}); //100см в м
    //Киловаты в час
    g_id = id;
    cursor.addRow(new Object[]{id++, "КВт.ч", 1, g_id});
    //кубометры
    g_id = id;
    cursor.addRow(new Object[]{id++, "куб.м", 1, g_id});
    //тонны
    g_id = id;
    cursor.addRow(new Object[]{id++, "тонн", 1, g_id});
    //Нету единиц измерения (развлечения например не в чем измерять)
    g_id = id;
    cursor.addRow(new Object[]{id++, "", 1, g_id});
  }

}
