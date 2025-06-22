package org.example.Strategy;

import org.example.Biblioteca.Libro;

import java.util.ArrayList;
import java.util.List;

public class OrdinaValutazione implements OrdinaStrategy {

    @Override
    public List<Libro> ordina(List<Libro> biblioteca) {
        List<Libro> copia = new ArrayList<>(biblioteca);
        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = 0; j < copia.size() - 1 - i; j++) {
                if (copia.get(j).getValutazione() > copia.get(j + 1).getValutazione()) {
                    Libro temp = copia.get(j);
                    copia.set(j, copia.get(j + 1));
                    copia.set(j + 1, temp);
                }
            }
        }
        return copia;
    }
}
