package Thunder;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Hero extends FlyingObject{
	private int life;
	private int doublefire;
	private BufferedImage[] images;
	private int index;//Э��ͼƬ���л�
	public Hero() {
		this.image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
		life = 3;
		doublefire = 0;
		images = new BufferedImage[]{ShootGame.hero0, ShootGame.hero1};
		index = 0;
	}
	public void step() {
		/*index++;
		int a = index / 5;
		int b = a % 2;
		image = images[b];*/
		image = images[index++ / 5 % images.length];
	}
	public Bullet[] shoot() {
		
		int xStep = this.width/4;//�ķ�֮һӢ�۵Ŀ��
		int yStep = 20;
		if(doublefire > 0) {//˫���ӵ�
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep, this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep, this.y-yStep);
			doublefire -= 2;
			return bs;
		}
		else {//�����ӵ�
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep, this.y-yStep);
			return bs;
		}
	}
	
	public int showdbfire() {
		return doublefire;
	}
	
	public void moveTo(int x, int y) {//Ӣ��������궯������x��y�����꣩
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}
	public boolean outOfBounds() {
		return false;
	}
	
	public void addLife() {
		life++;
	}
	
	public void addDoubleFire() {
		doublefire += 40;
	}
	
	public int getLife() {
		return life;
	}
	
	public void subLife() {
		life--;
	}
	
	public void clearDoublefire() {
		doublefire = 0;
	}
	
	public boolean hit(FlyingObject other) {//Ӣ����л�����ײ
		int x1 = other.x - this.width;
		int x2 = other.x + other.width;
		int y1 = other.y - this.height;
		int y2 = other.y + other.height;
		int x = this.x;
		int y = this.y;
		
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
 }







































