/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    // Guardar lista de objetos en archivo
    public static <T> void guardarLista(List<T> lista, String ruta) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
        }
    }
    
    // Cargar lista de objetos desde archivo
    @SuppressWarnings("unchecked")
    public static <T> List<T> cargarLista(String ruta) throws IOException, ClassNotFoundException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<T>) ois.readObject();
        }
    }
    
    // Guardar un solo objeto
    public static void guardarObjeto(Serializable objeto, String ruta) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(objeto);
        }
    }
    
    // Cargar un solo objeto
    public static Object cargarObjeto(String ruta) throws IOException, ClassNotFoundException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return ois.readObject();
        }
    }
    
    // Crear directorio si no existe
    public static void crearDirectorio(String ruta) {
        File dir = new File(ruta);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}