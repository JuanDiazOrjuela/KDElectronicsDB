package com.kdelectronics.test;

import com.kdelectronics.dao.ProductoDAO;
import com.kdelectronics.database.DatabaseConnection;
import com.kdelectronics.model.Producto;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;

public class TestCompleto {
    private static ProductoDAO productoDAO = new ProductoDAO();

    public static void main(String[] args) {
        System.out.println("🧪 ==========================================");
        System.out.println("🧪   PRUEBA COMPLETA - KD ELECTRONICS");
        System.out.println("🧪 ==========================================");
        
        // Ejecutar todas las pruebas
        testConexionBaseDatos();
        testTablaProductos();
        testCRUDProductos();
        testConsultas();
        testEliminacionLogica();
        
        System.out.println("🧪 ==========================================");
        System.out.println("🧪   PRUEBAS FINALIZADAS");
        System.out.println("🧪 ==========================================");
    }
    
    /**
     * Prueba 1: Conexión a la base de datos
     */
    public static void testConexionBaseDatos() {
        System.out.println("\n🔌 PRUEBA 1: CONEXIÓN A BASE DE DATOS");
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión establecida correctamente");
                System.out.println("   📍 URL: " + conn.getMetaData().getURL());
                System.out.println("   👤 Usuario: " + conn.getMetaData().getUserName());
                System.out.println("   🚀 Driver: " + conn.getMetaData().getDriverName());
            } else {
                System.out.println("❌ Error: No se pudo establecer conexión");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en conexión: " + e.getMessage());
        }
    }
    
    /**
     * Prueba 2: Verificar tabla de productos
     */
    public static void testTablaProductos() {
        System.out.println("\n📊 PRUEBA 2: VERIFICAR TABLA PRODUCTOS");
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            
            // Verificar si la tabla existe
            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'productos'");
            if (rs.next()) {
                System.out.println("✅ Tabla 'productos' existe en la base de datos");
                
                // Contar registros activos
                ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) as total FROM productos WHERE activo = 1");
                if (countRs.next()) {
                    int total = countRs.getInt("total");
                    System.out.println("   📦 Total de productos ACTIVOS: " + total);
                }
                
                // Contar registros inactivos
                ResultSet countInactivos = stmt.executeQuery("SELECT COUNT(*) as inactivos FROM productos WHERE activo = 0");
                if (countInactivos.next()) {
                    int inactivos = countInactivos.getInt("inactivos");
                    System.out.println("   📦 Total de productos INACTIVOS: " + inactivos);
                }
                
                // Mostrar estructura de la tabla
                System.out.println("   🏗️  Estructura de la tabla:");
                ResultSet structureRs = stmt.executeQuery("DESCRIBE productos");
                while (structureRs.next()) {
                    System.out.println("      - " + structureRs.getString("Field") + 
                                     " (" + structureRs.getString("Type") + ")");
                }
            } else {
                System.out.println("❌ Error: La tabla 'productos' no existe");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error verificando tabla: " + e.getMessage());
        }
    }
    
    /**
     * Prueba 3: Operaciones CRUD completas
     */
    public static void testCRUDProductos() {
        System.out.println("\n🔄 PRUEBA 3: OPERACIONES CRUD");
        
        // Test CREATE
        System.out.println("📝 Probando CREAR producto...");
        Producto nuevoProducto = new Producto(
            "TEST001", 
            "Producto de Prueba", 
            "Este es un producto de prueba para verificar el sistema",
            100.00, 
            150.00, 
            "Pruebas", 
            50,
            true  // ← NUEVO PARÁMETRO
        );
        
        boolean creado = productoDAO.crearProducto(nuevoProducto);
        if (creado) {
            System.out.println("✅ Producto creado exitosamente: " + nuevoProducto.getNombre());
        } else {
            System.out.println("❌ Error al crear producto");
            return; // Si falla la creación, no continuar
        }
        
        // Test READ (buscar)
        System.out.println("🔍 Probando BUSCAR producto...");
        Producto productoEncontrado = productoDAO.buscarPorCodigo("TEST001");
        if (productoEncontrado != null) {
            System.out.println("✅ Producto encontrado: " + productoEncontrado.getNombre());
            System.out.println("   💰 Precio: $" + productoEncontrado.getPrecioVenta());
            System.out.println("   📊 Stock: " + productoEncontrado.getCantidadDisponible());
            System.out.println("   🟢 Estado: " + (productoEncontrado.isActivo() ? "ACTIVO" : "INACTIVO"));
        } else {
            System.out.println("❌ Error: No se pudo encontrar el producto");
        }
        
        // Test UPDATE
        System.out.println("✏️  Probando ACTUALIZAR producto...");
        if (productoEncontrado != null) {
            productoEncontrado.setPrecioVenta(180.00);
            productoEncontrado.setCantidadDisponible(25);
            productoEncontrado.setDescripcion("Producto actualizado - prueba exitosa");
            
            boolean actualizado = productoDAO.actualizarProducto(productoEncontrado);
            if (actualizado) {
                System.out.println("✅ Producto actualizado exitosamente");
                System.out.println("   💰 Nuevo precio: $" + productoEncontrado.getPrecioVenta());
                System.out.println("   📊 Nuevo stock: " + productoEncontrado.getCantidadDisponible());
            } else {
                System.out.println("❌ Error al actualizar producto");
            }
        }
    }
    
    /**
     * Prueba 4: Consultas y listados
     */
    public static void testConsultas() {
        System.out.println("\n📋 PRUEBA 4: CONSULTAS Y LISTADOS");
        
        // Listar todos los productos ACTIVOS
        System.out.println("📦 Probando LISTAR todos los productos ACTIVOS...");
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        
        if (productos.isEmpty()) {
            System.out.println("ℹ️  No hay productos activos en la base de datos");
        } else {
            System.out.println("✅ Se encontraron " + productos.size() + " productos ACTIVOS:");
            System.out.println("┌───────┬────────────────────────────────────┬──────────────┬───────┬────────┐");
            System.out.println("│ Código │ Nombre                            │ Precio       │ Stock │ Estado │");
            System.out.println("├───────┼────────────────────────────────────┼──────────────┼───────┼────────┤");
            
            for (int i = 0; i < Math.min(productos.size(), 5); i++) {
                Producto p = productos.get(i);
                System.out.printf("│ %-5s │ %-34s │ $%-10.2f │ %-5d │ %-6s │\n",
                    p.getCodigoProducto(),
                    p.getNombre().length() > 34 ? p.getNombre().substring(0, 31) + "..." : p.getNombre(),
                    p.getPrecioVenta(),
                    p.getCantidadDisponible(),
                    p.isActivo() ? "ACTIVO" : "INACTIVO"
                );
            }
            
            if (productos.size() > 5) {
                System.out.println("│ ...   │ ...                               │ ...          │ ...   │ ...    │");
            }
            System.out.println("└───────┴────────────────────────────────────┴──────────────┴───────┴────────┘");
        }
        
        // Probar búsqueda de producto existente
        System.out.println("\n🔎 Probando BÚSQUEDA de producto existente...");
        Producto productoExistente = productoDAO.buscarPorCodigo("PROD001");
        if (productoExistente != null) {
            System.out.println("✅ Producto encontrado: " + productoExistente.getNombre());
        } else {
            System.out.println("ℹ️  Producto PROD001 no encontrado (puede ser normal si no existe)");
        }
        
        // Probar búsqueda de producto no existente
        System.out.println("🔎 Probando BÚSQUEDA de producto NO existente...");
        Producto productoNoExistente = productoDAO.buscarPorCodigo("NOEXISTE999");
        if (productoNoExistente == null) {
            System.out.println("✅ Comportamiento correcto: Producto no existente retorna null");
        } else {
            System.out.println("❌ Comportamiento inesperado");
        }
    }

    /**
     * Prueba 5: Eliminación lógica
     */
    public static void testEliminacionLogica() {
        System.out.println("\n🗑️  PRUEBA 5: ELIMINACIÓN LÓGICA");
        
        // Primero crear un producto para eliminar
        Producto productoEliminar = new Producto(
            "TESTDELETE", 
            "Producto a Eliminar", 
            "Este producto será eliminado lógicamente",
            50.00, 
            75.00, 
            "Pruebas", 
            10,
            true
        );
        
        productoDAO.crearProducto(productoEliminar);
        System.out.println("📝 Producto creado para prueba de eliminación: TESTDELETE");
        
        // Test DELETE (eliminación lógica)
        System.out.println("🗑️  Probando ELIMINAR producto (lógicamente)...");
        boolean eliminado = productoDAO.eliminarProducto("TESTDELETE");
        if (eliminado) {
            System.out.println("✅ Producto 'eliminado' lógicamente (activo = 0)");
        } else {
            System.out.println("❌ Error al eliminar producto");
        }
        
        // Verificar que no aparece en búsquedas normales
        System.out.println("🔍 Verificando que no aparece en búsquedas normales...");
        Producto productoEliminado = productoDAO.buscarPorCodigo("TESTDELETE");
        if (productoEliminado == null) {
            System.out.println("✅ Correcto: Producto no aparece en búsquedas de activos");
        } else {
            System.out.println("❌ Error: El producto aparece en búsquedas después de eliminado");
        }
        
        // Verificar que SÍ aparece en búsquedas con inactivos
        System.out.println("🔍 Verificando que SÍ aparece en búsquedas con inactivos...");
        Producto productoConInactivos = productoDAO.buscarPorCodigoConInactivos("TESTDELETE");
        if (productoConInactivos != null) {
            System.out.println("✅ Correcto: Producto encontrado en búsqueda con inactivos");
            System.out.println("   📝 Estado: " + (productoConInactivos.isActivo() ? "ACTIVO" : "INACTIVO"));
        } else {
            System.out.println("❌ Error: Producto no encontrado ni siquiera en búsqueda con inactivos");
        }
        
        // Test REACTIVAR
        System.out.println("🔄 Probando REACTIVAR producto...");
        boolean reactivado = productoDAO.reactivarProducto("TESTDELETE");
        if (reactivado) {
            System.out.println("✅ Producto reactivado exitosamente");
        } else {
            System.out.println("❌ Error al reactivar producto");
        }
        
        // Verificar que vuelve a aparecer en búsquedas normales
        System.out.println("🔍 Verificando que vuelve a aparecer en búsquedas normales...");
        Producto productoReactivado = productoDAO.buscarPorCodigo("TESTDELETE");
        if (productoReactivado != null) {
            System.out.println("✅ Correcto: Producto reactivado aparece en búsquedas normales");
        } else {
            System.out.println("❌ Error: Producto reactivado no aparece en búsquedas");
        }
        
        // Listar productos inactivos
        System.out.println("📋 Probando listar productos INACTIVOS...");
        List<Producto> inactivos = productoDAO.obtenerProductosInactivos();
        System.out.println("   📦 Productos inactivos encontrados: " + inactivos.size());
    }
}