package br.com.unipds;

import br.com.unipds.dto.ParametrosProcessamento;
import br.com.unipds.repository.GeradorEbook;
import br.com.unipds.repository.impl.GeradorEbookEpub;
import br.com.unipds.repository.impl.GeradorEbookPdf;
import br.com.unipds.repository.impl.GestorArquivosDiretorio;
import br.com.unipds.repository.impl.RenderizadorMarkdownCommonmark;
import br.com.unipds.service.GeradorEbookService;
import br.com.unipds.ui.LeitorOpcoesCLI;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Named;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {


        boolean modoVerboso = true;


        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            LeitorOpcoesCLI leitorOpcoesCLI = container.select(LeitorOpcoesCLI.class).get();

            leitorOpcoesCLI.executar(args);
            ParametrosProcessamento parametros = new ParametrosProcessamento();

            parametros.setDiretorioMarkd(leitorOpcoesCLI.getDiretorioDosMD());
            parametros.setFormato(leitorOpcoesCLI.getFormato());
            parametros.setArquivoDeSaida(leitorOpcoesCLI.getArquivoDeSaida());
            parametros.setModoVerboso(leitorOpcoesCLI.isModoVerboso());
            modoVerboso = leitorOpcoesCLI.isModoVerboso();

            GeradorEbookService ebookService = container.select(GeradorEbookService.class).get();
            ebookService.gerarEbook(parametros);

            System.out.println("Arquivo gerado com sucesso: " + parametros.getArquivoDeSaida());
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