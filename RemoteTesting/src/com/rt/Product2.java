package com.rt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rt.db.Product;
import com.rt.db.Product.ProductRepo;

public class Product2 extends Fragment implements TextWatcher{

	int pid;
    EditText pname;
    EditText phost;
    EditText puser;
    EditText ppass;
    EditText ptcp;
    EditText ptom;
    Spinner types;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);      
     }

  	@Override
  	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  		View v = inflater.inflate(R.layout.product2, container, false);
//        setContentView(R.layout.product2);

        pname = (EditText) v.findViewById(R.id.pname);
        phost = (EditText) v.findViewById(R.id.phost);
        puser = (EditText) v.findViewById(R.id.puser);
        ppass= (EditText) v.findViewById(R.id.ppass);
        ptcp= (EditText) v.findViewById(R.id.ptcp);
        ptom= (EditText) v.findViewById(R.id.ptom);
        types = (Spinner) v.findViewById(R.id.ptypes);
        
        pid=((MainActivity)getActivity()).pid;
        ProductRepo repo = new ProductRepo(getActivity());
        Product product = new Product();
        product = repo.getProductById(pid);
        if(product.pname!=null)
        	getActivity().getActionBar().setTitle("Product : "+product.pname);
        else
        	getActivity().getActionBar().setTitle("New Product");
  		pname.setText(product.pname);
        phost.setText(product.phost);
        puser.setText(product.puser);
        ppass.setText(product.ppass);
        ptcp.setText(product.ptcp);
        ptom.setText(product.ptom);
        String type = product.ptype;
        for (int i = 0; i < types.getCount(); i++) {
          String s = (String) types.getItemAtPosition(i);
          if (s.equalsIgnoreCase(type)) {
            types.setSelection(i);
          }
        }
            
        return v;
    }	
    
    
    //******************NIk code
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	MenuItem done = menu.add(Menu.NONE, 1, 3, "Done");			//1 means id for edit
        //menu.add(group of menu,id,order,text(title))
        done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		done.setIcon(R.drawable.ic_action_accept);	
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ProductRepo repo = new ProductRepo(getActivity());
    	
		switch (item.getItemId()) {
	  		case 1: 	//Done
		    			Product product = new Product();
		    			product.pname=pname.getText().toString();
		    			product.phost=phost.getText().toString();
		    			product.puser=puser.getText().toString();
		    			product.ppass=ppass.getText().toString();
		    			product.ptcp=ptcp.getText().toString();
		    			product.ptom=ptom.getText().toString();
		    			
		    			product.ptype=types.getSelectedItem().toString();
		    			product.pid=pid;
		    			if (pid==0){
		    				pid = repo.insert(product);
		    				((MainActivity)getActivity()).updateDisplay(1);
		        			Toast.makeText(getActivity(),"New Product Insert",Toast.LENGTH_SHORT).show();
		    			}else{
		        			repo.update(product);
		        			((MainActivity)getActivity()).newfrag(2, pid);
		        			Toast.makeText(getActivity(),"Product Record updated",Toast.LENGTH_SHORT).show();
		    			}
		    			getActivity().onBackPressed();
		    			getActivity().onBackPressed();
		    			return true;	//If performed successfully or pass to super
			  				
	  		default:
	  					return super.onContextItemSelected(item);
		}
	}

	
	//////////////////////Textwatcher methods for validations
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}