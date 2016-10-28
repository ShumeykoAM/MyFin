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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;
import java.util.Map;


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
    throw new UnsupportedOperationException();
  }
  public void setAdapter(TreeListAdapter adapter)
  {
    super.setAdapter(adapter);
  }





  public static abstract class TreeListAdapter
    extends SimpleCursorAdapter
  {
    public TreeListAdapter(Context context, int layout, TreeCursor c, String[] from, int[] to)
    {
      super(context, layout, c, from, to);
    }

  }

  public static class TreeCursor
    implements Cursor
  {
    class Node
    {
      public class NodeException
        extends Throwable
      {     }
      private Long _id;
      private Cursor cursor;
      private Map<Long, Node> sub_nodes = new HashMap<Long, Node>();
      public Node(Long _id, Cursor cursor)
      {
        this._id = _id;
        this.cursor = cursor;
        if(_id == null)
          for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
          {
            long id = cursor.getLong(cursor.getColumnIndex("_id"));
            sub_nodes.put(id, new Node(id, null));
          }
      }
      boolean change_expand() //Возвращает true если состояние изменилось
      {
        if(_id == null)
          return false;
        if(cursor == null)
        {
          cursor = db.rawQuery(sql_query_child, new String[]{_id.toString()});
          if(cursor.getCount() != 0)
          {
            for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
            {
              long _id = cursor.getLong(cursor.getColumnIndex("_id"));
              sub_nodes.put(_id, new Node(_id, null));
            }
            return true;
          }
          else
          {
            sub_nodes.clear();
            cursor = null;
            return false;
          }
        }
        else
        {
          cursor = null;
          return true;
        }
      }
      int getCountExpanded()
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
      Pair<Node, Cursor> getSubNode(int position) throws NodeException
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
    }

    private SQLiteDatabase db;
    private String sql_query_head;
    private String sql_query_child;
    private String sql_query_exist_child;

    private Node head_node;
    private int position;
    private Pair<Node, Cursor> current;

    public TreeCursor(SQLiteDatabase db, String sql_query_head, String sql_query_child, String sql_query_exist_child)
    {
      this.db                    = db                   ;
      this.sql_query_head        = sql_query_head       ;
      this.sql_query_child       = sql_query_child      ;
      this.sql_query_exist_child = sql_query_exist_child;

      head_node = new Node(null, db.rawQuery(sql_query_head, null));
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

    @Override
    public int getCount()
    {
      return head_node.getCountExpanded();
    }
    @Override
    public int getPosition()
    {
      return head_node.cursor.getPosition();
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
