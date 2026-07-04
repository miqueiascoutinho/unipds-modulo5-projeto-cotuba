package br.com.unipds.service;

import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.Livro;
import br.com.unipds.dto.ParametrosProcessamento;
import br.com.unipds.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.nio.file.Path;
import java.util.List;

import static br.com.unipds.domain.FormatoEbook.EPUB    ;
import static br.com.unipds.domain.FormatoEbook.PDF;

@ApplicationScoped
public class GeradorEbookService {
    private final GeradorEbook geradorPdf;
    private final GeradorEbook geradorEpub;
    private final RenderizadorMarkdown renderizadorMarkdown;
    private final GestaoArquivos gestorArquivosRepository;

    @Inject
    public GeradorEbookService(@Named("geradorPdf") GeradorEbook geradorPdf, @Named("geradorEpub") GeradorEbook geradorEpub, RenderizadorMarkdown renderizadorMarkdown, GestaoArquivos gestorArquivosRepository) {
        this.geradorPdf = geradorPdf;
        this.geradorEpub = geradorEpub;
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.gestorArquivosRepository = gestorArquivosRepository;
    }


    public void gerarEbook(ParametrosProcessamento parametros) {

        List<Path> paths = gestorArquivosRepository.obterArquivosMarkdown(parametros.getDiretorioMarkd());

        List<Capitulo> capitulos = renderizadorMarkdown.executar(paths);

        Livro livro = new Livro();
        livro.setCapitulos(capitulos);

        //TODO: Implementar
        livro.setAutor("Autor Teste");
        livro.setNome("Livro Qualquer");
        livro.setArquivoSaida(parametros.getArquivoDeSaida());
        livro.setFormatoEbook(parametros.getFormato());

        try {
            if (PDF.equals(livro.getFormatoEbook())) {
                geradorPdf.gerarEbook(livro);
            } else if (EPUB.equals(livro.getFormatoEbook())) {
                geradorEpub.gerarEbook(livro);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao gerar Ebook: " + ex.getMessage());
            throw new IllegalStateException("Erro ao gerar Ebook: " + ex.getMessage());
        }
    }
}
