package com.example.harsh.projectthree_a1;

/**
 * Created by harsh on 10/30/2017.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class DetailFragment extends Fragment {



    WebView myWebView;
    String mURL = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("DetailFragment", "onCreate()");

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("DetailFragment", "onActivityCreated()");
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mURL = savedInstanceState.getString("currentURL", "");
        }
        Log.v("DetailFragment", "onActivityCreated()"+" "+mURL);
        if(mURL!=null)
        if(!mURL.trim().equalsIgnoreCase("")){
          WebView myWebView = (WebView) getView().findViewById(R.id.pageInfo);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new WebViewClient());
            myWebView.loadUrl(mURL.trim());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentURL", mURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("DetailFragment", "onCreateView()");
        View view = inflater.inflate(R.layout.list_webview, container, false);

        return view;
    }

    public void setURLContent(String URL) {


        mURL = URL;
    }

    public void updateURLContent(String URL) {

        mURL = URL;
        myWebView = (WebView) getView().findViewById(R.id.pageInfo);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(mURL.trim());
    }
    public void onPause() {
        super.onPause();

    }

}
