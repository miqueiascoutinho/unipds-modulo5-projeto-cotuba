package br.com.unipds.repository.impl;

import br.com.unipds.FormatoEbookQualifier;
import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.FormatoEbook;
import br.com.unipds.domain.Livro;
import br.com.unipds.repository.GeradorEbook;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.properties.AreaBreakType;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@FormatoEbookQualifier(FormatoEbook.PDF)
@ApplicationScoped
public class GeradorEbookPdf implements GeradorEbook {

    public void gerarEbook(Livro livro) {

        List<Capitulo> capitulos = livro.getCapitulos();

        try (var writer = new PdfWriter(Files.newOutputStream(livro.getArquivoSaida()));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            pdf.getDocumentInfo().setTitle(livro.getNome());
            pdf.getDocumentInfo().setAuthor(livro.getAutor());

            capitulos.forEach(capitulo -> {
                try {
                    List<IElement> convertToElements = HtmlConverter.convertToElements(capitulo.getConteudoHtml());

                    if (pdf.getNumberOfPages() == 0) {
                        pdf.addNewPage();
                    }
                    PdfOutline rootOutline = pdf.getOutlines(false);
                    if (rootOutline == null) {
                        pdf.initializeOutlines();
                        rootOutline = pdf.getOutlines(false);
                    }

                    PdfOutline chapterOutline = rootOutline.addOutline(capitulo.getTitulo());
                    chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                    for (IElement element : convertToElements) {
                        pdfDocument.add((IBlockElement) element);
                    }
                    // TODO: não adicionar página depois do último capítulo
                    pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo ", ex);
                }
            });
        } catch (IOException ex) {
            throw new IllegalStateException("Erro ao gerar o arquivo PDF ", ex);
        }
    }
}
