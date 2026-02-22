package com.example.backoffice.dao;

import java.lang.reflect.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DAO {

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

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        }
    }

    public static <T> T get(String sql, Class<T> clazz, Object... params) throws Exception {
        List<T> list = getList(sql, clazz, params);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

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

                        try {
                            mapField(obj, colName, value);
                        } catch (Exception e) {
                            // Ignore mapping errors for missing fields
                        }
                    }

                    mapNestedObjectsLazy(obj, rs);

                    resultList.add(obj);
                }
            }
        }

        return resultList;
    }

    private static void mapField(Object obj, String columnName, Object value) throws Exception {
        String fieldName = toCamelCase(columnName);
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            assignValue(obj, field, value);
        } catch (NoSuchFieldException ignored) {
        }
    }

    private static void assignValue(Object obj, Field field, Object value) throws IllegalAccessException {
        if (value == null)
            return;
        if (value instanceof Timestamp && field.getType().equals(LocalDateTime.class)) {
            field.set(obj, ((Timestamp) value).toLocalDateTime());
        } else {
            field.set(obj, value);
        }
    }

    private static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == String.class
                || Number.class.isAssignableFrom(clazz)
                || clazz == Boolean.class
                || clazz == LocalDateTime.class
                || clazz == java.util.Date.class;
    }

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

    private static void mapNestedObjectsLazy(Object parent, ResultSet rs) throws Exception {
        for (Field field : parent.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (isSimpleType(type))
                continue;

            String table = type.getSimpleName().toLowerCase();
            String idColumn = "id_" + table;
            Object idValue;
            try {
                idValue = rs.getObject(idColumn);
            } catch (SQLException e) {
                continue;
            }

            if (idValue != null) {
                String sql = "SELECT * FROM " + table + " WHERE id = ?";
                Object nestedObj = get(sql, type, idValue);
                if (nestedObj != null) {
                    field.set(parent, nestedObj);
                }
            }
        }
    }

}
