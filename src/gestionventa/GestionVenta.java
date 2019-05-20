package gestionventa;

import gestionventa.beans.Empleado;
import gestionventa.beans.Producto;
import gestionventa.loads.Datos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GestionVenta {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static void limpiarPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static String leer(String mensaje) {
        String s;
        try {
            System.out.print(mensaje);
            s = br.readLine();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            s = null;
        }
        return s;
    }

    public static Empleado login() {
        Map<String, Empleado> empleados = Datos.getInstance().loadEmpleados();
        System.out.println("Sistema de gestión de compra y venta");
        String codigo = leer("Introduzca su código de empleado: ");
        if (codigo != null && empleados.containsKey(codigo)) {
            String contraseña = leer("Introduzca su contraseña: ");
            if (empleados.get(codigo).getContraseña().equals(contraseña)) {
                return empleados.get(codigo);
            } else {
                System.out.println("Error 222: Contraseña incorrecta");
                return null;
            }
        } else {
            System.out.println("Error 111: Login incorrecto");
            leer("");
            limpiarPantalla();
            return null;
        }
    }

    public static void accionesPrincipales(Empleado empleado) {
        String opcion;
        do {
            opcion = leer("Seleccione la acción:"
                    + "\n1: Hacer pedido"
                    + "\n2: Modificar un producto"
                    + "\n3: Cambiar contraseña empleado"
                    + "\n4: Cerrar sesión: ");
            switch (opcion) {
                case "1":
                    hacerPedido(empleado);
                    break;
                case "2":
                    modificarUnProducto(empleado);
                    break;
                case "3":
                    cambiarContraseña(empleado);
                    break;
                case "4":
                    System.out.println("Adios");
                    cerrarSesion(empleado);
                    break;
                default:
                    System.out.println("Opcion incorrecta");
                    break;

            }
        } while (!opcion.equals("4"));

    }

    public static void hacerPedido(Empleado empleado) {
        System.out.println("Opción hacer pedido..."
                + "\nProductos disponibles: ");
        Map<String, Producto> m = Datos.getInstance().loadProductos();
        List<Producto> carrito = new ArrayList();
        m.forEach((k, v) -> {
            System.out.println(
                    "\ncodigo: " + k
                    + "\nNombre: " + v.getNombre()
                    + "\nDescripción: " + v.getDescripcion()
                    + "\nPrecio: " + v.getPrecio()
                    + "--------------------------------------------------------"
            );
        });
        boolean agregarMas = true;
        while (agregarMas) {
            String codigo = leer("Introduzca el código del producto: ");
            if (m.containsKey(codigo)) {
                carrito.add(m.get(codigo));
            } else {
                System.out.println("Producto no encontrado");
            }
            agregarMas = leer("Introduzca 1 si desea agregar otro producto: ").equals("1");
        }
        if (carrito.isEmpty()) {
            System.out.println("No hay productos disponibles");
        } else {
            Datos.getInstance().guardarFactura(carrito, empleado);
        }
    }

    public static void modificarUnProducto(Empleado empleado) {
        System.out.println("Opción modificar producto..."
                + "\nProductos disponibles: ");
        Map<String, Producto> m = Datos.getInstance().loadProductos();
        m.forEach((k, v) -> {
            System.out.println(
                    "\ncodigo: " + k
                    + "\nNombre: " + v.getNombre()
                    + "\nDescripción: " + v.getDescripcion()
                    + "\nPrecio: " + v.getPrecio()
                    + "--------------------------------------------------------"
            );
        });
        String codigo = leer("Introduzca el código del producto que quiere modificar: ");
        Producto p = m.get(codigo);
        if (p == null) {
            System.out.println("Producot no encontrado");
        } else {
            System.out.println(
                    "\ncodigo: " + p.getCodigo()
                    + "\nNombre: " + p.getNombre()
                    + "\nDescripción: " + p.getDescripcion()
                    + "\nPrecio: " + p.getPrecio()
            );
            String opcion = leer("Introduzca el número de la opción"
                    + "\n 1: Modificar el nombre del producto"
                    + "\n 2: Modificar el precio del producto"
                    + "\n 2: Modificar el codigo del producto: "
            );
            boolean coincide = false;
            switch(opcion){
                case "1": 
                    String nuevoNombre = leer("Introduzca el nuevo nombre: ");
                    for(String key : m.keySet()){
                        if(m.get(key).getNombre().equals(nuevoNombre)){
                            coincide = true;
                            break;
                        }
                    }
                    if(coincide){
                        System.out.println("ERROR: El nuevo nombre ya está registrado.");
                    } else{
                        p.setNombre(nuevoNombre);
                        m.put(p.getCodigo(), p);
                        System.out.println("Nombre actualizado...");
                    }
                    break;
                case "2": 
                    String nuevoPrecio = leer("Introduzca el nuevo precio: ");
                    p.setPrecio(nuevoPrecio);
                    m.put(p.getCodigo(), p);
                    System.out.println("Precio actualizado....");
                    break;
                case "3": 
                    String nuevoCodigo = leer("Introduzca el nuevo codigo: ");
                    if(m.containsKey(nuevoCodigo)){
                        System.out.println("ERROR: El nuevo código ya está registrado");
                    } else {
                        m.remove(p.getCodigo());
                        p.setCodigo(nuevoCodigo);
                        m.put(p.getCodigo(), p);
                        System.out.println("Código actualizado...");
                    }
                    break;
                default:
                    System.out.println("Opción incorrecta...");
                    break;
            }
//            p.setNombre(leer("Introduzca el nombre: "));
//            p.setDescripcion(leer("Introduzca la descripción: "));
//            p.setPrecio(leer("Introduzca el precio: "));
//            m.put(p.getCodigo(), p);
            Datos.getInstance().updateProductos(m);
        }
    }

    public static void cambiarContraseña(Empleado empleado) {
        System.out.println("Opción cambiar contraseña...");
        String claveActual = leer("Introduzca su clave actual: ");
        if (empleado.getContraseña().equals(claveActual)) {
            String nuevaClave, nuevaClave2;
            do {
                nuevaClave = leer("Introduzca la nueva clave: ");
                nuevaClave2 = leer("Introduzca la nueva clave de nuevo: ");
                if (!nuevaClave.equals(nuevaClave2)) {
                    System.out.println("Las claves no coinciden. Intente nuevamente");
                }
            } while (!nuevaClave.equals(nuevaClave2));
            empleado.setContraseña(nuevaClave);
            Map<String, Empleado> m = Datos.getInstance().loadEmpleados();
            m.put(empleado.getCodigo(), empleado);
            Datos.getInstance().updateEmpleados(m);
        } else {
            System.out.println("La clave no es correcta");
        }
    }

    public static void cerrarSesion(Empleado empleado) {
        empleado = null;
        limpiarPantalla();
    }

   
    public static void main(String[] args) {
        Empleado empleado;
        while (true) {
            empleado = login();
            if (empleado != null) {
                accionesPrincipales(empleado);
            }
        }
    }

}
