package com.eusecom.attendance;

/**
 * Get period of accounting....
 * Called from AllEmpAbsMvvmActivity.java
 */

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class VyberUmeActivity extends ListActivity {
	
	private static final String TAG_ODKIAL = "odkial";
	private static final String TAG_PAGEX = "page";
    private static final String TAG_POKLX = "pokl";

	static String[] pormes = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
	String odkial;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        odkial = extras.getString(TAG_ODKIAL);
        
        
        String rokxy = SettingsActivity.getRok(this);
        String ume01 = "01." + rokxy; String ume02 = "02." + rokxy; String ume03 = "03." + rokxy;
        String ume04 = "04." + rokxy; String ume05 = "05." + rokxy; String ume06 = "06." + rokxy;
        String ume07 = "07." + rokxy; String ume08 = "08." + rokxy; String ume09 = "09." + rokxy;
        String ume10 = "10." + rokxy; String ume11 = "11." + rokxy; String ume12 = "12." + rokxy;
        
		String[] umes = { ume01, ume02, ume03, ume04, ume05, ume06, ume07, ume08, ume09, ume10, ume11, ume12  };
		
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, umes));
		
		

		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//startActivity(intents[position]);
		
		String umepos = pormes[position] + "";
		String rokxy = SettingsActivity.getRok(this);
		
		String umexy = umepos + "." + rokxy;

    	
    	//toto uklada preference
     	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
     	Editor editor = prefs.edit();

     	editor.putString("ume", umexy).apply();
     	
     	editor.commit();
     	
     	if( odkial.equals("0")) {
     		
     		Intent i = new Intent(getApplicationContext(), AllEmpsAbsMvvmActivity.class);
            Bundle extras = new Bundle();
            extras.putString(TAG_PAGEX, "0");
            extras.putString(TAG_POKLX, "0");
            i.putExtras(extras);
            startActivity(i);
            finish();
     	}
     	if( odkial.equals("1")) {
     		

     	}
     	if( odkial.equals("2")) {
     		

     	}
		
		
	}
	//koniec onlistitem

}
