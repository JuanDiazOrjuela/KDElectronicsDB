package com.kdelectronics.model;

public class Producto {
    private String codigoProducto;
    private String nombre;
    private String descripcion;
    private double precioBase;
    private double precioVenta;
    private String categoria;
    private int cantidadDisponible;
    private boolean activo = true;  

    // Constructor completo
    public Producto(String codigoProducto, String nombre, String descripcion, 
                   double precioBase, double precioVenta, String categoria, 
                   int cantidadDisponible, boolean activo) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.cantidadDisponible = cantidadDisponible;
        this.activo = activo; 
    }

    
    public Producto() {}

    // Getters y Setters
    public String getCodigoProducto() { 
        return codigoProducto; 
    }
    
    public void setCodigoProducto(String codigoProducto) { 
        this.codigoProducto = codigoProducto; 
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getDescripcion() { 
        return descripcion; 
    }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }

    public double getPrecioBase() { 
        return precioBase; 
    }
    
    public void setPrecioBase(double precioBase) { 
        this.precioBase = precioBase; 
    }

    public double getPrecioVenta() { 
        return precioVenta; 
    }
    
    public void setPrecioVenta(double precioVenta) { 
        this.precioVenta = precioVenta; 
    }

    public String getCategoria() { 
        return categoria; 
    }
    
    public void setCategoria(String categoria) { 
        this.categoria = categoria; 
    }

    public int getCantidadDisponible() { 
        return cantidadDisponible; 
    }
    
    public void setCantidadDisponible(int cantidadDisponible) { 
        this.cantidadDisponible = cantidadDisponible; 
    }

    
    public boolean isActivo() { 
        return activo; 
    }
    
    public void setActivo(boolean activo) { 
        this.activo = activo; 
    }

    @Override
    public String toString() {
        String estado = activo ? "ACTIVO" : "INACTIVO";
        return String.format("Código: %s | Nombre: %s | Precio: $%.2f | Stock: %d | Estado: %s", 
                           codigoProducto, nombre, precioVenta, cantidadDisponible, estado);
    }
}