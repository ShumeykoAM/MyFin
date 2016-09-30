package com.bloodliviykot.tools.DataBase;

/**
 * Created by Shumeiko on 30.09.2016.
 */
public abstract class Entity
{
  private int _id;

  //boolean select(int _id); наверное у сущности должен быть конструктор
  public abstract int insert(); //Должен вернуть 0 если не удалось вставить, иначе id вставленной записи
  public abstract boolean update();
  public abstract boolean delete(); //В случае успеха должен обнулять _id сущности
}
