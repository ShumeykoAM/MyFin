package com.bloodliviykot.MyFin.DB.entities;

import android.content.ContentValues;
import android.database.Cursor;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.tools.Common.Money;
import com.bloodliviykot.tools.DataBase.Entity;

/**
 * Created by Kot on 30.10.2016.
 */
public class Document
  extends Entity
{
  private Fields f;
  private Fields original;
  
  private void init()
  {
    f = new Fields();
    original = new Fields();
  }
  
  public Transact getTransact()
  {
    return f.transact;
  }
  public Document setTransact(Transact transact) throws EntityException
  {
    if(transact != null && transact.getId() == 0)
      throw new EntityException();
    this.f.transact = transact;
    return this;
  }
  public Category getCategory()
  {
    return f.category;
  }
  public Document setCategory(Category category) throws EntityException
  {
    if(category == null || category.getId() == 0)
      throw new EntityException();
    this.f.category = category;
    return this;
  }
  public Event getEvent()
  {
    return f.event;
  }
  public Document setEvent(Event event) throws EntityException
  {
    if(event != null && event.getId() == 0)
      throw new EntityException();
    this.f.event = event;
    return this;
  }
  public Money getPrice()
  {
    return f.price;
  }
  public Document setPrice(Money price) throws EntityException
  {
    this.f.price = price != null ? price.clone() : null;
    return this;
  }
  public double getOf(){ return f.of; }
  public Document setOf(double of)
  {
    this.f.of = of;
    return this;
  }
  public Unit getOfUnit(){ return f.of_unit; }
  public Document setOfUnit( Unit of_unit) throws EntityException
  {
    if(of_unit == null)
      throw new EntityException();
    this.f.of_unit = of_unit;
    return this;
  }
    
  public double getCount()
  {
    return f.count;
  }
  public Document setCount(double count)
  {
    this.f.count = count;
    return this;
  }
  public Unit getUnit()
  {
    return f.unit;
  }
  public Document setUnit(Unit unit) throws EntityException
  {
    if(unit == null)
      throw new EntityException();
    this.f.unit = unit;
    return this;
  }

  public Document(Transact transact, Category category, Event event, Money price, Double of, Unit of_unit,
                  double count, Unit unit) throws EntityException
  {
    init();
    if(category == null || count < 0 || unit == null)
      throw new EntityException();
    setTransact(transact);
    setCategory(category);
    setEvent(event);
    setPrice(price);
    setOf(of != null ? of : 1);
    setOfUnit(of_unit != null ? of_unit : unit);
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
    if(f.transact != null)
      values.put("_id_transact", this.f.transact.getId());
    values.put  ("_id_category", this.f.category.getId());
    if(f.event != null)
      values.put("_id_event"   , this.f.event.getId());
    if(f.price != null)
      values.put("price"       , this.f.price.getLongValue());
    values.put  ("of"          , this.f.of);
    values.put  ("of_id_unit"     , this.f.of_unit.getId());
    values.put  ("count"       , this.f.count);
    values.put  ("id_unit"     , this.f.unit.getId());
    return values;
  }
  @Override
  protected ContentValues getContentValuesChange()
  {
    ContentValues values = new ContentValues();
    compareInsert(values, original.transact != null ? original.transact.getId() : null,
      f.transact != null ? f.transact.getId() : null, "_id_transact");
    compareInsert(values, original.category.getId(),f.category.getId() , "_id_category");
    compareInsert(values, original.event != null ? original.event.getId() : null,
      f.event != null ? f.event.getId() : null, "_id_event");
    compareInsert(values, original.price != null ? original.price.getLongValue() : null,
      f.price != null ? f.price.getLongValue() : null, "price");
    compareInsert(values, original.of, f.of, "of");
    compareInsert(values, original.of_unit.getId(), f.of_unit.getId(), "of_id_unit");
    compareInsert(values, original.count, f.count, "count");
    compareInsert(values, original.unit.getId(), f.unit.getId(), "id_unit");
    return values;
  }
  @Override
  protected void initFromCursor(Cursor cursor) throws EntityException
  {
    init();
    if(!cursor.isNull(cursor.getColumnIndex("_id_transact")))
      this.f.transact = Transact.getTransactFromId(cursor.getLong(cursor.getColumnIndex("_id_transact")));
    this.f.category = Category.getCategoryFromId(cursor.getLong(cursor.getColumnIndex("_id_category")));
    if(!cursor.isNull(cursor.getColumnIndex("_id_event")))
      this.f.event    = Event.getEventFromId(cursor.getLong(cursor.getColumnIndex("_id_event")));
    if(!cursor.isNull(cursor.getColumnIndex("price")))
      this.f.price      = new Money(cursor.getLong(cursor.getColumnIndex("price")));
    this.f.of     = cursor.getDouble(cursor.getColumnIndex("of"));
    this.f.of_unit = Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("of_id_unit")));
    this.f.count    = cursor.getDouble(cursor.getColumnIndex("count"));
    this.f.unit     = Unit.getUnitFromId(cursor.getLong(cursor.getColumnIndex("id_unit")));
  }
  @Override
  protected void saveOriginal() { original = f.clone(); }
  
  //Поля записи
  public static class Fields
    implements Cloneable
  {
    private Transact transact;
    private Category category;
    private Event    event   ;
    private Money    price   ; //Цена
    private double   of      ; //  за
    private Unit     of_unit ; //  ед_измер
    private double   count   ; //Количество
    private Unit     unit    ; //Ед_Измер
    
    @Override
    public Fields clone()
    {
      Fields result = new Fields();
      result.transact = transact;
      result.category = category;
      result.event    = event   ;
      result.price    = price == null ? null : price.clone() ;
      result.of       = of      ;
      result.of_unit  = of_unit ;
      result.count    = count   ;
      result.unit     = unit    ;
      return result;
    }
  }
    
}
