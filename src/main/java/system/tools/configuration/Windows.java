package system.tools.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Windows {

    public static Process executarComandoPrompt(String path, String comando) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"" + path + "\" && " + comando);
        builder.redirectErrorStream(true);
        return builder.start();
    }

    public static void verificaExecucaoProcesso(Process processo, String[] partesRetornoSucesso) throws Exception {
        InputStream in = processo.getInputStream();
        Scanner scan = new Scanner(in);
        boolean sucesso = false;
        while (scan.hasNext()) {
            String linha = scan.nextLine();
            System.out.println("\t" + linha);
            for (String parteRetornoSucesso : partesRetornoSucesso) {
                if (linha.contains(parteRetornoSucesso)) {
                    sucesso = true;
                    break;
                }
            }
            if (sucesso) {
                break;
            }
        }

        if (!sucesso) {
            throw new Exception("Não foi possível executar o comando!");
        } else {
            System.out.println("[TÚNEL] Túnel conectado com sucesso");
        }
    }

    public static void finalizarProcesso(String aplicacao) throws IOException {
        Runtime.getRuntime().exec("taskkill /F /IM " + aplicacao + " /T");
    }

    public static File criarDiretorio(String diretorio) {
        File file = new File(diretorio);
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static void excluirArquivosPasta(String pathPasta) {
        File file = new File(pathPasta);
        deleteDirectory(file);
    }

    public static void excluirArquivosPastaExceto(String pathPasta, String parteNomeArquivo) {
        File file = new File(pathPasta);
        deleteDirectoryExcept(file, parteNomeArquivo);
    }

    private static void deleteDirectory(File file) {
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            System.out.println("\t\tDeleção do arquivo " + subfile.getName() + ": " + subfile.delete());
        }
    }

    private static void deleteDirectoryExcept(File file, String parteNomeArquivo) {
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            if (!subfile.getName().toLowerCase().contains(parteNomeArquivo.toLowerCase())) {
                System.out.println("\t\tDeleção do arquivo " + subfile.getName() + ": " + subfile.delete());
            }
        }
    }
}
