package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.bloodliviykot.MyFin.DB.entities.Account;

/**
 * Created by Shumeiko on 26.09.2016.
 */
@SuppressLint("ValidFragment")
public class AccountsDNew
  extends DialogFragment //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
{
  public AccountsDNew()
  {
    super();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Создать счет");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(R.layout.accounts_d_new, null);
    Spinner image = (Spinner)v.findViewById(R.id.accounts_d_new_icon);
    //Зададим программно, а то в редакторе плохо выглядит
    image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT));
    ImageAdapter adapter = new ImageAdapter(GlobalWars.application_context);
    image.setAdapter(adapter);

    return v;
  }
  @Override
  public void onDestroyView()
  {
    super.onDestroyView();
  }

  public class ImageAdapter
    extends ArrayAdapter<Account.E_IC_TYPE_RESOURCE>
  {
    ImageAdapter(Context context)
    {
      super(context, R.layout.accounts_d_new_image_item, Account.E_IC_TYPE_RESOURCE.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.account_item_image);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      if(convertView == null)
        convertView = View.inflate(GlobalWars.application_context, R.layout.accounts_d_new_image_item, null);
      ImageView image = (ImageView)convertView.findViewById(R.id.account_item_image);
      image.setImageResource(Account.E_IC_TYPE_RESOURCE.getE_IC_TYPE_RESOURCE(position).R_drawable);
      image.setBackgroundColor(getResources().getColor(R.color.black));
      return convertView;
    }

  }

}
