package com.bloodliviykot.tools.DataBase;


//Перечисление запросов хранящихся в файле res\raw\sql_querys.sql
//  Порядок запросов в этом файле должен совпадать с порядком соответствующих элементов перечисления EQ
public enum EQ
{
  ACCOUNTS,                 //Получить список счетов

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
