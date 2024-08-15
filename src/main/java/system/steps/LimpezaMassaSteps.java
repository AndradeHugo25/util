package system.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.openqa.selenium.WebElement;
import system.core.TrelloLogin;
import system.database.repository.NegotiationRepository;
import system.map.Urls;
import system.model.TrelloGrupo;
import system.utilRedspark.AtalhoConexaoBD;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static system.core.TrelloEngine.*;
import static system.hooks.Hooks.*;

public class LimpezaMassaSteps {

    private String data;

    @Dado("que acesse o banco de dados do PNLD")
    public void queAcesseOBancoDeDadosDoPNLD() throws Exception {
        AtalhoConexaoBD.conectar();
    }

    @E("que set a data {string}")
    public void queSetAData(String dataDesejada) {
        data = dataDesejada + " 23:59:59";
    }

    @E("que delete round_detail")
    public void queDeleteRound_detail() throws Exception {
        NegotiationRepository.deleteRoundDetailByCreatedDate(data);
    }

    @E("que delete round_document")
    public void queDeleteRound_document() {
    }

    @E("que delete round_publisher_item")
    public void queDeleteRound_publisher_item() {
    }

    @E("que delete publisher_round_action")
    public void queDeletePublisher_round_action() {
    }

    @E("que delete round")
    public void queDeleteRound() {
    }

    @E("que delete negotiation_edital_object_item")
    public void queDeleteNegotiation_edital_object_item() {
    }

    @E("que delete negotiation_edital_object")
    public void queDeleteNegotiation_edital_object() {
    }

    @E("que delete negotiation_justification_register")
    public void queDeleteNegotiation_justification_register() {
    }

    @E("que delete negotiation_justification")
    public void queDeleteNegotiation_justification() {
    }

    @E("que delete register_negotiation_object")
    public void queDeleteRegister_negotiation_object() {
    }

    @E("que delete register_file_signed")
    public void queDeleteRegister_file_signed() {
    }

    @E("que delete register_file_publisher")
    public void queDeleteRegister_file_publisher() {
    }

    @E("que delete register_negotiation_action")
    public void queDeleteRegister_negotiation_action() {
    }

    @E("que delete negotiation_history_download_request")
    public void queDeleteNegotiation_history_download_request() {
    }

    @Quando("que delete negotiation")
    public void queDeleteNegotiation() {
    }

    @Então("o sistema confirma limpeza de negociações")
    public void oSistemaConfirmaLimpezaDeNegociações() {
    }

    @E("que feche a conexão")
    public void queFecheAConexão() {
    }
}
