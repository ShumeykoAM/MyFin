package com.bloodliviykot.MyFin;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class Main
  extends TabActivity
{
  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    TabHost tab_host = getTabHost();

    TabHost.TabSpec tabSpec;

    tabSpec = tab_host.newTabSpec("tag_accounts");
    tabSpec.setIndicator(getString(R.string.tab_accounts));
    tabSpec.setContent(new Intent(this, Accounts.class));
    tab_host.addTab(tabSpec);

    tabSpec = tab_host.newTabSpec("tag_register");
    tabSpec.setIndicator(getString(R.string.tab_register));
    tabSpec.setContent(new Intent(this, Register.class));
    tab_host.addTab(tabSpec);


  }
}
