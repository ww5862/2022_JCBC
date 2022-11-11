import java.awt.*;
import javax.swing.*;

public class TableFrame extends JFrame {
	public TableFrame() {
		setTitle("GUI TITLE"); //扩档快 力格 积己
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		EmployeeTable t = new EmployeeTable();
		
		add(t);
		setSize(1000, 600);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new TableFrame();
	}
}


