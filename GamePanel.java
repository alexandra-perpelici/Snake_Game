import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // the size of the objects in the game
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // HOW MANY OBJECTS WE CAN FIT ON THE SCREEN
    static final int DELAY = 75; // how fast the game is running
    final int x[] = new int[GAME_UNITS]; // x coordinates of the snake
    final int y[] = new int[GAME_UNITS]; // y coordinates of the snake
    int bodyParts = 6; // we ll begin with 6 body parts on the snake

    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // direction of the snake
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
         random = new Random();
         this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
         this.setBackground(Color.BLACK);
         this.setFocusable(true);
         this.addKeyListener(new MyKeyAdapter());
         startGame();
    }
    public void startGame()
    {   newApple(); // create a new apple on the screen
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();


    }
    public void paintComponent(Graphics g)
    {    super.paintComponent(g);
         draw(g);

    }
    public void draw(Graphics g)
    {

        if(running) {
            //DRAW THE GRID ON THE PANEL
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // place the apple at the appleX and appleY coordinates having the size of UNIT_SIZE


            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) // head of snake
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else // body of snake
                {   g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                   // g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH- metrics.stringWidth("Score: " + applesEaten))/2,  g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void newApple()
    {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; // apple appears somewhere along the X axis
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; // apple appears somewhere along the Y axis

    }

    public void move()
    {
            for( int i = bodyParts;i>0;i--)
            {
                // shift the body parts of the snake around
                x[i] = x[i-1];
                y[i] = y[i-1];

            }
            //switch the direction of the snake
            switch(direction)
            {   case 'U':
                    y[0] = y[0]-UNIT_SIZE;
                    break;
                case 'D':
                    y[0] = y[0]+UNIT_SIZE;
                    break;
                case 'L':
                    x[0] = x[0]-UNIT_SIZE;
                    break;
                case 'R':
                    x[0] = x[0] + UNIT_SIZE;
                    break;
            }
    }
    public void checkApple()
    {
        if((x[0]== appleX) && (y[0] == appleY))
        {
            bodyParts ++;
            applesEaten ++;
            newApple();


        }
    }

    public void checkCollisions()
    {
        // check if the head collides with the body
        for(int i=bodyParts;i>0;i--)
    {
        if((x[0]==x[i]) && (y[0]==y[i])) // if the head collides with the body
        {
            running = false;
        }
    }
        // if head touches left border
        if(x[0]<0) {
            running = false;
        }
        // if head touches right border
        if( x[0]> SCREEN_WIDTH)
        { running = false;}
        // if head touches top border
        if(y[0]<0)
        { running = false;}
        // if head touches bottom border
        if(y[0]>SCREEN_HEIGHT)
        {  running = false;}

        if(!running)
            timer.stop();



    }
    public void gameOver(Graphics g){

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH- metrics1.stringWidth("Score: " + applesEaten))/2,  g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH- metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2 );

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running)
        {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e)
        {  switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(direction != 'R') // the snake doesn't go into itself
                { direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT: // the snake doesn't go into itself
                if(direction != 'L')
                { direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if(direction != 'D')
                { direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if(direction != 'U')
                { direction = 'D';
                }
                break;

        }


        }
    }


}
