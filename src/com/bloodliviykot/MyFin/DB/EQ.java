package com.bloodliviykot.MyFin.DB;


//Перечисление запросов хранящихся в файле res\raw\sql_querys.sql
//  Порядок запросов в этом файле должен совпадать с порядком соответствующих элементов перечисления EQ
public enum EQ
{
  //Выборка сущьности по _id
  CURRENCY,
  ACCOUNT,
  CO_USER,

  ACCOUNTS,                 //Список счетов, кроме счетов сопользователей
  USER_ACCOUNTS,            //Список счетов сопользователя

  EQ_COUNT;
  public final int value;
  private EQ()
  {
    this.value = Index_i.i++;
  }

  private static class Index_i
  {
    public static int i = 0;
  }
}
