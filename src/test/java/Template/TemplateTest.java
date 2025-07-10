package Template;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.LibreriaLista;
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

import static org.junit.jupiter.api.Assertions.*; // Usiamo JUnit 5 invece di JUnit 4

public class TemplateTest {

    private List<Libro> biblitoeca;
    private LibreriaTemplate libreriaTemplate;
    private File fileTemporaneo;
    private LibreriaLista libreriaLista;
    @BeforeEach
    public void setup() throws IOException {
        // Creo copia del file CSV originale per i test
        Path originalPath = Path.of(Costanti.percorsoFileCSVTest);
        Path tempPath = Files.createTempFile("testFile_", ".csv");
        Files.copy(originalPath, tempPath, StandardCopyOption.REPLACE_EXISTING);
        fileTemporaneo = tempPath.toFile();
        libreriaLista = new LibreriaLista();
        libreriaTemplate = new LibreriaCSV(fileTemporaneo, libreriaLista);
        libreriaTemplate.esegui();
    }

    @Test
    @DisplayName("Test lettura file csv")
    public void leggiCSV() {
        libreriaTemplate.esegui();
        this.biblitoeca = libreriaTemplate.getBiblitoeca();

        assertEquals(2, this.biblitoeca.size());
        assertEquals("Libro A", this.biblitoeca.get(0).getTitolo());
        assertEquals(StatoLettura.LETTO, this.biblitoeca.get(1).getStatoLettura());
    }

    @Test
    @DisplayName("Test lettura file json")
    public void leggiJSON() {
        libreriaTemplate = new LibreriaJSON(new File(Costanti.percorsoFileJSONTest), new LibreriaLista());
        libreriaTemplate.esegui();
        this.biblitoeca = libreriaTemplate.getBiblitoeca();

        assertEquals(2, this.biblitoeca.size());
        assertEquals("I fratelli Karamazov", this.biblitoeca.get(0).getTitolo());
        assertEquals(StatoLettura.LETTO, this.biblitoeca.get(1).getStatoLettura());
    }

    @Test
    @DisplayName("Test aggiunta libro")
    public void aggiungiLibro() {
        Libro l = new Libro(3L, "Libro A", "Autore A", "Genere A", 3, StatoLettura.LETTO);
        assertTrue(libreriaLista.aggiungiLibro(l));
        libreriaTemplate.scriviSuFile(l);
        assertFalse(libreriaLista.aggiungiLibro(l)); // Libro già presente
    }

    @Test
    @DisplayName("Test modifica Libro")
    public void modificaLibro() {
        Libro vecchio = libreriaLista.getLibro(1);
        Libro nuovo = new Libro(vecchio.getISBN(), vecchio.getTitolo(), "nuovo autore test", "nuovo genere test", vecchio.getValutazione(), vecchio.getStatoLettura());
        assertTrue(libreriaLista.modificaLibro(vecchio, nuovo));
    }

    @Test
    @DisplayName("Test rimuovi Libro")
    public void rimuoviLibro() {
        Libro vecchio = libreriaLista.getLibro(2);
        assertTrue(libreriaLista.rimuoviLibro(vecchio));
    }

    @AfterEach
    public void cleanup() {
        if (fileTemporaneo != null && fileTemporaneo.exists()) {
            fileTemporaneo.delete();
        }
    }
}
