package Template;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Costanti;
import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.LibreriaTemplate.LibreriaJSON;
import org.example.LibreriaTemplate.LibreriaTemplate;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.Assert.*;


/*
* Questo test va a verificare se l'implementazione del template sia corretta.
*
* */
@Disabled
public class TemplateTest {

    private List<Libro> biblitoeca;
    private LibreriaTemplate libreriaTemplate;
    private File fileTemporaneo;

    @BeforeEach
    public void setup() throws IOException {
        //Al fine di non toccare il file originale, vado a creare una copia di esso. è solo per test, alla fine verrà rimosso
        Path originalPath = Path.of(Costanti.percorsoFileCSVTest);
        Path tempPath = Files.createTempFile("testFile_", ".csv");
        Files.copy(originalPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
        fileTemporaneo = tempPath.toFile();

        libreriaTemplate = new LibreriaCSV(fileTemporaneo);
        libreriaTemplate.esegui();
    }

    @Test
    @DisplayName("Test lettura file csv")
    public void leggiCSV(){
        libreriaTemplate = new LibreriaCSV(new File(Costanti.percorsoFileCSVTest));
        libreriaTemplate.esegui(); //Viene eseguito l'algoritmo
        this.biblitoeca = libreriaTemplate.getBiblitoeca();
        assertEquals(2,this.biblitoeca.size());
        assertEquals("Libro A", this.biblitoeca.get(0).getTitolo());
        assertEquals(StatoLettura.LETTO, this.biblitoeca.get(1).getStatoLettura());

    }

    @Test
    @DisplayName("Test lettura file json")
    public void leggiJSON(){
        libreriaTemplate = new LibreriaJSON(new File(Costanti.percorsoFileJSONTest));
        libreriaTemplate.esegui(); //Viene eseguito l'algoritmo
        this.biblitoeca = libreriaTemplate.getBiblitoeca();
        assertEquals(2,this.biblitoeca.size());
        assertEquals("I fratelli Karamazov", this.biblitoeca.get(0).getTitolo());
        assertEquals(StatoLettura.LETTO, this.biblitoeca.get(1).getStatoLettura());
    }

    @Test
    @DisplayName("Test aggiunta libro")
    public void aggiungiLibro(){
        Libro l = new Libro(3L, "Libro A", "Autore A", "Genere A", 3, StatoLettura.LETTO);
        assertTrue(libreriaTemplate.aggiungiLibro(l));
        libreriaTemplate.scriviSuFile(l);
        assertFalse(libreriaTemplate.aggiungiLibro(l)); //Deve essere falso perchè il libro è già presente
    }

    @Test
    @DisplayName("Test modifica Libro")
    public void modificaLibro(){
        Libro vecchio = libreriaTemplate.getLibro(1);
        Libro nuovo = new Libro(vecchio.getISBN(),vecchio.getTitolo(),vecchio.getAutore(),vecchio.getGenere(),vecchio.getValutazione(),vecchio.getStatoLettura());
        nuovo.setGenere("nuovo genere test");
        nuovo.setAutore("nuovo autore test");
        assertTrue(libreriaTemplate.modificaLibro(vecchio,nuovo));
    }

    @Test
    @DisplayName("Test rimuovi Libro")
    public void rimuoviLibro(){
        Libro vecchio = libreriaTemplate.getLibro(2);
        assertTrue(libreriaTemplate.rimuoviLibro(vecchio));
    }


    @AfterEach
    public void cleanup() {
        //Vado ad eliminare il file di test temporaneo
        if (fileTemporaneo != null && fileTemporaneo.exists()) {
            fileTemporaneo.delete();
        }
    }

}
