package Thunder;

import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1;
	private int ySpeed = 2;
	private int Awardtype;
	public Bee() {
		this.image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();//�������������
		x = rand.nextInt(ShootGame.WIDTH - this.width);//0�����ڿ��ȥ�۷��֮��
		y = -this.height;//y����С�۷�ĸ�
		Awardtype = rand.nextInt(2);//0 or 1
	} 
	public int get_type() {return Awardtype;}
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if(this.x >= ShootGame.WIDTH - this.width) {
			xSpeed = -1;
		}
		if(this.x <= 0) {
			xSpeed = 1;
		}
	}
	public boolean outOfBounds() {
		return this.y >= ShootGame.HEIGHT;
	}
}
