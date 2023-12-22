/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import org.bson.Document;

/**
 *
 * @author rafacampa9
 */
public class Conexion {
    
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DB = "RAGNAROCK";
    private static final String COLLECTION = "PICOS DE AFORO";
    
    private MongoCollection<Document> collection;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private ArrayList<Registro> registros;
    
    /**
     * Constructor vacío
     */
    public Conexion(){
        //conectar();
        //crearDB();
        //crearConexion();
    }
    
    /**
     * 
     * @param collection
     * 
     * Constructor con el parámetro collection,
     * ya que en la clase Ctrl nos interesa 
     * que le pasemos por parámetro la colección,
     * para que de este modo no se de una excepción
     * del tipo NullPointerException
     * 
     */
    
    public Conexion (MongoCollection <Document> collection){    
        this.collection = collection;   
    }
    
    /**
     * Método para conectar
     * con la base de datos
     * de MongoDB
     */
    public void conectar(){
        try{
            
            if (mongoClient == null)
                System.out.println("La conexión estaba cerrada al principio");
            // Crea la conexión a MongoDB
            mongoClient = new MongoClient(HOST, PORT);
            
            //Conexión a la base de datos
            database = mongoClient.getDatabase(DB);
            System.out.println("Conectado a la DB: " + DB);
        } catch (MongoException e){
            e.printStackTrace();
        }
    }
    
    
    /**
     * Con este método, crearemos la base de datos
     * en MongoDB. No será necesario utilizarlo en el 
     * proyecto una vez ya creada. Si es la primera
     * vez que vas a ejecutar el proyecto, puedes incluir
     * este método y el método crearColeccion() en el 
     * constructor de esta clase. Las secuencia sería:
     *      1º - conectar();
     *      2º - crearDB();
     *      3º - crearColeccion();
     */
    public void crearDB(){
        
        //Crear la base de datos si no existe
        if (!mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(DB)){
            database.createCollection(DB);
            System.out.println("Base de datos creada: " + DB);
        } else {
            System.out.println("La base de datos ya existe: " + DB);
        }
    }
    
    public void crearColeccion(){
        if (!database.listCollectionNames().into(new ArrayList<>()).contains(COLLECTION)){
            database.createCollection(COLLECTION);
            System.out.println("Colección creada: " + COLLECTION);
            
        } else {
            System.out.println("Colección ya existente: " + COLLECTION);
        }    
    }
    
    
    /**
     * Por si en algún momento
     * queremos eliminar la colección
     */
    public void borrarColeccion(){
        try{
            if (mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(DB)){
                conectar();
                //Eliminar la base de datos
                mongoClient.dropDatabase(DB);
                System.out.println("Base de datos eliminada: " + DB);
                
            } else {
                System.out.println("Base de datos no existía: " + DB);
            }
        } catch (MongoException e){
            e.printStackTrace();
        }
    }
    
    
    /**
     * Para cerrar la conexión con nuestra
     * Base de Datos MongoDB. Llamaremos a 
     * este método al cerrar la aplicación
     */
    public void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión cerrada correctamente.");
        }
    }
    
    
    /**
     * 
     * @param documento 
     * 
     * Con este método, se insertará
     * el documento pasado por parámetro
     * en la colección de la base de datos
     */
    public void insertarDatos(Document documento){
        try{
            collection = database.getCollection(COLLECTION);
            collection.insertOne(documento);
            System.out.println("Documento insertado correctamente");
        } catch (MongoException e){
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     * @return
     * 
     * Este método nos devolverá
     * una lista con todos los
     * registros realizados cuando
     * el pico de aforo ha llegado
     * al máximo (25)
     */
    public ArrayList<Registro> picosMax(){
        collection = database.getCollection(COLLECTION);
        registros = new ArrayList<>();
        int cont = 1;
        Document query = new Document("Máximo aforo", true);
        
        FindIterable<Document> result = collection.find(query);
        
        try (MongoCursor<Document> cursor = result.iterator()){
            while(cursor.hasNext()){
                Document document = cursor.next();
                Registro registro = new Registro();
                //System.out.println(document.toJson());
                Document documento = Document.parse(document.toJson());
                
                registro.setId(cont);
                registro.setFecha(document.getString("Fecha"));
                registro.setHora(document.getString("Hora"));
                
                registros.add(registro);
                cont++;
            }
        }
        return registros;
    }
    
    
    /**
     * 
     * @return
     * 
     * Este método nos devolverá una 
     * lista con todos los registros que
     * se han realizado cuando el aforo
     * era igual a 0
     */
    public ArrayList<Registro> picosMin(){
        collection = database.getCollection(COLLECTION);
        registros = new ArrayList<>();
        int cont = 1;
        
        Document query = new Document("Mínimo aforo", true);
        
        FindIterable<Document> regs = collection.find(query);
        
        try (MongoCursor<Document> cursor = regs.iterator()){
            while(cursor.hasNext()){
                Document document = cursor.next();
                Registro registro = new Registro();
                Document documento = Document.parse(document.toJson());
                registro.setId(cont);
                registro.setFecha(documento.getString("Fecha"));
                registro.setHora(documento.getString("Hora"));
                
                registros.add(registro);
                cont++;
            }
        }
        return registros;
    }
    
    
    /**
     * 
     * @param nombre
     * @param valor 
     * 
     * Este método se deberá utilizar en la clase Registrar
     * y solo será necesario si queremos eliminar los registros
     * de la colección de PICOS DE AFORO
     */
    public void eliminarPicos(String nombre, boolean valor){
        collection = database.getCollection(COLLECTION);
        Document filtro = new Document(nombre, valor);
        collection.deleteMany(filtro);
    }
}
