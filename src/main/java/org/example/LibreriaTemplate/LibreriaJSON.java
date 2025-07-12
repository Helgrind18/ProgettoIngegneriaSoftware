package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libreria;
import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * Implementazione della libreria che utilizza file JSON per persistenza.
 * Sfrutta il pattern Template Method definito in LibreriaTemplate.
 */
public class LibreriaJSON extends LibreriaTemplate {
    private JSONArray libriArray; // Array JSON che conterrà tutti i libri letti dal file
    private Iterator<JSONObject> iterator;  // Iteratore per scorrere ciascun oggetto JSON nel JSONArray
    JSONParser parser = new JSONParser(); // legge il testo in formato json

    public LibreriaJSON(File fileLibreria, Libreria libreria) {
        super(fileLibreria, libreria);
    }

    @Override
    protected void apriFile() {
        // leggo il contenuto del file
        try (FileReader reader = new FileReader(fileLibreria)) {
            // Parsing di tutto il file in un oggetto Java
            Object obj = parser.parse(reader);
            // Casting dell'oggetto in JSONArray
            libriArray = (JSONArray) obj;
            iterator = libriArray.iterator();
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Errore nell'apertura del file JSON", e);
        }
    }

    @Override
    protected String leggiLinea() {
        // Controllo che l'iteratore sia inizializzato e che ci siano elementi
        if (iterator != null && iterator.hasNext()) {
            JSONObject libroJson = iterator.next();
            return libroJson.toJSONString();
        } else {
            return null; // EOF
        }
    }

    /**
     * Converte una stringa JSON in un oggetto Libro.
     */
    @Override
    protected Libro ottieniLibro(String linea) {
        try {
            // Parser per la singola riga JSON
            JSONObject json = (JSONObject) parser.parse(linea);
            // Estraggo i campi dal JSONObject
            long isbn = Long.parseLong((String) json.get("isbn"));
            String titolo = (String) json.get("titolo");
            String autore = (String) json.get("autore");
            String genere = (String) json.get("genere");
            int valutazione = ((Long) json.get("valutazione")).intValue();
            StatoLettura stato = StatoLettura.valueOf((String) json.get("stato"));
            // Creo e restituisco il nuovo oggetto Libro
            return new Libro(isbn, titolo, autore, genere, valutazione, stato);
        } catch (ParseException e) {
            throw new RuntimeException("Errore nel parsing della riga JSON", e);
        }
    }

    /**
     * Chiusura del file azzeriamo le variabili per favorire la garbage collection.
     */
    @Override
    protected void chiudiFile() {
        libriArray = null;
        iterator = null;
    }

    /**
     * Scrive l'intera lista di libri in formato JSON sul file, sovrascrivendo quello esistente.
     */
    @Override
    public void scriviSuFile(Libro nuovo) {
        // Creo un nuovo JSONArray per l'output
        JSONArray outputArray = new JSONArray();
        for (Libro libro : super.getBiblitoeca()) {
            // Creo un JSONObject per ciascun libro
            JSONObject json = new JSONObject();
            json.put("isbn", Long.toString(libro.getISBN()));
            json.put("titolo", libro.getTitolo());
            json.put("autore", libro.getAutore());
            json.put("genere", libro.getGenere());
            json.put("valutazione", libro.getValutazione());
            json.put("stato", libro.getStatoLettura().name());
            // Aggiunta dell'oggetto JSON all'array di output
            outputArray.add(json);
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria, false))) {
            pw.write(outputArray.toJSONString());
            pw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Errore in scrittura JSON", e);
        }
    }

    /**
     * Sovrascrive il file utilizzando scriviSuFile.
     */
    @Override
    public void sovrascriviFile() {
        scriviSuFile(null);
    }
}
