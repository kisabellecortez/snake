import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;
import javax.swing.Timer;
import java.util.*; 

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_HEIGHT = 600; 
    static final int SCREEN_WIDTH = 600; 
    static final int UNIT_SIZE = 25; 
    static final int NUM_SPACES = (SCREEN_HEIGHT * SCREEN_WIDTH)/UNIT_SIZE; 
    static final int DELAY = 75;
    final int x[] = new int[NUM_SPACES]; 
    final int y[] = new int [NUM_SPACES]; 
    final int bombX[] = new int[10]; 
    final int bombY[] = new int[10]; 
    int bodyParts = 3; 
    int numBombs = 0;
    int score; 
    int appleX; 
    int appleY; 
    char direction = 'R'; 
    
    boolean running = false;
    boolean gameOver = false;  
    Timer timer; 
    Random r_apple; 
    Random r_bomb; 

    private int page = 0; 
    private int mode = 0; 
    private JButton start = new JButton("START"); 
    private JButton m_invert = new JButton("INVERT"); 
    private JButton m_bombs = new JButton("BOMBS"); 
    private JButton m_classic = new JButton("CLASSIC"); 
    private JButton retry = new JButton("PLAY AGAIN"); 
    private JButton modes = new JButton("MODE SELECT"); 

    GamePanel(){
        r_apple = new Random(); 

        this.setLayout(null); 
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); 
        this.setBackground(Color.black); 
        this.setFocusable(true); 
        this.addKeyListener(new MyKeyAdapter()); 

        start.setBounds(150, 300, 300, 100);
        m_classic.setBounds(50, 125, 200, 100);
        m_bombs.setBounds(50, 250, 200, 100); 
        m_invert.setBounds(50, 375, 200, 100); 
        retry.setBounds(225, 350, 150, 75); 
        modes.setBounds(225, 450, 150, 75); 

        this.add(start); 
        this.add(m_classic); 
        this.add(m_bombs); 
        this.add(m_invert); 
        this.add(retry); 
        this.add(modes);  
        
        start.addActionListener(this); 
        m_classic.addActionListener(this); 
        m_bombs.addActionListener(this); 
        m_invert.addActionListener(this); 
        retry.addActionListener(this); 
        modes.addActionListener(this); 
        
        start.setVisible(true); 
        m_classic.setVisible(false); 
        m_bombs.setVisible(false); 
        m_invert.setVisible(false); 
        retry.setVisible(false); 
        modes.setVisible(false); 

        for(int i = 0; i < 10; i++){
            bombX[i] = -100; 
            bombY[i] = -100; 
        }
        
    }

    public void startGame(){
        newApple(); 
        running = true; 
        timer = new Timer(DELAY, this); 
        timer.start(); 

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g); 
        draw(g); 

    }

    public void draw(Graphics g){

        if(running){
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); 
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); 
            }   

            g.setColor(Color.green); 
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 

            if(mode == 2){
                for(int i = 0; i < 10; i++){
                    g.setColor(Color.red); 
                    g.fillOval(bombX[i], bombY[i], UNIT_SIZE, UNIT_SIZE); 
                }
            }

            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.blue); 
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); 
                }

                else{
                    g.setColor(new Color(51, 153, 255)); 
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.blue); 
            g.setFont(new Font("Dialog", Font. BOLD, 20)); 
            FontMetrics metrics = getFontMetrics(g.getFont()); 
            g.drawString("Score: " + score, SCREEN_WIDTH - metrics.stringWidth("Score: " + score), g.getFont().getSize());

        }

        else if(!running && gameOver){
            gameOver(g); 
        }

        else if(page == 0){
            g.setColor(Color.green); 
            g.setFont(new Font("Dialog", Font.BOLD, 80)); 
            FontMetrics metrics1 = getFontMetrics(g.getFont()); 
            g.drawString("SNAKE", (SCREEN_WIDTH - metrics1.stringWidth("SNAKE"))/2, SCREEN_HEIGHT/3); 

        }

        else if(page == 1){
            g.setColor(Color.green); 
            g.setFont(new Font("Dialog", Font.BOLD, 75)); 
            FontMetrics metrics1 = getFontMetrics(g.getFont()); 
            g.drawString("MODE SELECT", (SCREEN_WIDTH - metrics1.stringWidth("MODE SELECT"))/2, 75); 
            
            g.setColor(Color.green); 
            g.setFont(new Font("Dialog", Font.PLAIN, 15));  
            g.drawString("CLASSIC: collect apples to grow larger", 260, 175); 

            g.setColor(Color.green); 
            g.setFont(new Font("Dialog", Font.PLAIN, 15));  
            g.drawString("BOMBS: avoid red the red bombs on the field", 260, 300); 

            g.setColor(Color.green); 
            g.setFont(new Font("Dialog", Font.PLAIN, 15));  
            g.drawString("INVERT: the keyspaces are inverted", 260, 425); 

        }

    }

    public void newApple(){
        appleX = r_apple.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; 
        appleY = r_apple.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; 

        if(mode == 2){
            bombX[numBombs] = r_bomb.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
            bombY[numBombs] = r_bomb.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        }
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1]; 
            y[i] = y[i-1]; 
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE; 
                break; 
            case 'D':
                y[0] = y[0] + UNIT_SIZE; 
                break; 
            case 'R':
                x[0] = x[0] + UNIT_SIZE; 
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; 
                break;
        }

    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++; 
            score++; 
            newApple(); 

            if(mode == 2){
                numBombs++; 
            }
        }

    }

    public void checkCollisions(){
        if(x[0] < 0 || x[0] > SCREEN_WIDTH){
            running = false; 
            gameOver = true; 
        }

        if(y[0] < 0 || y[0] > SCREEN_HEIGHT){
            running = false; 
            gameOver = true; 
        }

        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false; 
                gameOver = true; 
            }
        }

        if(mode == 2){
            for(int i = 0; i < 10; i++){
                if((x[0] == bombX[i]) && (y[0] == bombY[i])){
                    running = false; 
                    gameOver = true; 
                }
            }
        }
        
    }

    public void gameOver(Graphics g){
        g.setColor(Color.red); 
        g.setFont(new Font("Dialog", Font.BOLD, 75)); 
        FontMetrics metrics1 = getFontMetrics(g.getFont()); 
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/3); 

        g.setColor(Color.red); 
        g.setFont(new Font("Dialog", Font. BOLD, 20)); 
        FontMetrics metrics2 = getFontMetrics(g.getFont()); 
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + score))/2, SCREEN_HEIGHT/3 + 95);
        
        for(int i = NUM_SPACES - 1; i >= 0; i--){
            x[i] = 0; 
            y[i] = 0; 
        }

        bodyParts = 3;  
        direction = 'R'; 
        timer.stop(); 

        if(mode == 2){
            for(int i = 0; i < 10; i++){
                bombX[i] = -100; 
                bombY[i] = -100; 
            }

            numBombs = 0; 
        }
        score = 0;

        retry.setVisible(true); 
        modes.setVisible(true); 
    }

    @Override 
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == start){
            start.setVisible(false); 
            m_classic.setVisible(true); 
            m_bombs.setVisible(true); 
            m_invert.setVisible(true); 
            page = 1; 
        }

        if(e.getSource() == m_classic){
            m_classic.setVisible(false); 
            m_bombs.setVisible(false); 
            m_invert.setVisible(false); 
            mode = 1; 
            startGame();
        }

        if(e.getSource() == m_bombs){
            r_bomb = new Random(); 
            m_classic.setVisible(false); 
            m_bombs.setVisible(false); 
            m_invert.setVisible(false); 
            mode = 2;
            startGame();
        }

        if(e.getSource() == m_invert){
            m_classic.setVisible(false); 
            m_bombs.setVisible(false); 
            m_invert.setVisible(false); 
            mode = 3; 
            startGame(); 
        }

        if(running){
            move(); 
            checkApple(); 
            checkCollisions(); 
        }

        repaint(); 

        if(e.getSource() == modes){
            gameOver = false; 
            running = false; 
            retry.setVisible(false); 
            modes.setVisible(false); 
            m_classic.setVisible(true); 
            m_bombs.setVisible(true); 
            m_invert.setVisible(true); 
            page = 1; 
        }

        if(e.getSource() == retry){
            retry.setVisible(false); 
            modes.setVisible(false); 
            gameOver = false; 
            running = true; 
            startGame(); 
        }

    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override 
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(mode != 3){
                        if(direction != 'R'){
                            direction = 'L'; 
                        }
                    }
                    else{
                        if(direction != 'L'){
                            direction = 'R'; 
                        }
                    }
                    
                    break; 
                
                case KeyEvent.VK_RIGHT:
                    if(mode != 3){
                        if(direction != 'L'){
                            direction = 'R'; 
                        }
                    }   

                    else{
                        if(direction != 'R'){
                            direction = 'L'; 
                        }
                    }

                    break; 

                case KeyEvent.VK_UP:
                    if(mode != 3){
                        if(direction != 'D'){
                            direction = 'U'; 
                        }
                    }

                    else{
                        if(direction != 'U'){
                            direction = 'D'; 
                        }
                    }

                    break; 
                
                case KeyEvent.VK_DOWN:
                    if(mode != 3){
                        if(direction != 'U'){
                            direction = 'D'; 
                        }
                    }
                    
                    else{
                        if(direction != 'D'){
                            direction = 'U'; 
                        }
                    }
                    break; 
            }

        }
    }
    
}
