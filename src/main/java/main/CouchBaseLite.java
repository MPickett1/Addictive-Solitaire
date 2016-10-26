package main;

import com.couchbase.lite.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CouchBaseLite {

    private Manager mgr;
    private List<Database> db = new ArrayList<>();
    private Context context = new JavaContext();
    private static CouchBaseLite _instance = new CouchBaseLite();

    public Database getDatabase(String databaseName) {
        if (mgr == null) {
            try {
                mgr = new Manager(context, Manager.DEFAULT_OPTIONS);
            } catch (IOException e) {
                System.out.println("Couch Error : " + e.getMessage());
                return null;
            }
        }

        Iterator iterator = db.iterator();
        while(iterator.hasNext()){
            Database d = (Database) iterator.next();
            if (d.getName().equals(databaseName)) {
                return d;
            }
        }
        // create a new database
        try {
            Database d = mgr.getDatabase(databaseName);
            dbListener(d);
            db.add(d);
            return d;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CouchBaseLite getInstance(){
        return _instance;
    }

    public Manager getManager(){
        return mgr;
    }

    public void closeAllDatabase(){
        Iterator iterator = db.iterator();
        while(iterator.hasNext()){
            Database d = (Database) iterator.next();
            d.close();
        }
    }

    protected void dbListener(Database d){
        if(d != null) {
            d.addChangeListener((Database.ChangeEvent event) -> {
                for(DocumentChange change: event.getChanges()){
                    System.out.println("DB CHANGED : NAME = " + change.getAddedRevision().getProperties().get("name"));
                }
            });
        }
    }

    protected String entityToJson (SaveState entity) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public SaveState loadGame(String name, Database db) throws CouchbaseLiteException {
        Map<String, Object> properties = new HashMap<>();

        QueryEnumerator q = db.createAllDocumentsQuery().run();

        for(QueryRow res : q){
            try{
                if(res.getDocument().getProperties().get("name").equals(name)){
                    properties.putAll(res.getDocument().getProperties());

                    // Jackson Unmarshalling JSON with Unknown Properties - http://www.baeldung.com/jackson-deserialize-json-unknown-properties
                    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    mapper.enable(SerializationFeature.INDENT_OUTPUT);
                    String json = mapper.writeValueAsString(properties);

                    //  Convert Couch JSON to Flight object
                    SaveState load = mapper.readValue(json, SaveState.class);
                    return load;
                }
            }catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    public void update(Database d, SaveState e){
        // Update station information
        try {
        Document doc = d.getDocument(e.getId());

        if(doc == null){
            doc = d.createDocument();
            e.setId(doc.getId());
        }

        String json = entityToJson(e);
        ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, Object> properties = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});

        doc.update((UnsavedRevision ur) -> {
            System.out.println("UPDATE");
            ur.setProperties(properties);
            return true;
        });
        } catch (CouchbaseLiteException ex) {
            Logger.getLogger(CouchBaseLite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    protected void onDestroy() {
        if(mgr != null) {
            mgr .close();
        }
    }
}
