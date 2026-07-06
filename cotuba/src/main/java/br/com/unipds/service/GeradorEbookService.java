package br.com.unipds.service;

import br.com.unipds.FormatoEbookFilter;
import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.Livro;
import br.com.unipds.domain.Markdown;
import br.com.unipds.dto.ParametrosProcessamento;
import br.com.unipds.repository.GeradorEbook;
import br.com.unipds.repository.GestaoArquivos;
import br.com.unipds.repository.RenderizadorMarkdown;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GeradorEbookService {
    private final Instance<GeradorEbook> geradorEbooks;
    private final RenderizadorMarkdown renderizadorMarkdown;
    private final GestaoArquivos gestorArquivosRepository;

    @Inject
    public GeradorEbookService(@Any Instance<GeradorEbook> geradorEbooks, RenderizadorMarkdown renderizadorMarkdown, GestaoArquivos gestorArquivosRepository) {
        this.geradorEbooks = geradorEbooks;
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.gestorArquivosRepository = gestorArquivosRepository;
    }


    public void gerarEbook(ParametrosProcessamento parametros) {
        try {
            List<Markdown> markdowns = gestorArquivosRepository.obterArquivosMarkdown(parametros.diretorioMarkd());

            List<Capitulo> capitulos = renderizadorMarkdown.executar(markdowns);


            //TODO: Implementar
            var autor = "Autor Teste";
            var titulo = "Livro Qualquer";

            var livro = new Livro(titulo, autor, capitulos, parametros.arquivoDeSaida(), parametros.formato());


            GeradorEbook geradorEbook = geradorEbooks.select(FormatoEbookFilter.of(livro.formatoEbook())).get();
            geradorEbook.gerarEbook(livro);
        } catch (Exception ex) {
            System.out.println("Erro ao gerar Ebook: " + ex.getMessage());
            throw new IllegalStateException("Erro ao gerar Ebook: " + ex.getMessage());
        }
    }
}
