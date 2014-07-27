package com.rt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;



@SuppressLint("NewApi")
public class Home extends Fragment implements OnClickListener{
	
@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
           View v = inflater .inflate(R.layout.home, container, false);  
           getActivity().getActionBar().setTitle("Home");
     	   v.findViewById(R.id.Automation).setOnClickListener(this);
           v.findViewById(R.id.Installation).setOnClickListener(this);
           v.findViewById(R.id.Reports).setOnClickListener(this);
           v.findViewById(R.id.Exit).setOnClickListener(this);
		    
           return v;
   }

@Override
public void onClick(View v) {
	if(v.getId()==R.id.Exit){getActivity().finish();}
	Main.ptype= getResources().getResourceEntryName(v.getId());
	((Main)getActivity()).updateDisplay(1);
}
}