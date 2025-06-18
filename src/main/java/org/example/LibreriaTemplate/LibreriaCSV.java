package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libro;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;

public class LibreriaCSV extends LibreriaTemplate {
    public LibreriaCSV(File fileLibreria) {
        super(fileLibreria);
    }

    @Override
    void leggiFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLibreria));
            while (true){
                String linea = br.readLine();
                if (linea == null){
                    System.out.println("EOF!");
                    br.close();
                    return;
                }
                System.out.println("Linea letta = "+linea);
                Libro l = ottieniLibro(linea,",");
                if (super.aggiungiLibro(l)){
                    System.out.println("Libro "+l +" aggiunto correttametne");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scriviSuFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria),true)) {
            for (Libro libro : super.getBiblitoeca()){
                pw.println(libro.getISBN()+","+libro.getTitolo()+","+libro.getAutore()+","+libro.getGenere()+","+libro.getValutazione()+","+libro.getStatoLettura());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
