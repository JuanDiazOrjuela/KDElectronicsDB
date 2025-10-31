package com.kdelectronics.database;

import com.kdelectronics.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private DatabaseConfig config;

    // Constructor privado para Singleton
    private DatabaseConnection() {
        config = new DatabaseConfig();
        conectar();
    }

    // Método estático para obtener la instancia
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    private void conectar() {
        try {
            // Cargar el driver
            Class.forName(config.getDriver());
            
            // Establecer conexión
            connection = DriverManager.getConnection(
                config.getUrl(), 
                config.getUser(), 
                config.getPassword()
            );
            
            System.out.println("✅ Conexión establecida con la base de datos");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Driver JDBC no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            manejarErrorSQL(e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("🔁 Reconectando a la base de datos...");
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error verificando conexión: " + e.getMessage());
            conectar();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
        }
    }

    private void manejarErrorSQL(SQLException e) {
        System.err.println("📋 Detalles del error SQL:");
        System.err.println("   Código: " + e.getErrorCode());
        System.err.println("   Estado: " + e.getSQLState());
        System.err.println("   Mensaje: " + e.getMessage());
    }
}