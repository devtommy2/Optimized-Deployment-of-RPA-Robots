package RPADeployment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;

// 创建一个JPanel，并添加一个表格
public class ProcessShowPanel extends JPanel {
	public static  String selectedValue1;//部署RPA机器人的活动名称
	public static  int valueAbove;
	public static  int valueBelow;
	public static  int cost;

	
	public void setSelectedValue1() {
		
	}
	public ProcessShowPanel() {
		
//		FloatLayout layout = new FloatLayout(null, autoscrolls);
		setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(800, 500));//设置整个插件页面的大小
		JLabel JL1 = new JLabel("请输入选择部署RPA机器人的活动");
		JL1.setBounds(10,800,200,30);//失败
		JL1.setPreferredSize(new Dimension(300,30));//设置标签的大小
		Font font = new Font("宋体", Font.BOLD, 18);
		Font font1 = new Font("宋体", Font.BOLD, 20);
		JL1.setFont(font);
		JComboBox cmb = new JComboBox();//改变下拉框的大小
		cmb.setPreferredSize(new Dimension(400,30));
		cmb.addItem("--请选择--");
		
		JLabel JL2 = new JLabel("请输入部署后活动持续时间下限：");
		JL2.setBounds(10,1000,200,30);
		JL2.setPreferredSize(new Dimension(300,30));//设置标签的大小
		JL2.setFont(font);
		JTextField JT1 = new JTextField();
		JT1.setForeground(new Color(40, 40, 40));
		JT1.setPreferredSize(new Dimension(400,30));
		JT1.setFont(font1);
		JLabel JL5 = new JLabel("请输入部署后活动持续时间上限：");
		JL5.setBounds(10,1000,200,30);
		JL5.setPreferredSize(new Dimension(300,30));//设置标签的大小
		JL5.setFont(font);
		JTextField JT5 = new JTextField();
		JT5.setForeground(new Color(40, 40, 40));
		JT5.setPreferredSize(new Dimension(400,30));
		JT5.setFont(font1);
		
		JLabel JL3 = new JLabel("请输入当前RPA机器人成本");
		JL3.setBounds(10,1000,200,30);
		JL3.setPreferredSize(new Dimension(300,30));//设置标签的大小
		JL3.setFont(font);
		JTextField JT2 = new JTextField();
		JT2.setForeground(new Color(40, 40, 40));
		JT2.setPreferredSize(new Dimension(400,30));
		JT2.setFont(font1);
		JLabel JL4 = new JLabel("当前流程RPA机器人总成本");
		JL4.setBounds(10,1000,200,30);
		JL4.setPreferredSize(new Dimension(300,30));
		JL4.setFont(font);
		JTextField JT3 = new JTextField();
		JT3.setForeground(new Color(40, 40, 40));
		JT3.setPreferredSize(new Dimension(400,30));
		JT3.setFont(font1);
		JT3.setText(String.valueOf(0));
		// 创建表格模型
		DefaultTableModel tableModel = new DefaultTableModel();

		// 添加表格列
		tableModel.addColumn("持续时间类型");
		tableModel.addColumn("关键流程路径");
		tableModel.addColumn("流程总执行时间");
		//将数据传入表格
		Petrinet net1 = GetTPNT.net1;
		HashMap<String, Integer> Mintime = GetTPNT.MTime1;
		HashMap<String, Integer> Maxtime = GetTPNT.MTime2;
		Object[] msg = GetBestPathPlugin.SimRankSamplingTechnique(net1, Mintime);
		Object[] msg1 = GetBestPathPlugin.SimRankSamplingTechnique(net1, Maxtime);
		HashMap<String, Integer> FT = (HashMap<String, Integer>) msg[0];
		HashMap<String, Integer> LT = (HashMap<String, Integer>) msg[1];
		HashMap<String, Integer> BestPath = (HashMap<String, Integer>) msg[2];
		ArrayList<String> bestPathArray = (ArrayList<String>) msg[3];
		String[] Best = (String[])  bestPathArray.toArray(new String[10]);//将ArrayList<String>转化成数组
		ArrayList<String> allActivity =(ArrayList<String>) msg[6];
		String[] Allact = (String[]) allActivity.toArray(new String[50]);//所有活动的数组
		HashMap<String, Integer> KeyTime = (HashMap<String, Integer>) msg[5];
		HashMap<String, Integer> FT1 = (HashMap<String, Integer>) msg1[0];
		HashMap<String, Integer> LT1 = (HashMap<String, Integer>) msg1[1];
		HashMap<String, Integer> BestPath1 = (HashMap<String, Integer>) msg1[2];
		ArrayList<String> bestPathArray1 = (ArrayList<String>) msg1[3];
		HashMap<String, Integer> KeyTime1 = (HashMap<String, Integer>) msg1[5];
		int Completedtime1 = (int) msg1[4];
		String TimeType = "持续时间下限";
		String TimeType1 = "持续时间上限";
		for(int i=0;i<Allact.length;i++) {
			cmb.addItem(Allact[i]);
		}
		ActionListener listener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // 处理事件的代码
		    	selectedValue1 = (String) cmb.getSelectedItem();
		    	System.out.println("Selected value: " + selectedValue1);
		    }
		};
		cmb.addActionListener(listener);
		ActionListener listener1 = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // 处理事件的代码
		    	JTextField source = (JTextField)e.getSource();
		        String text = source.getText();
		        valueBelow = Integer.parseInt(text);
		        System.out.println("Import value: " + valueBelow);
		    }
		};
		JT1.addActionListener(listener1);
		ActionListener listener2 = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // 处理事件的代码
		    	JTextField source = (JTextField)e.getSource();
		        String text1 = source.getText();
		        
		        valueAbove = Integer.parseInt(text1);
		        System.out.println("Import value1: " + valueAbove);
		    }
		};
		JT5.addActionListener(listener2);
		ActionListener listener3 = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // 处理事件的代码
		    	JTextField source = (JTextField)e.getSource();
		        String a = source.getText();
		         cost = Integer.parseInt(a);
		        
		        System.out.println("RPA cost: " + cost);
		    }
		};
		JT2.addActionListener(listener3);
		int Completedtime = (int) msg[4];
		tableModel.addRow(new Object[] {TimeType,bestPathArray,Completedtime});// 添加表格数据
		tableModel.addRow(new Object[] {TimeType1,bestPathArray1,Completedtime1});
		// 创建表格
		JTable table = new JTable(tableModel);
		//对表头进行操作	
		JTableHeader header = table.getTableHeader();
		header.setBorder(new LineBorder(Color.BLACK));
		Border border = new LineBorder(Color.BLACK, 1);
		header.setBorder(border);
		header.setDefaultRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				// 创建表头标签
				javax.swing.JLabel label = new javax.swing.JLabel();
				row = 3;
				// 设置字体大小
				Font font = new Font("宋体", Font.PLAIN, 20);
				label.setFont(font);

				// 设置水平对齐方式
				label.setHorizontalAlignment(SwingConstants.LEFT);

				// 设置标签文本
				label.setText(value.toString());

				return label;
			}
		});

		Font font2 = new Font("宋体", Font.PLAIN, 18);//succes
		table.setFont(font2);

		table.setPreferredScrollableViewportSize(new Dimension(780, 200));//设置整个Table的页面大小
		table.setRowHeight(40);//表中每行表格的高度

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		TableColumnModel columnModel = table.getColumnModel();

		// 您可以使用getColumn方法来获取TableColumn对象，并设置其宽度

		TableColumn column = columnModel.getColumn(0);
		column.setPreferredWidth(130); // 将第一列宽度设置为100

		column = columnModel.getColumn(1);
		column.setPreferredWidth(520);
		column = columnModel.getColumn(2);
		column.setPreferredWidth(100);
		// 将表格添加到面板
		add(panel1);
		panel1.add(new JScrollPane(table), BorderLayout.NORTH);//
//		panel1.add(cmb,BorderLayout.EAST);
		panel1.add(JL1);
		panel1.add(cmb);
		panel1.add(JL2);
		panel1.add(JT1);
		panel1.add(JL5);
		panel1.add(JT5);
		panel1.add(JL3);
		panel1.add(JT2);
		panel1.add(JL4);
		panel1.add(JT3);
		

	}

}
