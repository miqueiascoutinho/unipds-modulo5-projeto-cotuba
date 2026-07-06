package br.com.unipds.repository.impl;

import br.com.unipds.FormatoEbookQualifier;
import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.FormatoEbook;
import br.com.unipds.domain.Livro;
import br.com.unipds.repository.GeradorEbook;
import jakarta.enterprise.context.ApplicationScoped;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@FormatoEbookQualifier(FormatoEbook.EPUB)
@ApplicationScoped
public class GeradorEbookEpub implements GeradorEbook {
    public void gerarEbook(Livro livro) {
        List<Capitulo> capitulos1 = livro.capitulos();
        try {
            var epub = new Book();

            //TODO: definir título e autor para o livro
            epub.getMetadata().addTitle(livro.titulo());
            epub.getMetadata().addAuthor(new Author(livro.autor()));

            boolean[] ehPrimeiroCapitulo = {true};

            capitulos1.forEach(capitulo -> {
                var html = capitulo.conteudoHTML();

                var chapter = new Resource(html.getBytes(), MediatypeService.XHTML);
                epub.addSection(capitulo.titulo(), chapter);

                if (ehPrimeiroCapitulo[0]) {
                    epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                    ehPrimeiroCapitulo[0] = false;
                }

                var epubWriter = new EpubWriter();

                try {
                    epubWriter.write(epub, Files.newOutputStream(livro.arquivoSaida()));
                } catch (IOException ex) {
                    throw new IllegalStateException("Erro ao criar arquivo EPUB: " + livro.arquivoSaida().toAbsolutePath(), ex);
                }
            });

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + livro.arquivoSaida().toAbsolutePath(), ex);
        }
    }
}
