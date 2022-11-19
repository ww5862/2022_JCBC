import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class EmployeeTable extends JPanel{
	
	private final String selectW[] = {"전체", "부서", "성별", "연봉", "생일" };
	private JComboBox setSelectW = new JComboBox(selectW);

	// private final String selectD[] = { "Headquarters", "Administration",
	// "Research" };
	// private JComboBox setSelectD = new JComboBox(selectD);

	private final String selectS[] = { "M", "F" };
	private JComboBox setSelectS = new JComboBox(selectS);

	private JTextField selectD = new JTextField(20);
	private JTextField selectSal = new JTextField(10);
	private JTextField underline = new JTextField(15);

	// hyein : 부서별 평균 월급
	private JLabel dSalary = new JLabel("부서 평균 월급 : ");

	private final String month[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
	private JComboBox setMonth = new JComboBox(month);
	
	private JLabel label = new JLabel("�˻� �׸�");
	private JCheckBox name_b = new JCheckBox("Name", true);
	private JCheckBox ssn_b = new JCheckBox("Ssn", true);
	private JCheckBox bdate_b = new JCheckBox("Bdate", true);
	private JCheckBox address_b = new JCheckBox("Address", true);
	private JCheckBox sex_b = new JCheckBox("Sex", true);
	private JCheckBox salary_b = new JCheckBox("Salary", true);
	private JCheckBox supervisor_b = new JCheckBox("Supervisor", true);
	private JCheckBox department_b = new JCheckBox("Department", true);
	private JButton search_b = new JButton("�˻�");
	private JButton insert_b = new JButton("�߰��ϱ�");
	private Vector<String> header = new Vector<String>();
	
	private DefaultTableModel model;
	private JTable table;
	private JPanel table_panel;
	JScrollPane Sc;
	private Container del = this;
	private boolean isTable = false;

	private int tableCnt;

	private Statement stmt;
	private ResultSet rs;
	
	private JLabel selectTotalLabel = new JLabel("�ο� ��: ");
	private JLabel boxSelect = new JLabel("������ ����: ");
	private JLabel selectTotalCnt = new JLabel(" ");
	private JLabel boxSelectCnt = new JLabel(" ");
	private JButton del_b =  new JButton("����");
	
	private static final String updateString[] = {"Address", "Sex", "Salary"};
	private JLabel setUpdateL = new JLabel("����:");
	private JComboBox setUpdateC = new JComboBox(updateString);
	private JTextField setUpdate = new JTextField(20);
	private JButton setUpdateb = new JButton("Update");

	private final String updateSex[] = { "M", "F" };
	private JComboBox setUpdateSex = new JComboBox(updateSex);

	//hyein
	private final String sorting[] = { "정렬 없음", "NAME 순", "SSN 순", "BDATE 순", "SALARY 순" };
	private JComboBox setSorting = new JComboBox(sorting);

	private JPanel updateMenu = new JPanel();
	private JPanel southMenu = new JPanel();
	private JPanel southMenuN = new JPanel();
	private JPanel southMenuS = new JPanel();
	private JPanel selectMenu = new JPanel();
	private JPanel selectMenuS = new JPanel();
	private JPanel selectMenuN = new JPanel();

	private static Boolean isSearch = false;

	//hyein
	private Boolean search_as_dSalary = false;
	private String order_by = "";

	public EmployeeTable() {

		setLayout(new BorderLayout());

		selectMenu.setLayout(new BorderLayout());

		selectMenuN.add(setSelectW);
		// selectMenuN.add(setSelectD);
		selectMenuN.add(setSelectS);
		selectMenuN.add(selectSal);
		selectMenuN.add(selectD);
		selectMenuN.add(setMonth);
		selectMenuN.add(underline);

		// hyein
		selectMenuN.add(setSorting);
		selectMenuN.add(dSalary);
		
		selectD.setVisible(false);
		dSalary.setVisible(false);

		// setSelectD.setVisible(false);
		setSelectS.setVisible(false);
		selectSal.setVisible(false);
		setMonth.setVisible(false);
		underline.setVisible(false);

		setUpdate.setVisible(true);
		setUpdateSex.setVisible(false);

		selectMenu.add(selectMenuN, BorderLayout.NORTH);

		selectMenuS.add(label);
		selectMenuS.add(name_b);
		selectMenuS.add(ssn_b);
		selectMenuS.add(bdate_b);
		selectMenuS.add(address_b);
		selectMenuS.add(sex_b);
		selectMenuS.add(salary_b);
		selectMenuS.add(supervisor_b);
		selectMenuS.add(department_b);
		selectMenuS.add(search_b);
		selectMenuS.add(insert_b);
		selectMenuS.add(salary_up);
		selectMenuS.add(knowDepent);
		selectMenu.add(selectMenuS, BorderLayout.SOUTH);

		this.add(selectMenu, BorderLayout.NORTH);

		// 밑의 메뉴
		southMenu.setLayout(new BorderLayout());
		southMenu.setPreferredSize(new Dimension(1000, 65));

		southMenuN.setLayout(new BorderLayout());
		southMenuS.setLayout(new BorderLayout());

		southMenuN.add(boxSelect, BorderLayout.WEST);
		southMenuN.add(boxSelectCnt);
		southMenu.add(southMenuN, BorderLayout.NORTH);

		JPanel southMenuSW = new JPanel();
		southMenuSW.add(selectTotalLabel);
		southMenuSW.add(selectTotalCnt);

		southMenuS.add(southMenuSW, BorderLayout.WEST);

		southMenuS.add(del_b, BorderLayout.EAST);

		updateMenu.add(setUpdateL);
		updateMenu.add(setUpdateC);
		updateMenu.add(setUpdate);
		updateMenu.add(setUpdateSex);
		updateMenu.add(setUpdateb);

		updateMenu.setPreferredSize(new Dimension(10, 10));

		southMenuS.add(updateMenu, BorderLayout.CENTER);
		southMenu.add(southMenuN, BorderLayout.NORTH);
		southMenu.add(southMenuS, BorderLayout.SOUTH);

		this.add(southMenu, BorderLayout.SOUTH);

		EmployeeTableHandler handler = new EmployeeTableHandler();
		setSelectW.addActionListener(handler);
		setUpdateC.addActionListener(handler);
		search_b.addActionListener(handler);
		del_b.addActionListener(handler);
		setUpdateb.addActionListener(handler);
		insert_b.addActionListener(handler);
		salary_up.addActionListener(handler);
		knowDepent.addActionListener(handler);

		//hyein
		setSorting.addActionListener(handler);

	}

	private class EmployeeTableHandler implements ActionListener {

		private String selectWhere(ActionEvent e) {
			String res = "";
			/*
			 * selectMenuN.add(setSelectD); selectMenuN.add(setSelectS);
			 * selectMenuN.add(selectSal); selectMenuN.add(setMonth);
			 */
			
			if(setSelectW.getSelectedItem() == "전체") return "";
			
			if(setSelectW.getSelectedItem() == "부서") {
				res = " and Dname = '" + setSelectD.getSelectedItem() + "'";
				
				//hyein
				search_as_dSalary = true;
			}
			
			if(setSelectW.getSelectedItem() == "성별") {
				res = " and e.Sex = '" + setSelectS.getSelectedItem() + "'";
			}
			
			if(setSelectW.getSelectedItem() == "연봉") {
				res = " and e.Salary >= " + selectSal.getText();
			}
			
			if(setSelectW.getSelectedItem() == "생일") {
				res = " and Month(e.Bdate) = " + setMonth.getSelectedItem();
			}
			if (setSelectW.getSelectedItem() == "부하 직원") {
				res = " and concat(s.Fname, ' ',s. Minit, ' ', s.Lname) = '" + underline.getText() + "'";
			}

			return res;
		}

		private void selectSearch(ActionEvent e) {
			if (name_b.isSelected() || ssn_b.isSelected() || bdate_b.isSelected() || address_b.isSelected()
					|| sex_b.isSelected() || salary_b.isSelected() || supervisor_b.isSelected()
					|| department_b.isSelected()) { // 선택을 할 시에만 검색을 한다.
				// sql conn 호출
				Main getConn = new Main();
				Connection conn = getConn.getConn();
				String sql = "select ";
				
				//Header ä���
				header.add("선택");
						
				if(name_b.isSelected()) {
					if(!ssn_b.isSelected() && !bdate_b.isSelected() && !address_b.isSelected() &&
					!sex_b.isSelected() && !salary_b.isSelected() && !supervisor_b.isSelected() && !department_b.isSelected())  sql += "concat(e.Fname, ' ',e. Minit, ' ',e. Lname) as name ";
					else sql += "concat(e.Fname, ' ',e. Minit, ' ',e. Lname) as name, ";
					header.add("Name");
				}

				if (ssn_b.isSelected()) {
					if (!bdate_b.isSelected() && !address_b.isSelected() && !sex_b.isSelected()
							&& !salary_b.isSelected() && !supervisor_b.isSelected() && !department_b.isSelected())
						sql += "e.Ssn ";
					else
						sql += "e.Ssn, ";
					header.add("SSN");
				}

				if (bdate_b.isSelected()) {
					if (!address_b.isSelected() && !sex_b.isSelected() && !salary_b.isSelected()
							&& !supervisor_b.isSelected() && !department_b.isSelected())
						sql += "e.Bdate ";
					else
						sql += "e.Bdate, ";
					header.add("BDATE");
				}

				if (address_b.isSelected()) {
					if (!sex_b.isSelected() && !salary_b.isSelected() && !supervisor_b.isSelected()
							&& !department_b.isSelected())
						sql += "e.Address ";
					else
						sql += "e.Address, ";
					header.add("ADDRESS");
				}

				if (sex_b.isSelected()) {
					if (!salary_b.isSelected() && !supervisor_b.isSelected() && !department_b.isSelected())
						sql += "e.Sex ";
					else
						sql += "e.Sex, ";
					header.add("SEX");
				}

				if (salary_b.isSelected()) {
					if (!supervisor_b.isSelected() && !department_b.isSelected())
						sql += "e.Salary ";
					else
						sql += "e.Salary, ";
					header.add("Salary");
				}

				if (supervisor_b.isSelected()) {
					if (!department_b.isSelected())
						sql += "concat(s.Fname, ' ',s. Minit, ' ', s.Lname) as supervisor ";
					else
						sql += "concat(s.Fname, ' ',s. Minit, ' ', s.Lname) as supervisor, ";
					header.add("SUPERVISOR");
				}

				if (department_b.isSelected()) {
					sql += "Dname ";
					header.add("DEPARTMENT");
				}

				sql += "from employee e left outer join employee s on e.super_ssn=s.ssn, Department where e.Dno = Dnumber";
				sql += selectWhere(e);
				//hyein
				sql += order_by;

				System.out.println(sql);
				try {
					stmt = conn.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				model = new DefaultTableModel(header, 0) {
					@Override
					public boolean isCellEditable(int row, int column) {
						if (column > 0) {
							return false;
						} else {
							return true;
						}
					}
				};

				table = new JTable(model) {
					@Override
					public Class getColumnClass(int column) {
						if (column == 0) {
							return Boolean.class;
						} else
							return String.class;
					}
				};

				try {
					stmt = conn.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				try {
					rs = stmt.executeQuery(sql);
					int cnt = 0;
					//hyein
					double salary_total = 0;

					while (rs.next()) {
						cnt++;
						Vector tmp = new Vector();
						tmp.add(false);
						if (name_b.isSelected())
							tmp.add(rs.getString("name"));
						if (ssn_b.isSelected())
							tmp.add(rs.getString("e.ssn"));
						if (bdate_b.isSelected())
							tmp.add(rs.getString("e.bdate"));
						if (address_b.isSelected())
							tmp.add(rs.getString("e.Address"));
						if (sex_b.isSelected())
							tmp.add(rs.getString("e.sex"));
						if (salary_b.isSelected()){
							tmp.add(Double.toString(rs.getDouble("e.Salary")));
							//hyein
							salary_total += rs.getDouble("e.Salary");
						}
						if (supervisor_b.isSelected())
							tmp.add(rs.getString("supervisor"));
						if (department_b.isSelected())
							tmp.add(rs.getString("Dname"));

						model.addRow(tmp);
					}
					

					//hyein
					if (search_as_dSalary) {
						double salary_avg = salary_total/cnt;
						dSalary.setText("부서 평균 월급 : "+Double.toString(salary_avg));
					} else {
						dSalary.setText("부서 평균 월급 : ");
					}

					search_as_dSalary = false;
					

					EmployeeTableHandler handler = new EmployeeTableHandler();
					table.getModel().addTableModelListener(new TableEvent());

					selectTotalCnt.setText("" + cnt);
					table.setRowHeight(30);
					isTable = true;
					table_panel = new JPanel();
					Sc = new JScrollPane(table);
					Sc.setPreferredSize(new Dimension(1000, 400));
					table_panel.add(Sc);

					add(table_panel);
					revalidate();

				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			} // end if

		}// end function

		private void selectDelete(ActionEvent e) {
			int ssnLoc = 1;
			String colName = model.getColumnName(ssnLoc);
			if (colName != "SSN")
				colName = model.getColumnName(++ssnLoc);
			if (colName != "SSN")
				return;

			Vector<String> arrSsn = new Vector<String>();
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.getValueAt(i, 0) == Boolean.TRUE)
					arrSsn.add((String) table.getValueAt(i, ssnLoc));
			}
			Main getConn = new Main();
			Connection conn = getConn.getConn();

			String sql = "delete from employee where Ssn = ?";
			try {
				PreparedStatement p = conn.prepareStatement(sql);
				p.clearParameters();

				for (Object a : arrSsn) {
					p.clearParameters();
					p.setString(1, (String) a);
					p.executeUpdate();
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
		}

		private void selectUpdate(ActionEvent e) {
			int ssnLoc = 1;
			String colName = model.getColumnName(ssnLoc);
			if (colName != "SSN")
				colName = model.getColumnName(++ssnLoc);
			if (colName != "SSN")
				return;

			Vector<String> arrSsn = new Vector<String>();
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.getValueAt(i, 0) == Boolean.TRUE)
					arrSsn.add((String) table.getValueAt(i, ssnLoc));
			}
			Main getConn = new Main();
			Connection conn = getConn.getConn();

			String sql = "Update EMPLOYEE set " + setUpdateC.getSelectedItem()
					+ " = ? , modified = CURRENT_TIMESTAMP() where Ssn = ?";

			try {
				PreparedStatement p = conn.prepareStatement(sql);
				p.clearParameters();

				for (Object a : arrSsn) {
					if (setUpdateC.getSelectedItem() == "Salary")
						p.setDouble(1, Double.parseDouble(setUpdate.getText()));
					else if (setUpdateC.getSelectedItem() == "Sex") {
						p.setString(1, (String) setUpdateSex.getSelectedItem());
					} else
						p.setString(1, (String) setUpdate.getText());
					p.setString(2, (String) a);
					p.executeUpdate();
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}

		}

		private void selectInsert(ActionEvent e) {
			InsertFrame a = new InsertFrame();
		}

		private void increaseSalary(ActionEvent e) {
			IncreaseSalary a = new IncreaseSalary();
		}

		private void printDependent(ActionEvent e) {
			int ssnLoc = 1;
			String colName = model.getColumnName(ssnLoc);
			if (colName != "SSN")
				colName = model.getColumnName(++ssnLoc);
			if (colName != "SSN")
				return;

			Vector<String> arrSsn = new Vector<String>();
			for (int i = 0; i < table.getRowCount(); i++) {
				arrSsn.add((String) table.getValueAt(i, ssnLoc));
			}
			PrintDependent a = new PrintDependent(arrSsn);
			// here to
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(isTable) { // ���̺� �ʱ�ȭ
				boxSelectCnt.setText("");
				del.remove(table_panel);
				selectTotalCnt.setText("");
				
				revalidate();
			}
			
			//�˻���
			if(e.getSource() == setSelectW) {
				setSelectD.setVisible(false);
				setSelectS.setVisible(false);
				selectSal.setVisible(false);
				setMonth.setVisible(false);
				
				if(setSelectW.getSelectedItem() == "�μ�") setSelectD.setVisible(true);
				if(setSelectW.getSelectedItem() == "����") setSelectS.setVisible(true);
				if(setSelectW.getSelectedItem() == "����") selectSal.setVisible(true);
				if(setSelectW.getSelectedItem() == "����") setMonth.setVisible(true);
				
				revalidate();
				
			}
			
			//�˻� ��ư Ŭ����
			if(e.getSource() == search_b) {
				isSearch = true;
				header.clear();
				selectSearch(e);
			}
			
			if(e.getSource() == del_b) {
				if(isSearch == true) selectDelete(e);
				else return;
				
				//repaint�� ���ؼ� �ٽ� ȣ��
				header.clear();
				selectSearch(e);
			}
			
			if(e.getSource() == setUpdateb) {
				if(isSearch == true) selectUpdate(e);
				else return;
				
				//repaint�� ���ؼ� �ٽ� ȣ��
				header.clear();
				selectSearch(e);
				printDependent(e);
			}

			if (e.getSource() == insert_b) {
				if (isTable) { // 테이블 초기화
					boxSelectCnt.setText("");
					del.remove(table_panel);
					selectTotalCnt.setText("");

					revalidate();
				}

				selectInsert(e);

				header.clear();
				selectSearch(e);
				repaint();
			}
		} // end actionPerform function

	}// end private class

	public class TableEvent implements TableModelListener {
		@Override
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();

			if (col == 0) {
				TableModel model = (TableModel) e.getSource();
				String columnName = model.getColumnName(1);
				Boolean checked = (Boolean) model.getValueAt(row, col);

				String output = "";
				for (int i = 0; i < table.getRowCount(); i++) {
					if (table.getValueAt(i, 0) == Boolean.TRUE)
						output += (String) table.getValueAt(i, 1) + " ";
				} // end for

				boxSelectCnt.setText(output);
			} // end if
		}// end function
	}

}// end class

//here to code
class PrintDependent extends JFrame {
	private DefaultTableModel model;
	private JTable table;
	private JPanel table_panel;
	JScrollPane Sc;
	private Container del = this;
	
	private JLabel title = new JLabel("���ο� ���� ���� �߰�");
	
	private JLabel FnameL = new JLabel("First Name: ");
	private JTextField Fname = new JTextField();

	private JLabel MnameL = new JLabel("Middle init: ");
	private JTextField Mname = new JTextField();

	private JLabel lnameL = new JLabel("Last Name: ");
	private JTextField lname = new JTextField();

	private JLabel ssnL = new JLabel("ssn: ");
	private JTextField ssn = new JTextField();

	private JLabel birthL = new JLabel("Birthdate: ");
	private JTextField birth = new JTextField();

	private JLabel addressL = new JLabel("Address: ");
	private JTextField address = new JTextField();

	private static final String sexString[] = { "M", "F" };
	private JLabel sexL = new JLabel("Sex");
	private JComboBox sexC = new JComboBox(sexString);

	private JLabel salaryL = new JLabel("Salary: ");
	private JTextField salary = new JTextField();

	private JLabel superSL = new JLabel("super_ssn: ");
	private JTextField superS = new JTextField();

	private JLabel dnoL = new JLabel("Dno: ");
	private JTextField dno = new JTextField();
	private JButton b= new JButton("���� �߰��ϱ�");
	
	private JPanel top = new JPanel();
	private JPanel button = new JPanel();
	private JPanel bPanel = new JPanel();

	public InsertFrame() {
		setTitle("INSERT"); //������ ���� ����
		setLayout(new BorderLayout());

		top.setLayout(new BorderLayout());
		top.add(title, BorderLayout.CENTER);

		button.setLayout(new GridLayout(10, 2));

		button.add(FnameL);
		button.add(Fname);
		button.add(MnameL);
		button.add(Mname);
		button.add(lnameL);
		button.add(lname);
		button.add(ssnL);
		button.add(ssn);
		button.add(birthL);
		button.add(birth);
		button.add(addressL);
		button.add(address);
		button.add(sexL);
		button.add(sexC);
		button.add(salaryL);
		button.add(salary);
		button.add(superSL);
		button.add(superS);
		button.add(dnoL);
		button.add(dno);

		bPanel.add(b);

		this.add(top, BorderLayout.NORTH);
		this.add(button, BorderLayout.CENTER);
		this.add(b, BorderLayout.SOUTH);

		InsertHandler handler = new InsertHandler();

		b.addActionListener(handler);

		setSize(500, 500);
		setVisible(true);

	}

	private class InsertHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// here to copy
			Main getConn = new Main();
			Connection conn = getConn.getConn();

			String sql = "insert into EMPLOYEE value(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())";

			try {
				PreparedStatement p = conn.prepareStatement(sql);
				p.clearParameters();

				p.setString(1, Fname.getText());
				p.setString(2, Mname.getText());
				p.setString(3, lname.getText());
				p.setString(4, ssn.getText());
				p.setString(5, birth.getText());
				p.setString(6, address.getText());
				p.setString(7, (String) sexC.getSelectedItem());
				if (salary.getText().contentEquals(""))
					p.setDouble(8, 0);
				else
					p.setDouble(8, Double.parseDouble(salary.getText()));
				p.setString(9, superS.getText());
				if (dno.getText().contentEquals(""))
					p.setInt(10, 1);
				else
					p.setInt(10, Integer.parseInt(dno.getText()));

				int c = p.executeUpdate();
				if (c == 1)
					dispose();
			} catch (SQLException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "다시 입력하세요.");
			}
		}
	}

}
