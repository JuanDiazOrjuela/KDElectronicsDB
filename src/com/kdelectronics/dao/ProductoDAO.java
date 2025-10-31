package com.kdelectronics.dao;

import com.kdelectronics.database.DatabaseConnection;
import com.kdelectronics.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private Connection connection;

    public ProductoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // CREATE - Crear nuevo producto
    public boolean crearProducto(Producto producto) {
        String sql = "INSERT INTO productos (codigo_producto, nombre, descripcion, " +
                    "precio_base, precio_venta, categoria, cantidad_disponible, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getCodigoProducto());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getDescripcion());
            stmt.setDouble(4, producto.getPrecioBase());
            stmt.setDouble(5, producto.getPrecioVenta());
            stmt.setString(6, producto.getCategoria());
            stmt.setInt(7, producto.getCantidadDisponible());
            stmt.setBoolean(8, producto.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            manejarExcepcion("Error al crear producto", e);
            return false;
        }
    }

    // READ - Obtener todos los productos ACTIVOS
    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = 1 ORDER BY nombre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = mapearProducto(rs);
                productos.add(producto);
            }
            
            System.out.println("📦 Productos activos encontrados: " + productos.size());
            
        } catch (SQLException e) {
            manejarExcepcion("Error al obtener productos", e);
        }
        return productos;
    }

    // READ - Buscar producto por código (solo ACTIVOS)
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo_producto = ? AND activo = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
            
        } catch (SQLException e) {
            manejarExcepcion("Error al buscar producto", e);
        }
        return null;
    }

    // UPDATE - Actualizar producto
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, " +
                    "precio_base = ?, precio_venta = ?, categoria = ?, " +
                    "cantidad_disponible = ? WHERE codigo_producto = ? AND activo = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecioBase());
            stmt.setDouble(4, producto.getPrecioVenta());
            stmt.setString(5, producto.getCategoria());
            stmt.setInt(6, producto.getCantidadDisponible());
            stmt.setString(7, producto.getCodigoProducto());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            manejarExcepcion("Error al actualizar producto", e);
            return false;
        }
    }

    // DELETE - Eliminación LÓGICA (no física)
    public boolean eliminarProducto(String codigo) {
        String sql = "UPDATE productos SET activo = 0 WHERE codigo_producto = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            manejarExcepcion("Error al eliminar (desactivar) producto", e);
            return false;
        }
    }

    // MÉTODO EXTRA: Obtener productos INACTIVOS (eliminados lógicamente)
    public List<Producto> obtenerProductosInactivos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = 0 ORDER BY nombre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = mapearProducto(rs);
                productos.add(producto);
            }
            
            System.out.println("📦 Productos inactivos encontrados: " + productos.size());
            
        } catch (SQLException e) {
            manejarExcepcion("Error al obtener productos inactivos", e);
        }
        return productos;
    }

    // MÉTODO EXTRA: Reactivar producto eliminado
    public boolean reactivarProducto(String codigo) {
        String sql = "UPDATE productos SET activo = 1 WHERE codigo_producto = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            manejarExcepcion("Error al reactivar producto", e);
            return false;
        }
    }

    // MÉTODO EXTRA: Buscar producto por código (incluye INACTIVOS)
    public Producto buscarPorCodigoConInactivos(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo_producto = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
            
        } catch (SQLException e) {
            manejarExcepcion("Error al buscar producto (con inactivos)", e);
        }
        return null;
    }

    // Método auxiliar para mapear ResultSet a Producto
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getString("codigo_producto"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getDouble("precio_base"),
            rs.getDouble("precio_venta"),
            rs.getString("categoria"),
            rs.getInt("cantidad_disponible"),
            rs.getBoolean("activo")
        );
    }

    // Manejo de excepciones
    private void manejarExcepcion(String mensaje, SQLException e) {
        System.err.println("❌ " + mensaje);
        System.err.println("   Mensaje: " + e.getMessage());
        System.err.println("   Código: " + e.getErrorCode());
        System.err.println("   Estado: " + e.getSQLState());
    }
}