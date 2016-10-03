package com.bloodliviykot.MyFin;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

public class Main
  extends TabActivity
{

  private static boolean IS_DEBUG = true;
  private static boolean first_run = true;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    //Для отладки удалим базу
    if(IS_DEBUG && first_run)
      MySQLiteOpenHelper.debugDeleteDB(getApplicationContext());
    first_run = false;

    setGlobalVars();

    TabHost tab_host = getTabHost();
    TabHost.TabSpec tabSpec;

    tabSpec = tab_host.newTabSpec("tag_accounts");
    tabSpec.setIndicator(getString(R.string.tab_accounts), getResources().getDrawable(R.drawable.ic_accounts));
    tabSpec.setContent(new Intent(this, Accounts.class));
    tab_host.addTab(tabSpec);

    tabSpec = tab_host.newTabSpec("tag_register");
    tabSpec.setIndicator(getString(R.string.tab_register));
    tabSpec.setContent(new Intent(this, Register.class));
    tab_host.addTab(tabSpec);


  }
  private void setGlobalVars()
  {
    Common.application_context = getApplicationContext();

  }
}
