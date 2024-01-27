package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Define dataSource/connection pool for Resource Injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	private StudentDbUtil studentDbUtil;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			studentDbUtil = new StudentDbUtil(this.dataSource);
		}catch(Exception ex) {
			throw new ServletException(ex);
		}
	}


	/**
	 * @throws ServletException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		String command = request.getParameter("command");
		
		if (command == null) {
			command = "";
		}
		
		switch(command) {
			case "LOAD":
				loadStudents(request,response);
			case "UPDATE":
				updateStudent(request,response);
			case "DELETE":
				deleteStudent(request,response);
			default:
				listStudents(request, response);	
		}						
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		
		try {
			this.studentDbUtil.deleteStudent(studentId);
			listStudents(request,response);
		}catch(Exception ex) {
			throw new ServletException(ex);
		}
	}


	private void loadStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		String studentId = request.getParameter("studentId");
		try {
		Student result = this.studentDbUtil.getStudentById(Integer.parseInt(studentId));
		
		request.setAttribute("THE_STUDENT", result);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);		
		}catch(Exception ex) {
			throw new ServletException(ex);
		}
	}


	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		List<Student> result;
		try {
		result = studentDbUtil.getStudents();
		request.setAttribute("STUDENT_LIST",result);
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		}catch(Exception ex) {
			throw new ServletException(ex);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Student newStudent = new Student(req.getParameter("firstName").toString(),req.getParameter("lastName").toString(),req.getParameter("email").toString());		
		try {
			this.studentDbUtil.addStudent(newStudent);
			listStudents(req,resp);		
		}catch(Exception ex) {
			throw new ServletException(ex);
		}		
	}


	private void updateStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		Student newStudent = new Student(Integer.parseInt(req.getParameter("studentId")),req.getParameter("firstName").toString(),req.getParameter("lastName").toString(),req.getParameter("email").toString());
		log(newStudent.toString());
		log(req.getParameter("studentId"));
		try {
			this.studentDbUtil.updateStudent(newStudent);
			listStudents(req,resp);
		}catch(Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	
}