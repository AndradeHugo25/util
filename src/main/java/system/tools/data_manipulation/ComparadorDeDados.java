package system.tools.data_manipulation;

import java.text.Normalizer;
import java.util.ArrayList;

public class ComparadorDeDados {

    public static ArrayList<String> textosSite;
//    private static Log log = getLog();

    public static void addTextoParaValidar(String texto) {
        if (texto != null && !textosSite.contains(texto)) {
            textosSite.add(texto);
        }
    }

    public static void compararTextosPorSeparador(String texto1, String texto2, String separador, int diferencasEsperadas) throws Exception {
        String[] linhasTexto1 = texto1.split(separador);
        String[] linhasTexto2 = texto2.split(separador);
        int diferencas = getDiferencasVetoresStrings(linhasTexto1, linhasTexto2);

        if (diferencas > diferencasEsperadas) {
            throw new Exception(diferencas + " diferenças não esperadas foram encontradas!");
        }
    }

    public static void compararTextosLinhaPorLinha(String texto1, String texto2, int diferencasEsperadas) throws Exception {
        String[] linhasTexto1 = texto1.split("\n");
        String[] linhasTexto2 = texto2.split("\n");
        int diferencas = getDiferencasVetoresStrings(linhasTexto1, linhasTexto2);

        if (diferencas > diferencasEsperadas) {
            throw new Exception(diferencas + " diferenças não esperadas foram encontradas!");
        }
    }

    private static int getDiferencasVetoresStrings(String[] v1, String[] v2) throws Exception {
        int qtdDiffsNaoEsperadas = 0;

        for (int i = 0; i < v1.length; i++) {
            if (!v1[i].equals(v2[i])) {
                qtdDiffsNaoEsperadas += 1;
            }
        }

        if (v2.length > v1.length) {
            for (int i = v1.length; i < v2.length; i++) {
                qtdDiffsNaoEsperadas += 1;
            }
        }
        return qtdDiffsNaoEsperadas;
    }

    public static void compararDadosDeInsert(String[] dadosEsperados, String[] dadosDaAplicacao) throws Exception {
        if (dadosEsperados.length != dadosDaAplicacao.length) {
            throw new Exception("Número de dados diferentes para comparação: dadosEperados = " + dadosEsperados.length
                    + " / dadosAplicacao = " + dadosDaAplicacao.length + "!");
        }

        if (dadosEsperados.length > 0) {
            for (int i = 0; i < dadosEsperados.length; i++) {
                if (!dadosEsperados[i].equals("#ignore")) {
                    if (dadosEsperados[i].equals("#notnull")) {
                        if (dadosDaAplicacao[i] == null) {
                            throw new Exception("Campo da coluna " + (i + 1) + " não deveria ser nulo!!");
                        }
                    } else if (dadosEsperados[i].equals("#null")) {
                        if (dadosDaAplicacao[i] != null) {
                            throw new Exception("Campo da coluna " + (i + 1) + " deveria ser nulo!!");
                        }
                    } else if (!dadosEsperados[i].equals(dadosDaAplicacao[i])) {
                        throw new Exception("Campo da coluna " + (i + 1) + " diferente do esperado!");
                    }
                }
            }
        } else {
            throw new Exception("Vetor de dados esperados está vazio!");
        }
    }

    public static void compararDadosDeUpdate(String[] tagsUpdate, String[] dadosDaAplicacaoAntes, String[] dadosDaAplicacaoDepois) throws Exception {
        if (tagsUpdate.length > 0) {
            for (int i = 0; i < tagsUpdate.length; i++) {
                if (tagsUpdate[i].equals("#changed")) {
                    comparaCamposTagChanged(dadosDaAplicacaoAntes[i], dadosDaAplicacaoDepois[i], i);
                } else if (tagsUpdate[i].equals("#nochanges")) {
                    comparaCamposTagNoChanges(dadosDaAplicacaoAntes[i], dadosDaAplicacaoDepois[i], i);
                } else {
                    comparaCamposSemTag(tagsUpdate[i], dadosDaAplicacaoAntes[i], dadosDaAplicacaoDepois[i], i);
                }
            }
        } else {
            throw new Exception("Vetor de dados esperados está vazio!");
        }
    }

    private static void comparaCamposTagChanged(String dadoDaAplicacaoAntes, String dadoDaAplicacaoDepois, int coluna) throws Exception {
        if (dadoDaAplicacaoAntes == null) {
            if (dadoDaAplicacaoDepois == null) {
                throw new Exception("ERRO campo da coluna " + (coluna + 1) + "! " +
                        "Dado da aplicação [null] deveria ter sido alterado!!");
            }
        } else {
            if (dadoDaAplicacaoDepois != null) {
                if (dadoDaAplicacaoAntes.equals(dadoDaAplicacaoDepois)) {
                    throw new Exception("ERRO campo da coluna " + (coluna + 1) + "! " +
                            "Dado da aplicação [" + dadoDaAplicacaoDepois + "] deveria ter sido alterado!!");
                }
            }
        }
    }

    private static void comparaCamposTagNoChanges(String dadoDaAplicacaoAntes, String dadoDaAplicacaoDepois, int coluna) throws Exception {
        if (dadoDaAplicacaoAntes == null) {
            if (dadoDaAplicacaoDepois != null) {
                throw new Exception("ERRO campo da coluna " + (coluna + 1) + "! " +
                        "Dado da aplicação [" + dadoDaAplicacaoDepois + "] deveria ser mantido com valor anterior [null]!!");
            }
        } else {
            if (dadoDaAplicacaoDepois == null) {
                throw new Exception("ERRO campo da coluna " + (coluna + 1) + "! " +
                        "Dado da aplicação [null] deveria ser mantido com valor anterior [" + dadoDaAplicacaoAntes + "]!!");
            } else if (!dadoDaAplicacaoAntes.equals(dadoDaAplicacaoDepois)) {
                throw new Exception("ERRO campo da coluna " + (coluna + 1) + "! " +
                        "Dado da aplicação [" + dadoDaAplicacaoDepois + "] deveria ser mantido com valor anterior [" + dadoDaAplicacaoAntes + "]!!");
            }
        }
    }

    private static void comparaCamposSemTag(String dadoEsperado, String dadoDaAplicacaoAntes, String dadoDaAplicacaoDepois, int coluna) throws Exception {
        String[] split = dadoEsperado.split("=");
        dadoEsperado = split[1];
        String tag = split[0];

        if (isEqualDoEsperado(dadoEsperado, dadoDaAplicacaoDepois)) {
            if (tag.equals("#changed")) {
                comparaCamposTagChanged(dadoDaAplicacaoAntes, dadoDaAplicacaoDepois, coluna);
            } else if (tag.equals("#nochanges")) {
                comparaCamposTagChanged(dadoDaAplicacaoAntes, dadoDaAplicacaoDepois, coluna);
            }
        } else {
            throw new Exception("ERRO campo da coluna " + (coluna + 1) + "! " +
                    "Dado da aplicação [" + dadoDaAplicacaoDepois + "] diferente do esperado [" + dadoEsperado + "]!!");
        }
    }

    private static boolean isEqualDoEsperado(String dadoEsperado, String dadoDaAplicacaoDepois) {
        if (dadoEsperado.equals("null")) {
            return dadoDaAplicacaoDepois == null;
        } else {
            return dadoEsperado.equals(dadoDaAplicacaoDepois);
        }
    }

//    public static boolean compararDadosDoBanco(ResultSet resultadoQuery, String[] dadosEsperados) throws SQLException, IOException, QtdColunasDiferenteException {
//        ResultSetMetaData rsm = resultadoQuery.getMetaData();
//        int qtdColunas = rsm.getColumnCount();
//
//        if (qtdColunas != dadosEsperados.length){
//            throw new QtdColunasDiferenteException("");
//        }
//
//        int diferencas = 0;
//        for (int i = 0; i < dadosEsperados.length; i++) {
//            String dadoDoBanco;
//            try {
//                dadoDoBanco = resultadoQuery.getString(i+1).trim().replace(" ", "");
//                if (rsm.getColumnTypeName(i+1).equals("TIMESTAMP")) {
//                    dadoDoBanco = DataEHora.converterTimeStampParaDate(dadoDoBanco);
//                }
//            } catch (Exception e) {
//                dadoDoBanco = "";
//            }
//            String dadoEsperado = dadosEsperados[i].trim().replace(" ", "");
//            if (!dadoEsperado.equals("")) {
//                if (!TratamentoDeDados.removerAcentuacao(dadoDoBanco).equals(TratamentoDeDados.removerAcentuacao(dadoEsperado))){
//                    log.escrever( "DADO DO BANCO: [" + dadoDoBanco + "] DADO ESPERADO: [" + dadoEsperado
//                            + "]  (NOME DO CAMPO: " + rsm.getColumnName(i+1) + ")");
//                    diferencas++;
//                }
//            }
//        }
//
//        if (diferencas > 0)
//            return false;
//        return true;
//    }

    public static boolean compararIgnorandoAcentuacao(String texto1, String texto2) {
        texto1 = Normalizer.normalize(texto1, Normalizer.Form.NFD);
        texto1 = texto1.replaceAll("[^\\p{ASCII}]", "");
        texto2 = Normalizer.normalize(texto2, Normalizer.Form.NFD);
        texto2 = texto2.replaceAll("[^\\p{ASCII}]", "");

        if (texto1.equalsIgnoreCase(texto2))
            return true;
        return false;
    }

    //verificar se eh necessario
//    public static void compararTextosPaginas(String[] textosEsperados, ArrayList<String> textosSite) throws QtdTextosSiteDiferenteDoEsperadoException {
//        if (textosEsperados.length != textosSite.size()){
//            throw new QtdTextosSiteDiferenteDoEsperadoException("", textosEsperados.length, textosSite.size());
//        } else {
//            for (int i = 0; i < textosEsperados.length; i++) {
//                Assert.assertEquals(textosEsperados[i], textosSite.get(i));
//            }
//        }
//    }

    public static void compararTextos(String[] textosEsperados) throws Exception {
        if (textosEsperados.length == textosSite.size()) {
            int i = 0;
            for (String esperado : textosEsperados) {
                if (!esperado.equals(textosSite.get(i))) {
                    throw new Exception("O texto do site é diferente ou não contém o texto esperado!\n" +
                            "Texto do Site:\n" + textosSite.get(i) + "\n" +
                            "Texto Esperado:\n" + esperado);
                }
                i++;
            }
        } else {
            throw new Exception("ERRO!!! Qtd de textos esperados: " + textosEsperados.length + ", qtd de textos do site"
                    + textosSite.size());
        }
    }

    public static void verificarSeTextosSiteContemEsperados(ArrayList<String[]> textosEsperados, ArrayList<String> textosSite) throws Exception {
        int i = 0;
        for (String texto : textosSite) {
            verificarSeTextoSiteContemEsperados(textosEsperados.get(i), texto);
            i++;
        }
    }

    public static void verificarSeTextoSiteContemEsperados(String[] textosEsperados, String txtSite) throws Exception {
        for (String esperado : textosEsperados) {
            if (!txtSite.contains(esperado)) {
                throw new Exception("O texto do site é diferente ou não contém o texto esperado!\n" +
                        "Texto do Site:\n" + txtSite + "\n" +
                        "Texto Esperado:\n" + esperado);
            }
        }
    }

    // utilizado no método comparar Strings

//    public void compararString(String textoparaComparar, File arquivoOriginal) throws IOException, PalavraDiferenteDoEsperadoException {
//        String[] textoPartido = textoparaComparar.split("\\s+");
//        Scanner ler = new Scanner(arquivoOriginal);
//        int i = 0;
//        while (ler.hasNext()) {
//            String palavra = ler.next();
//            if (i < textoPartido.length) {
//                if (i != 0) { //Ignorando a primeira palavra que está apresentando um lixo ' no inicio
//                    if (!palavra.equals(textoPartido[i])) {
//                        throw new PalavraDiferenteDoEsperadoException("Palavra " + palavra + " e diferente de " + textoPartido[i] +" do arquivo txt");
//                    }
//                }
//                i++;
//            }
//        }
//        ler.close();
//
//    }

}