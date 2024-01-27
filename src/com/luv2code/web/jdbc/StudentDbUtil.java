package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;

	public StudentDbUtil(DataSource dataSource) {		
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
		myConn = this.dataSource.getConnection();
		
		String sql = "select * from student order by last_name";
		myStmt = myConn.createStatement();
		
		myRs = myStmt.executeQuery(sql);
		
		while(myRs.next()) {
			int id = myRs.getInt("id");
			
			students.add(new Student(myRs.getInt("id"),myRs.getString("first_name"), myRs.getString("last_name"), myRs.getString("email")));
		}
		
		return students;
		}finally {
			close(myConn, myStmt, myRs);
		}
		
	}


	public void addStudent(Student s) throws Exception{
		// TODO Auto-generated method stub
		
		Connection myConn = null;
		PreparedStatement myStmt = null;		
		
		try {
			myConn = this.dataSource.getConnection();						
			String sql = "insert into student "
					+ "(first_name,last_name,email) "
					+"values(?,?,?)";			
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, s.getFirstName());
			myStmt.setString(2, s.getLastName());
			myStmt.setString(3, s.getEmail());
			
			myStmt.execute();
		}finally{
			close(myConn,myStmt,null);
		}
	}
	
	private void close(Connection conn, Statement stmt, ResultSet rs){
		try {
			if(conn != null)
				conn.close();
			if(stmt != null)
				stmt.close();
			if(rs != null)
				rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public Student getStudentById(int studentId) throws Exception{
		Student result = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = this.dataSource.getConnection();
			String sql = "select * from student where id=?";
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1,studentId);
			
			myRs = myStmt.executeQuery();
			
			if(myRs.next()) {
				result = new Student(myRs.getInt("id"),myRs.getString("first_name"),myRs.getString("last_name"),myRs.getString("email"));
				return result;
			}
			throw new Exception("Could not find student id: " + studentId);
		}finally {
			close(myConn,myStmt,myRs);
		}
	}

	public void updateStudent(Student newStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			myConn = this.dataSource.getConnection();
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, newStudent.getFirstName());
			myStmt.setString(2, newStudent.getLastName());
			myStmt.setString(3, newStudent.getEmail());
			myStmt.setInt(4, newStudent.getId());
			
			myStmt.execute();
		}finally {
			close(myConn,myStmt,null);
		}
	}

}
