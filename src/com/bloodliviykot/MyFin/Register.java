package com.bloodliviykot.MyFin;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by Kot on 23.09.2016.
 */
public class Register
  extends TabActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register);
    TabHost tab_host = getTabHost();

    TabHost.TabSpec tabSpec;

    tabSpec = tab_host.newTabSpec("tag_transactions");
    tabSpec.setIndicator(getString(R.string.tab_transactions));
    tabSpec.setContent(new Intent(this, TransactionsPayed.class));
    tab_host.addTab(tabSpec);

    tabSpec = tab_host.newTabSpec("tag_planned");
    tabSpec.setIndicator(getString(R.string.tab_planned));
    tabSpec.setContent(new Intent(this, Planned.class));
    tab_host.addTab(tabSpec);

    tabSpec = tab_host.newTabSpec("tag_planned");
    tabSpec.setIndicator(getString(R.string.tab_regular));
    tabSpec.setContent(new Intent(this, Regular.class));
    tab_host.addTab(tabSpec);
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
    //Задаем текущую активную вкладку
    TabHost tab_host = getTabHost();
    tab_host.setCurrentTabByTag("tag_transactions");
  }
}