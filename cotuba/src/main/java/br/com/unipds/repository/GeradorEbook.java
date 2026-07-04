package br.com.unipds.repository;

import br.com.unipds.domain.Livro;

public interface GeradorEbook {
    void gerarEbook(Livro livro);
}
