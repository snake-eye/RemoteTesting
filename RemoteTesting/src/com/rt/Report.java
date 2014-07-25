package com.rt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


@SuppressLint("NewApi")
public class Report extends Fragment {

@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
           View rootView = inflater .inflate(R.layout.report, container, false);  
           return rootView;
   }
}