package com.example.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/save-number")
public class SaveNumberServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/numbersdb";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "mysecretpassword";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String number = request.getParameter("number");

        // Set response content type
        response.setContentType("text/plain");

        // Validate if 'number' is a valid integer
        int parsedNumber;
        try {
            parsedNumber = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid number format!");
            return; // Exit early if the input is not valid
        }

        // Register the PostgreSQL JDBC driver
        try {
            Class.forName("org.postgresql.Driver");  // Explicitly load the PostgreSQL driver
        } catch (ClassNotFoundException e) {
            throw new ServletException("PostgreSQL JDBC Driver not found", e);
        }
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO numbers (value) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, parsedNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Unable to save the number", e);
        }

        response.getWriter().write("Number saved successfully!");
    }
}
