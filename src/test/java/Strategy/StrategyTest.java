package Strategy;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Strategy.OrdinaContext;
import org.example.Strategy.OrdinaTitolo;
import org.example.Strategy.OrdinaValutazione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategyTestTitolo {
    private List<Libro> biblioteca;
    private OrdinaContext ctx;

    @BeforeEach
    public void setup() {
        biblioteca = new ArrayList<>();
        biblioteca.add(new Libro(1L, "Libro A", "Autore A", "Genere A", 3, StatoLettura.LETTO));
        biblioteca.add(new Libro(2L, "Libro B", "Autore B", "Genere B", 5, StatoLettura.DA_LEGGERE));
        biblioteca.add(new Libro(3L, "Libro C", "Autore C", "Genere C", 1, StatoLettura.IN_LETTURA));
        biblioteca.add(new Libro(4L, "Libro D", "Autore D", "Genere D", 5, StatoLettura.LETTO));
        ctx = new OrdinaContext(biblioteca);
        ctx.setStrategy(new OrdinaTitolo());
    }

    @Test
    @DisplayName("OrdinamentoValutazione")
    public void testOrdinamentoValutazione() {
        List<Libro> ordinata = ctx.ordina();
        assertEquals(3, ordinata.get(0).getValutazione());
        assertEquals(5, ordinata.get(1).getValutazione());
        assertEquals(1, ordinata.get(2).getValutazione());
        assertEquals(1, ordinata.get(3).getValutazione());
    }
}
