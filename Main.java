import java.sql.*;

public class Main {
	private static Connection conn;
	private static final String url = "jdbc:mysql://localhost:3306/mydb?serverTimeZone=UTC";
	private static final String user = "root";
	private static final String password = "root";
	
	private static Connection getConnecting() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	public Connection getConn() {
		return conn;
	}
	
	public static void main(String[] args) {
		try {
			conn = getConnecting();
			System.out.println("DataBase Connect!");
		} catch (SQLException e1) { e1.printStackTrace(); 
		JOptionPane.showMessageDialog(null, e1.getMessage());}
		
		TableFrame t = new TableFrame();
	}

}
