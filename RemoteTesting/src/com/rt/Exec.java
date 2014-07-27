package com.rt;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rt.db.Product;
import com.rt.db.Product.ProductRepo;
import com.rt.db.Sub;
import com.rt.db.Sub.SubRepo;

public class Exec extends Fragment implements ActionBar.TabListener {

	int mode;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	static View PreviousToolbar;
	static Product product = new Product();

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().getActionBar().setNavigationMode(mode);
		Main.remote.disconnect();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		int id = Main.pid;
		ProductRepo repo = new ProductRepo(getActivity());
		product = repo.getProductById(id);
		getActivity().getActionBar().setTitle("Execution");
		if (product.pname != null) {
			getActivity().getActionBar().setTitle(
					"Execution : " + product.pname);
			Main.remote.connect(product.phost, product.ptcp, product.puser,
					product.ppass);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.exec, container, false);

		final ActionBar actionBar = getActivity().getActionBar();
		mode = actionBar.getNavigationMode();
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

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new Current();
				break;
			case 1:
				fragment = new History();
				break;
			default:
				fragment = new Current();
				break;
			}
			// Bundle args = new Bundle();
			// args.putInt(Current.ARG_SECTION_NUMBER, position + 1);
			// fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				// return getString(R.string.title_section1).toUpperCase(l);
				return "Current";
			case 1:
				return "History";
				// getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	public static class Current extends ListFragment {
		EditText sname;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.exec1, container, false);
			TextView msg = (TextView) rootView.findViewById(R.id.section_label);

			final int pid = Main.pid;
			if (product.pname == null) {
				msg.setText("Please select a product ! ");
				View sadd = rootView.findViewById(R.id.sadd);
				sadd.setVisibility(View.GONE);
				return rootView;
			}
			final SubRepo srepo = new SubRepo(getActivity());

			msg.setText("Parent product : " + product.pname);

			sname = (EditText) rootView.findViewById(R.id.sname);

			final View stoolbar = rootView.findViewById(R.id.stoolbar);
			((LinearLayout.LayoutParams) stoolbar.getLayoutParams()).bottomMargin = -50;
			stoolbar.setVisibility(View.GONE);

			View sadd = rootView.findViewById(R.id.sadd);
			View sdone = rootView.findViewById(R.id.sdone);

			sadd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View view) {
					ListAnim expandAni = new ListAnim(stoolbar);
					stoolbar.startAnimation(expandAni);
				}
			});

			sdone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View view) {
					if (sname.getText().toString().isEmpty()) {
						Toast.makeText(getActivity(), "Please enter name",
								Toast.LENGTH_SHORT).show();
					} else {
						Sub sub = new Sub();
						sub.sname = sname.getText().toString();
						sub.pid = pid;
						sub.sid = 0;
						srepo.insert(sub);
						ListAllSub();
						Toast.makeText(getActivity(),
								"New Sub Product Inserted", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			ListAllSub();
		}

		void AddSub() {
		}

		void ListAllSub() {
			SubRepo repo = new SubRepo(getActivity());
			int pid = Main.pid;
			ArrayList<HashMap<String, String>> items = repo.getSubList(pid);
			ListView lv = getListView();

			if (items.size() != 0) {
				setListAdapter(new PopupAdapter(items));
			} else {
				lv.setAdapter(null);
				Toast.makeText(getActivity(), "No Sub Products!",
						Toast.LENGTH_SHORT).show();
			}
		}

		class PopupAdapter extends SimpleAdapter {
			PopupAdapter(ArrayList<HashMap<String, String>> items) {
				super(getActivity(), items, R.layout.exec3, new String[] {
						"id", "name" },
						new int[] { R.id.sub_Id, R.id.sub_name });
			}

			@Override
			public View getView(int position, View convertView,
					ViewGroup container) {
				View view = super.getView(position, convertView, container);
				final View stoolbar1 = view.findViewById(R.id.stoolbar1);
				View sexec1 = view.findViewById(R.id.sexec1);
				View sedit1 = view.findViewById(R.id.sedit1);
				View sdel1 = view.findViewById(R.id.sdel1);
				final View sdone1 = view.findViewById(R.id.sdone1);
				final View sdone2 = view.findViewById(R.id.sdone2);
				final View spn1 = view.findViewById(R.id.spn1);
				final View spn2 = view.findViewById(R.id.spn2);

				TextView s_Id = (TextView) view.findViewById(R.id.sub_Id);
				TextView s_Name = (TextView) view.findViewById(R.id.sub_name);
				final int sid = Integer.parseInt(s_Id.getText().toString());
				final int pid = Main.pid;

				final String sname = s_Name.getText().toString();
				final EditText sname1 = (EditText) view
						.findViewById(R.id.sname1);
				sname1.setText(sname);
				final SubRepo srepo = new SubRepo(getActivity());

				sedit1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						sdone2.setVisibility(View.GONE);
						sdone1.setVisibility(View.VISIBLE);
						spn2.setVisibility(View.GONE);
						spn1.setVisibility(View.VISIBLE);
						boolean SameItemClicked = false;
						if ((PreviousToolbar == stoolbar1)) {
							PreviousToolbar = null;
							SameItemClicked = true;
						}
						if (PreviousToolbar != null) {
							ListAnim tempExpandAni = new ListAnim(
									PreviousToolbar);
							PreviousToolbar.startAnimation(tempExpandAni);
						}
						ListAnim expandAni = new ListAnim(stoolbar1);
						stoolbar1.startAnimation(expandAni);
						if (SameItemClicked) {
							PreviousToolbar = null;
						} else {
							PreviousToolbar = stoolbar1;
						}
					}
				});

				sdone1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						if (sname1.getText().toString().isEmpty()) {
							Toast.makeText(getActivity(), "Please enter name",
									Toast.LENGTH_SHORT).show();
						} else {
							Sub sub = new Sub();
							sub.sname = sname1.getText().toString();
							sub.pid = pid;
							sub.sid = sid;
							srepo.update(sub);
							ListAllSub();
							Toast.makeText(getActivity(),
									"Sub Product Updated", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

				sexec1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						sdone1.setVisibility(View.GONE);
						sdone2.setVisibility(View.VISIBLE);
						spn1.setVisibility(View.GONE);
						spn2.setVisibility(View.VISIBLE);

						boolean SameItemClicked = false;
						if ((PreviousToolbar == stoolbar1)) {
							PreviousToolbar = null;
							SameItemClicked = true;
						}
						if (PreviousToolbar != null) {
							ListAnim tempExpandAni = new ListAnim(
									PreviousToolbar);
							PreviousToolbar.startAnimation(tempExpandAni);
						}
						ListAnim expandAni = new ListAnim(stoolbar1);
						stoolbar1.startAnimation(expandAni);
						if (SameItemClicked) {
							PreviousToolbar = null;
						} else {
							PreviousToolbar = stoolbar1;
						}
					}
				});

				sdone2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						if (sname1.getText().toString().isEmpty()) {
							Toast.makeText(getActivity(),
									"Please enter command", Toast.LENGTH_SHORT)
									.show();
						} else {
							Main.remote.send(sname1.getText().toString());
						}
					}
				});

				sdel1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						srepo.delete(sid);
						Toast.makeText(getActivity(), "Sub Product Deleted",
								Toast.LENGTH_SHORT).show();
						ListAllSub();
					}
				});

				return view;
			}
		}

	}// /current

	public static class History extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.exec2, container, false);
			TextView msg = (TextView) rootView.findViewById(R.id.section_label);
			int id = Main.pid;
			ProductRepo repo = new ProductRepo(getActivity());
			Product product = new Product();
			product = repo.getProductById(id);
			if (product.pname != null)
				msg.setText("Current " + product.pname);
			else
				msg.setText("Please select a product ! ");
			return rootView;
		}
	}
}
