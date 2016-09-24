package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.bloodliviykot.tools.DataBase.MySQLiteOpenHelper;

/**
 * Created by Kot on 23.09.2016.
 */
public class Accounts
  extends Activity
{
  private MySQLiteOpenHelper oh;
  private ListView list_accounts;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.accounts);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    list_accounts = (ListView)findViewById(R.id.accounts_listView);
    //list_accounts.setAdapter();

  }
  @Override
  protected void onPause()
  {
    super.onPause();

  }
  @Override
  protected void onResume()
  {
    super.onResume();
  }

}