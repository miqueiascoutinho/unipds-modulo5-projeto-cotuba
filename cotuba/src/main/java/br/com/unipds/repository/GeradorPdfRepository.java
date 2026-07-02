package br.com.unipds.repository;

import br.com.unipds.domain.Capitulo;
import br.com.unipds.domain.Livro;
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

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class GeradorPdfRepository {
    public void executar(Livro livro) throws IOException {

        List<Capitulo> capitulos1 = livro.getCapitulos();

        try (var writer = new PdfWriter(Files.newOutputStream(livro.getArquivoSaida()));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            pdf.getDocumentInfo().setTitle(livro.getNome());
            pdf.getDocumentInfo().setAuthor(livro.getAutor());

            capitulos1.forEach(capitulo -> {
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
