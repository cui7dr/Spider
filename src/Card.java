import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card extends JLabel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	// 纸牌的位置
	Point point = null;
	Point initPoint = null;

	int value = 0;
	int type = 0;
	String name = null;
	Container pane = null;
	Spider main = null;
	boolean canMove = false;
	boolean isFront = false;
	Card previousCard = null;

	// 构造函数
	public Card(String name, Spider spider) {
		super();
		this.type = new Integer(name.substring(0, 1)).intValue();
		this.value = new Integer(name.substring(2)).intValue();
		this.name = name;
		this.main = spider;
		this.pane = this.main.getContentPane();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.setSize(71, 96);
		this.setVisible(true);
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void flashCard(Card card) {
		new Flash(card).start();// 启动 flash 线程
		if (main.getNextCard(card) != null) {// 不停获得下一张牌直到完成
			card.flashCard(main.getNextCard(card));
		}
	}

	class Flash extends Thread {
		private Card card = null;

		public Flash(Card card) {
			this.card = card;
		}

		// 线程的 run() 方法
		public void run() {
			boolean is = false;
			ImageIcon icon = new ImageIcon("images/white.gif");// 纸牌正面白色
			for (int i = 0; i < 4; i++) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (is) {
					this.card.turnFront();
					is = !is;
				} else {
					this.card.setIcon(icon);
					is = !is;
				}
				card.updateUI();// 根据当前外观将 card 的 UI 重置
			}
		}
	}

	// 点击鼠标
	public void mousePressed(MouseEvent mp) {
		point = mp.getPoint();
		main.setNA();
		this.previousCard = main.getPreviousCard(this);
	}

	// 释放鼠标
	@SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent mr) {
		Point point = ((JLabel) mr.getSource()).getLocation();
		// 判断可行列
		int n = this.whichColumnAvailaable(point);
		if (n == -1 || n == this.whichColumnAvailaable(this.initPoint)) {
			this.setNextCardLocation(null);
			main.table.remove(this.getLocation());
			this.setLocation(this.initPoint);
			main.table.put(this.initPoint, this);
			return;
		}
		point = main.getLastCardLocation(n);
		boolean isEmpty = false;
		Card card = null;
		if (point == null) {
			point = main.getGroundLabelLocation(n);
			isEmpty = true;
		} else {
			card = (Card) main.table.get(point);
		}

		if (isEmpty || (this.value + 1 == card.getCardValue())) {
			point.y += 40;
			if (isEmpty)
				point.y -= 20;
			this.setNextCardLocation(point);
			main.table.remove(this.getLocation());
			point.y -= 20;
			this.setLocation(point);
			main.table.put(point, this);
			this.initPoint = point;
			if (this.previousCard != null) {
				this.previousCard.turnFront();
				this.previousCard.setCanMove(true);
			}
			this.setCanMove(true);
		} else {
			this.setNextCardLocation(null);
			main.table.remove(this.getLocation());
			this.setLocation(this.initPoint);
			main.table.put(this.initPoint, this);
			return;
		}
		point = main.getLastCardLocation(n);
		card = (Card) main.table.get(point);
		if (card.getCardValue() == 1) {
			point.y -= 240;
			card = (Card) main.table.get(point);
			if (card != null && card.isCardCanMove()) {
				main.haveFinish(n);
			}
		}
	}

	// 放置纸牌
	@SuppressWarnings("unchecked")
	private void setNextCardLocation(Object object) {
		Card card = main.getNextCard(this);
		if (card != null) {
			if (point == null) {
				card.setNextCardLocation(null);
				main.table.remove(card.getLocation());
				card.setLocation(card.initPoint);
				main.table.put(card.initPoint, card);
			} else {
				point = new Point(point);
				point.y += 20;
				card.setNextCardLocation(point);
				point.y -= 20;
				main.table.remove(card.getLocation());
				card.setLocation(point);
				main.table.put(card.getLocation(), card);
				card.initPoint = card.getLocation();
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	// 用鼠标拖动纸牌
	public void mouseDragged(MouseEvent arg0) {
		if (canMove) {
			int x = 0;
			int y = 0;
			Point point1 = arg0.getPoint();
			x = point1.x - point.x;
			y = point1.y - point.y;
			this.moving(x, y);
		}

	}

	// 移动 (x, y) 个位置
	@SuppressWarnings("unchecked")
	public void moving(int x, int y) {
		Card card = main.getNextCard(this);
		Point point1 = this.getLocation();
		pane.setComponentZOrder(this, 1);// 将组件移动到容器中指定的顺序索引
		main.table.remove(point1);// 在 Hashtable 中保存新的节点信息
		point1.x += x;
		point1.y += y;
		this.setLocation(point1);
		main.table.put(point1, this);
		if (card != null) {
			card.moving(x, y);
		}
	}

	public void mouseMoved(MouseEvent arg0) {

	}

	// 判断是否能移动
	public void setCanMove(boolean can) {
		this.canMove = can;
		Card card = main.getPreviousCard(this);
		if (card != null && card.isCardFront()) {
			if (!can) {
				if (!card.isCardCanMove()) {
					return;
				} else {
					card.setCanMove(can);
				}
			} else {
				if (this.value + 1 == card.getCardValue() && this.type == card.getCardType()) {
					card.setCanMove(can);
				} else {
					card.setCanMove(false);
				}
			}
		}
	}

	// 判断纸牌是否正面
	public boolean isCardFront() {
		return this.isFront;
	}

	// 是否能够移动
	public boolean isCardCanMove() {
		return this.canMove;
	}

	// 获得 card 的内容值
	public int getCardValue() {
		return value;
	}

	// 获得 card 的类型
	public int getCardType() {
		return type;
	}

	// 纸牌显示背面
	public void turnRear() {
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.isFront = false;
		this.canMove = false;
	}

	// 将纸牌移动到 point
	public void moveto(Point point2) {
		this.setLocation(point2);
		this.initPoint = point2;
	}

	// 判断可用列（返回 int 值）
	public int whichColumnAvailaable(Point point) {
		int x = point.x;
		int y = point.y;
		int a = (x - 20) / 101;
		int b = (x - 20) % 101;
		if (a != 9) {
			if (b > 30 && b <= 71) {
				a = -1;
			} else if (b > 71) {
				a++;
			}
		} else if (b > 71) {
			a = -1;
		}

		if (a != -1) {
			Point point1 = main.getLastCardLocation(a);
			if (point1 == null) {
				point1 = main.getGroundLabelLocation(a);
			}
			b = y - point1.y;
			if (b <= -96 || b >= 96) {
				a = -1;
			}
		}
		return a;
	}

	// 纸牌显示正面
	public void turnFront() {
		this.setIcon(new ImageIcon("images/" + name + ".gif"));
		this.isFront = true;
	}

}
