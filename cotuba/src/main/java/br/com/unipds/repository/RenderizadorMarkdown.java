package br.com.unipds.repository;

import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.Markdown;

import java.util.List;

public interface RenderizadorMarkdown {
    List<Capitulo> executar(List<Markdown> markdowns);
}
