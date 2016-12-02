package org.nosemaj.service_lock_test;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.sax.StartElementListener;
import android.widget.Toast;

public class Off extends DeviceAdminReceiver {
	
	private void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		
		 SharedPreferences saved_values=context.getApplicationContext().getSharedPreferences( "data", Context.MODE_PRIVATE);
    	 boolean checkstate = saved_values.getBoolean("tgpref", false); 
    	 int pr=saved_values.getInt("progress", 100);
    	    if(checkstate)
    	    {
			   
    	    	showToast(context,
    					context.getString(R.string.admin_receiver_status_enabled));
    	    	TheService.SHAKE_THRESHOLD=pr;
			    context.startService(new Intent(context, TheService.class));
    	    }
    	    else
    	    {
    	    	  Intent in=new Intent(context,ServiceLockTest.class);
    	    	  in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_FROM_BACKGROUND);
    	    	  context.startActivity(in);
    	    	 // startActivityForResult(intent,REQUEST_ADD_DEVICE_ADMIN);
                ServiceLockTest.mgr.removeActiveAdmin(ServiceLockTest.cn);
                //showToast(context, "Please try to activate the admin from application ");
    	    }
		    
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		showToast(context,
				context.getString(R.string.admin_receiver_status_disabled));
		    context.stopService(new Intent(context, TheService.class));
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
	}
	

	
}
