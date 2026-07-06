package br.com.unipds.repository.impl;

import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.CapituloBuilder;
import br.com.unipds.domain.Markdown;
import br.com.unipds.repository.RenderizadorMarkdown;
import jakarta.enterprise.context.ApplicationScoped;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RenderizadorMarkdownCommonmark implements RenderizadorMarkdown {
    @Override
    public List<Capitulo> executar(List<Markdown> markdowns) {

        return markdowns.stream().map(markdown -> {
            CapituloBuilder capituloBuilder = CapituloBuilder.builder();

            try {
                Parser parser = Parser.builder().build();

                Node document = parser.parse(markdown.conteudo());

                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            // capítulo
                            capituloBuilder.titulo(((Text) heading.getFirstChild()).getLiteral());
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

                // 4. Injeta o HTML renderizado dentro do body (preservando as tags do renderer)
                doc.body().append(html);

                // 5. Obtém a string final estruturada
                String envelopeHtml = doc.outerHtml();

                return capituloBuilder.conteudoHTML(envelopeHtml)
                        .markdown(markdown)
                        .build();

            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + markdown.arquivo(), ex);
            }

        }).collect(Collectors.toList());
    }
}
