package com.example.harsh.projectthree_a1;

/**
 * Created by harsh on 10/30/2017.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment implements BackKeyListener {

    OnURLSelectedListener mListener;
    ArrayAdapter<String> dataAdapter;
    ListView listView;
    int pos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ListFragment", "onCreate()");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("ListFragment", "onActivityCreated().");
        setHasOptionsMenu(true);
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        //Generate list View from ArrayList
        displayListView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("ListFragment", "onCreateView()");
        Log.v("ListContainer", container == null ? "true" : "false");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.list_view, container, false);
        if (savedInstanceState != null) {
            pos = savedInstanceState.getInt("currentPos", -1);
        }
        Log.v("ListsavedInstanceState","true" + pos);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        // User pressed back, select default item..

        //change here for selection
       listView.clearChoices();
        dataAdapter.notifyDataSetChanged();
        // In this case, prevent normal back processing or
        // you might get a double backstack pop.
        return true;
    }


    // Container Activity must implement this interface
    public interface OnURLSelectedListener {
        public void onURLSelected(String URL);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnURLSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnURLSelectedListener");
        }
    }

    private void displayListView() {

        //Array list of countries
        List<String> urlList = new ArrayList<String>();
        urlList.add("The Museum of Science and Industry");
        urlList.add("Willis Tower");
        urlList.add("Millennium Park");
        urlList.add("Cloud Gate");
        urlList.add("Navy Pier");
        urlList.add("Wrigley Field");
      final String urls[]={ "https://www.msichicago.org/",
         "http://www.willistower.com/", "https://www.cityofchicago.org/city/en/depts/dca/supp_info/millennium_park_-artarchitecture.html#cloud", "https://www.cityofchicago.org/city/en/depts/dca/supp_info/millennium_park.html", "https://navypier.org/", "http://chicago.cubs.mlb.com/chc/ballpark"};


        //create an ArrayAdaptar from the String Array
         dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.url_list, urlList);
        listView = (ListView) getView().findViewById(R.id.listofURLs);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);
        if(pos !=-1)
            listView.setSelection(pos);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onURLSelected(urls[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Send the URL to the host activity
                pos=position;

                mListener.onURLSelected(urls[position]);


            }
        });

    }
    public void deleselectlist(){
        listView.clearChoices();
        dataAdapter.notifyDataSetChanged();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPos", pos);
    }
    public int getSelectedItem(){
        return listView.getSelectedItemPosition();
    }
    public void setSelection(int i){
        pos =i;
    }

}