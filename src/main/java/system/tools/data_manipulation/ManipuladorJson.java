package system.tools.data_manipulation;

public class ManipuladorJson {

    public static String getValue(String json, String campo) {
        int indexInicial = json.indexOf(campo);
        json = json.substring(indexInicial);

        int indexDoisPontos = json.indexOf(":");
        json = json.substring(indexDoisPontos);

        int indexPrimeiraAspa = json.indexOf("\"");
        json = json.substring(indexPrimeiraAspa + 1);

        int indexSegundaAspa = json.indexOf("\"");
        json = json.substring(0, indexSegundaAspa);

        return json;
    }

}
