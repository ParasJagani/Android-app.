package org.nosemaj.service_lock_test;




import android.R;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class TheService extends Service implements SensorEventListener {
    public static final String TAG = TheService.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
    private Notification notification;
    private SensorManager mSensorManager = null;
    private WakeLock mWakeLock = null;
    PowerManager manager;
    Window w;
    PowerManager pm;
    float sg;
    float t;
    private DevicePolicyManager mgr=null;
    private ComponentName cn=null;
    boolean f=true;
    //-------------------------------------------------------------------------------------------------------------------
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    static  int SHAKE_THRESHOLD =0;
    
    /*
     * Register this as a sensor event listener.
     */
    private void registerListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    
    
  //  ------------------------------------------------------------------------------------------------------------------------------------
    
   
    
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.i(TAG, "onReceive("+intent+")");

            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            	
            	
            	Log.v(TAG, "inside");
                return;
            }
           
             
            Runnable runnable = new Runnable() {
                public void run() {
                    Log.i(TAG, "Runnable executing.");
                    unregisterListener();
                    registerListener();
                    
                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
        }
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "onAccuracyChanged().");
       
    }

    @SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged().");
        
      /*  if(event.values[0]==3.0)
        {
        	 if (mgr.isAdminActive(cn)) {
   		      mgr.lockNow();
   		    }
   		    else {
   		      Intent intent1=
   		          new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
   		      intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
   		      intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
   		                     "hi");
   		      intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   		      startActivity(intent1);
   		    }
        	
      /* 
        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);	
        if(!pm.isScreenOn()) {	
        KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();
       
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK 
                                                 | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        wl.acquire();
       
        
        wl.release();      
        }
        else
        {
        	 boolean isScreenOn = pm.isScreenOn();
             System.out.println(isScreenOn);
             PowerManager.WakeLock wl = pm.newWakeLock(32, "tag");
				wl.acquire();
				
				
				//wl.release(); 
         
        
            
        }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "yo", Toast.LENGTH_LONG).show();
            
        } */
       Sensor mySensor1 = event.sensor;
    	if(mySensor1.getType()==Sensor.TYPE_PROXIMITY){
    		if(event.values[0]==mySensor1.getMaximumRange())
    		{
    			
    			 f=true;
    			 
    		}
    		else
    		{
    			/*
    			boolean isScreenOn = pm.isScreenOn();
                System.out.println(isScreenOn);
                mWakeLock= pm.newWakeLock(32, "tag");
   				mWakeLock.acquire();
   				mWakeLock.release();*/
    			 if (mgr.isAdminActive(cn)) 
    			    {
    	   		      mgr.lockNow();
    	   		      f=false;
    	   		    }
    	   		   
    		}
    		
    	}
    	
    	else{
    		if(f){
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
     
            long curTime = System.currentTimeMillis();
     
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
     
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
                	
                	
			        		
					        	 pm = (PowerManager)getSystemService(Context.POWER_SERVICE);	
					             if(!pm.isScreenOn()) {	
					           /*  KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
					             final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
					             kl.disableKeyguard();*/
					            
					             PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK 
					                                                      | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
					             wl.acquire();
					            
					             
					             wl.release(); 
			                 }
			        		
        	   
                   
                }
     
                last_x = x;
                last_y = y;
                last_z = z;
            }
          } 
    	}
        }  
        
      /*  float z=event.values[2];
        if(z>=t)
        {    System.out.print(z);
        	Sensor mySensor = event.sensor;
        	if(mySensor.getType()==Sensor.TYPE_PROXIMITY){
        		if(event.values[0]==3.0)
        		{
        			boolean isScreenOn = pm.isScreenOn();
                    System.out.println(isScreenOn);
                    PowerManager.WakeLock wl = pm.newWakeLock(32, "tag");
       				wl.acquire();
        		}
        		else{
		        	 pm = (PowerManager)getSystemService(Context.POWER_SERVICE);	
		             if(!pm.isScreenOn()) {	
		             KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		             final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
		             kl.disableKeyguard();
		            
		             PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK 
		                                                      | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
		             wl.acquire();
		            
		             
		             wl.release(); 
                 }
        		}
        	}
        }
        else if(z<=-t)
        {
        	Sensor mySensor = event.sensor;
        	if(mySensor.getType()==Sensor.TYPE_PROXIMITY){
        		if(event.values[0]==3.0)
        		{
        			boolean isScreenOn = pm.isScreenOn();
                    System.out.println(isScreenOn);
                    PowerManager.WakeLock wl = pm.newWakeLock(32, "tag");
       				wl.acquire();
        		}
        		else{
		        	 pm = (PowerManager)getSystemService(Context.POWER_SERVICE);	
		             if(!pm.isScreenOn()) {	
		             KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		             final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
		             kl.disableKeyguard();
		            
		             PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK 
		                                                      | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
		             wl.acquire();
		            
		             
		             wl.release(); 
                 }
        		}
        	}
        }
        else
        {
        	
        	Sensor mySensor = event.sensor;
        	if(mySensor.getType()==Sensor.TYPE_PROXIMITY){
        		if(event.values[0]==3.0)
        		{
        			boolean isScreenOn = pm.isScreenOn();
                    System.out.println(isScreenOn);
                    PowerManager.WakeLock wl = pm.newWakeLock(32, "tag");
       				wl.acquire();
        		}
        		else{
		        	 pm = (PowerManager)getSystemService(Context.POWER_SERVICE);	
		             if(!pm.isScreenOn()) {	
		             KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		             final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
		             kl.disableKeyguard();
		            
		             PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK 
		                                                      | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
		             wl.acquire();
		            
		             
		             wl.release(); 
                 }
        		}
        	}
        	
        }   */
        }

  
	@Override
    public void onCreate() {
        super.onCreate();
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

         manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
           
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        
        cn=new ComponentName(this, Off.class);
	    mgr=(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        sg=SensorManager.STANDARD_GRAVITY;
        t=sg/2;
        
        
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Intent in=new Intent(getApplicationContext(),ServiceLockTest.class);
        PendingIntent contentIntent =PendingIntent.getActivity(getApplicationContext(), 0, in, PendingIntent.FLAG_CANCEL_CURRENT);
        
      /*  NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);*/

        
        Notification.Builder mBuilder =
                new Notification.Builder(getApplicationContext())
        .setContentTitle("Sensor service is runing...")
        .setSmallIcon(R.drawable.ic_lock_idle_alarm)
        .setContentText("Please off service if you dont require it")
        .setContentIntent(contentIntent);
        notification = mBuilder.build();
      
        startForeground(Process.myPid(),notification);
        registerListener();
        mWakeLock.acquire();
        
           /*    notification.defaults |= Notification.DEFAULT_ALL;
        nm.notify(0, notification);*/
        
       //--------------------------------------------------------------------------------------------------------
        
      
        
        
        
        
        return START_STICKY;
    }
}

