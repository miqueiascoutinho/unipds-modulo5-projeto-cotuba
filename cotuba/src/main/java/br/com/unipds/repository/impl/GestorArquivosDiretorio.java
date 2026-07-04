package br.com.unipds.repository.impl;

import br.com.unipds.repository.GestaoArquivos;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class GestorArquivosDiretorio implements GestaoArquivos {
    @Override
    public List<Path> obterArquivosMarkdown(Path diretorioMD) {

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

        try (Stream<Path> streamMDs = Files.list(diretorioMD)) {
            List<Path> arquivosMD = streamMDs
                    .filter(matcher::matches)
                    .sorted()
                    .toList();

            if (arquivosMD.isEmpty()) {
                throw new IllegalStateException("Não foram encontrados capítulos (arquivos .md) no diretório: " + diretorioMD.toAbsolutePath());
            }

            return arquivosMD;

        } catch (IOException ex) {
            throw new IllegalStateException("Erro tentando encontrar arquivos *.md em " + diretorioMD.toAbsolutePath(), ex);
        }
    }
}
