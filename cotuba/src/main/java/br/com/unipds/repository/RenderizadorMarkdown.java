package br.com.unipds.repository;

import br.com.unipds.domain.Capitulo;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class RenderizadorMarkdown {
    public List<Capitulo> executar(Path diretorioMarkdown) {

        GestorArquivosRepository gestorArquivosRepository = new GestorArquivosRepository();
        List<Path> arquivosMarkdown = gestorArquivosRepository.obterArquivosMarkdown(diretorioMarkdown);

        return arquivosMarkdown.stream().map(arquivo -> {
            Capitulo capitulo = new Capitulo();

            try {
                capitulo.setConteudoMarkdown(Files.readString(arquivo));
                Parser parser = Parser.builder().build();

                Node document = parser.parse(capitulo.getConteudoMarkdown());

                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            // capítulo
                            String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                            capitulo.setTitulo(tituloDoCapitulo);
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }
                });
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String html = renderer.render(document);

                // 1. Cria um documento XHTML vazio e configura a saída para XML (garante fechamento de tags)
                Document doc = Document.createShell("");
                doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

                // 2. Define o elemento raiz <html> com o namespace correto
                Element htmlRoot = doc.selectFirst("html");
                if (htmlRoot != null) {
                    htmlRoot.attr("xmlns", "http://www.w3.org/1999/xhtml");
                }

                // 3. Define o título (o jsoup faz o escape automático do texto)
                doc.title(capitulo.getTitulo());

                // 4. Injeta o HTML renderizado dentro do body (preservando as tags do renderer)
                doc.body().append(html);

                // 5. Obtém a string final estruturada
                String envelopeHtml = doc.outerHtml();

                capitulo.setConteudoHtml(envelopeHtml);
                return capitulo;

            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivo, ex);
            }

        }).collect(Collectors.toList());
    }
}
