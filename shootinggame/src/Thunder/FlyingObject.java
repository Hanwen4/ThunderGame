package Thunder;
import java.awt.image.BufferedImage;

public abstract class FlyingObject {
	protected BufferedImage image;
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	public abstract void step();
	
	public abstract boolean outOfBounds();
	
	//���˱��ӵ����
	public boolean shootBy(Bullet obj) {
		int x1 = this.x;
		int x2 = this.x + this.width;
		int y1 = this.y;
		int y2 = this.y + this.height;
		int x = obj.x;
		int y = obj.y;
		
		return x > x1 && x < x2 && y > y1 && y < y2;
	}
}
