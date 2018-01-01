package com.bloodliviykot.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Kot on 12.11.2016.
 */
public class ButtonID
  extends Button
{
  private Long _id;
  public ButtonID(Context context)
  {
    super(context);
  }
  public ButtonID(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }
  public ButtonID(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
  }
  public void setID(Long _id)
  {
    this._id = _id;
  }
  public Long getID()
  {
    return _id;
  }
}