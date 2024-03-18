package system.tools.database;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

@Data
public class ConexaoBD {

    private final Properties properties;
    private static PreparedStatement stmt;
    private static ResultSet resultado;
    private Connection connection;

    public ConexaoBD(Properties properties) {
        this.properties = properties;
    }

    public enum Tipos {
        VARCHAR, INTEGER, DECIMAL, TIME_STAMP, BOOL, DATE, NULL
    }

    public void conectarBanco() throws Exception {
        String urlJdbc = "jdbc:postgresql://34.171.27.115:5432/postgres";
        String usuarioBD = "";
        String senhaBD = "";
        String ambiente = (String) properties.get("ambienteBanco");

        if (ambiente.equalsIgnoreCase("TESTE") || ambiente.equalsIgnoreCase("TST")) {
            usuarioBD = properties.getProperty("userPostgreTst");
            senhaBD = properties.getProperty("passworPostgreTst");
        } else if (ambiente.equalsIgnoreCase("DESENV") || ambiente.equalsIgnoreCase("DEV")) {
            usuarioBD = properties.getProperty("userPostgreDev");
            senhaBD = properties.getProperty("passworPostgreDev");
        }

        connection = DriverManager.getConnection(urlJdbc, usuarioBD, senhaBD);
    }

    private PreparedStatement setarParametros(PreparedStatement statement, String[] valores, Tipos[] tipos) throws SQLException {
        int param = 1;
        for (Tipos tipo : tipos) {
            switch (tipo) {
                case VARCHAR -> statement.setString(param, valores[param - 1]);
                case INTEGER -> statement.setInt(param, Integer.parseInt(valores[param - 1]));
                case DECIMAL -> statement.setBigDecimal(param, new BigDecimal(valores[param - 1]));
                case TIME_STAMP -> statement.setTimestamp(param, Timestamp.valueOf(valores[param - 1]));
                case DATE -> statement.setDate(param, Date.valueOf(valores[param - 1]));
                case BOOL -> statement.setBoolean(param, Boolean.parseBoolean(valores[param - 1]));
                case NULL -> statement.setNull(param, Types.NULL);
            }
            param++;
        }
        return statement;
    }

//    public void conectarBancoMySql(Properties properties, String ambiente) throws Exception {
//        String urlJdbc = "";
//        String usuarioBD = "";
//        String senhaBD = "";
//
//        if (ambiente.toUpperCase().equals("HOMOLOG") || ambiente.toUpperCase().equals("HML")) {
//            usuarioBD = properties.getProperty("userMysqlHomolog");
//            senhaBD = properties.getProperty("passwordMysqlHomolog");
//            urlJdbc = properties.getProperty("urlJDBCMysqlHomolog");
//        } else if (ambiente.toUpperCase().equals("DESENV") || ambiente.toUpperCase().equals("DEV")) {
//            usuarioBD = properties.getProperty("userMysqlDesenv");
//            senhaBD = properties.getProperty("passwordMysqlDesenv");
//            urlJdbc = properties.getProperty("urlJDBCMysqlDesenv");
//        }
//
//        connection = DriverManager.getConnection(urlJdbc, usuarioBD, senhaBD);
//    }
//
//    public void conectarBancoGenerico(Properties properties) throws SQLException {
//        String ambiente = properties.getProperty("ambiente");
//        String usuarioBD = properties.getProperty("userBD");
//        String senhaBD = properties.getProperty("passwordBD");
//        String urlJdbc = properties.getProperty("serverHost");
//        connection = DriverManager.getConnection(urlJdbc, usuarioBD, senhaBD);
//    }

    public void desconectarBanco() throws Exception {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

//    public void compararDadosBancoComDadosEsperados(String parametro, String[] dadosEsperados, String query) throws SQLException, DadosDoBancoDiferentesException, IOException, QtdColunasDiferenteException {
//        ResultSet resultado = select(parametro, query);
//        if (!ComparadorDeDados.compararDadosDoBanco(resultado, dadosEsperados)) {
//            throw new DadosDoBancoDiferentesException("");
//        }
//    }
//
//    public void compararDadosBancoVariosRegistros(String parametro, String query, String[] dadosEsperados, int divisorRegistros) throws SQLException, DadosDoBancoDiferentesException, IOException, QtdColunasDiferenteException {
//        ResultSet resultado = select(parametro, query);
//        int i = 0;
//        do {
//            String[] registroEsperado = new String[divisorRegistros];
//            for (int j = 0; j < registroEsperado.length; j++) {
//                registroEsperado[j] = dadosEsperados[i];
//                i++;
//            }
//            if (!ComparadorDeDados.compararDadosDoBanco(resultado, registroEsperado)) {
//                throw new DadosDoBancoDiferentesException("");
//            }
//        } while (resultado.next());
//    }

    //-----------------------DELETE----------------------------------------------------------------------------

    public void delete(String parametro, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        stmt.setString(1, parametro);
        stmt.execute();
        stmt.close();
    }

    public void deleteVariosParams(String[] valores, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        for (int i = 0; i < valores.length; i++) {
            stmt.setString(i + 1, valores[i]);
        }
        stmt.execute();
        stmt.close();
    }

    //-----------------------UPDATES----------------------------------------------------------------------------
    public void update(String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
        stmt.close();
    }

    public void update(String parametro, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        stmt.setString(1, parametro);
        stmt.executeUpdate();
        stmt.close();
    }

    public void updateVariosParams(String[] valores, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        int param = 1;
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] != null && !valores[i].equals("#notnull")) {
                stmt.setString(param, valores[i]);
                param += 1;
            }
        }
        stmt.executeUpdate();
        stmt.close();
    }

    public void update(String[] valores, Tipos[] tipos, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        stmt = setarParametros(stmt, valores, tipos);
        stmt.executeUpdate();
        stmt.close();
    }

    public String[] prepararValoresParaUpdate(int[] colunas, String[] valores, String query) throws SQLException {
        String[] valoresETipos = new String[valores.length * 2];
        String[] tipos = getTiposColunas(colunas, query);
        for (int i = 0; i < valores.length; i++) {
            valoresETipos[i * 2] = valores[i];
            valoresETipos[i * 2 + 1] = tipos[i];
        }
        return valoresETipos;
    }

    //-----------------------SELECT----------------------------------------------------------------------------
    public ResultSet select(String[] valores, Tipos[] tipos, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        stmt = setarParametros(stmt, valores, tipos);
        resultado = stmt.executeQuery();
        return resultado;
    }

    //select utilizando um parametro dinamico (utilizado por sistemas antigos)
    public ResultSet select(String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        resultado = stmt.executeQuery();
        return resultado;
    }

    //select utilizando um parametro dinamico (utilizado por sistemas antigos)
    public ResultSet select(String parametro, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        if (!parametro.equals("")) {
            stmt.setString(1, parametro);
        }
        resultado = stmt.executeQuery();
        return resultado;
    }

    //select utilizando lista de valores dinamicos
    public ResultSet selectVariosParams(String[] valores, String query) throws SQLException {
        stmt = connection.prepareStatement(query);
        if (valores.length != 0) {
            int param = 1;
            for (int i = 0; i < valores.length; i++) {
                if (valores[i] != null && !valores[i].equals("#notnull")) {
                    stmt.setString(param, valores[i]);
                    param += 1;
                }
            }
        }
        resultado = stmt.executeQuery();
        return resultado;
    }

    //retorna o valor da coluna desejada de um registro da tabela, utilizando um parametro dinamico
    public String getDadoDeUmRegistro(String parametro, String query, int posicaoDadoNoBanco) throws SQLException {
        resultado = select(parametro, query);
        if (resultado != null) {
            return resultado.getString(posicaoDadoNoBanco);
        }
        return null;
    }

    //retorna o valor da coluna desejada de um registro da tabela, utilizando uma lista de parametros dinamicos
    public String getDadoDeUmRegistro(String[] parametros, String query, int posicaoDadoNoBanco) throws SQLException {
        resultado = selectVariosParams(parametros, query);
        if (resultado != null) {
            return resultado.getString(posicaoDadoNoBanco);
        }
        return null;
    }

    //retorna os valores das colunas desejadas de um registro da tabela
    public String[] getDadosDeUmRegistro(String parametro, String query, int[] posicoesDadosNoBanco) throws SQLException {
        resultado = select(parametro, query);
        String[] dados = new String[posicoesDadosNoBanco.length];
        for (int i = 0; i < posicoesDadosNoBanco.length; i++) {
            dados[i] = resultado.getString(posicoesDadosNoBanco[i]);
        }
        return dados;
    }

    //retorna os valores da coluna desejada de varios registros da consulta
    public String[] getDadosDeUmColuna(String parametro, String query, String nomeColuna) throws SQLException {
        ArrayList<String> aux = new ArrayList<String>();
        resultado = select(parametro, query);
        if (resultado != null) {
            do {
                aux.add(resultado.getString(nomeColuna));
            } while (resultado.next());
            return aux.toArray(new String[aux.size()]);
        } else {
            return null;
        }
    }

    public String[] getTiposColunas(int[] colunas, String query) throws SQLException {
        String[] tipos = new String[colunas.length];
        resultado = select("", query);
        if (resultado != null) {
            for (int i = 0; i < tipos.length; i++) {
                tipos[i] = resultado.getMetaData().getColumnTypeName(colunas[i]);
            }
        }
        return tipos;
    }

}

