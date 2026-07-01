package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {

        LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI();

        try {
            leitorOpcoesCLI.executar(args);
        } catch (Exception e) {
            System.out.println("Erro no menu de opções " + e.getMessage());
            return 1;
        }

        Path diretorioDosMD = leitorOpcoesCLI.getDiretorioDosMD();
        String formato = leitorOpcoesCLI.getFormato();
        Path arquivoDeSaida = leitorOpcoesCLI.getArquivoDeSaida();
        boolean modoVerboso = leitorOpcoesCLI.isModoVerboso();

        try {

            RenderizadorHtml renderizadorHtml = new RenderizadorHtml();
            List<String> htmls = renderizadorHtml.executar(diretorioDosMD);


            if ("pdf".equals(formato)) {
                GeradorPdf geradorPdf = new GeradorPdf();
                geradorPdf.executar(htmls, arquivoDeSaida);
            } else if ("epub".equals(formato)) {
                GeradorEpub geradorEpub = new GeradorEpub();
                geradorEpub.execute(htmls, arquivoDeSaida);

            } else {
                throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
            }

            System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);
            return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (modoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }

}