import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Dialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JPanel jMainPane = new JPanel();
	JTabbedPane jTabbedPane = new JTabbedPane();
	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private JTextArea jta1 = new JTextArea("将电脑多次分发给你的牌按照相同的花色由大至小排列起来，直至桌面上的牌全部消失");
	private JTextArea jta2 = new JTextArea("该游戏中，纸牌的图片来源与 Windows XP 的纸牌游戏，版权归原作者所有");

	// 构造函数
	public Dialog() {
		setTitle("纸牌");
		setSize(300, 200);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container con = this.getContentPane();
		jta1.setSize(260, 200);
		jta2.setSize(260, 200);
		jta1.setEditable(false);
		jta2.setEditable(false);
		jta1.setLineWrap(true);
		jta2.setLineWrap(true);
		jta1.setFont(new Font("楷体_GB2312", java.awt.Font.BOLD, 13));
		jta1.setForeground(Color.blue);
		jta2.setFont(new Font("楷体_GB2312", java.awt.Font.BOLD, 13));
		jta2.setForeground(Color.black);
		jPanel1.add(jta1);
		jPanel2.add(jta2);
		jTabbedPane.setSize(300, 200);
		jTabbedPane.addTab("游戏规则", null, jPanel1, null);
		jTabbedPane.addTab("声明", null, jPanel2, null);
		jMainPane.add(jTabbedPane);
		con.add(jMainPane);
		pack();
		this.setVisible(true);
	}
}
