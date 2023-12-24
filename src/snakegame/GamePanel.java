/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 *
 * @author skylar
 */
public class GamePanel extends javax.swing.JPanel implements ActionListener {

    /**
     * Creates new form GamePanel
     */
    public static int screenWidth=600;
    public static int screenHeight=600;
    public static int unitSize=25;
    Random random=new Random();
    public static int appleX;
    public static int appleY;
    public static int gameUnits=(screenWidth*screenHeight)/unitSize;
    final int[]x=new int[gameUnits];
    final int[]y=new int[gameUnits];
    int applesEaten=0;
    int bestScore=0;
    int bodyParts=7;
    char direction='R';
    boolean running=false;
    Timer timer;
    
    public GamePanel() {
        initComponents();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame()
    {   
        timer=new Timer(100,this);
        timer.start();
        newApple();
        running=true;
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        if(running)
        {
            for(int i=0; i<screenWidth/unitSize; i++ )
            {
                g.setColor(Color.gray);
                g.drawLine(i*unitSize, 0, i*unitSize, screenHeight);
                g.drawLine(0, i*unitSize, screenWidth, i*unitSize);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);
        
            for(int i=0; i<bodyParts; i++)
            {
                if(i==0)
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
                else
                {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
        }
        else
        {            
            gameOver(g);                
        }
        
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metric=getFontMetrics(g.getFont());
        g.drawString("Scores "+applesEaten, (screenWidth-metric.stringWidth("Scores "+applesEaten))/2, g.getFont().getSize());
    }
    
    public void newApple()
    {
        appleX=random.nextInt((int)(screenWidth/unitSize))*unitSize;
        appleY=random.nextInt((int)(screenHeight/unitSize))*unitSize;
    }
    
    public void move()
    {
        for(int i=bodyParts; i>0; i--)
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        
        switch(direction)
        {
            case 'R': 
                x[0]=x[0]+unitSize; break;
            case 'L': 
                x[0]=x[0]-unitSize; break;
            case 'U': 
                y[0]=y[0]-unitSize; break;
            case 'D': 
                y[0]=y[0]+unitSize; break;
        }
    }
    
    public void checkApple()
    {
        if(x[0]==appleX && y[0]==appleY)
        {
            applesEaten++;
            bodyParts++;
            newApple();
        }
        
    }
    
    public void checkCollisions()
    {
        for(int i=bodyParts; i>0; i--)
        {
            if(x[0]==x[i] && y[0]==y[i])
            {
                running=false;
            }
        }
        if(x[0]>screenWidth)
        {
            running=false;
        }
        if(x[0]<0)
        {
            running=false;
        }
        if(y[0]>screenHeight)
        {
            running=false;
        }
        if(y[0]<0)
        {
            running=false;
        }
        if(!running)
        {
            timer.stop();
        }
    }
    
    public void gameOver(Graphics g)
    {
        if(applesEaten > bestScore)
        {
            bestScore=applesEaten;
            g.setColor(Color.green);
            g.setFont(new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("New High Score "+bestScore, (screenWidth-metrics.stringWidth("New High Score "+bestScore))/2, (screenHeight/6));
        }
        else
        {
            g.setColor(Color.green);
            g.setFont(new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("Best Score "+bestScore, (screenWidth-metrics.stringWidth("Best Score "+bestScore))/2, (screenHeight/6));
        }        
        
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metric=getFontMetrics(g.getFont());
        g.drawString("Game Over", (screenWidth-metric.stringWidth("Game Over"))/2, screenHeight/2);
        
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free",Font.BOLD, 30));
        FontMetrics metrics=getFontMetrics(g.getFont());
        g.drawString("Press ENTER To Restart", (screenWidth-metrics.stringWidth("Press ENTER To Restart"))/2, (screenHeight*2/3)-40);
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            if(running==true)
            {
                switch(e.getKeyCode())
                {
                    case KeyEvent.VK_RIGHT:
                        if(direction!='L')
                        {
                            direction='R';
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if(direction!='R')
                        {
                            direction='L';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if(direction!='D')
                        {
                            direction='U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(direction!='U')
                        {
                            direction='D';
                        }
                        break;
                }                    
            }
            else if(running==false)
            {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    for(int i=bodyParts; i>0; i--)
                    {
                        x[i]=0;
                        y[i]=0;
                    }
                    x[0]=0;
                    y[0]=0;
                    bodyParts=7;
                    applesEaten=0;
                    direction='R';
                    startGame();
                }
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(java.awt.Color.black);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
