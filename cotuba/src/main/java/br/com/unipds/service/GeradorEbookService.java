package br.com.unipds.service;

import br.com.unipds.dto.ParametrosProcessamento;
import br.com.unipds.repository.RenderizadorMarkdown;
import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.Livro;
import br.com.unipds.repository.GeradorEpubRepository;
import br.com.unipds.repository.GeradorPdfRepository;

import java.io.IOException;
import java.util.List;

import static br.com.unipds.domain.FormatoEbook.EPUB;
import static br.com.unipds.domain.FormatoEbook.PDF;

public class GeradorEbookService {
    public void gerarEbook(ParametrosProcessamento parametros) {

        RenderizadorMarkdown renderizadorMarkdown = new RenderizadorMarkdown();
        List<Capitulo> capitulos = renderizadorMarkdown.executar(parametros.getDiretorioMarkd());

        Livro livro = new Livro();
        livro.setCapitulos(capitulos);

        //TODO: Implementar
        livro.setAutor("Autor Teste");
        livro.setNome("Livro Qualquer");
        livro.setArquivoSaida(parametros.getArquivoDeSaida());


        try {
            if (PDF.equals(parametros.getFormato())) {
                GeradorPdfRepository geradorPdf = new GeradorPdfRepository();
                geradorPdf.executar(livro);
            } else if (EPUB.equals(parametros.getFormato())) {
                GeradorEpubRepository geradorEpub = new GeradorEpubRepository();
                geradorEpub.execute(livro);

            }
        } catch (IOException ex) {
            System.out.println("Erro ao gerar Ebook: " + ex.getMessage());
            throw new IllegalStateException("Erro ao gerar Ebook: " + ex.getMessage());
        }
    }
}
