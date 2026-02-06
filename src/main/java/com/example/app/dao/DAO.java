package com.example.app.dao;

import java.sql.*;
import java.util.*;
import java.lang.reflect.*;

public class DAO {

    // --- 1. Connexion PostgreSQL ---
    private static final String URL = "jdbc:postgresql://localhost:5432/AeroAssign";
    private static final String USER = "postgres";
    private static final String PASSWORD = " ";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // --- 2. Execute Update (INSERT / UPDATE / DELETE) ---
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            int affectedRows = stmt.executeUpdate();
            return affectedRows;
        }
    }

    // --- 3. Execute Query pour un seul objet ---
    public static <T> T get(String sql, Class<T> clazz, Object... params) throws Exception {
        List<T> list = getList(sql, clazz, params);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    // --- 4. Execute Query → map result to object T ---
    public static <T> List<T> getList(String sql, Class<T> clazz, Object... params) throws Exception {
        List<T> resultList = new ArrayList<>();

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    T obj = clazz.getDeclaredConstructor().newInstance();

                    for (int i = 1; i <= colCount; i++) {
                        String colName = meta.getColumnLabel(i);
                        Object value = rs.getObject(i);

                        // Convert snake_case → camelCase
                        String fieldName = toCamelCase(colName);

                        try {
                            Field field = clazz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            field.set(obj, value);
                        } catch (NoSuchFieldException ignored) {
                            // ignore fields not found
                        }
                    }
                    resultList.add(obj);
                }
            }
        }

        return resultList;
    }

    // --- Helper : snake_case → camelCase ---
    private static String toCamelCase(String snake) {
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (char c : snake.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else {
                sb.append(upperNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                upperNext = false;
            }
        }
        return sb.toString();
    }
}
