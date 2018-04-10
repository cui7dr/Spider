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
	// 生成 spider 框架对象
	Spider main = null;
	// 生成菜单组
	JMenu newGame = new JMenu("游戏");
	JMenu help = new JMenu("帮助");
	JMenuItem about = new JMenuItem("关于");
	JMenuItem open = new JMenuItem("开局");
	JMenuItem playAgain = new JMenuItem("重新发牌");
	// 生成单选框
	JRadioButtonMenuItem easy = new JRadioButtonMenuItem("简单");
	JRadioButtonMenuItem normal = new JRadioButtonMenuItem("一般");
	JRadioButtonMenuItem hard = new JRadioButtonMenuItem("困难");

	JMenuItem exit = new JMenuItem("退出");
	JMenuItem valid = new JMenuItem("提示");

	// 构造函数
	public Menu(Spider spider) {
		this.main = spider;

		// 初始化菜单栏
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
		 * 为组件添加事件监听并实现
		 */
		// 开局
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.newGame();
			}
		});

		// 重新发牌
		playAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (main.getC() < 60) {
					main.deal();
				}
			}
		});

		// 提示
		valid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Show().start();
			}
		});

		// 退出
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.dispose();
				System.exit(0);
			}
		});

		// 默认简单
		easy.setSelected(true);

		// 简单
		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.setGrade(Spider.easy);
				main.initCards();
				main.newGame();
			}
		});

		// 一般
		normal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.setGrade(Spider.natural);
				main.initCards();
				main.newGame();
			}
		});

		// 困难
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

		// 关于
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Dialog();
			}
		});
	}

	// 构造函数：显示可执行操作
	class Show extends Thread {
		public void run() {
			main.showEnableOperator();
		}
	}

}
