package system.tools.configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

    public static void escreverLinhaSemDataEHora(String path, String mensagem) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
        bw.write(mensagem);
        bw.close();
    }

    public static void escrever(String path, String linha) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
        String[] linhaPartida = linha.split("\n");
        for (int i = 0; i < linhaPartida.length; i++) {
            bw.write(getDataAtualFormatada("yyyy-MM-dd HH:mm:ss.SSS") + ": " + linhaPartida[i] + "\n");
        }
        bw.close();
    }

    public static String getDataAtualFormatada(String formato) {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatadorData = new SimpleDateFormat(formato);
        return formatadorData.format(calendario.getTime());
    }

}
