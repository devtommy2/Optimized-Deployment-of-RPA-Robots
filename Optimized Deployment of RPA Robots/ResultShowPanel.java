package RPADeployment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;

// 创建一个JPanel，并添加一个表格
public class ResultShowPanel extends JPanel {

	private JTable table;

	public ResultShowPanel() {
		setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		//		panel1.setPreferredSize(new Dimension(4000,2000));//设置整个插件页面的大小
		// 创建表格模型
		DefaultTableModel tableModel = new DefaultTableModel();
		// 创建表格
		JTable table = new JTable(tableModel);

		// 添加表格列
		tableModel.addColumn("流程状态");
		tableModel.addColumn("持续时间类型");
		tableModel.addColumn("关键活动及持续时间");
		tableModel.addColumn("流程总执行时间");
		//将数据传入表格
		Petrinet net1 = GetTPNT.net1;
		HashMap<String, Integer> Mintime = GetTPNT.MTime1;
		HashMap<String, Integer> Maxtime = GetTPNT.MTime2;
		HashMap<String, Integer> Mintime1 = GetTPNT.MMintime;
		HashMap<String, Integer> Maxtime1 = GetTPNT.MMaxtime;
		String nameRPA = ProcessShowPanel.selectedValue1;
		int valuebelow1 = ProcessShowPanel.valueBelow;
		int valueabove1 = ProcessShowPanel.valueAbove;
		String nameRPA2 = ProcessShowPanel1.selectedValue2;
		int valuebelow2 = ProcessShowPanel1.valueBelow2;
		int valueabove2 = ProcessShowPanel1.valueAbove2;
		Mintime1.put(nameRPA,valuebelow1);
		Maxtime1.put(nameRPA,valueabove1);
		System.out.println(Mintime1);
		System.out.println("****************************");
		Mintime1.put(nameRPA2,valuebelow2);
		Maxtime1.put(nameRPA2,valueabove2);
		Object[] msg = GetBestPathPlugin.SimRankSamplingTechnique(net1, Mintime);
		Object[] msg1 = GetBestPathPlugin.SimRankSamplingTechnique(net1, Maxtime);
		Object[] msg2 = GetBestPathPlugin.SimRankSamplingTechnique(net1, Mintime1);
		Object[] msg3 = GetBestPathPlugin.SimRankSamplingTechnique(net1, Maxtime1);
		
		HashMap<String, Integer> FT = (HashMap<String, Integer>) msg[0];
		HashMap<String, Integer> LT = (HashMap<String, Integer>) msg[1];
		HashMap<String, Integer> BestPath = (HashMap<String, Integer>) msg[2];
		ArrayList<String> bestPathArray = (ArrayList<String>) msg[3];
		HashMap<String, Integer> KeyTime = (HashMap<String, Integer>) msg[5];
		int Completedtime = (int) msg[4];
		
		HashMap<String, Integer> FT1 = (HashMap<String, Integer>) msg1[0];
		HashMap<String, Integer> LT1 = (HashMap<String, Integer>) msg1[1];
		HashMap<String, Integer> BestPath1 = (HashMap<String, Integer>) msg1[2];
		ArrayList<String> bestPathArray1 = (ArrayList<String>) msg1[3];
		HashMap<String, Integer> KeyTime1 = (HashMap<String, Integer>) msg1[5];
		int Completedtime1 = (int) msg1[4];
		
		HashMap<String, Integer> FT2 = (HashMap<String, Integer>) msg2[0];
		HashMap<String, Integer> LT2 = (HashMap<String, Integer>) msg2[1];
		HashMap<String, Integer> BestPath2 = (HashMap<String, Integer>) msg2[2];
		ArrayList<String> bestPathArray2 = (ArrayList<String>) msg2[3];
		HashMap<String, Integer> KeyTime2 = (HashMap<String, Integer>) msg2[5];
		int Completedtime2 = (int) msg2[4];
		
		HashMap<String, Integer> FT3 = (HashMap<String, Integer>) msg3[0];
		HashMap<String, Integer> LT3 = (HashMap<String, Integer>) msg3[1];
		HashMap<String, Integer> BestPath3 = (HashMap<String, Integer>) msg3[2];
		ArrayList<String> bestPathArray3 = (ArrayList<String>) msg3[3];
		HashMap<String, Integer> KeyTime3 = (HashMap<String, Integer>) msg3[5];
		int Completedtime3 = (int) msg3[4];
		int costTotal1 = ProcessShowPanel.cost;
		int costTotal2 = ProcessShowPanel1.cost2;
		int costTotal = costTotal1 + costTotal2;
		double a = Completedtime-Completedtime2;
		double b = Completedtime1-Completedtime3;
		double rate1 = a / Completedtime*100;
		double rate2 =  b / Completedtime1*100 ;
		System.out.println("小数:"+ rate1);
		System.out.println("小数:"+ rate2);
		
		String Number1 = String.format("%.2f", rate1);
		String Number2 = String.format("%.2f", rate2);
		System.out.println("小小数:"+ Number1);
		System.out.println("小小数:"+ Number2);
		String minpower,maxpower;
		if(rate1>=rate2) {
			minpower=Number2;
			maxpower=Number1;
		}else {
			minpower=Number1;
			maxpower=Number2;
		}
		String efficiency ="[" + minpower +"%" + "-" + maxpower+ "%" + "]";
		String lastTime = "初始流程";
		String lastTime1 = "最终流程";
		String TImprove = "";
		String Cost = "RPA部署总成本(RMB)";
		String Set = "RPA部署结点集合";
		String TimeType = "持续时间下限";
		String TimeType1 = "持续时间上限";
		String Empty = " ";
		String Empty1 = " ";
		tableModel.addRow(new Object[] { lastTime, TimeType, KeyTime, Completedtime });// 初始流程下限
		tableModel.addRow(new Object[] { Empty, TimeType1, KeyTime1, Completedtime1 });//初始流程上限
		tableModel.addRow(new Object[] { lastTime1, TimeType, KeyTime2, Completedtime2 });//最终流程下限
		tableModel.addRow(new Object[] { Empty, TimeType1, KeyTime3, Completedtime3 });//最终流程上限
		tableModel.addRow(new Object[] { TImprove, efficiency, Empty, Empty1 });//时间提升效率
		tableModel.addRow(new Object[] { Cost,costTotal, Empty, Empty1 });//成本
		tableModel.addRow(new Object[] { Set, Empty, Empty, Empty });//结点
		tableModel.addRow(new Object[] { Empty, Empty, Empty, Empty });//空行
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
				//	                row=3;
				// 设置字体大小
				Font font = new Font("宋体", Font.PLAIN, 25);//表头字体设置
				label.setFont(font);

				// 设置水平对齐方式
				label.setHorizontalAlignment(SwingConstants.LEFT);

				// 设置标签文本
				label.setText(value.toString());

				return label;
			}
		});
		int headerHeight = 50; // 设置表头高度为50像素
		Color headerColor = Color.LIGHT_GRAY;
		header.setDefaultRenderer(new CustomTableHeaderRenderer(headerHeight, headerColor));
		Font headerFont = header.getFont();
		Font newHeaderFont = headerFont.deriveFont(Font.PLAIN, 25);//表头文字大小设置（成功）
		header.setFont(newHeaderFont);
		Font font = new Font("宋体", Font.PLAIN, 21);//表格中字体设置，宋体支持汉字显示
		table.setFont(font);

		//		table.setPreferredScrollableViewportSize(new Dimension(800, 600));//设置表格大小
		table.setRowHeight(60);//表中每行表格的高度*
		TableColumnModel columnModel = table.getColumnModel();

		// 您可以使用getColumn方法来获取TableColumn对象，并设置其宽度

		TableColumn column = columnModel.getColumn(0);
		column.setPreferredWidth(50); // 将第一列宽度设置为100

		column = columnModel.getColumn(1);
		column.setPreferredWidth(10); // 将第二列宽度设置为200
		column = columnModel.getColumn(2);
		column.setPreferredWidth(500);
		column = columnModel.getColumn(3);
		column.setPreferredWidth(60);
		add(panel1);
		// 将表格添加到面板
		add(new JScrollPane(table), BorderLayout.CENTER);

	}

	public class CustomTableHeaderRenderer extends DefaultTableCellRenderer {
		private int headerHeight;
		private Color headerColor;

		public CustomTableHeaderRenderer(int headerHeight,Color headerColor) {
			this.headerHeight = headerHeight;
			this.headerColor = headerColor;
		}

		 @Override
		    public Dimension getPreferredSize() {
		        Dimension dimension = super.getPreferredSize();
		        dimension.height = headerHeight;
		        return dimension;
		    }

		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        JTableHeader header = table.getTableHeader();
		        component.setBackground(headerColor);
		        component.setForeground(Color.BLACK); // 设置表头文字颜色
		        component.setFont(header.getFont());
		        return component;
	}
	}
}
