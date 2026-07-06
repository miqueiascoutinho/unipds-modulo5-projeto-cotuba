package br.com.unipds.domain;

import java.nio.file.Path;

public record Markdown(String conteudo, Path arquivo) {
}
