package com.kdelectronics.main;

import com.kdelectronics.dao.ProductoDAO;
import com.kdelectronics.model.Producto;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🎯 ===================================");
        System.out.println("🎯   SISTEMA KD-ELECTRONICS - CRUD COMPLETO");
        System.out.println("🎯 ===================================");
        
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> agregarProducto();
                case 3 -> buscarProducto();
                case 4 -> actualizarProducto();
                case 5 -> eliminarProducto();
                case 6 -> mostrarProductosInactivos();
                case 7 -> reactivarProducto();
                case 8 -> salir = true;
                default -> System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        }
        
        System.out.println("👋 ¡Hasta pronto! Gracias por usar KD-Electronics");
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n📋 --- MENÚ PRINCIPAL ---");
        System.out.println("1. 📦 Listar productos activos");
        System.out.println("2. ➕ Agregar nuevo producto");
        System.out.println("3. 🔍 Buscar producto por código");
        System.out.println("4. ✏️  Actualizar producto");
        System.out.println("5. 🗑️  Eliminar producto (lógico)");
        System.out.println("6. 📋 Ver productos eliminados");
        System.out.println("7. 🔄 Reactivar producto");
        System.out.println("8. 🚪 Salir");
        System.out.print("👉 Seleccione una opción: ");
    }

    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void listarProductos() {
        System.out.println("\n📦 --- LISTA DE PRODUCTOS ACTIVOS ---");
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        
        if (productos.isEmpty()) {
            System.out.println("ℹ️  No hay productos activos registrados.");
        } else {
            System.out.println("┌─────────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                                PRODUCTOS ACTIVOS                                     │");
            System.out.println("├─────────────────────────────────────────────────────────────────────────────────────┤");
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                System.out.printf("│ %d. %-70s │\n", (i + 1), p.toString());
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────────────────────┘");
        }
    }

    private static void agregarProducto() {
        System.out.println("\n➕ --- AGREGAR NUEVO PRODUCTO ---");
        
        try {
            System.out.print("📝 Código del producto: ");
            String codigo = scanner.nextLine();
            
            // Verificar si el código ya existe (incluyendo inactivos)
            Producto existente = productoDAO.buscarPorCodigoConInactivos(codigo);
            if (existente != null) {
                if (existente.isActivo()) {
                    System.out.println("❌ Ya existe un producto ACTIVO con ese código.");
                } else {
                    System.out.println("❌ Ya existe un producto INACTIVO con ese código.");
                    System.out.print("¿Desea reactivarlo? (s/n): ");
                    String respuesta = scanner.nextLine();
                    if (respuesta.equalsIgnoreCase("s")) {
                        if (productoDAO.reactivarProducto(codigo)) {
                            System.out.println("✅ Producto reactivado exitosamente.");
                        }
                    }
                }
                return;
            }
            
            System.out.print("📝 Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("📝 Descripción: ");
            String descripcion = scanner.nextLine();
            
            System.out.print("💰 Precio Base: ");
            double precioBase = Double.parseDouble(scanner.nextLine());
            
            System.out.print("💰 Precio de Venta: ");
            double precioVenta = Double.parseDouble(scanner.nextLine());
            
            System.out.print("📂 Categoría: ");
            String categoria = scanner.nextLine();
            
            System.out.print("📊 Cantidad Disponible: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            
            // Crear producto como ACTIVO por defecto
            Producto producto = new Producto(codigo, nombre, descripcion, 
                                           precioBase, precioVenta, categoria, cantidad, true);
            
            if (productoDAO.crearProducto(producto)) {
                System.out.println("✅ Producto agregado exitosamente.");
            } else {
                System.out.println("❌ Error al agregar el producto.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Formato numérico inválido.");
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    private static void buscarProducto() {
        System.out.print("\n🔍 Ingrese código del producto a buscar: ");
        String codigo = scanner.nextLine();
        
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto != null) {
            System.out.println("\n✅ Producto encontrado:");
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.println("│                    DETALLES DEL PRODUCTO               │");
            System.out.println("├────────────────────────────────────────────────────────┤");
            System.out.printf("│ Código: %-45s │\n", producto.getCodigoProducto());
            System.out.printf("│ Nombre: %-45s │\n", producto.getNombre());
            System.out.printf("│ Descripción: %-38s │\n", 
                producto.getDescripcion().length() > 38 ? 
                producto.getDescripcion().substring(0, 35) + "..." : producto.getDescripcion());
            System.out.printf("│ Precio Base: $%-38.2f │\n", producto.getPrecioBase());
            System.out.printf("│ Precio Venta: $%-37.2f │\n", producto.getPrecioVenta());
            System.out.printf("│ Categoría: %-40s │\n", producto.getCategoria());
            System.out.printf("│ Stock: %-43d │\n", producto.getCantidadDisponible());
            System.out.printf("│ Estado: %-42s │\n", producto.isActivo() ? "ACTIVO" : "INACTIVO");
            System.out.println("└────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("❌ Producto no encontrado o está inactivo.");
        }
    }

    private static void actualizarProducto() {
        System.out.print("\n✏️  Ingrese código del producto a actualizar: ");
        String codigo = scanner.nextLine();
        
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto == null) {
            System.out.println("❌ Producto no encontrado o está inactivo.");
            return;
        }
        
        System.out.println("📝 Actualizando producto: " + producto.getNombre());
        System.out.println("💡 Deje en blanco para mantener el valor actual");
        
        try {
            System.out.printf("📝 Nombre [%s]: ", producto.getNombre());
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) producto.setNombre(nombre);
            
            System.out.printf("📝 Descripción [%s]: ", producto.getDescripcion());
            String descripcion = scanner.nextLine();
            if (!descripcion.isEmpty()) producto.setDescripcion(descripcion);
            
            System.out.printf("💰 Precio Base [%.2f]: ", producto.getPrecioBase());
            String precioBaseStr = scanner.nextLine();
            if (!precioBaseStr.isEmpty()) producto.setPrecioBase(Double.parseDouble(precioBaseStr));
            
            System.out.printf("💰 Precio Venta [%.2f]: ", producto.getPrecioVenta());
            String precioVentaStr = scanner.nextLine();
            if (!precioVentaStr.isEmpty()) producto.setPrecioVenta(Double.parseDouble(precioVentaStr));
            
            System.out.printf("📂 Categoría [%s]: ", producto.getCategoria());
            String categoria = scanner.nextLine();
            if (!categoria.isEmpty()) producto.setCategoria(categoria);
            
            System.out.printf("📊 Cantidad [%d]: ", producto.getCantidadDisponible());
            String cantidadStr = scanner.nextLine();
            if (!cantidadStr.isEmpty()) producto.setCantidadDisponible(Integer.parseInt(cantidadStr));
            
            if (productoDAO.actualizarProducto(producto)) {
                System.out.println("✅ Producto actualizado exitosamente.");
            } else {
                System.out.println("❌ Error al actualizar el producto.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Formato numérico inválido.");
        }
    }

    private static void eliminarProducto() {
        System.out.print("\n🗑️  Ingrese código del producto a eliminar: ");
        String codigo = scanner.nextLine();
        
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto == null) {
            System.out.println("❌ Producto no encontrado o ya está eliminado.");
            return;
        }
        
        System.out.println("⚠️  ¿Está seguro de eliminar: " + producto.getNombre() + "?");
        System.out.println("💡 Nota: Esta eliminación será LÓGICA (no se borrará de la base de datos)");
        System.out.print("⚠️  Escriba 'ELIMINAR' para confirmar: ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("ELIMINAR")) {
            if (productoDAO.eliminarProducto(codigo)) {
                System.out.println("✅ Producto eliminado lógicamente.");
                System.out.println("   📝 El producto ya no aparecerá en las consultas normales.");
            } else {
                System.out.println("❌ Error al eliminar el producto.");
            }
        } else {
            System.out.println("✅ Eliminación cancelada.");
        }
    }

    private static void mostrarProductosInactivos() {
        System.out.println("\n📋 --- PRODUCTOS ELIMINADOS (INACTIVOS) ---");
        List<Producto> productos = productoDAO.obtenerProductosInactivos();
        
        if (productos.isEmpty()) {
            System.out.println("ℹ️  No hay productos eliminados.");
        } else {
            System.out.println("┌─────────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("│                               PRODUCTOS INACTIVOS                                    │");
            System.out.println("├─────────────────────────────────────────────────────────────────────────────────────┤");
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                System.out.printf("│ %d. %-70s │\n", (i + 1), p.toString());
            }
            System.out.println("└─────────────────────────────────────────────────────────────────────────────────────┘");
        }
    }

    private static void reactivarProducto() {
        System.out.print("\n🔄 Ingrese código del producto a reactivar: ");
        String codigo = scanner.nextLine();
        
        // Buscar incluyendo inactivos
        Producto producto = productoDAO.buscarPorCodigoConInactivos(codigo);
        if (producto == null) {
            System.out.println("❌ Producto no encontrado.");
            return;
        }
        
        if (producto.isActivo()) {
            System.out.println("ℹ️  El producto ya está activo.");
            return;
        }
        
        System.out.println("📝 Producto a reactivar: " + producto.getNombre());
        System.out.print("¿Está seguro de reactivar este producto? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            if (productoDAO.reactivarProducto(codigo)) {
                System.out.println("✅ Producto reactivado exitosamente.");
                System.out.println("   📝 El producto ahora aparecerá en las consultas normales.");
            } else {
                System.out.println("❌ Error al reactivar el producto.");
            }
        } else {
            System.out.println("✅ Reactivación cancelada.");
        }
    }
}