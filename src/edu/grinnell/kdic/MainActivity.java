package edu.grinnell.kdic;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import edu.grinnell.schedule.ParseSchedule;
import edu.grinnell.schedule.Show;

public class MainActivity extends FragmentActivity {

	public ArrayList<Show> mSchedule = new ArrayList<Show>();
	// InputStream scheduleJSON;
	ParseSchedule parser = new ParseSchedule();
	Boolean scheduleShowing = false;
	View schedule;
	public static String[] url = new String[] { "http://tcdb.grinnell.edu/apps/glicious/KDIC/schedule.json" };
	public boolean scheduleInitialized = false;
	public int diskImage = 1;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		

		//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
		//	getActionBar().hide();

		// open the inputStream to the file
		// scheduleJSON = getAssets().open("schedule.json");

		// mSchedule = parser.parseShows();
		parser.execute(url);
		mSchedule = parser.Schedule;

		getFragmentManager()
				.beginTransaction()
				.replace(R.id.radio_banner_container,
						new StreamBannerFragment()).commit();

	}

	@Override
	public void onBackPressed() {
		if (scheduleShowing) {
			// schedule.isEnabled();
			schedule.setVisibility(View.INVISIBLE);
			findViewById(R.id.schedule).setBackgroundResource(
					R.drawable.list_black);
			scheduleShowing = false;
		} else
			super.onBackPressed();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	}

	public void showSchedule(View view) {

		if (!scheduleInitialized) {
			schedule = findViewById(R.id.schedule_container);
			schedule.setVisibility(View.INVISIBLE);
			ListFragment schedule_frag = ScheduleFragment
					.newInstance(mSchedule);

			getFragmentManager().beginTransaction()
					.replace(R.id.schedule_container, schedule_frag)
					.addToBackStack("schedule").commit();

			scheduleInitialized = true;
			scheduleShowing = true;
			schedule.setVisibility(View.VISIBLE);
			view.setBackgroundResource(R.drawable.list_white);
		} else if (scheduleShowing == false) {
			scheduleShowing = true;
			schedule.setVisibility(View.VISIBLE);
			view.setBackgroundResource(R.drawable.list_white);
		} else {
			scheduleShowing = false;
			schedule.setVisibility(View.GONE);
			view.setBackgroundResource(R.drawable.list_black);
		}
	}

	public void swapDisk(View view) {
		// switch the disk image
		final ImageView diskView = (ImageView) findViewById(R.id.diskImage);
		
		diskView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (diskImage == 0)
					diskView.setImageResource(R.drawable.medium_kington);
				return true;
			}
		});
		
		if (diskImage == 0) {
			// swap to disk 1
			diskView.setImageResource(R.drawable.medium_kdicdisk);
			diskImage = 1;
		} else if (diskImage == 1) {
			// swap to disk 2
			diskView.setImageResource(R.drawable.medium_kdictext);
			diskImage = 2;
		} else if (diskImage == 2) {
			diskView.setImageResource(R.drawable.medium_tribe);
			diskImage = 3;
		} else if (diskImage == 3) {
			diskView.setImageResource(R.drawable.medium_chronic);
			diskImage = 0;
		}

	}
}
