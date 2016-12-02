package org.nosemaj.service_lock_test;



import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceLockTest extends Activity {
	
	
	static SeekBar seekBar;
	TextView text;
	static int progressValue ;
	
    public static final String TAG = ServiceLockTest.class.getName();
	private static final int REQUEST_ADD_DEVICE_ADMIN = 1;
    static ComponentName cn=null;
    static DevicePolicyManager mgr=null;
    Switch s;
    static SharedPreferences preferences = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Create a new component identifier from a Context and Class object.
       
        cn=new ComponentName(this, Off.class);  //use to check admin status
        mgr=(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        s = (Switch) findViewById(R.id.toggleButton1);
      //  preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());  
	     preferences=getSharedPreferences("data", MODE_MULTI_PROCESS) ; 
        

	    //seek bar code
        
         seekBar = (SeekBar) findViewById(R.id.seekBar1);
         text = (TextView) findViewById(R.id.textView1);
         // Initialize the textview with '0'
         text.setText(seekBar.getProgress() + "/" + seekBar.getMax());
        
	        seekBar.setProgress(0);
	        seekBar.incrementProgressBy(100);
	        seekBar.setMax(1000);
	        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			 
        
        	 
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = preferences.edit();
		        editor.putInt("progress", seekBar.getProgress());
		        editor.commit();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				 // text.setText(progressValue + "/" + seekBar.getMax());
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
			
				    progress = progress / 10;
			        progress = progress * 10;
			        text.setText(String.valueOf(progress));
			        progressValue=progress;
			        TheService.SHAKE_THRESHOLD=progressValue;
			
			        
			        
			}
		});
       
    }
    
   
   
    @Override
    public void onResume() {
        super.onResume();
        
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                	
                	
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("tgpref", isChecked); // value to store
				    editor.commit();
                    // The toggle is enabled
                	 if (mgr.isAdminActive(cn))
                	 {
                		 Toast.makeText(getBaseContext(),"Service is afor",Toast.LENGTH_LONG).show();
             					
                		 
   		   		     }
                	 else
                	 {
                		 Log.i("Admin", "Not an admin");//mean admin is not active
                		 showAct();//will open new activity
                	 }
                } 
                else
                {
                    // The toggle is disabled

                	SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("tgpref", isChecked); // value to store
				    editor.commit();
                	
                	stopService(new Intent(ServiceLockTest.this, TheService.class)); //remove service
                	mgr.removeActiveAdmin(cn);
                	
                }
            }

		
        });
        
       
      
        
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	// TODO Auto-generated method stub
    	super.onWindowFocusChanged(hasFocus);
    	  if(hasFocus)
    	  {   
    		  Log.i("hasFocus", "check");
    		  boolean tgpref = preferences.getBoolean("tgpref", false);  //default is false
              progressValue= preferences.getInt("progress", 100); //setting default progress as 100
              seekBar.setProgress( progressValue); 
              
             
              
    	      s.setChecked(tgpref);
    	      if(!mgr.isAdminActive(cn))
    	    	  s.setChecked(false);
    	       
    	  }
    }
       
    
   
    
    protected void showAct() {
		// TODO Auto-generated method stub
    	/*Toast.makeText(getBaseContext(), R.string.device_admin_not_enabled,
					Toast.LENGTH_LONG).show();*/
    	Intent intent=
		          new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		      intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
		      intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
		                      "test");
		      startActivityForResult(intent,REQUEST_ADD_DEVICE_ADMIN);//will start service
	}
    
    
    
    

	@Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	
    	super.onPause();
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	switch (requestCode) {
    	case REQUEST_ADD_DEVICE_ADMIN:
    	if (resultCode == Activity.RESULT_OK) {


    	} else {
    		s.setChecked(false);
    		Toast.makeText(getBaseContext(),"cancel",
					Toast.LENGTH_LONG).show();  //if click on cancel button
    	}

    	break;
    	} 
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
		
	}
      

}
