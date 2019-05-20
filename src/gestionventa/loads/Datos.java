package gestionventa.loads;

import gestionventa.beans.Empleado;
import gestionventa.beans.Producto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Datos {

    private static Datos datos = null;

    private Datos() {

    }

    public static Datos getInstance() {
        if (datos == null) {
            datos = new Datos();
        }
        return datos;
    }

    public static final String DATOS_EMPLEADOS = "datos-empleados.txt";
    public static final String DATOS_PRODUCTOS = "DatosProductos.txt";
    public static final String DATOS_FACTURA = "factura.txt";

    public String determineEncoding(String filePath) {
        String c;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
            c = isr.getEncoding();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            c = "ex";
        }
        return c;
    }

    /**
     * Carga los datos de los empleados desde el archivo DATOS_EMPLEADOS
     *
     * @return mapa con los datos de los empleados
     */
    public Map<String, Empleado> loadEmpleados() {
        Map<String, Empleado> m = new HashMap();
        File archivo;
        FileReader fr = null;
        try {
            archivo = new File(DATOS_EMPLEADOS);
            BufferedReader br = new BufferedReader(
                    //                    new InputStreamReader(new FileInputStream(archivo), "ISO-8859-1"));
                    new InputStreamReader(new FileInputStream(archivo), "UTF8"));
            String linea;
            Empleado e = null;
            boolean nextCodigo = false;
            boolean nextNombre = false;
            boolean nextApellido = false;
            boolean nextContraseña = false;
            while ((linea = br.readLine()) != null) {
                //System.out.println(linea);
//                if (linea.trim().equalsIgnoreCase("[empleado]")) {
                if (linea.trim().contains("[empleado]")) {
                    if (e != null) {
                        m.put(e.getCodigo(), e);
                    }
                    e = new Empleado();
                } else if (linea.trim().equalsIgnoreCase("[codigo]")) {
                    nextCodigo = true;
                } else if (linea.trim().equalsIgnoreCase("[nombre]")) {
                    nextNombre = true;
                } else if (linea.trim().equalsIgnoreCase("[apellidos]")) {
                    nextApellido = true;
//                } else if (linea.trim().equalsIgnoreCase("[contraseña]")) {
                } else if (linea.trim().contains("[contrase")) {
                    nextContraseña = true;
                } else {
                    if (nextCodigo) {
                        if (e != null) {
                            e.setCodigo(linea.trim());
                            nextCodigo = false;
                        }
                    } else if (nextNombre) {
                        if (e != null) {
                            e.setNombre(linea.trim());
                            nextNombre = false;
                        }
                    } else if (nextApellido) {
                        if (e != null) {
                            e.setApellidos(linea.trim());
                            nextApellido = false;
                        }
                    } else if (nextContraseña) {
                        if (e != null) {
                            e.setContraseña(linea.trim());
                            nextContraseña = false;
                        }
                    }
                }
            }
            if (e != null) {
                m.put(e.getCodigo(), e);
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace(System.out);
            }
        }

        return m;
    }

    /**
     * Carga los datos de los productos desde el archivo DATOS_PRODUCTOS
     *
     * @return mapa con los datos de los productos
     */
    public Map<String, Producto> loadProductos() {
        Map<String, Producto> m = new HashMap();
        File archivo;
        FileReader fr = null;
        try {
            archivo = new File(DATOS_PRODUCTOS);
            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(new FileInputStream(archivo), "ISO-8859-1"));
                    new InputStreamReader(new FileInputStream(archivo), "UTF8"));
            String linea;
            Producto p = null;
            boolean nextCodigo = false;
            boolean nextNombre = false;
            boolean nextDescripcion = false;
            boolean nextPrecio = false;
            while ((linea = br.readLine()) != null) {
                //System.out.println(linea);
//                if (linea.trim().equalsIgnoreCase("[producto]")) {
                if (linea.trim().contains("[producto]")) {
                    if (p != null) {
                        m.put(p.getCodigo(), p);
                    }
                    p = new Producto();
                } else if (linea.trim().equalsIgnoreCase("[codigo]")) {
                    nextCodigo = true;
                } else if (linea.trim().equalsIgnoreCase("[nombre]")) {
                    nextNombre = true;
                } else if (linea.trim().equalsIgnoreCase("[descripcion]")) {
                    nextDescripcion = true;
                } else if (linea.trim().equalsIgnoreCase("[precio]")) {
                    nextPrecio = true;
                } else {
                    if (nextCodigo) {
                        if (p != null) {
                            p.setCodigo(linea.trim());
                            nextCodigo = false;
                        }
                    } else if (nextNombre) {
                        if (p != null) {
                            p.setNombre(linea.trim());
                            nextNombre = false;
                        }
                    } else if (nextDescripcion) {
                        if (p != null) {
                            p.setDescripcion(linea.trim());
                            nextDescripcion = false;
                        }
                    } else if (nextPrecio) {
                        if (p != null) {
                            p.setPrecio(linea.replace(",", ".").trim());
                            nextPrecio = false;
                        }
                    }
                }
            }
            if (p != null) {
                m.put(p.getCodigo(), p);
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace(System.out);
            }
        }

        return m;
    }

    /**
     * Actualiza los datos de los empleados en el archivo
     *
     * @param empleados
     */
    public void updateEmpleados(Map<String, Empleado> empleados) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(DATOS_EMPLEADOS);
            pw = new PrintWriter(fichero);
            empleados.forEach((k, v) -> {
                pw.println("[empleado]");
                pw.println("[codigo]");
                pw.println(k);
                pw.println("[nombre]");
                pw.println(v.getNombre());
                pw.println("[apellidos]");
                pw.println(v.getApellidos());
                pw.println("[contraseña]");
                pw.println(v.getContraseña());
            });

        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace(System.out);
            }
        }
    }

    /**
     * Actualiza los datos de los productos en el archivo
     *
     * @param productos
     */
    public void updateProductos(Map<String, Producto> productos) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(DATOS_PRODUCTOS);
            pw = new PrintWriter(fichero);
            productos.forEach((k, v) -> {
                pw.println("[producto]");
                pw.println("[codigo]");
                pw.println(k);
                pw.println("[nombre]");
                pw.println(v.getNombre());
                pw.println("[descripcion]");
                pw.println(v.getDescripcion());
                pw.println("[precio]");
                pw.println(v.getPrecio());
            });

        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace(System.out);
            }
        }
    }

    public void guardarFactura(List<Producto> carrito, Empleado empleado) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(DATOS_FACTURA, true);
            pw = new PrintWriter(fichero);
            double total = 0.0;
            System.out.println("Factura simplificada: ");
            System.out.println("-------------------------------------------------------");
            pw.println("Factura simplificada: ");
            pw.println("-------------------------------------------------------");
            for (Producto producto : carrito) {
                System.out.println("Código: " + producto.getCodigo());
                System.out.println("Nombre: " + producto.getNombre());
                System.out.println("Descripción: " + producto.getDescripcion());
                System.out.println("Precio: " + producto.getPrecio());

                pw.println("Código: " + producto.getCodigo());
                pw.println("Nombre: " + producto.getNombre());
                pw.println("Descripción: " + producto.getDescripcion());
                pw.println("Precio: " + producto.getPrecio());
                total += Double.parseDouble(producto.getPrecio());
                System.out.println();
                pw.println();
            }
            System.out.println("-------------------------------------------------------");
            System.out.println("El precio TOTAL es: " + total + "€ ");
            System.out.println("Atendido por: " + empleado.getNombre() + " " + empleado.getApellidos());
            System.out.println();
            System.out.println();

            pw.println("-------------------------------------------------------");
            pw.println("El precio TOTAL es: " + total + "€ ");
            pw.println("Atendido por: " + empleado.getNombre() + " " + empleado.getApellidos());
            pw.println();
            pw.println();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace(System.out);
            }
        }
    }

    public static void main(String[] args) {
        Datos e = new Datos();
        System.out.println("empleados: " + e.determineEncoding(DATOS_EMPLEADOS));
        e.loadEmpleados().forEach((k, v) -> {
            System.out.println("-----------------------------------------------");
            System.out.println("codigo: " + k);
            System.out.println("nombre: " + v.getNombre());
            System.out.println("Apellido: " + v.getApellidos());
            System.out.println("Contraseña: " + v.getContraseña());
        });

        System.out.println("\n\nproductos: " + e.determineEncoding(DATOS_PRODUCTOS));
        e.loadProductos().forEach((k, v) -> {
            System.out.println("-----------------------------------------------");
            System.out.println("codigo: " + k);
            System.out.println("nombre: " + v.getNombre());
            System.out.println("Descripcion: " + v.getDescripcion());
            System.out.println("Precio: " + v.getPrecio());
        });
    }
}
