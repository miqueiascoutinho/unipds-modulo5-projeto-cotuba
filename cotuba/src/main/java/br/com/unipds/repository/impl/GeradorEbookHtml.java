package br.com.unipds.repository.impl;

import br.com.unipds.FormatoEbookQualifier;
import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.FormatoEbook;
import br.com.unipds.domain.Livro;
import br.com.unipds.repository.GeradorEbook;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@FormatoEbookQualifier(FormatoEbook.HTML)
@ApplicationScoped
public class GeradorEbookHtml implements GeradorEbook {

    @Override
    public void gerarEbook(Livro livro) {
        Path arquivoSaida = livro.getArquivoSaida();
        Map<Capitulo, Path> arquivosHTMLs = new LinkedHashMap<>();

        try {
            Path diretorioHtml = Files.createDirectory(arquivoSaida);

            int i = 1;
            for (Capitulo capitulo : livro.getCapitulos()) {
                String nomeCapitulo = obterNomeCapituloHTML(i, capitulo);
                Path arquivohtml = diretorioHtml.resolve(nomeCapitulo);

                escreverArquivoHTML(capitulo, arquivohtml);
                arquivosHTMLs.put(capitulo, arquivohtml);

                i++;
            }

            criarSumario(arquivosHTMLs, diretorioHtml);
        } catch (IOException ex) {
            throw new IllegalStateException("Erro ao criar Ebook HTML " + arquivoSaida, ex);
        }

    }

    private void criarSumario(Map<Capitulo, Path> arquivosHTMLs, Path diretorioHtml) throws IOException {
        Path indexHtml = diretorioHtml.resolve("index.html");

        String sumarioHtml = arquivosHTMLs.entrySet().stream().map(entry -> {
                    var capitulo = entry.getKey();
                    var arquivo = entry.getValue();

                    return """
                            <li><a href="%s">%s</a></li>
                            """.formatted(arquivo.getFileName(), capitulo.getTitulo());
                }
        ).collect(Collectors.joining());

        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <title>Index</title>
                </head>
                <body>
                    <h1>Sumário</h1>
                    <nav>
                      <ul>
                        %s
                      </ul>
                    </nav>
                </body>
                </html>
                """.formatted(sumarioHtml);

        Files.writeString(indexHtml, html, StandardCharsets.UTF_8);
    }

    private void escreverArquivoHTML(Capitulo capitulo, Path arquivoHTML) throws IOException {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                </head>
                <body>
                    %s
                </body>
                </html>
                """.formatted(capitulo.getTitulo(), capitulo.getConteudoHtml());

        Files.writeString(arquivoHTML, html, StandardCharsets.UTF_8);
    }

    private String obterNomeCapituloHTML(int seq, Capitulo capitulo) {
        String tituloLimpo = capitulo.getTitulo().toLowerCase().replaceAll("\\W", "");
        return "%02d-%s.html".formatted(seq, tituloLimpo);
    }
}
