package com.bloodliviykot.MyFin.DB;


//Перечисление запросов хранящихся в файле res\raw\sql_querys.sql
//  Порядок запросов в этом файле должен совпадать с порядком соответствующих элементов перечисления EQ
public enum EQ
{
  //Выборка сущности по _id
  CURRENCY,
  ACCOUNT,
  CO_USER,
  TRANSACTION,
  CATEGORY,
  DOCUMENT,

  ACCOUNTS,                 //Список счетов, кроме счетов сопользователей
  USER_ACCOUNTS,            //Список счетов сопользователя
  CURRENCIES,               //Список валют
  TRANSACTIONS,             //Транзакции
  CURRENCY_COD_ISO,         //Валюта по коду ISO
  SUB_CATEGORIES,           //Категории определенного родителя
  CATEGORIES_NO_PARENT,     //Головные категории
  EXIST_SUB_CATEGORY,       //Возвращает одну запись если есть под категории или ни одной если нету
  PLANNED,                  //Запланированные документы

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
