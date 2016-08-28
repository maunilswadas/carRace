/**
* Applet code for a car racing game
* @author G-13 
*/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.Random;

/**
*<applet code="NewApplet" width=650 height=600>
*<param name="img" value="redfinal.png">
*<param name="yellow" value="yellowfinal.png">
*<param name="back" value="back.png">
*<param name="sou" value="Mario.wav">
*<param name="die" value="mariodie.wav">
*<param name="win" value="win.wav">
*</applet>
*/

/*
This class implements Runnable for thread usage and
KeyListener for moving car.
*/
public class NewApplet extends Applet implements Runnable,KeyListener {
	int min=0,sec=0; 			//clock paramerters
	int a[]=new int[50];		//yellow car position
	Thread t,u;					//thread 't' for yellow cars and thread 'u' for clock
	Font f;
	volatile int flag=2;		//initial flag
    int acc=100;				//acceleration of yellow cars
	int y1=10,x2=0,chey=0;		//co-ordinates of starting position. chey=checkpoint, y1=y value of yellow car, x2=x value of redcar
    Image img,yellow,back;		//img=redcar, yellow=yellowcar, back=background
    AudioClip audioClip,die,win;	//audioClip=Mario bros theme, die=crash sound

    public void init() 	{
		Random r= new Random();
		for(int i=0;i<50;i++)
			a[i]=r.nextInt(240);	//generation random number for yellow cars
		addKeyListener(this);		
		requestFocus();				//priority to KeyListener 
		f = new Font("Arial", Font.BOLD, 22);
		
		setFont(f);
        
        img = getImage(getDocumentBase(),getParameter("img"));					//red car
        yellow = getImage(getDocumentBase(),getParameter("yellow"));			//yellow car
        back = getImage(getDocumentBase(),getParameter("back"));				//background image
        audioClip = getAudioClip(getDocumentBase(),getParameter( "sou"));		//main theme
        die = getAudioClip(getDocumentBase(),getParameter( "die"));	
        win = getAudioClip(getDocumentBase(),getParameter("win"));			//sound after crash
	}
	
	public void start() {}
	
	public void run() {
		audioClip.play();				//start main audio theme
		while(flag==0 || flag==3 || flag==6) {			
			try {
				repaint();
			
				if(Thread.currentThread().getName().equals("thread1")) {		//speed of yellow car
					Thread.sleep(acc);
					y1+=12;
				} else 	{
					Thread.sleep(500);				//speed of checkpoint and clock
                    chey+=12;
                    if(Thread.currentThread().getName().equals("thread2") && x2<-125 || x2>125)
			chey-=6;
		
					sec++;
					if(sec==60) {
						min++;
						sec=0;
					}
				}
			}catch(InterruptedException e){ }
		}

		if(flag==1) {					//to stop audio
				audioClip.stop();			
				die.play();
		}

		if(flag==9) {
			audioClip.stop();
			win.play();
		}
	}

	public void update(Graphics g) {
    	g.drawImage(back,0,chey,this);					//background
		g.drawImage(back,0,chey-500,this);
		g.drawImage(back,0,chey-1000,this);
		if(flag==0 || flag==3 || flag==6)
		g.drawImage(img,275+x2,500,this);

		for(int i=0;i<50;i++) {	
				g.drawImage(yellow,160+a[i],y1,this);
				if((y1>440 && y1<500)&& (((75+x2)<a[i] && (115+x2)>a[i] )|| ((325+x2)>(160+a[i]) && (325+x2)<(200+a[i])) || (x2+120>a[i]) && ((x2+100)<a[i]) ) ) { //crash condition
                            flag=1;
				}
			//if(sec >=45)
                         //   flag=1;				//timer out
                        if(chey>500){
                            flag+=3;				//checkpoint reached
                            chey=0;
                           //g.drawLine(150,chey,450,chey);
                             // g.drawImage(back,0,chey-500,this);
                        	//g.drawImage(back,0,chey-1000,this);
                        }
                            y1-=200;
			}       
			y1+=10000;
		paint(g);
    }

	public void paint(Graphics g) {
		
    	g.drawString("Min: "+ min+" sec: "+sec,480,100);
    	
		if(flag==2)
                {
                    g.drawString("PRESS ENTER",290,280);
                    g.drawString(" Time limit :",300 , 310);
                    g.drawString("Checkpoint-1 :0 min 45 sec", 240, 340);
                    g.drawString("Checkpoint-2 :1 min 25 sec", 240, 380);
                    g.drawString("finish line  :2 min 07 sec", 240, 420);
                    
                    g.drawString("Use arrow keys for control", 240, 460);
                }
		else if(flag==0 || flag==3 ||flag==6) {               
            g.drawLine(150,chey,450,chey);
           
			if(flag==6)
                               g.drawString("FINISH",270,chey);
                        else
                            g.drawString("CHECKPOINT",270,chey );
                        
		}
                else if(flag==3 || flag==6){
                //chey=0;
                } //g.drawString("CONGRATULATIONS",260,280);
		else if(flag==9){
                    g.drawString("CONGRATULATIONS",260,280);
                    g.drawString("Min: "+ min+" sec: "+sec,260,310);
                    }
		else { 
			g.drawString("GAME OVER",260,280);
			g.drawString("Min: "+ min+" sec: "+sec,260,310);
		}
                if((chey<500 && sec>45 && flag<3) |(chey<500 && min==1 && sec>27 && flag==3) |(chey<500 && min==2 && sec>7 && flag==6))
                        {
                          flag=1;
                        }
     
}

/*
Implementaion of methods of Interface Keylistener
*/
public void keyPressed(KeyEvent ke){
	int key=ke.getKeyCode();
		
	switch(key){
		case KeyEvent.VK_LEFT:
			x2-=10;
			if(x2<-260){
				x2+=10;
			chey-=3;}
			break;
		case KeyEvent.VK_RIGHT:
			x2+=10;
			if(x2>250){
				x2-=10;
			chey-=3;}
			break;
        case KeyEvent.VK_UP:
            acc-=5;
            break;
        case KeyEvent.VK_DOWN:
            acc+=5;
            break;
		case KeyEvent.VK_ENTER:
			flag=0;
			t=new Thread(this,"thread1");
			u=new Thread(this,"thread2");
			t.start();
			u.start();
            break;
	}

	repaint();
}

public void keyReleased(KeyEvent ke){}
public void keyTyped(KeyEvent ke){}
}