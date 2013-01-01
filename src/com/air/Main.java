package com.air;

import java.util.Hashtable;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import vInAppAdEngine.VservAd;
import vInAppAdEngine.VservAdListener;
import vInAppAdEngine.VservManager;

import com.emobtech.googleanalyticsme.PageView;
import com.emobtech.googleanalyticsme.Tracker;

public class Main extends MIDlet implements VservAdListener {
	
	
	Display disp;
	MyCanvas mycan;
	Tracker tracker;
	//String ANALYTICS_ID = "UA-37083261-1";
	String ANALYTICS_ID = "UA-33137252-1";
	private VservAd vservAd;
    private VservManager vservManager;
	

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
	    tracker.addToQueue(new PageView("/NokiaS40FullTouch/Exit"));
	    tracker.flush(false);
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
		System.out.println("midlet has just started!!!");
		
		disp = Display.getDisplay(this);
		if(mycan == null)
		{
		initialize() ;
		tracker = Tracker.getInstance(this, ANALYTICS_ID, 0);
		
		// tracker 3rd parameter compulsory?
	    tracker.addToQueue(new PageView("/NokiaS40FullTouch/Entry"));
		tracker.flush(false);
	    mycan = new MyCanvas(this, true);
		disp.setCurrent(mycan);
		}
		
		
	}
	
	
	
	 private void initialize() 
	 {//GEN-END:MVDInitBegin
	        // Insert pre-init code here
	        
	        //Replace your application id here
	        Hashtable vservConfigTable=new Hashtable();
	        vservConfigTable.put("appId","903");
	        VservManager vservManager=new VservManager(this,vservConfigTable);
	        
	        vservAd=new VservAd(this);
	        vservAd.requestAd();
	        
	        // Insert post-init code here
	   }//GE
	
	
	 public void showAlertMessage(String title, String alertText, AlertType type) 
	 {
	        showAlertMessage(disp, title, alertText, type);
	  }

	 public static void showAlertMessage(Display display, String title,
	        String alertText, AlertType type) 
	  {
	        Alert alert = new Alert(title, alertText, null, type);
	        display.setCurrent(alert, display.getCurrent());
	  }

	public void vservAdFailed(Object obj) 
	{
		
		
		
		showAlertMessage("no ad", "no ad received", AlertType.INFO);
		// TODO Auto-generated method stub
		
		
	}

	public void vservAdReceived(Object obj) 
	{

        if(obj==vservAd)
        {
            
            if(((VservAd)obj).getAdType().equals(VservAd.AD_TYPE_IMAGE))
            {
                Image imageAd=(Image)((VservAd)obj).getAd();
                mycan.setAdImage(imageAd);
               // imageItem = new ImageItem("", imageAd, ImageItem.LAYOUT_DEFAULT, "", Item.BUTTON);
                //helloForm.append(imageItem);
              //  imageItem.setDefaultCommand(new Command("ClickImgAd", Command.OK, 2));
              //  imageItem.setItemCommandListener(this);
            }
            else if(((VservAd)obj).getAdType().equals(VservAd.AD_TYPE_TEXT))
            {
               String textAd=(String)((VservAd)obj).getAd();
               //stringItem = new StringItem("", textAd, Item.BUTTON);
             //  stringItem.setPreferredSize(216,36);
             //  helloForm.append(stringItem);
              // stringItem.setDefaultCommand(new Command("ClickTextAd", Command.OK, 2));
              // stringItem.setItemCommandListener(this);
            }
           
        }
		
		// TODO Auto-generated method stub
		
	}
	

}
