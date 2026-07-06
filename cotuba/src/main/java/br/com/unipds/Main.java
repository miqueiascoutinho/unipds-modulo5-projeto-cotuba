package br.com.unipds;

import br.com.unipds.dto.ParametrosProcessamento;
import br.com.unipds.service.GeradorEbookService;
import br.com.unipds.ui.LeitorOpcoesCLI;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

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

            ParametrosProcessamento parametros = leitorOpcoesCLI.executar(args);
            modoVerboso = parametros.modoVerboso();

            GeradorEbookService ebookService = container.select(GeradorEbookService.class).get();
            ebookService.gerarEbook(parametros);

            System.out.println("Arquivo gerado com sucesso: " + parametros.arquivoDeSaida());
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