import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Menu extends JMenuBar {
	private static final long serialVersionUID = 1L;
	// ���� spider ��ܶ���
	Spider main = null;
	// ���ɲ˵���
	JMenu newGame = new JMenu("��Ϸ");
	JMenu help = new JMenu("����");
	JMenuItem about = new JMenuItem("����");
	JMenuItem open = new JMenuItem("����");
	JMenuItem playAgain = new JMenuItem("���·���");
	// ���ɵ�ѡ��
	JRadioButtonMenuItem easy = new JRadioButtonMenuItem("��");
	JRadioButtonMenuItem normal = new JRadioButtonMenuItem("һ��");
	JRadioButtonMenuItem hard = new JRadioButtonMenuItem("����");

	JMenuItem exit = new JMenuItem("�˳�");
	JMenuItem valid = new JMenuItem("��ʾ");

	// ���캯��
	public Menu(Spider spider) {
		this.main = spider;

		// ��ʼ���˵���
		newGame.add(open);
		newGame.add(playAgain);
		newGame.add(valid);
		newGame.addSeparator();
		newGame.add(easy);
		newGame.add(normal);
		newGame.add(hard);
		newGame.addSeparator();
		newGame.add(exit);

		ButtonGroup group = new ButtonGroup();
		group.add(easy);
		group.add(normal);
		group.add(hard);

		help.add(about);

		this.add(newGame);
		this.add(help);

		/**
		 * Ϊ�������¼�������ʵ��
		 */
		// ����
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.newGame();
			}
		});

		// ���·���
		playAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (main.getC() < 60) {
					main.deal();
				}
			}
		});

		// ��ʾ
		valid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Show().start();
			}
		});

		// �˳�
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.dispose();
				System.exit(0);
			}
		});

		// Ĭ�ϼ�
		easy.setSelected(true);

		// ��
		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.setGrade(Spider.easy);
				main.initCards();
				main.newGame();
			}
		});

		// һ��
		normal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.setGrade(Spider.natural);
				main.initCards();
				main.newGame();
			}
		});

		// ����
		hard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.setGrade(Spider.hard);
				main.initCards();
				main.newGame();
			}
		});

		newGame.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent e) {
				if (main.getC() < 60) {
					playAgain.setEnabled(true);
				} else {
					playAgain.setEnabled(false);
				}
			}

			public void menuDeselected(MenuEvent e) {

			}

			public void menuCanceled(MenuEvent e) {

			}
		});

		// ����
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Dialog();
			}
		});
	}

	// ���캯������ʾ��ִ�в���
	class Show extends Thread {
		public void run() {
			main.showEnableOperator();
		}
	}

}
