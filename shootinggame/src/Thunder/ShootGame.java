package Thunder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Font;

public class ShootGame extends JPanel{
	public static final int WIDTH = 400;//���ڵĿ�
	public static final int HEIGHT = 654;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;

	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
		}catch(Exception e) {
			e.printStackTrace();//�ֱ���ÿ��ͼ�������쳣����������
		}
	}
	
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAMEOVER = 3;
	private int state = START;
	
	private Hero hero = new Hero();//һ��Ӣ�ۻ�
	private FlyingObject[] flyings = {};//һ�ѵ���
	private Bullet[] bullets = {};//һ���ӵ�
	
	int flyEnteredIndex = 0;
	
	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20);
		if(type < 4) {
			return new Bee();
		}
		else {
			return new Airplane();
		}
	}
	
	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()) {
				flyingLives[index] = f;
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);
		
        Bullet[] bulletLives = new Bullet[bullets.length];
        index = 0;
        for(int i = 0; i < bullets.length; i++) {
        	Bullet b = bullets[i];
        	if(! b.outOfBounds()) {
        		bulletLives[index] = b;
        		index++;
        	}
        }
		bullets = Arrays.copyOf(bulletLives, index);
	}
	
	public void enterAction() {
		flyEnteredIndex++;
		if(flyEnteredIndex % 40 == 0) {
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1);//ÿ������һ��
			flyings[flyings.length-1] = obj;
		}
	}
	
	public void stepAction() {
		hero.step();
		for(int i = 0; i < flyings.length; i++) {
			flyings[i].step();
		}
		for(int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}
	
	int shootIndex = 0;
	public void shootAction() {
		shootIndex++;
		if(shootIndex % 30 == 0) {
			Bullet[] bs = hero.shoot();//��ȡ�ӵ�����
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
		}
	}
	
	public void bangAction() {
		for(int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			bang(b);
		}
	}
	
	int score = 0;	
	public void bang(Bullet b) {


		int index = -1;
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if(f.shootBy(b)) {
				index = i;
				break;
			}
		}
		if(index != -1) {
			FlyingObject one = flyings[index];
			if(one instanceof Enemy) {
				Enemy e = (Enemy) one;
				score += e.getScore();
			}
			if(one instanceof Award) {
				Award a  = (Award) one;
				int type = a.get_type();
				switch(type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = t;
			flyings = Arrays.copyOf(flyings, flyings.length-1);
		}
	}
	
	public void hitAction() {
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if(hero.hit(f)) {
				hero.subLife();
				hero.clearDoublefire();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length-1];
				flyings[flyings.length-1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
			}
		}
	}
	
	public void checkGameoverAction() {
		if(hero.getLife() <= 0) {
			state = GAMEOVER;
		}
	}
	
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			//��д
			public void mouseMoved(MouseEvent e) {
				if(state == RUNNING) {
					int x = e.getX();
					int y = e.getY();//��ȡ����x�����y����
					hero.moveTo(x, y);
				}
			}
			public void mouseClicked(MouseEvent e) {
				switch(state) {
				case START:
					state = RUNNING;
					break;
				case GAMEOVER:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			public void mouseExited(MouseEvent e) {
				if(state == RUNNING) {
					state = PAUSE;
				}
			}
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE) {
					state = RUNNING;
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		
		Timer timer = new Timer();   
		int interval = 10;//ʱ����������
		timer.schedule(new TimerTask() {
			public void run() {//��ʱ�ɵ��Ǹ���
				if(state == RUNNING) {//����״̬��
					enterAction();//�����볡���л���С�۷�
					stepAction();
					shootAction();//Ӣ�۷����ӵ�
					bangAction();
					hitAction();
					checkGameoverAction();
				}
				repaint();//���µ���paint
			}
		}, interval, interval);
	}
	
	public void paint(Graphics g) {//��ʼ��ͼ��������
		g.drawImage(background, 0,0,null);//�̶���ʽ
		 paintHero(g);
		 paintFlyingObjects(g);
		 paintBullets(g);
		 paintScoreAndLife(g);
		 paintState(g);
	}
	
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	
	public void paintFlyingObjects(Graphics g) {
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}
	
	public void paintBullets(Graphics g){
		for(int j = 0; j < bullets.length; j++) {
			Bullet b = bullets[j];
			g.drawImage(b.image, b.x,b.y, null);
		}
	}
	
	public void paintScoreAndLife(Graphics g) {//g���ǻ��ʶ���
		g.setColor(new Color(0x00FF00));
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		g.drawString("SCORE: " + score, 10, 25);
		g.drawString("LIFE: " + hero.getLife(), 10, 45);
		g.drawString("FIRE: " + hero.showdbfire(), 10, 65);//λ������
	}
	
	public void paintState(Graphics g) {
		switch(state) {//����״̬�Ĳ�ͬ����ͼ
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAMEOVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");//�������ڶ���
		ShootGame game = new ShootGame();//����������
		frame.add(game);//�������ӵ�������
		
		frame.setSize(WIDTH, HEIGHT);//���ô��ڵĴ�С
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//����Ĭ�Ϲرմ��ڣ����ڹر�ʱ�˳�����һ�㶼��
		frame.setLocationRelativeTo(null);//���þ�����ʾ
		frame.setVisible(true);//���ô��ڿɼ�
		game.action();
	}

}
