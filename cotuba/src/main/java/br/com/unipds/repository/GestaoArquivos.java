package br.com.unipds.repository;

import java.nio.file.Path;
import java.util.List;

public interface GestaoArquivos {
    List<Path> obterArquivosMarkdown(Path diretorioMD);
}
