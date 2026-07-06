package br.com.unipds.domain;

import java.nio.file.Path;
import java.util.List;

public record Livro(String titulo, String autor, List<Capitulo> capitulos, Path arquivoSaida, FormatoEbook formatoEbook) {
}
