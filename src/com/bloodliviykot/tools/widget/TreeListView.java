package com.bloodliviykot.tools.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;
import java.util.Map;

//Многоуровневое дерево
public class TreeListView
  extends ListView
{
  public TreeListView(Context context)
  {
    super(context);
  }
  public TreeListView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }
  public TreeListView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
  }

  @Override
  public void setAdapter(ListAdapter adapter)
  {
    TreeListAdapter tree_adapter = (TreeListAdapter)adapter; //Должно приводиться
    super.setAdapter(adapter);
  }

  //Нужно реализовать адаптер для многоуровневого дерева
  public static abstract class TreeListAdapter
    extends SimpleCursorAdapter
  {
    public TreeListAdapter(Context context, int layout, TreeCursor c, String[] from, int[] to)
    {
      super(context, layout, c, from, to);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      bindView(view, context, (TreeCursor)cursor);
    }
    public abstract void bindView(View view, Context context, TreeCursor cursor);
  }

  //Курсор по БД для многоуровневого дерева
  public static class TreeCursor
    implements Cursor
  {
    //Внутренний вспомогательный класс
    private class Node
    {
      public class NodeException
        extends Throwable
      {     }
      private Long _id;
      private Cursor cursor;
      private int deep;
      private boolean is_last_inGroup, is_expandable, is_expanded;
      private Map<Long, Node> sub_nodes = new HashMap<Long, Node>();
      public Node(Long _id, Cursor cursor, int deep, boolean is_last_inGroup, boolean is_expandable)
      {
        this._id = _id;
        this.cursor = cursor;
        this.deep = deep;
        this.is_last_inGroup = is_last_inGroup;
        this.is_expandable = is_expandable;
        this.is_expanded = false;
        if(_id == null)
          fillSubNodes();
      }
      public boolean change_expand() //Возвращает true если состояние изменилось
      {
        if(_id == null)
          return false;
        if(cursor == null)
        {
          if(is_expandable)
          {
            cursor = db.rawQuery(sql_query_child, new String[]{_id.toString()});
            if(cursor.getCount() != 0)
            {
              fillSubNodes();
              is_expanded = true;
              return true;
            } else
            {
              cursor = null;
              return false;
            }
          }
          else
            return false;
        }
        else
        {
          cursor = null;
          is_expanded = false;
          return true;
        }
      }
      private void fillSubNodes()
      {
        sub_nodes.clear();
        Node last_node_in_group = null;
        for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
        {
          Long id = cursor.getLong(cursor.getColumnIndex("_id"));
          sub_nodes.put(id, last_node_in_group = new Node(id, null, deep + 1, false,
            (db.rawQuery(sql_query_exist_child, new String[]{id.toString()}).getCount() != 0)));
        }
        if(last_node_in_group != null)
          last_node_in_group.is_last_inGroup = true;
      }
      public int getCountExpanded()
      {
        int count = 0;
        if(cursor != null)
        {
          count = cursor.getCount();
          for(Map.Entry<Long, Node> entry : sub_nodes.entrySet())
            count += entry.getValue().getCountExpanded();
        }
        return count;
      }
      public Pair<Node, Cursor> getSubNode(int position) throws NodeException
      {
        Pair<Node, Cursor> result = null;
        int count_expanded = getCountExpanded();
        if(position > count_expanded-1)
          throw new NodeException();
        for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
        {
          result = new Pair<Node, Cursor>(sub_nodes.get(cursor.getLong(cursor.getColumnIndex("_id"))), cursor) ;
          if(position == 0)
            break;
          count_expanded = result.first.getCountExpanded();
          if(position <= count_expanded)
          {
            result = result.first.getSubNode(position-1);
            break;
          }
          position = position - 1 - count_expanded;
        }

        return result;
      }
      public int getDeep()
      {
        return deep;
      }
      public boolean isLastInGroup()
      {
        return is_last_inGroup;
      }
      public boolean isExpandable()
      {
        return is_expandable;
      }
      public boolean isExpanded()
      {
        return is_expanded;
      }
    }

    private SQLiteDatabase db;
    private String sql_query_head;
    private String head_args[];
    private String sql_query_child;
    private String sql_query_exist_child;

    private Node head_node;
    private int position;
    private Pair<Node, Cursor> current;

    //Конструктор курсора
    public TreeCursor(SQLiteDatabase db,           //БД
                      String sql_query_head,       //строка запроса для выборки головных элементов дерева, должна содержать поле _id
                      String head_args[],          //  параметры, если надо, для запроса sql_query_head
                      String sql_query_child,      //строка запроса для выборки под элементов, должна содержать поля _id и _pid, и условие _pid=?
                      String sql_query_exist_child)//строка очень быстрого запроса, который возвращает хотя бы одну строку с под элементом, должна содержать условие _pid=?
    {
      if(db == null || sql_query_head == null || sql_query_child == null || sql_query_exist_child == null)
        throw new NullPointerException();
      this.db                    = db                   ;
      this.sql_query_head        = sql_query_head       ;
      this.head_args             = head_args            ;
      this.sql_query_child       = sql_query_child      ;
      this.sql_query_exist_child = sql_query_exist_child;
      head_node = new Node(null, db.rawQuery(sql_query_head, head_args), -1, true, true);
    }
    public boolean changeExpand(int position) //Возвращает true если состояние изменилось
    {
      boolean change_expand = false;
      Node subnode_node;
      try
      {
        if( (subnode_node = head_node.getSubNode(position).first) != null)
          change_expand = subnode_node.change_expand();
      } catch(Node.NodeException e)
      {
        e.printStackTrace();
      }
      return change_expand;
    }
    public int getDeep()
    {
      return current.first.getDeep();
    }
    public boolean isLastInGroup()
    {
      return current.first.isLastInGroup();
    }
    public boolean isExpandable()
    {
      return current.first.isExpandable();
    }
    public boolean isExpanded()
    {
      return current.first.isExpanded();
    }

    @Override
    public int getCount()
    {
      return head_node.getCountExpanded();
    }
    @Override
    public int getPosition()
    {
      return position;
    }
    @Override
    public boolean move(int offset)
    {
      return head_node.cursor.move(offset);
    }
    @Override
    public boolean moveToPosition(int position)
    {
      try
      {
        current = head_node.getSubNode(this.position = position);
        return true;
      } catch(Node.NodeException e)
      {
        return false;
      }
    }
    @Override
    public boolean moveToFirst()
    {
      return head_node.cursor.moveToFirst();
    }
    @Override
    public boolean moveToLast()
    {
      return head_node.cursor.moveToLast();
    }
    @Override
    public boolean moveToNext()
    {
      return head_node.cursor.moveToNext();
    }
    @Override
    public boolean moveToPrevious()
    {
      return head_node.cursor.moveToPrevious();
    }
    @Override
    public boolean isFirst()
    {
      return head_node.cursor.isFirst();
    }
    @Override
    public boolean isLast()
    {
      return head_node.cursor.isLast();
    }
    @Override
    public boolean isBeforeFirst()
    {
      return head_node.cursor.isBeforeFirst();
    }
    @Override
    public boolean isAfterLast()
    {
      return head_node.cursor.isAfterLast();
    }
    @Override
    public int getColumnIndex(String columnName)
    {
      return current.second.getColumnIndex(columnName);
    }
    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException
    {
      return head_node.cursor.getColumnIndexOrThrow(columnName);
    }
    @Override
    public String getColumnName(int columnIndex)
    {
      return head_node.cursor.getColumnName(columnIndex);
    }
    @Override
    public String[] getColumnNames()
    {
      return head_node.cursor.getColumnNames();
    }
    @Override
    public int getColumnCount()
    {
      return head_node.cursor.getColumnCount();
    }
    @Override
    public byte[] getBlob(int columnIndex)
    {
      return current.second.getBlob(columnIndex);
    }
    @Override
    public String getString(int columnIndex)
    {
      return current.second.getString(columnIndex);
    }
    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer)
    {
      current.second.copyStringToBuffer(columnIndex, buffer);
    }
    @Override
    public short getShort(int columnIndex)
    {
      return current.second.getShort(columnIndex);
    }
    @Override
    public int getInt(int columnIndex)
    {
      return current.second.getInt(columnIndex);
    }
    @Override
    public long getLong(int columnIndex)
    {
      return current.second.getLong(columnIndex);
    }
    @Override
    public float getFloat(int columnIndex)
    {
      return current.second.getFloat(columnIndex);
    }
    @Override
    public double getDouble(int columnIndex)
    {
      return current.second.getDouble(columnIndex);
    }
    @Override
    public int getType(int columnIndex)
    {
      return current.second.getType(columnIndex);
    }
    @Override
    public boolean isNull(int columnIndex)
    {
      return current.second.isNull(columnIndex);
    }
    @Override
    public void deactivate()
    {
      head_node.cursor.deactivate();
    }
    @Override
    public boolean requery()
    {
      return head_node.cursor.requery();
    }
    @Override
    public void close()
    {
      head_node.cursor.close();
    }
    @Override
    public boolean isClosed()
    {
      return head_node.cursor.isClosed();
    }
    @Override
    public void registerContentObserver(ContentObserver observer)
    {
      head_node.cursor.registerContentObserver(observer);
    }
    @Override
    public void unregisterContentObserver(ContentObserver observer)
    {
      head_node.cursor.unregisterContentObserver(observer);
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
      head_node.cursor.registerDataSetObserver(observer);
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {
      head_node.cursor.unregisterDataSetObserver(observer);
    }
    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri)
    {
      head_node.cursor.setNotificationUri(cr, uri);
    }
    @Override
    public boolean getWantsAllOnMoveCalls()
    {
      return head_node.cursor.getWantsAllOnMoveCalls();
    }
    @Override
    public Bundle getExtras()
    {
      return head_node.cursor.getExtras();
    }
    @Override
    public Bundle respond(Bundle extras)
    {
      return head_node.cursor.respond(extras);
    }
  }

}
