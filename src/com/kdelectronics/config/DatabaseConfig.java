package com.kdelectronics.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private Properties properties;

    public DatabaseConfig() {
        properties = new Properties();
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            System.out.println("✅ Configuración cargada desde: " + CONFIG_FILE);
        } catch (IOException e) {
            // Valores por defecto si no existe el archivo
            System.out.println("⚠️  Usando configuración por defecto");
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/kdelectronics");
            properties.setProperty("db.user", "root");
            properties.setProperty("db.password", "");
            properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        }
    }

    public String getUrl() { return properties.getProperty("db.url"); }
    public String getUser() { return properties.getProperty("db.user"); }
    public String getPassword() { return properties.getProperty("db.password"); }
    public String getDriver() { return properties.getProperty("db.driver"); }
}