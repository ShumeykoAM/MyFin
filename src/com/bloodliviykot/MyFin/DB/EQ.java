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
  ROOT_CATEGORIES,          //Все корневые категории
  SUB_CATEGORIES,           //Подкатегории указанного родителя, только перове колено
  ALL_SUB_CATEGORIES,       //Все подкатегории указанного родителя, включая под под и т.д.
  EXIST_SUB_CATEGORY,       //Возвращает одну строку если есть подкатегории у указанной категории иначе не возвращает ни одной строки
  PLANNED,                  //Запланированные документы
  ALL_CATEGORIES_LIKE,      //Все категории c указанным трендом и указанной подстрокй
  SUB_CATEGORIES_LIKE,      //Все подкатегории указанного родителя c указанным трендом и указанной подстрокй, включая под под и т.д.
  ALL_PARENTS,              //Все родительские категории указанной категории начиная с корневой

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
