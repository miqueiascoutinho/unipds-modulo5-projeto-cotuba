package br.com.unipds.dto;

import br.com.unipds.domain.FormatoEbook;

import java.nio.file.Path;

public record ParametrosProcessamento(
        Path diretorioMarkd,
        FormatoEbook formato,
        Path arquivoDeSaida,
        Boolean modoVerboso) {
}