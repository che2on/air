package com.air;

import java.io.IOException;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

import com.air.sound.SoundManager;
import com.nokia.mid.ui.LCDUIUtil;

public class MyCanvas extends GameCanvas implements Runnable{
	
	
	
	private static final int MAX_CPS = 100;
	private static final int MS_PER_FRAME = 1000 / MAX_CPS;
	
	private int cps=0;
	private int cyclesThisSecond=0;
	private long lastCPSTime=0;
	
	int clock = 0;
	long timekeeper = 0;
	
	
	Image adImage;
	Sprite adSprite;
	
	
	Main midlet;
	Thread t;
	int burstCount = 0;
	
	
	Image splashImage;
	Image menuImage;
	Image gameOverImage;
	Image gamePlayImage;
	Image burstbubble1Image;
	Image popupImage;
	Image backImage;
	
	SoundManager burstmusic = new SoundManager();
	SoundManager splashmusic = new SoundManager ();
	SoundManager gameovermusic = new SoundManager();
	
	
	//Colors
	int WHITE = 0XFFFFFF;
	int BLACK = 0X000000;
	boolean MUSIC= true;
	
	
	
	boolean isPlay = true;
	int splashScreen = 100;
	int menuScreen = 101;
	int gameplayScreen = 102;
	int gameoverScreen =103;
	int pauseScreen = 104;
	int popMoreScreen = 105;
	boolean popMoreScreenPaintOnce  = false;
	int whichScreen = splashScreen;
	bubble b;
	
	
	public void setStatusBarVisible(boolean visible) 
	{
			        Boolean v = (visible ? Boolean.TRUE : Boolean.FALSE);
		        LCDUIUtil.setObjectTrait(this, "nokia.ui.canvas.status_zone", v);
    }
	
	
	
	public MyCanvas(Main midlet, boolean state)
	{
		
		super(state);
		this.midlet = midlet;
	//	System.out.println("landed in mycanvas");
	
		setFullScreenMode(true);
		setStatusBarVisible(true);
		
		loadResources();
		createBubbleSheet1();
		t = new Thread(this);
		t.start();
	}
	
	
	
	
	public void resetGame()
	{
		
		b = new bubble();
		burstCount = 0;
		createBubbleSheet1();
		
	}
	
	
	private void createBubbleSheet1()
	{
		
		b = new bubble();
		int x=0;
		int y=0;
		int jup=0;
		
		for(int i=0; i<6; i ++)
		{
		x =0;
		jup=4;
		if(i%2!=0)
		{
		x =30;
		jup =3;
		}
		for(int j=0; j<jup; j ++)
		{
			b.setx("bubble"+i+j, ""+x);
			b.sety("bubble"+i+j, ""+y);
			x += 60;
		}
		y +=60;
		}
		
	}
	
	
	public void updateBurstedBubbles(Graphics g)
	{
		
		for(int i=0; i <6; i++)
		for(int j=0; j <4; j++)
		{
			if(b.isBursted("bubble"+i+j))
			g.drawImage(burstbubble1Image, b.getx("bubble"+i+j), b.gety("bubble"+i+j), Graphics.TOP | Graphics.LEFT);
		}
	}

	private void loadResources() 
	{
		
		try 
		{
			splashImage = Image.createImage("/oo/splash.jpg");
			menuImage = Image.createImage("/oo/menu.jpg");
			gamePlayImage = Image.createImage("/oo/gameplay.jpg");
		    burstbubble1Image = Image.createImage("/oo/burst1.jpg");
		    popupImage = Image.createImage("/oo/popup.jpg");
			backImage = Image.createImage("/back.jpg");
			adImage = Image.createImage("/back.jpg");
			adSprite = new Sprite(adImage, adImage.getWidth(), adImage.getHeight());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		
	}

	
	long timeSinceStart;
	public void run()
	{
		
		while(isPlay)
		{
			
			long cycleStartTime = System.currentTimeMillis();

		    //  ... process the cycle (repaint etc)

		      // sleep if we've finished our work early
		      timeSinceStart = (cycleStartTime - System.currentTimeMillis());
		      if (timeSinceStart < MS_PER_FRAME)
		      {
		         try
		         {
		            Thread.sleep(MS_PER_FRAME - timeSinceStart);
		         }
		         catch(java.lang.InterruptedException e) 
		         { }
		      }
			
			
//			timekeeper = System.currentTimeMillis() % 1000;
//			
//		  if (System.currentTimeMillis() - lastCPSTime > 1000)
//		   {
//		         lastCPSTime = System.currentTimeMillis();
//		         cps = cyclesThisSecond;
//		         cyclesThisSecond = 0;
//		  } else
//		         cyclesThisSecond++;
			
			updateScreen(getGraphics());
			
		}
		
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void setAdImage(Image img)
	{
		adSprite.setImage(img, img.getWidth(), img.getHeight());
	}

	private void updateScreen(Graphics g)
	{
		
		clock++;
		
		if(whichScreen == splashScreen)
		{
			
		//	System.out.println("in splash screen");
			//g.setColor(WHITE);
		//	g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(splashImage, 0, 0, Graphics.TOP | Graphics.LEFT);
		
			if(clock > 20)
			whichScreen = menuScreen;
		}
		
		else if(whichScreen == menuScreen)
		{
			
			g.drawImage(menuImage, 0 , 0 , Graphics.TOP | Graphics.LEFT);
			
		}
		
		else if(whichScreen == gameoverScreen)
		{
			
			
		}
		
		else if(whichScreen == pauseScreen)
		{
			
			
		}
		
		
		else if(whichScreen == gameplayScreen)
		{
			
			g.drawImage(gamePlayImage, 0, 0, Graphics.TOP | Graphics.LEFT );
			paintmany(g, 10);
			
			//	g.drawImage(burstbubble1Image, 0, 0, Graphics.TOP | Graphics.LEFT );
			//g.drawImage(burstbubble1Image, 60, 0, Graphics.TOP | Graphics.LEFT );

			g.setColor(WHITE);
			
			g.drawImage(backImage, 240-backImage.getWidth(), 400-backImage.getHeight(), Graphics.TOP | Graphics.LEFT );

			//g.drawString("exit", 240-70, 400-40, Graphics.TOP | Graphics.LEFT );
			updateBurstedBubbles(g);
			adSprite.setPosition(10, 160);
			adSprite.paint(g);
			
			
		}
		
		
		
		else if(whichScreen == popMoreScreen)
		{
			
			if(popMoreScreenPaintOnce)
			{
				
			g.drawImage(popupImage, 20, 100, Graphics.TOP | Graphics.LEFT);
				
			}
		}
		
		g.setColor(0xff0000);
		int FPS =  cyclesThisSecond;
		
		int a = (int) (MS_PER_FRAME - timeSinceStart);
		g.drawString("FPS:"+a, 0, 0, Graphics.TOP| Graphics.LEFT);
		flushGraphics();
		
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void paintmany(Graphics g, int paintcount)
	{
		
		
		for(int i=0;i<paintcount;i++)
		g.drawImage(gamePlayImage, 0, 0, Graphics.TOP | Graphics.LEFT );

	}
	
	
	protected void pointerPressed(int x, int y) 
	{
		
		
		if(whichScreen == splashScreen)
		{
			
			whichScreen = menuScreen;
		}
		
		else
		if(whichScreen == menuScreen)
		{
			
			
			if(x >20 &&  x <220 && y>105 && y<145)
			{
				whichScreen = gameplayScreen;
			}
			else
			if(x >20 &&  x <220 && y>160 && y<205)
			{
				
				String text = "Press on the bubbles and pop to your heart's content!";
				midlet.showAlertMessage("Help", text, AlertType.INFO);
			}
			else
			if(x >20 &&  x <215 && y>215 && y<255)
			{
				
				
				String appname = midlet.getAppProperty("MIDlet-Name");
				String version = "Version: "+midlet.getAppProperty("MIDlet-Version");
				String vendor = midlet.getAppProperty("MIDlet-Vendor");
				String support = "Support: heyramb@yahoo.com";
				
				String text = appname+ "\n"+ version+ "\n"+ vendor +"\n"+support;
				System.out.println("text is "+text);
				
				midlet.showAlertMessage("About", text, AlertType.INFO);
				
			}
			
			else 
			if(x > 240-70 && x <240 && y>400-40 && y <400)
			{
				midlet.notifyDestroyed();
			}
			
		}
		
		else
		if(whichScreen == popMoreScreen)
		{
			
			if(x >20 &&  x <220 && y>150 && y<150+40)
			{
				resetGame();
				whichScreen = gameplayScreen;
				
			}
			
			else
			if(x > 20 &&  x <220 && y>210 && y<+250)
			{
				
				resetGame();
				whichScreen = menuScreen;
				
			}
			
		}
		
		
		else
		if(whichScreen == gameplayScreen)
		{
			
				if(x > 240-backImage.getWidth() && x <240 && y>400-backImage.getHeight() && y <400)
				{
					whichScreen = menuScreen;
				}
				
				
				else
				{
			
			
			
			System.out.println(" x is "+x+ " and y is "+y);
			int bx = 0;
			int by = 0;
			int jup= 4;
			String foundbubble = "";
			
			for(int i=0; i<6; i ++)
			{
			bx =0;
			jup =4;
			if(i%2!=0)
			{
			bx =30;
			jup =3;
			}
			for(int j=0; j<jup; j ++)
			{
				
				if(x > bx &&  x <bx+60 && y>by && y<by+60)
				{
					
					
					
					System.out.println("bubble is "+i+j);
					
//					b.getx("bubble"+i+j);
//					b.gety("bubble"+i+j);
//					b.setx("bubble"+i+j, ""+x);
//					b.sety("bubble"+i+j, ""+y);
					
					foundbubble = "bubble"+i+j;
					//break;
				}
				bx += 60;
			}
			by +=60;
			}
			
			
			if(!b.isBursted(foundbubble))
			{
			
			burstCount++;
			b.burst(foundbubble);
			updateBurstedBubbles(getGraphics());
			playBurstMusic();
			
			if(burstCount >= 21)
			{
				whichScreen = popMoreScreen;
				popMoreScreenPaintOnce = true;
			}
			
			}
			
			//for(int i=0; i <)
		}
				
				
				
		}
		
		
		// TODO Auto-generated method stub
		super.pointerPressed(x, y);
	}
	
	
	
	
	public void playBurstMusic()
	{
		
		if(MUSIC)
		{
			
			burstmusic.playSound(0);
			
		}
		
	}
	
	public void playSplashMusic()
	{
		
		if(MUSIC)
		{
			
		
			
		}
		
	}
	
	
	public void playGameoverMusic()
	{
		
		if(MUSIC)
		{
			
		}
		
	}
	
	
	

}
