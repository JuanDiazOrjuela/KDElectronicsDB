# 🏪 KD-Electronics - Sistema de Gestión de Inventario

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![JDBC](https://img.shields.io/badge/JDBC-Connectivity-green)
![NetBeans](https://img.shields.io/badge/IDE-NetBeans-purple)

Sistema CRUD completo para gestión de productos electrónicos desarrollado en Java con conexión a base de datos MySQL mediante JDBC. Implementa todas las operaciones básicas de un sistema de inventario con eliminación lógica.

## 🚀 Características

### ✅ Operaciones CRUD Completas
- **📝 Create**: Registrar nuevos productos en el inventario
- **🔍 Read**: Consultar productos por código y listar todos los activos
- **✏️ Update**: Actualizar información de productos (excepto código)
- **🗑️ Delete**: Eliminación lógica (no física) de productos

### 🛡️ Funcionalidades Avanzadas
- **Eliminación Lógica**: Los productos no se borran físicamente, se marcan como inactivos
- **Validaciones**: Manejo robusto de errores y validación de datos
- **Persistencia**: Conexión segura a base de datos MySQL
- **Interfaz Consola**: Menú interactivo fácil de usar

## 📋 Tabla de Contenidos

- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías](#-tecnologías)
- [Base de Datos](#-base-de-datos)

## ⚙️ Instalación

### Prerrequisitos
- **Java JDK 8 o superior**
- **MySQL Server 5.7+**
- **MySQL Connector/J**
- **NetBeans IDE** (recomendado) o cualquier IDE Java

### Pasos de Instalación
1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/JuanDiazOrjuela/KDElectronicsDB.git