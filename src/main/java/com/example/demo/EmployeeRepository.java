package com.example.demo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeRepository {

    /*public static void main(String[] args) {
        getConnection();

        Employee employee = new Employee();

        employee.setName("oleg");
        employee.setEmail(" ");
        employee.setCountry(" ");
        save(employee);
    }*/

    public static Connection getConnection() {

        Connection connection = null;
        String url = "jdbc:postgresql://localhost:5432/employee";
        String user = "postgres";
        String password = "postgres";

        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
        return connection;
    }

    public static int save(Employee employee) {
        int status = 0;
        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("insert into users_test_married (name,email,country, phone_number, age, is_married) values (?,?,?,?,?,?)");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getCountry());
            ps.setString(4, employee.getPhoneNumber());
            ps.setInt(5, employee.getAge());
            ps.setBoolean(6, employee.isMarried());

            status = ps.executeUpdate();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static int update(Employee employee) {

        int status = 0;

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("update users_test_married set name=?,email=?,country=?, phone_number=?, age=?, is_married=? where id=?");
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getCountry());
            ps.setString(4, employee.getPhoneNumber());
            ps.setInt(5, employee.getAge());
            ps.setBoolean(6, employee.isMarried());
            ps.setInt(7, employee.getId());

            status = ps.executeUpdate();
            connection.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return status;
    }

    public static int delete(int id) {

        int status = 0;

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("delete from users_test_married where id=?");
            ps.setInt(1, id);
            status = ps.executeUpdate();

            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public static Employee getEmployeeById(int id) {

        Employee employee = new Employee();

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from users_test_married where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setEmail(rs.getString(3));
                employee.setCountry(rs.getString(4));
                employee.setPhoneNumber(rs.getString(5));
                employee.setAge(rs.getInt(6));
                employee.setMarried(rs.getBoolean(7));
            }
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return employee;
    }

    public static List<Employee> getAllEmployees() {

        List<Employee> listEmployees = new ArrayList<>();

        try {
            Connection connection = EmployeeRepository.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from users_test_married");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Employee employee = new Employee();

                employee.setId(rs.getInt(1));
                employee.setName(rs.getString(2));
                employee.setEmail(rs.getString(3));
                employee.setCountry(rs.getString(4));
                employee.setPhoneNumber(rs.getString(5));
                employee.setAge(rs.getInt(6));
                employee.setMarried(rs.getBoolean(7));

                listEmployees.add(employee);
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listEmployees;
    }


    // technical methods
    public static boolean checkEmployeeParameters(HttpServletRequest req, PrintWriter out) {
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = req.getParameter(name);
            if (name.equals("id") && Integer.parseInt(value) < 0) {
                out.println("wrong id parameter");
                return false;
            }
            Pattern phonePattern = Pattern.compile("\\d+");
            if (name.equals("phoneNumber") && !phonePattern.matcher(value).find()) {
                out.println("Wrong phone number");
                return false;
            }
            Pattern emailPattern = Pattern.compile("[\\w\\.-]+@\\w+\\.\\w+");
            if (name.equals("email") && !emailPattern.matcher(value).find()) {
                out.println("Wrong email");
                return false;
            }
        }
        return true;
    }

    public static boolean checkRequest(HttpServletRequest req) {
        String[] rightParams = new String[]{"id", "name", "country", "email", "phoneNumber", "age", "is_married"};
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            if (Arrays.stream(rightParams).noneMatch(name::equals)) {
                return false;
            }
        }
        return true;
    }
}
