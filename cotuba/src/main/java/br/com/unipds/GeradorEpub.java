package br.com.unipds;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GeradorEpub {
    public void execute(List<String> htmls, Path arquivoSaida) throws IOException {
        try {
            var epub = new Book();

            //TODO: definir título e autor para o livro
            epub.getMetadata().addTitle("Livro");
            epub.getMetadata().addAuthor(new Author("Autor"));

            boolean[] ehPrimeiroCapitulo = {true};

            htmls.forEach(html -> {
                var chapter = new Resource(html.getBytes(), MediatypeService.XHTML);
                epub.addSection("Capítulo", chapter);

                if (ehPrimeiroCapitulo[0]) {
                    epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                    ehPrimeiroCapitulo[0] = false;
                }

                var epubWriter = new EpubWriter();

                try {
                    epubWriter.write(epub, Files.newOutputStream(arquivoSaida));
                } catch (IOException ex) {
                    throw new IllegalStateException("Erro ao criar arquivo EPUB: " + arquivoSaida.toAbsolutePath(), ex);
                }
            });

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + arquivoSaida.toAbsolutePath(), ex);
        }
    }
}
