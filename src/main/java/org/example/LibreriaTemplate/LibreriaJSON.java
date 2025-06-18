package org.example.LibreriaTemplate;

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
import java.util.HashSet;
import java.util.Set;

public class LibreriaJSON extends LibreriaTemplate {

    public LibreriaJSON(File fileLibreria) {
        super(fileLibreria);
    }

    @Override
    public void leggiFile() {
        // Creo il parser JSON‑Simple
        JSONParser parser = new JSONParser();

        // Apro il FileReader in try-with-resources per chiusura automatica
        try (FileReader fr = new FileReader(fileLibreria)) {
            // Parsiamo l’intero contenuto del file come JSON
            Object obj = parser.parse(fr);
            // Castiamo a JSONArray (ci aspettiamo un array di oggetti libro)
            JSONArray array = (JSONArray) obj;

            // Scorriamo ogni elemento dell’array
            for (Object o : array) {
                // Ogni elemento è un JSONObject
                JSONObject json = (JSONObject) o;

                // Estraiamo i campi dal JSONObject
                long isbn       = Long.parseLong((String) json.get("isbn"));
                String titolo   = (String)      json.get("titolo");
                String autore   = (String)      json.get("autore");
                String genere   = (String)      json.get("genere");
                // JSON‑Simple rappresenta numeri interi come Long
                int valutazione = ((Long)       json.get("valutazione")).intValue();
                StatoLettura stato = StatoLettura.valueOf((String) json.get("stato"));

                // Costruiamo l’istanza Libro
                Libro libro = new Libro(isbn, titolo, autore, genere, valutazione, stato);

                // Aggiungiamo alla lista interna filtrando i duplicati
                if (aggiungiLibro(libro)) {
                    System.out.println("Aggiunto: " + libro);
                } else {
                    System.out.println("Duplicato saltato: " + libro);
                }
            }

        } catch (IOException | ParseException e) {
            // In caso di errore di I/O o parsing, rilanciamo come unchecked
            throw new RuntimeException("Errore in lettura JSON", e);
        }
    }

    @Override
    public void scriviSuFile() {
        // Costruiamo un JSONArray dei libri filtrati
        JSONArray outputArray = new JSONArray();
        for (Libro libro : super.getBiblitoeca()) {

            // Creiamo un JSONObject per questo libro
            long isbn = libro.getISBN();
            JSONObject json = new JSONObject();
            json.put("isbn",       Long.toString(isbn));
            json.put("titolo",     libro.getTitolo());
            json.put("autore",     libro.getAutore());
            json.put("genere",     libro.getGenere());
            json.put("valutazione", libro.getValutazione());
            json.put("stato",      libro.getStatoLettura().name());

            // Aggiungiamo l’oggetto all’array
            outputArray.add(json);
        }

        // Scriviamo l’intero array su file in un colpo solo
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria, false))) {
            // toJSONString() restituisce la rappresentazione compatta in JSON
            pw.write(outputArray.toJSONString());
            pw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Errore in scrittura JSON", e);
        }
    }
}
