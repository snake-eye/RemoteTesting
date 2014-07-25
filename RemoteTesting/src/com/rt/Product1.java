package com.rt;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rt.db.Product.ProductRepo;

public class Product1 extends ListFragment
{
	View PreviousToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);      
     }

  	@Override
  	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  		View v = inflater.inflate(R.layout.product1, container, false);
  		getActivity().getActionBar().setTitle("Product");
	    return v;
    }
    
  	@Override
  	public void onActivityCreated (Bundle savedInstanceState){
  		 super.onActivityCreated(savedInstanceState);   
  		 registerForContextMenu(getListView());
  		 ListAllProduct();
  	}
  	void AddProduct(){((MainActivity)getActivity()).newfrag(1, 0);}
  	void ListAllProduct(){
  		ProductRepo repo = new ProductRepo(getActivity());
  		String ptype=((MainActivity)getActivity()).ptype;
        ArrayList<HashMap<String, String>> items =  repo.getProductList(ptype);
        ListView lv = getListView();
        
        if(items.size()!=0) {
        	setListAdapter(new PopupAdapter(items));            
        }else{
        	lv.setAdapter(null);
            Toast.makeText(getActivity(),"No product!",Toast.LENGTH_SHORT).show();
        }
  	}
  	
//******************************menu  Actionbar
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem add = menu.add(Menu.NONE, 1, 3, "Add");			//1 means id for add
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		add.setIcon(R.drawable.ic_action_new);	
		
		MenuItem listall = menu.add(Menu.NONE, 2, 3, "List All");	//2 means id for listall
		listall.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		listall.setIcon(R.drawable.ic_drawer);			
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
  		case 1: AddProduct();return true;	//If performed successfully or pass to super			
  		case 2:ListAllProduct();return true;	//If performed successfully or pass to super
  default:
      return super.onContextItemSelected(item);
  }
}


@Override
public void onListItemClick(ListView listView, View view, int position, long id) {
	TextView product_Id = (TextView) view.findViewById(R.id.product_Id);
	((MainActivity)getActivity()).newfrag(2, Integer.parseInt(product_Id.getText().toString()));
}


class PopupAdapter extends SimpleAdapter{

    PopupAdapter(ArrayList<HashMap<String, String>> items) {
        	super(getActivity(),items, R.layout.product3, new String[] { "id","name"}, new int[] {R.id.product_Id, R.id.product_name});        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        View view = super.getView(position, convertView, container);
        View popupButton = view.findViewById(R.id.button_popup);
        final View toolbar=view.findViewById(R.id.toolbar);
        View pexec = view.findViewById(R.id.pexec);
        View pedit = view.findViewById(R.id.pedit);
        View pdel = view.findViewById(R.id.pdel);
        TextView product_Id = (TextView) view.findViewById(R.id.product_Id);
    	final int pid=Integer.parseInt(product_Id.getText().toString());
        
        popupButton.setTag(toolbar);
        popupButton.setOnClickListener(new OnClickListener(){@Override
        	public void onClick(final View view) {
                	boolean SameItemClicked = false;
                	if ((PreviousToolbar==toolbar)) {
                	PreviousToolbar = null;
                	SameItemClicked = true;
                	}
                	if (PreviousToolbar!=null){
                	ListAnim tempExpandAni = new ListAnim(PreviousToolbar);
                	PreviousToolbar.startAnimation(tempExpandAni);
                	}
                	ListAnim expandAni = new ListAnim(toolbar);
                	toolbar.startAnimation(expandAni);
                	if (SameItemClicked) {
                	PreviousToolbar = null;
                	}
                	else {
                	PreviousToolbar = toolbar;
                	}
             }
        });
    	
        pexec.setOnClickListener(new OnClickListener() {@Override
			public void onClick(View v) {
        	((MainActivity)getActivity()).newfrag(2,pid);
            }
        });
        
        pedit.setOnClickListener(new OnClickListener() {@Override
			public void onClick(View v) {
        	((MainActivity)getActivity()).newfrag(1,pid);
            }
        });
        
        pdel.setOnClickListener(new OnClickListener() {@Override
			public void onClick(View v) {
        	ProductRepo repo = new ProductRepo(getActivity());
  	    	repo.delete(pid);
  	    	Toast.makeText(getActivity(), "Product Record Deleted", Toast.LENGTH_SHORT).show();
  	    	((MainActivity)getActivity()).updateDisplay(1);
			}
        });
        
        return view;
    }
}

}