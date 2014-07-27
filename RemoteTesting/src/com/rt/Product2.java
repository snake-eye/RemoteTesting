package com.rt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class Product2 extends Fragment{

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
        
        pid=Main.pid;
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
		done.setIcon(R.drawable.ic_action_acceptw);	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ProductRepo repo = new ProductRepo(getActivity());
    	switch (item.getItemId()) {
	  		case 1: 	//Done
	  					boolean flag=true;
	  					if(pname.getText().toString().isEmpty()){flag=false;Toast.makeText(getActivity(),"Please enter name",Toast.LENGTH_SHORT).show();}
	  					else if(phost.getText().toString().isEmpty()){flag=false;Toast.makeText(getActivity(),"Please enter host",Toast.LENGTH_SHORT).show();}
	  					else if(puser.getText().toString().isEmpty()){flag=false;Toast.makeText(getActivity(),"Please enter user",Toast.LENGTH_SHORT).show();}
	  					else if(ppass.getText().toString().isEmpty()){flag=false;Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_SHORT).show();}
	  					else if(ptcp.getText().toString().isEmpty()){flag=false;Toast.makeText(getActivity(),"Please enter TCP port",Toast.LENGTH_SHORT).show();}
	  					else if(ptom.getText().toString().isEmpty()){flag=false;Toast.makeText(getActivity(),"Please enter tpmcat port",Toast.LENGTH_SHORT).show();}
		    			if(flag){
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
				    				((Main)getActivity()).updateDisplay(1);
				        			Toast.makeText(getActivity(),"New Product Inserted",Toast.LENGTH_SHORT).show();
				    			}else{
				        			repo.update(product);
				        			((Main)getActivity()).newfrag(2, pid);
				        			Toast.makeText(getActivity(),"Product Record updated",Toast.LENGTH_SHORT).show();
				    			}
				    			getActivity().onBackPressed();
				    			getActivity().onBackPressed();
				    			return true;	//If performed successfully or pass to super
		    			}
			  		default:
			  					return super.onContextItemSelected(item);
		}
	}	
}