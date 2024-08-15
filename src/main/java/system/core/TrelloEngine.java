package system.core;

import system.model.TrelloCard;
import system.model.TrelloGrupo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static system.hooks.Hooks.googlePages;

public class TrelloEngine {

    public static final SimpleDateFormat formatoSemAno = new SimpleDateFormat("dd/MM");
    public static final SimpleDateFormat formatoComAno = new SimpleDateFormat("dd/MM/yyyy");
    public static final ArrayList<TrelloGrupo> TRELLO_GRUPOS = new ArrayList<>();
    public static final Set<Date> datas = new TreeSet<>();

    public static ArrayList<String> listaNaoFormatada;
    public static String txtComChaves = "";
    public static String resultado = "";

    public static void salvarGrupos(ArrayList<String> listaNaoFormatada) throws ParseException {
        for (String card : listaNaoFormatada) {
            String[] partesCard = card.split("\n");
            String tag = partesCard[0];
            String conteudo = partesCard[1];

            TrelloCard novoTrelloCard;
            if (partesCard.length == 2) {
                novoTrelloCard = new TrelloCard(tag, conteudo);
            } else {
                String dataNaoFormatada = partesCard[2];
                Object[] retorno = capturarDatas(dataNaoFormatada);
                String dataParaCard = (String) retorno[0];
                Date dataParaLista = (Date) retorno[1];
                novoTrelloCard = new TrelloCard(tag, conteudo, dataParaCard, dataParaLista);
                datas.add(dataParaLista);
            }

            if (!txtComChaves.contains(novoTrelloCard.getChave())) {
                txtComChaves = txtComChaves.concat(novoTrelloCard.getChave());
                TrelloGrupo novoTrelloGrupo = new TrelloGrupo(novoTrelloCard.getData(), novoTrelloCard.getDataFormatada(), novoTrelloCard.getTag());
                novoTrelloGrupo.addCard(novoTrelloCard);
                TRELLO_GRUPOS.add(novoTrelloGrupo);
            } else {
                for (TrelloGrupo atual : TRELLO_GRUPOS) {
                    if (atual.getChave().equals(novoTrelloCard.getChave())) {
                        atual.addCard(novoTrelloCard);
                        break;
                    }
                }
            }
        }
    }

    public static Object[] capturarDatas(String dataNaoFormatada) throws ParseException {
        String primeiraData;
        String dataParaCard;
        Date dataParaLista;

        String[] palavras = dataNaoFormatada.split(" ");
        if (dataNaoFormatada.contains("-")) {
            primeiraData = palavras[0] + "/" + formatarMes(palavras[2]);
            dataParaCard = "De: " + primeiraData + " Até: " + palavras[4] + "/" + formatarMes(palavras[6]);
        } else if (dataNaoFormatada.contains("Começ")) {
            primeiraData = palavras[1] + "/" + formatarMes(palavras[3]);
            dataParaCard = "A partir de: " + primeiraData;
        } else {
            primeiraData = palavras[0] + "/" + formatarMes(palavras[2]);
            dataParaCard = primeiraData;
        }

        Calendar calendario = Calendar.getInstance();
        String anoAtual = String.valueOf(calendario.get(Calendar.YEAR));
        primeiraData = primeiraData + "/" + anoAtual;
        dataParaLista = formatoComAno.parse(primeiraData);

        return new Object[]{dataParaCard, dataParaLista};
    }

    public static String formatarMes(String mesNaoFormatado) {
        String mesFormatado = "";
        switch (mesNaoFormatado) {
            case "jan" -> mesFormatado = "01";
            case "fev" -> mesFormatado = "02";
            case "mar" -> mesFormatado = "03";
            case "abr" -> mesFormatado = "04";
            case "mai" -> mesFormatado = "05";
            case "jun" -> mesFormatado = "06";
            case "jul" -> mesFormatado = "07";
            case "ago" -> mesFormatado = "08";
            case "set" -> mesFormatado = "09";
            case "out" -> mesFormatado = "10";
            case "nov" -> mesFormatado = "11";
            case "dez" -> mesFormatado = "12";
        }
        return mesFormatado;
    }

    public static void addAoResultado(ArrayList<TrelloGrupo> trelloGrupos) {
        if (trelloGrupos.size() > 0) {
            resultado = resultado.concat("*" + trelloGrupos.get(0).getDataFormatada() + "*\n");
            for (TrelloGrupo g : trelloGrupos) {
                resultado = resultado.concat("\t" + g.getTag() + "\n");
                for (TrelloCard c : g.getTrelloCards()) {
                    resultado = resultado.concat("\t\t" + c.getConteudo() + "\n");
                }
            }
        }
    }

    public static void preencherCorpoEmailFormatado() {
        String[] linhas = resultado.split("\n");
        boolean anteriorNegrito = false;
        for (String linha : linhas) {
            int totalTabs = contarOcorrenciasChar('\t', linha);

            if (totalTabs == 0) {
                googlePages.negrito.click();
                googlePages.getCampoCorpoEmail().sendKeys(linha + "\n");
                anteriorNegrito = true;
            } else if (totalTabs == 1) {
                tabEmail(1, true);
                verificaNegrito(anteriorNegrito);
                anteriorNegrito = false;
                linha = linha.replace("\t", "");
                googlePages.getCampoCorpoEmail().sendKeys(linha + "\n");
                tabEmail(1, false);
            } else if (totalTabs == 2) {
                tabEmail(2, true);
                verificaNegrito(anteriorNegrito);
                anteriorNegrito = false;
                linha = linha.replace("\t", "");
                googlePages.getCampoCorpoEmail().sendKeys(linha + "\n");
                tabEmail(2, false);
            }
        }
    }

    public static int contarOcorrenciasChar(char caractere, String texto) {
        int totalTabs = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == caractere) {
                totalTabs++;
            }
        }
        return totalTabs;
    }

    public static void tabEmail(int qtdVezes, boolean praFrente) {
        for (int i = 0; i < qtdVezes; i++) {
            if (praFrente) {
                googlePages.setaExpandirEditor.click();
                googlePages.maiorTab.click();
            } else {
                googlePages.setaExpandirEditor.click();
                googlePages.menorTab.click();
            }
        }
    }

    public static void verificaNegrito(boolean anteriorNegrito) {
        if (anteriorNegrito) {
            googlePages.negrito.click();
        }
    }
}
