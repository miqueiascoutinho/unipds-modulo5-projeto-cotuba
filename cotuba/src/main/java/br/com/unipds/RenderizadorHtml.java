package br.com.unipds;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenderizadorHtml {
    public List<String> executar(Path diretorioMD) {

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

        try (Stream<Path> streamMDs = Files.list(diretorioMD)) {
            List<Path> arquivosMD = streamMDs
                    .filter(matcher::matches)
                    .sorted()
                    .toList();


            if (arquivosMD.isEmpty()) {
                throw new IllegalStateException("Não foram encontrados capítulos (arquivos .md) no diretório: " + diretorioMD.toAbsolutePath());
            }

            return arquivosMD.stream().map(arquivo -> {
                Node document = generateHtmlDocument(arquivo);
                try {
                    return generateHtmlString(document);
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivo, ex);
                }

            }).collect(Collectors.toList());
        } catch (IOException ex) {
            throw new IllegalStateException("Erro tentando encontrar arquivos *.md em " + diretorioMD.toAbsolutePath(), ex);
        }
    }

    private static Node generateHtmlDocument(Path arquivo) {
        Parser parser = Parser.builder().build();
        Node document;

        try {
            document = parser.parseReader(Files.newBufferedReader(arquivo));
            document.accept(new AbstractVisitor() {
                @Override
                public void visit(Heading heading) {
                    if (heading.getLevel() == 1) {
                        // capítulo
                        String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                        // TODO: usar título do capítulo
                    } else if (heading.getLevel() == 2) {
                        // seção
                    } else if (heading.getLevel() == 3) {
                        // título
                    }
                }
            });
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivo, ex);
        }
        return document;
    }

    private static String generateHtmlString(Node document) {
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);

        // TODO: usar título do capítulo
        return """
                  <html xmlns="http://www.w3.org/1999/xhtml">
                    <head>
                      <title>Capítulo</title>
                    </head>
                    <body>
                      %s
                    </body>
                  </html>
                """.formatted(html);
    }
}
