package br.com.unipds.domain;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Capitulo(String titulo, String conteudoHTML, Markdown markdown) {
}
