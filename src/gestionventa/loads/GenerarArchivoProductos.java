package gestionventa.loads;

import gestionventa.beans.Producto;
import java.util.HashMap;
import java.util.Map;


 // Esta clase es utilizada para generar el archivo inicial de productos
 
public class GenerarArchivoProductos {
    
    public static void main(String [] args){
        Producto p1 = new Producto();
        p1.setCodigo("222");
        p1.setNombre("Gigabyte B450M DS3H");
        p1.setDescripcion("Placa base GIGABYTE serie 400 con tecnologia AMD StoreMI");
        p1.setPrecio("65,99");
        
        Producto p2 = new Producto();
        p1.setCodigo("223");
        p1.setNombre("Kingston A400 SSD 240GB");
        p1.setDescripcion("Disco duro A400 de estado sólido de Kingston");
        p1.setPrecio("32,95");
        
        Producto p3 = new Producto();
        p1.setCodigo("224");
        p1.setNombre("Lenovo Ideapad 530S");
        p1.setDescripcion("Ordenador portátil con Intel Core i5, 8GB RAM, 256GB SSD");
        p1.setPrecio("599");
        
        
        Map<String, Producto> productos = new HashMap();
        productos.put(p1.getCodigo(), p1);
        productos.put(p2.getCodigo(), p2);
        productos.put(p3.getCodigo(), p3);
        
        Datos.getInstance().updateProductos(productos);

    }
}
