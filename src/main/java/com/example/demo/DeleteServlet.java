package com.example.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/deleteServlet")
public class DeleteServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        if (EmployeeRepository.checkRequest(request, out) && EmployeeRepository.checkEmployeeParameters(request, out)) {
            String sid = request.getParameter("id");
            int id = Integer.parseInt(sid);
            EmployeeRepository.delete(id);
            response.sendRedirect("viewServlet");
        }
    }
}
