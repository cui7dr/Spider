import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.FontUIResource;

public class Spider extends JFrame {

	private static final long serialVersionUID = 1L;
	// ���ͱ�������ʾ�Ѷȵȼ�
	public static final int easy = 1;// ��
	public static final int natural = 2;// ��ͨ
	public static final int hard = 3;// ����

	private int grade = Spider.easy;// ���ó�ʼ�ȼ���
	private Container pane = null;

	private Card cards[] = new Card[104];// ����ֽ������
	private JLabel clickLabel = null;
	private int a = 0;
	private int c = 0;
	private int n = 0;
	private int finish = 0;

	@SuppressWarnings("rawtypes")
	Hashtable table = null;
	private JLabel groundLabel[] = null;

	public static void main(String[] args) {
		Spider spider = new Spider();
		spider.setVisible(true);
	}

	// ���캯��
	public Spider() {
		Font font = new Font("Dialog", Font.PLAIN, 12);
		@SuppressWarnings("rawtypes")
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, font);
			}
		}
		setTitle("֩��ֽ��");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);// ���ÿ�ܴ�С��1024��742��
		setJMenuBar(null);// ���� SpiderMenuBar ���󲢷��ڿ��֮��
		pane = this.getContentPane();
		pane.setBackground(new Color(0, 112, 26));// ���ñ���ɫ
		pane.setLayout(null);// �����ֹ���������Ϊ null
		clickLabel = new JLabel();
		clickLabel.setBounds(883, 606, 121, 96);
		pane.add(clickLabel);
		clickLabel.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unused")
			public void mouseRelease(MouseEvent me) {
				if (c < 60) {
					Spider.this.deal();
				}
			}
		});

		this.initCards();
		this.randomCards();
		this.setCardsLocation();
		groundLabel = new JLabel[10];

		int x = 20;
		for (int i = 0; i < 10; i++) {
			groundLabel[i] = new JLabel();
			groundLabel[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			groundLabel[i].setBounds(x, 25, 71, 96);
			x += 101;
			this.pane.add(groundLabel[i]);
		}
		this.setVisible(true);
		this.deal();

		this.addKeyListener(new KeyAdapter() {
			class Show extends Thread {
				public void run() {
					Spider.this.showEnableOperator();
				}
			}

			public void keyPressed(KeyEvent e) {
				if (finish != 8)
					if (e.getKeyCode() == KeyEvent.VK_D && c < 60) {
						Spider.this.deal();
					} else if (e.getKeyCode() == KeyEvent.VK_M) {
						new Show().start();
					}
			}
		});
	}

	// ��ʼ����Ϸ
	public void newGame() {
		this.randomCards();
		this.setCardsLocation();
		this.setGroundLiabelZOrder();
		this.deal();
	}

	// �����Ƶ�����
	public int getC() {
		return c;
	}

	// ���õȼ�
	public void setGrade(int grade) {
		this.grade = grade;
	}

	// ֽ�Ƴ�ʼ��
	public void initCards() {
		// ���ֽ���ѱ���ֵ��������ӿ�ܵ��������ȥ
		if (cards[0] != null) {
			for (int i = 0; i < 104; i++) {
				pane.remove(cards[i]);
			}
		}

		// ͨ���Ѷȵȼ�Ϊ n ��ֵ
		int n = 0;
		if (this.grade == Spider.easy) {
			n = 1;
		} else if (this.grade == Spider.natural) {
			n = 2;
		} else {
			n = 4;
		}

		// Ϊ cards ��ֵ
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 13; j++) {
				cards[(i - 1) * 13 + j - 1] = new Card((i % n + 1) + "-" + j, this);
			}
		}

		// ���ֽ�Ƴ�ʼ��
		this.randomCards();
	}

	// ���û�ԭ
	public void setNA() {
		a = 0;
		n = 0;
	}

	private void setGroundLiabelZOrder() {

	}

	// ��ʾ���ƶ��Ĳ���
	public void showEnableOperator() {
		int x = 0;
		out: while (true) {
			Point point = null;
			Card card = null;
			do {
				if (point != null) {
					n++;
				}
				point = this.getLastCardLocation(n);
				while (point == null) {
					point = this.getLastCardLocation(++n);
					if (n == 10)
						n = 0;
					x++;
					if (x == 10)
						break out;
				}
				card = (Card) this.table.get(point);
			} while (!card.isCardCanMove());
			while (this.getPreviousCard(card) != null && this.getPreviousCard(card).isCardCanMove()) {
				card = this.getPreviousCard(card);
			}
			if (a == 10) {
				a = 0;
			}
			for (; a < 10; a++) {
				if (a != n) {
					Point p = null;
					Card c = null;
					do {
						if (p != null) {
							a++;
						}
						p = this.getLastCardLocation(a);
						int z = 0;
						while (p == null) {
							p = this.getLastCardLocation(++a);
							if (a == 10)
								a = 0;
							if (a == n)
								a++;
							z++;
							if (z == 10)
								break out;
						}
						c = (Card) this.table.get(p);
					} while (!c.isCardCanMove());
					if (c.getCardValue() == card.getCardValue() + 1) {
						card.flashCard(card);
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						c.flashCard(c);
						a++;
						if (a == 10) {
							n++;
						}
						break out;
					}
				}
			}
			n++;
			if (n == 10) {
				n = 0;
			}
			x++;
			if (x == 10) {
				break out;
			}
		}
	}

	// ��� card �����������
	public Card getPreviousCard(Card card) {
		Point point = new Point(card.getLocation());
		point.y -= 5;
		card = (Card) table.get(point);
		if (card != null) {
			return card;
		}
		point.y -= 15;
		card = (Card) table.get(point);
		return card;
	}

	// ��� card �����������
	public Card getNextCard(Card card) {
		Point point = new Point(card.getLocation());
		point.y += 5;
		card = (Card) table.get(point);
		if (card != null) {
			return card;
		}
		point.y += 15;
		card = (Card) table.get(point);
		return card;
	}

	// ��õ� column �����һ���Ƶ�λ��
	public Point getLastCardLocation(int column) {
		Point point = new Point(20 + column * 101, 25);
		Card card = (Card) this.table.get(point);
		if (card == null)
			return null;
		while (card != null) {
			point = card.getLocation();
			card = this.getNextCard(card);
		}
		return point;
	}

	public Point getGroundLabelLocation(int column) {
		return new Point(groundLabel[column].getLocation());
	}

	// ���� groundlabel ���
	@SuppressWarnings("unused")
	private void setGroundLabelZOrder() {
		for (int i = 0; i < 10; i++) {
			/*
			 * @����� groundlabel �ƶ���������ָ��������˳��
			 * 
			 * @˳�� 105 + i ȷ���˻��������˳��
			 * 
			 * @�����������������һ�����ƣ����������˳�����������һ������
			 * 
			 * @������ص��ĵط������нϵ�˳�����������Ǿ��нϸ�˳������
			 */
			pane.setComponentZOrder(groundLabel[i], 105 + i);
		}
	}

	// ��Ϸ����
	@SuppressWarnings("unchecked")
	public void deal() {
		this.setNA();
		// �ж� 10 �����Ƿ����
		for (int i = 0; i < 10; i++) {
			if (this.getLastCardLocation(i) == null) {
				JOptionPane.showMessageDialog(this, "�п�λ���ܷ��ƣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		@SuppressWarnings("unused")
		int x = 20;

		for (int i = 0; i < 10; i++) {
			Point lastPoint = this.getLastCardLocation(i);
			// ����Ӧ��������
			if (c == 0) {
				lastPoint.y += 5;
			}
			// ����Ӧ��������
			else {
				lastPoint.y += 20;
			}

			table.remove(cards[c + i].getLocation());
			cards[i + c].moveto(lastPoint);
			table.put(new Point(lastPoint), cards[c + i]);
			cards[c + i].turnFront();
			cards[c + i].setCanMove(true);

			// ����� card �ƶ���������ָ����˳������
			this.pane.setComponentZOrder(cards[c + i], 1);

			Point point = new Point(lastPoint);
			if (cards[c + i].getCardValue() == 1) {
				int n = cards[c + i].whichColumnAvailaable(point);
				point.y -= 240;
				Card card = (Card) this.table.get(point);
				if (card != null && card.isCardCanMove()) {
					this.haveFinish(n);
				}
			}
			x += 101;
		}
		c += 10;
	}

	@SuppressWarnings("unchecked")
	public void haveFinish(int column) {
		Point point = this.getLastCardLocation(column);
		Card card = (Card) this.table.get(point);
		do {
			this.table.remove(point);
			card.moveto(new Point(20 + finish * 10, 580));
			// ������ƶ���������ָ����˳������
			pane.setComponentZOrder(card, 1);
			// ��ֽ���µ������Ϣ���� Hashtable
			this.table.put(card.getLocation(), card);
			card.setCanMove(false);
			point = this.getLastCardLocation(column);
			if (point == null)
				card = null;
			else
				card = (Card) this.table.get(point);
		} while (card != null && card.isCardCanMove());
		finish++;
		// ��� 8 ����ȫ����ϳɹ�������ʾ�ɹ��ĶԻ���
		if (finish == 8) {
			JOptionPane.showMessageDialog(this, "��ϲ�㣬˳��ͨ����", "�ɹ�", JOptionPane.PLAIN_MESSAGE);
		}
		if (card != null) {
			card.turnFront();
			card.setCanMove(true);
		}
	}

	// ����ֽ�Ƶ�λ��
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setCardsLocation() {
		table = new Hashtable();
		c = 0;
		finish = 0;
		n = 0;
		a = 0;
		int x = 883;
		int y = 580;
		// ��ʼ����չ����ֽ��
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 10; j++) {
				int n = i * 10 + j;
				pane.add(cards[n]);
				cards[n].turnRear();// �� card ת����
				cards[n].moveto(new Point(x, y));// �� card ���ڹ̶�λ��
				table.put(new Point(x, y), cards[n]);// �� card ��λ�ü������Ϣ����
			}
			x += 10;
		}

		x = 20;
		y = 45;
		// ��ʼ��������ʾ��ֽ��
		for (int i = 10; i > 5; i--) {
			for (int j = 0; j < 10; j++) {
				int n = i * 10 + j;
				if (n >= 104)
					continue;
				cards[n].turnRear();
				cards[n].moveto(new Point(x, y));
				table.put(new Point(x, y), cards[n]);
				x += 101;
			}
			x = 20;
			y -= 5;
		}
	}

	// ֽ���������
	private void randomCards() {
		Card temp = null;
		// �����������
		for (int i = 0; i < 52; i++) {
			int a = (int) (Math.random() * 104);
			int b = (int) (Math.random() * 104);
			temp = cards[a];
			cards[a] = cards[b];
			cards[b] = temp;
		}
	}

}
