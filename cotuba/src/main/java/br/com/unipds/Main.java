package br.com.unipds;

import br.com.unipds.dto.ParametrosProcessamento;
import br.com.unipds.input.LeitorOpcoesCLI;
import br.com.unipds.service.GeradorEbookService;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {

        LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI();
        boolean modoVerboso = true;

        try {
            leitorOpcoesCLI.executar(args);
            ParametrosProcessamento parametros = new ParametrosProcessamento();

            parametros.setDiretorioMarkd(leitorOpcoesCLI.getDiretorioDosMD());
            parametros.setFormato(leitorOpcoesCLI.getFormato());
            parametros.setArquivoDeSaida(leitorOpcoesCLI.getArquivoDeSaida());
            parametros.setModoVerboso(leitorOpcoesCLI.isModoVerboso());
            modoVerboso = leitorOpcoesCLI.isModoVerboso();

            GeradorEbookService ebookService = new GeradorEbookService();
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