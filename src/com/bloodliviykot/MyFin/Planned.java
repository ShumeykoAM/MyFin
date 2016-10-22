package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Kot on 23.09.2016.
 */
public class Planned
  extends Activity
{
  private Cursor cursor;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.planned);
    ListView planned = (ListView)findViewById(R.id.planned_list_view);


  }

}
