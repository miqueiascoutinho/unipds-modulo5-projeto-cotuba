package br.com.unipds.repository;

import br.com.unipds.domain.Capitulo;

import java.nio.file.Path;
import java.util.List;

public interface RenderizadorMarkdown {
    List<Capitulo> executar(List<Path> arquivosMarkdown);
}
