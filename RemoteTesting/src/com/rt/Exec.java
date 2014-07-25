package com.rt;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rt.db.Product;
import com.rt.db.Product.ProductRepo;

public class Exec extends Fragment implements ActionBar.TabListener {

	int mode;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	@Override
	public void onDestroyView (){
		super.onDestroyView();
		getActivity().getActionBar().setNavigationMode(mode);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
	@Override
  	public void onActivityCreated (Bundle savedInstanceState){
  		 super.onActivityCreated(savedInstanceState);
  
  		int id=((MainActivity)getActivity()).pid;
        ProductRepo repo = new ProductRepo(getActivity());
        Product product = new Product();
        product = repo.getProductById(id);
        getActivity().getActionBar().setTitle("Execution");
        if(product.pname!=null)
  		 getActivity().getActionBar().setTitle("Execution : "+product.pname);
        
  	}
  	
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.exec, container, false);
		   
		final ActionBar actionBar = getActivity().getActionBar();
		mode=actionBar.getNavigationMode();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());
		
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

        actionBar.removeAllTabs();
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	return v;
	}


	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new Current();
			Bundle args = new Bundle();
			args.putInt(Current.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			//Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				//return getString(R.string.title_section1).toUpperCase(l);
				return "Current";
			case 1:
				return "History";
						//getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class Current extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public Current() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.exec_cur,
					container, false);
			TextView msg = (TextView) rootView
					.findViewById(R.id.section_label);
			int id=((MainActivity)getActivity()).pid;
	        ProductRepo repo = new ProductRepo(getActivity());
	        Product product = new Product();
	        product = repo.getProductById(id);
	        if(product.pname!=null)
			msg.setText("Current "+product.pname);
	        else
	        msg.setText("Please select a product ! ");
	        return rootView;
		}
	}





//*************************************************Nik Code ********************
	
	/**
		 * A dummy fragment representing a section of the app, but that simply
		 * displays dummy text.
		 */
		public static class History extends Fragment {
			/**
			 * The fragment argument representing the section number for this
			 * fragment.
			 */
			public static final String ARG_SECTION_NUMBER = "section_number";
	
			public History() {
			}
	
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.exec_cur,
						container, false);
				TextView dummyTextView = (TextView) rootView
						.findViewById(R.id.section_label);
				int id=((MainActivity)getActivity()).pid;
		        ProductRepo repo = new ProductRepo(getActivity());
		        Product product = new Product();
		        product = repo.getProductById(id);
	
	//	        editTextAge.setText(String.valueOf(product.age));
	//	        editTextName.setText(product.name);
	//	        editTextEmail.setText(product.email);
	
				
				dummyTextView.setText(Integer.toString(getArguments().getInt(
						ARG_SECTION_NUMBER))+product.pname);
				return rootView;
			}
		}





	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		   //inflater.inflate(R.menu.listmenu, menu);		-->  Menu by xml		
			MenuItem edit = menu.add(Menu.NONE, 1, 3, "Edit");			//1 means id for edit
	        //menu.add(group of menu,id,order,text(title))
	        edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			edit.setIcon(R.drawable.ic_action_edit);	
					
			MenuItem del = menu.add(Menu.NONE,2, 3, "Del");			//1 means id for edit
	        //menu.add(group of menu,id,order,text(title))
	        del.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			del.setIcon(R.drawable.ic_action_discard);	
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	  		case 1: ((MainActivity)getActivity()).newfrag(1, ((MainActivity)getActivity()).pid);
	  					return true;	//If performed successfully or pass to super
	  				
	  		case 2:
	  			ProductRepo repo = new ProductRepo(getActivity());
	  	    	repo.delete(((MainActivity)getActivity()).pid);
	  	    	Toast.makeText(getActivity(), "Product Record Deleted", Toast.LENGTH_SHORT).show();
    			((MainActivity)getActivity()).onBackPressed();
    			return true;	//If performed successfully or pass to super
		
	  		default:
	  					return super.onContextItemSelected(item);
		}
	}
}
