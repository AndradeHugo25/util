package system.tools.data_manipulation;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataEHora {

    //Retorna a data atual de acordo com o formato passado por parametro (Exemplo de formato: "yyyy-MM-dd")
    public static String getDataAtualFormatada(String formato) {
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatadorData = new SimpleDateFormat(formato);
        return formatadorData.format(calendario.getTime());
    }

    //Retorna a data formatada que foi passada mais a qtdDias passados por parametro
    public static String adicionarDiasAUmaData(String data, String formato, int qtdDias) throws ParseException {
        SimpleDateFormat formatadorData = new SimpleDateFormat(formato);
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(formatadorData.parse(data));
        calendario.add(Calendar.DATE , qtdDias);
        return formatadorData.format(calendario.getTime());
    }

    //Retorna a data formatada que foi passada menos a qtdDias passados por parametro
    public static String subtrairDiasDeUmaData(String data, String formato, int qtdDias) throws ParseException {
        SimpleDateFormat formatadorData = new SimpleDateFormat(formato);
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(formatadorData.parse(data));
        calendario.add(Calendar.DATE , qtdDias*-1);
        return formatadorData.format(calendario.getTime());
    }

    public static String addHorasData(String data, int hours) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(data);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        String strDate = formatter.format(calendar.getTime());
        return strDate;
    }

    public static String addMinutosData(String formato, String data, int minutos) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formato);
        Date date = formatter.parse(data);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutos);

        String strDate = formatter.format(calendar.getTime());
        return strDate;
    }

    public static String addDiasTimeStamp(String data, int dias) {
        Timestamp timestamp = Timestamp.valueOf(data);
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.DATE, dias);
        return String.valueOf(new Timestamp(cal.getTime().getTime()));
    }

    public static String converterTimeStampParaDate(String timestamp) {
        String date = timestamp.substring(0, 10);
        return date;
    }

    public static String padronizarData(String data){
        data = data.replace("-", "/");
        data = data.substring(8,10) + data.substring(4,5) + data.substring(5,7) + data.substring(7,8) + data.substring(0,4);
        return data;
    }

    //Converter a data Americana yyyyMMdd para ddMMyyyy sem utilizar "-" ou "/"
    public static String converterDataParaBr(String dataEua){
        dataEua = dataEua.replace("-", "");
        dataEua = dataEua.substring(6,8) + dataEua.substring(4,6) + dataEua.substring(0,4);
        return dataEua;
    }

    //Converter a data Americana yyyyMMdd para dd/MM/yyyy
    public static String converterDataParaBrComSeparador(String dataEua){
        dataEua = converterDataParaBr(dataEua);
        dataEua = dataEua.substring(0,2) + "/" + dataEua.substring(2,4) + "/" + dataEua.substring(4);
        return dataEua;
    }

    //Converter a data Americana MM/dd/yyyy para dd/MM/yyyy
    public static String converterDataParaBrComBarra(String dataEua) {
        String[] split = dataEua.split("/");
        String dia = completarValorDataComZero(split[1]);
        String mes = completarValorDataComZero(split[0]);
        return dia + "/" + mes + "/20" + split[2];
    }

    private static String completarValorDataComZero(String valor) {
        if (valor.length() == 1) {
            return "0" + valor;
        }
        return valor;
    }

    public static boolean isDate(String texto){
        if (texto.split("-").length >= 3 && !texto.contains(":")){
            return true;
        }
        return false;
    }

    public static boolean isTimeStamp(String texto){
        if (texto.split("-").length >= 3 && texto.split(":").length >= 3 ){
            return true;
        }
        return false;
    }

    //Compara data A e data B no formato br dd/MM/yyyyy
    //Return 1 se A > B; return -1 se A < B; return 0 se forem iguais
    public static int compareToParaDatas(String dataA, String dataB) {
        int anoDataA = Integer.parseInt(dataA.substring(6));
        int mesDataA = Integer.parseInt(dataA.substring(3, 5));
        int diaDataA = Integer.parseInt(dataA.substring(0, 2));

        int anoDataB = Integer.parseInt(dataB.substring(6));
        int mesDataB = Integer.parseInt(dataB.substring(3, 5));
        int diaDataB = Integer.parseInt(dataB.substring(0, 2));

        if (anoDataA == anoDataB && mesDataA == mesDataB && diaDataA == diaDataB) {
            return 0;
        } else {
            if (anoDataA > anoDataB) {
                return 1;
            } else if (anoDataB > anoDataA) {
                return -1;
            } else {
                if (mesDataA > mesDataB) {
                    return 1;
                } else if (mesDataB > mesDataA) {
                    return -1;
                } else {
                    if (diaDataA > diaDataB) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }
}
