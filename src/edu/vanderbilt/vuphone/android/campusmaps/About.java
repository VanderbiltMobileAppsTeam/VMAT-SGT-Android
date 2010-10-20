package edu.vanderbilt.vuphone.android.campusmaps;

/**
 * 
 */


import android.app.TabActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * @author Hamilton Turner
 * 
 */
public class About extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		setContentView(R.layout.about);

		Resources res = getResources();
		CharSequence[] developers = res.getTextArray(R.array.about_developers);
		CharSequence[] studentleaders = res.getTextArray(R.array.student_leaders);
		CharSequence[] facultyleaders = res.getTextArray(R.array.faculty_leaders);
		CharSequence[] contribs = res.getTextArray(R.array.contributors);
		
		TextView devnames = (TextView) findViewById(R.about.dev_names);
		devnames.setText("");
		for (CharSequence c:developers)
			devnames.append(c + "\n");
		devnames.invalidate();
		
		TextView slnames = (TextView) findViewById(R.about.student_leader_names);
		slnames.setText("");
		for (CharSequence c:studentleaders)
			slnames.append(c + "\n");
		slnames.invalidate();
		
		TextView flnames = (TextView) findViewById(R.about.faculty_leader_names);
		flnames.setText("");
		for (CharSequence c:facultyleaders)
			flnames.append(c + "\n");
		flnames.invalidate();
		
		TextView cont = (TextView) findViewById(R.about.contributor_names);
		cont.setText("");
		for (CharSequence c:contribs)
			cont.append(c + "\n");
		cont.invalidate();
		
		
		TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Developers"/*, 
				getResources().getDrawable(android.R.drawable.ic_menu_sort_alphabetically)*/).setContent(R.about.tab_devs));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("VMAT"/*,
				getResources().getDrawable(android.R.drawable.ic_dialog_info)*/).setContent(R.about.tab_vmat));
	}
}
