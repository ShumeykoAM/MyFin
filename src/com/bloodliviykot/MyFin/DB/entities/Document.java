package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;

import java.io.Serializable;

/**
 * Created by Kot on 30.10.2016.
 */
public class Document
  extends Entity
  implements Serializable
{

  public Transact getTransact()
  {
    return transact;
  }
  public void setTransact(Transact transact) throws EntityException
  {
    if(transact != null && transact.getId() == 0)
      throw new EntityException();
    this.transact = transact;
  }
  public Category getCategory()
  {
    return category;
  }
  public void setCategory(Category category) throws EntityException
  {
    if(category == null || category.getId() == 0)
      throw new EntityException();
    this.category = category;
  }
  public Event getEvent()
  {
    return event;
  }
  public void setEvent(Event event) throws EntityException
  {
    if(event != null && event.getId() == 0)
      throw new EntityException();
    this.event = event;
  }
  public Money getSum()
  {
    return sum;
  }
  public void setSum(Money sum) throws EntityException
  {
    this.sum = sum;
  }
  public double getCount()
  {
    return count;
  }
  public void setCount(double count)
  {
    this.count = count;
  }
  public Unit getUnit()
  {
    return unit;
  }
  public void setUnit(Unit unit) throws EntityException
  {
    if(unit == null)
      throw new EntityException();
    this.unit = unit;
  }

  public Document(Transact transact, Category category, Event event, Money sum, double count, Unit unit) throws EntityException
  {
    setTransact(transact);
    setCategory(category);
    setEvent(event);
    setSum(sum);
    setCount(count);
    setUnit(unit);
  }
  private Document(long _id) throws EntityException
  {
    super(_id, EQ.DOCUMENT);
  }
  public static Document getDocumentFromId(long _id) throws EntityException
  {
    return new Document(_id);
  }

  @Override
  public String getTableName()
  {
    return "Document";
  }
  @Override
  protected ContentValues getContentValues() throws EntityException
  {
    ContentValues values = new ContentValues();
    if(transact != null)
      values.put("_id_transact", this.transact.getId());
    values.put  ("_id_category", this.category.getId());
    if(event != null)
      values.put("_id_event"   , this.event.getId());
    if(sum != null)
      values.put("sum"         , this.sum.getLongValue());
    values.put  ("count"       , this.count);
    values.put  ("id_unit"     , this.getId());
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.transact != null ? original.transact.getId() : null,
      transact != null ? transact.getId() : null, "_id_transact");
    compareInsert(values, original.category.getId(),category.getId() , "_id_category");
    compareInsert(values, original.event != null ? original.event.getId() : null,
      event != null ? event.getId() : null, "_id_event");
    compareInsert(values, original.sum != null ? original.sum.getLongValue() : null,
      sum != null ? sum.getLongValue() : null, "sum");
    compareInsert(values, original.count, count, "count");
    compareInsert(values, original.unit.getId(), unit.getId(), "id_unit");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    if(!cursor.isNull(cursor.getColumnIndex("_id_transact")))
      this.transact = Transact.getTransactFromId(cursor.getLong(cursor.getColumnIndex("_id_transact")));
    this.category = Category.getCategoryFromId(cursor.getLong(cursor.getColumnIndex("_id_category")));
    if(!cursor.isNull(cursor.getColumnIndex("_id_event")))
      this.event    = Event.getEventFromId(cursor.getLong(cursor.getColumnIndex("_id_event")));
    if(!cursor.isNull(cursor.getColumnIndex("sum")))
      this.sum      = new Money(cursor.getLong(cursor.getColumnIndex("sum")));
    this.count    = cursor.getDouble(cursor.getColumnIndex("count"));
    this.unit     = Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("count")));
  }
  @Override
  protected void saveOriginal()
  {
    if(original == null)
      original = new Original();
    original.transact = transact;
    original.category = category;
    original.event    = event   ;
    original.sum      = sum     ;
    original.count    = count   ;
    original.unit     = unit    ;
  }

  //Поля записи
  private Transact transact;
  private Category category;
  private Event    event   ;
  private Money    sum     ;
  private double   count   ;
  private Unit     unit    ;

  //Оригинальные (как были втавлены\извлечены из базы)
  private static class Original
  {
    Transact transact;
    Category category;
    Event    event   ;
    Money    sum     ;
    double   count   ;
    Unit     unit    ;
  }
  Original original;

}
