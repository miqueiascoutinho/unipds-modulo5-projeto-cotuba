package br.com.unipds.repository;

import br.com.unipds.domain.Markdown;

import java.nio.file.Path;
import java.util.List;

public interface GestaoArquivos {
    List<Markdown> obterArquivosMarkdown(Path diretorioMD);
}
