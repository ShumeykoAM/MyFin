package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kot on 23.09.2016.
 */
public class Planned
  extends Activity
{

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.planned);
    setTitle("Locale Date");

    ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
    ArrayList<String> children1 = new ArrayList<String>();
    ArrayList<String> children2 = new ArrayList<String>();
    children1.add("Child_1");
    children1.add("Child_2");
    children2.add("Child_3");
    children2.add("Child_4");
    groups.add(children1);
    children2.add("Child_1");
    children2.add("Child_2");
    children2.add("Child_3");
    children2.add("Child_4");
    children2.add("Child_5");
    children2.add("Child_6");
    groups.add(children2);

    ExpListAdapter adapter = new ExpListAdapter(this, groups);

    ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expListView);
    expandableListView.setAdapter(adapter);
  }

  public class ExpListAdapter extends BaseExpandableListAdapter
  {

    private ArrayList<ArrayList<String>> mGroups;
    private Context mContext;

    public ExpListAdapter (Context context,ArrayList<ArrayList<String>> groups){
      mContext = context;
      mGroups = groups;
    }

    @Override
    public int getGroupCount() {
      return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      return mGroups.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
      return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      return mGroups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_view, null);
      }

      if (isExpanded){
        //Изменяем что-нибудь, если текущая Group раскрыта
      }
      else{
        //Изменяем что-нибудь, если текущая Group скрыта
      }

      TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
      textGroup.setText("Group " + Integer.toString(groupPosition));

      return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
      if (convertView == null)
      {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.child_view, parent, false);
      }

      ArrayList<ArrayList<String>> grou = new ArrayList<ArrayList<String>>();
      ArrayList<String> ildren1 = new ArrayList<String>();
      ArrayList<String> ildren2 = new ArrayList<String>();
      ildren1.add("111");
      ildren1.add("222");
      ildren2.add("3333");
      ildren2.add("4444");
      grou.add(ildren1);
      ildren2.add("кк1");
      ildren2.add("кк2");
      ildren2.add("кк3");
      ildren2.add("кк4");
      ildren2.add("кк5");
      ildren2.add("кк6");
      grou.add(ildren2);

      ExpListAdapter2 adapt = new ExpListAdapter2(convertView.getContext(), grou);
      ExpandableListView expandable = (ExpandableListView) convertView.findViewById(R.id.expListView_ch);
      expandable.setAdapter(adapt);

      return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }
  }

  public class ExpListAdapter2 extends BaseExpandableListAdapter
  {

    private ArrayList<ArrayList<String>> mGroups;
    private Context mContext;

    public ExpListAdapter2 (Context context,ArrayList<ArrayList<String>> groups){
      mContext = context;
      mGroups = groups;
    }

    @Override
    public int getGroupCount() {
      return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      return mGroups.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
      return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      return mGroups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_view, null);
      }

      if (isExpanded){
        //Изменяем что-нибудь, если текущая Group раскрыта
      }
      else{
        //Изменяем что-нибудь, если текущая Group скрыта
      }

      TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
      textGroup.setText("Group " + Integer.toString(groupPosition));

      return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
      if (convertView == null)
      {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.child_view, parent, false);
      }

      ArrayList<ArrayList<String>> grou = new ArrayList<ArrayList<String>>();
      ArrayList<String> ildren1 = new ArrayList<String>();
      ArrayList<String> ildren2 = new ArrayList<String>();
      ildren1.add("111");
      ildren1.add("222");
      ildren2.add("3333");
      ildren2.add("4444");
      grou.add(ildren1);
      ildren2.add("кк1");
      ildren2.add("кк2");
      ildren2.add("кк3");
      ildren2.add("кк4");
      ildren2.add("кк5");
      ildren2.add("кк6");
      grou.add(ildren2);

      ExpListAdapter adapt = new ExpListAdapter(convertView.getContext(), grou);
      ExpandableListView expandable = (ExpandableListView) convertView.findViewById(R.id.expListView_ch);
      expandable.setAdapter(adapt);

      return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }
  }
}
