package system.database.repository;

import system.database.model.Negotiation;

import java.sql.ResultSet;
import java.sql.SQLException;

import static system.database.model.Negotiation.Campos.*;
import static system.hooks.Hooks.conexaoBD;

public class NegotiationRepository {

    private static final String DELETE_ROUND_DETAIL_BY_CREATED_DATE = "select * from negotiation where created_date < '?' limit 1;";

    //===============METODOS SET MODEL==================================================================================
    private static Negotiation popularDadosNegotiation(ResultSet resultado) throws Exception {
        Negotiation negotiation = new Negotiation();
        if (resultado.next()) {
            negotiation.setNegotiationId(resultado.getString(NEGOTIATION_ID.name()));
            negotiation.setType(resultado.getString(TYPE.name()));
            negotiation.setStatus(resultado.getString(STATUS.name()));
            negotiation.setRounds(resultado.getString(ROUNDS.name()));
            negotiation.setCreatedBy(resultado.getString(CREATED_BY.name()));
            negotiation.setUpdatedBy(resultado.getString(UPDATED_BY.name()));
            negotiation.setCreatedDate(resultado.getString(CREATED_DATE.name()));
            negotiation.setUpdatedDate(resultado.getString(UPDATED_DATE.name()));
            negotiation.setHasConvocation(resultado.getString(HAS_CONVOCATION.name()));
            negotiation.setCountEmail(resultado.getString(COUNT_EMAIL.name()));
            negotiation.setOpeningDate(resultado.getString(OPENING_DATE.name()));
            negotiation.setClosingDate(resultado.getString(CLOSING_DATE.name()));
            negotiation.setCanImportFile(resultado.getString(CAN_IMPORT_FILE.name()));
            negotiation.setAcceptSendDate(resultado.getString(ACCEPT_SEND_DATE.name()));
        }
        return negotiation;
    }

    //=================METODO DE SELECAO=================================================================================
    private static Negotiation getNegotiation(String query) throws Exception {
        ResultSet resultado = conexaoBD.select(query);
        Negotiation negotiation = popularDadosNegotiation(resultado);
        if (negotiation.getCreatedDate() == null) {
            throw new Exception("Não foi possível achar registro na " + Negotiation.NOME_TABELA + "!");
        }
        return negotiation;
    }

    //=================METODOS DE DELECAO=================================================================================
    public static void deleteNegotiationByCreatedDate(String data, String query) throws SQLException {
        conexaoBD.delete(data, query);
    }

    public static void deleteRoundDetailByCreatedDate(String data) throws Exception {
        Negotiation n = getNegotiation(DELETE_ROUND_DETAIL_BY_CREATED_DATE.replace("?", data));
        System.out.println(n.getNegotiationId());
        System.out.println("Deu certo");
    }

    //=================METODOS DE ATUALIZACAO==============================================================================
//    public static void updateAcceptSendDateByNegotiationId(String[] valores) throws Exception {
//        ConexaoBD.Tipos[] tipos = {TIME_STAMP, UUID};
//        conexaoBD.update(valores, tipos, UPDATE_ACCEPT_SEND_DATE);
//    }

}
