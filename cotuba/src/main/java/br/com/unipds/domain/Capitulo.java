package br.com.unipds.domain;

import java.nio.file.Path;

public class Capitulo {
    private String titulo;
    private String conteudoHtml;
    private String conteudoMarkdown;
    private Path diretorio;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudoHtml() {
        return conteudoHtml;
    }

    public void setConteudoHtml(String conteudoHtml) {
        this.conteudoHtml = conteudoHtml;
    }

    public String getConteudoMarkdown() {
        return conteudoMarkdown;
    }

    public void setConteudoMarkdown(String conteudoMarkdown) {
        this.conteudoMarkdown = conteudoMarkdown;
    }

    public Path getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(Path diretorio) {
        this.diretorio = diretorio;
    }
}
