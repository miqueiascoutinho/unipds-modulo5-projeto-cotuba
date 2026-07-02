package br.com.unipds.dto;

import br.com.unipds.domain.FormatoEbook;

import java.nio.file.Path;

public class ParametrosProcessamento {
    private Path diretorioMarkd;
    private FormatoEbook formato;
    private Path arquivoDeSaida;
    private Boolean modoVerboso;

    public Path getDiretorioMarkd() {
        return diretorioMarkd;
    }

    public void setDiretorioMarkd(Path diretorioMarkd) {
        this.diretorioMarkd = diretorioMarkd;
    }

    public FormatoEbook getFormato() {
        return formato;
    }

    public void setFormato(FormatoEbook formato) {
        this.formato = formato;
    }

    public Path getArquivoDeSaida() {
        return arquivoDeSaida;
    }

    public void setArquivoDeSaida(Path arquivoDeSaida) {
        this.arquivoDeSaida = arquivoDeSaida;
    }

    public Boolean getModoVerboso() {
        return modoVerboso;
    }

    public void setModoVerboso(Boolean modoVerboso) {
        this.modoVerboso = modoVerboso;
    }
}

