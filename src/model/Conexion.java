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
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author rafacampa9
 */
public class Conexion {
    
    private  final String HOST = "localhost";
    private  final int PORT = 27017;
    private  final String DB = "RAGNAROCK";
    private  final String COLLECTION = "AFORO";
    
    private MongoCollection<Document> collection;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private ArrayList<Registro> registros;
    
    /**
     * Constructor vacío
     */
    public Conexion(){
        conectar();
        //crearDB();
        crearColeccion();
        
        
        //borrarColeccion();
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
     * queremos eliminar la DB
     */
    public void borrarDB(){
        try{
            if (mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(DB)){

                //Eliminar la base de datos
                mongoClient.dropDatabase(DB);
                System.out.println("Base de datos eliminada: " + DB);
                
            } else {
                System.out.println("Base de datos no existe: " + DB);
            }
        } catch (MongoException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Por si queremos eliminar
     * alguna colección
     */
    public void borrarColeccion(){
        
        try{
            if (mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(DB)){
                
                //Obtenemos la DB 
                database =mongoClient.getDatabase(DB);
                
                //Obtenemos la colección y la eliminamos
                collection = database.getCollection(COLLECTION);
                
                // Verificamos si la colección existe antes de intentar eliminarla
                if(database.listCollectionNames().into(new ArrayList<>()).contains(COLLECTION)){
                    collection.drop();
                    System.out.println("Colección eliminada: " + collection);
                } else{
                    System.out.println("Esta colección no existe");
                }
            } else {
                System.out.println("Esta base de datos no existe: " + DB);
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
        if (isConnected()) {
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
     * Para saber si estoy conectado
     * a la BBDD
     */
    public boolean isConnected(){
        if (mongoClient != null){
            try{
                mongoClient.listDatabaseNames();
                return true;
            }catch (MongoException e){
                return false;
            }   
        }
        return false;
    }
    
    
    /**
     * 
     * @return
     * 
     * Este método nos devolverá
     * una lista con todos los
     * documentos realizados cuando
     * el pico de aforo ha llegado
     * al máximo (25)
     */
    public ArrayList<Registro> picosMax(){
        collection = database.getCollection(COLLECTION);
        registros = new ArrayList<>();
        //int cont = 1;
        Document query = new Document("Aforo", 25);
        
        FindIterable<Document> result = collection.find(query).sort(Sorts.descending("Fecha", "Hora"));
        
        try (MongoCursor<Document> cursor = result.iterator()){
            while(cursor.hasNext()){
                Document document = cursor.next();
                Registro registro = new Registro();
                //System.out.println(document.toJson());
                //Document documento = Document.parse(document.toJson());
                
                registro.setId(document.getObjectId("_id").toString());
                registro.setFecha(document.getString("Fecha"));
                registro.setHora(document.getString("Hora"));
                registro.setAforo(document.getInteger("Aforo"));
                registro.setMovimiento(document.getString("Movimiento"));
                
                
                registros.add(registro);
                //cont++;
            }
        }
        return registros;
    }
    
    
    /**
     * 
     * @return
     * 
     * Este método nos devolverá una 
     * lista con todos los documentos que
     * se han realizado cuando el aforo
     * era igual a 0
     */
    public ArrayList<Registro> picosMin(){
        collection = database.getCollection(COLLECTION);
        registros = new ArrayList<>();
        //int cont = 1;
        
        Document query = new Document("Aforo", 0);
        
        FindIterable<Document> docs = collection.find(query).
                sort(Sorts.descending("Fecha","Hora"));
        
        try (MongoCursor<Document> cursor = docs.iterator()){
            while(cursor.hasNext()){
                Document document = cursor.next();
                Registro registro = new Registro();
                //Document documento = Document.parse(document.toJson());
                registro.setId(document.getObjectId("_id").toString());
                registro.setFecha(document.getString("Fecha"));
                registro.setHora(document.getString("Hora"));
                registro.setAforo(document.getInteger("Aforo"));
                registro.setMovimiento(document.getString("Movimiento"));
                
                registros.add(registro);
                //cont++;
            }
        }
        return registros;
    }
    
    /**
     * 
     * @return
     * 
     * Registrará todas las entradas y salidas
     */
    public ArrayList<Registro> entryExit(){
        collection = database.getCollection(COLLECTION);
        registros = new ArrayList<>();
        
        
        Document query = new Document ("Movimiento", new Document("$exists", true));
        
        FindIterable<Document> docs = collection.find(query)
                                    .sort(Sorts.descending("Fecha",
                                                                "Hora"));
        
        try(MongoCursor<Document> cursor = docs.iterator()){
            while (cursor.hasNext()){
                Document document = cursor.next();
                Registro registro = new Registro();
                //Document documento = Document.parse(document.toJson());
                registro.setId(document.getObjectId("_id").toString());
                registro.setFecha(document.getString("Fecha"));
                registro.setHora(document.getString("Hora"));
                registro.setAforo(document.getInteger("Aforo"));
                registro.setMovimiento(document.getString("Movimiento"));
                
                registros.add(registro);
                
            }
        }
        return registros;
    }
    
    public ArrayList<Registro> block(){
        collection = database.getCollection(COLLECTION);
        registros = new ArrayList<>();
        int cont = 1;
        
        Bson filter = Filters.or(Filters.exists("Estado Entrada 2", true),
            Filters.exists("Estado Salida 2", true)
        );
        
        FindIterable<Document> docs = collection.find(filter).sort(Sorts.descending("Fecha","Hora"));
        
        try(MongoCursor<Document> cursor = docs.iterator()){
            while (cursor.hasNext()){
                Document document = cursor.next();
                Registro registro = new Registro();
                //Document documento = Document.parse(document.toJson());
                registro.setId(document.getObjectId("_id").toString());
                registro.setFecha(document.getString("Fecha"));
                registro.setHora(document.getString("Hora"));
                registro.setAforo(document.getInteger("Aforo"));
                
                if (document.containsKey("Estado Entrada 2")) {
                    registro.setEstado(document.getString("Estado Entrada 2"));
                } else if (document.containsKey("Estado Salida 2")) {
                    registro.setEstado(document.getString("Estado Salida 2"));
                } 
                
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
     * de la colección de AFORO
     */
    public void eliminarPicos(String nombre, boolean valor){
        collection = database.getCollection(COLLECTION);
        Document filtro = new Document(nombre, valor);
        collection.deleteMany(filtro);
    }
}
