package org.nosemaj.service_lock_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;


//broadcast receiver to start service at boot time if admin is active



public class MyReceiver extends BroadcastReceiver {
	
	

    @Override
    public void onReceive(Context context, Intent intent) {
     
    
    	
  
    	 SharedPreferences saved_values=context.getApplicationContext().getSharedPreferences( "data", Context.MODE_PRIVATE);
    	 boolean checkstate = saved_values.getBoolean("tgpref", false); 
    	 int pr=saved_values.getInt("progress", 100);
    	
         TheService.SHAKE_THRESHOLD=pr;
       
         if(checkstate)
         {
	     Intent myIntent = new Intent(context, TheService.class);
	     context.startService(myIntent);
	     Toast.makeText(context,"yo"+pr+checkstate , Toast.LENGTH_LONG).show();
    	 
         }
         else
         {
        	 Toast.makeText(context,"yo"+pr+checkstate , Toast.LENGTH_LONG).show();
        	 
         }
  
    
    } 
}
