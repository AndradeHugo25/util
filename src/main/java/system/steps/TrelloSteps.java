package system.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import system.map.Urls;
import system.model.TrelloGrupo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static system.hooks.Hooks.*;
import static system.core.TrelloCore.*;

public class TrelloSteps {

    @Dado("que o usuário esteja logado")
    public void logar() {
        driver.get(Urls.TRELLO_LOGIN.getUrl());
        trelloPages.botaoContinuarGoogle.click();
        trelloPages.campoLogin.sendKeys(loginCenario);
        trelloPages.botaoAvancarLogin.click();
        trelloPages.getSenha().sendKeys(senhaCenario);
        WebElement next = trelloPages.getBotaoAvancarSenha();
        web.clickWithJSExecutor(next);
    }

    @Dado("acesse o board {string}")
    public void acessarBoard(String tituloBoard) {
        WebElement board = trelloPages.getBoard(tituloBoard);
        board.click();
    }

    @E("capture a lista dos cards da coluna {string}")
    public void capturarListaCards(String coluna) {
        trelloPages.getPrimeiraTag(coluna).click();
        listaNaoFormatada = trelloPages.getListaToDoAtHome();
    }

    @E("agrupe os cards de maneira formatada")
    public void agruparCardsFormatados() throws ParseException {
        salvarGrupos(listaNaoFormatada);
    }

    @Quando("ele preparar o resultado")
    public void prepararResultado() {
        for (Date dataSalva : datas) {
            String data = formatoSemAno.format(dataSalva);

            ArrayList<TrelloGrupo> exatas = new ArrayList<>();
            ArrayList<TrelloGrupo> aPartirDe = new ArrayList<>();

            Map<String, ArrayList<TrelloGrupo>> map = new HashMap<>();

            for (TrelloGrupo trelloGrupo : new ArrayList<>(TRELLO_GRUPOS)) {
                if (trelloGrupo.getChave().contains(data)) {
                    if (trelloGrupo.getChave().contains("De:")) {
                        String dataGrupoAtual = trelloGrupo.getDataFormatada();
                        if (map.containsKey(dataGrupoAtual)) {
                            map.get(dataGrupoAtual).add(trelloGrupo);
                        } else if (!map.containsKey(dataGrupoAtual)) {
                            ArrayList<TrelloGrupo> deAte = new ArrayList<>();
                            deAte.add(trelloGrupo);
                            map.put(dataGrupoAtual, deAte);
                        }
                    } else if (trelloGrupo.getChave().contains("partir")) {
                        aPartirDe.add(trelloGrupo);
                    } else {
                        exatas.add(trelloGrupo);
                    }
                    TRELLO_GRUPOS.remove(trelloGrupo);
                }
            }
            addAoResultado(exatas);
            for (ArrayList<TrelloGrupo> deAte : map.values()) {
                addAoResultado(deAte);
            }
            addAoResultado(aPartirDe);
        }
        addAoResultado(TRELLO_GRUPOS);
    }

    @Então("o sistema envia resultado por email")
    public void enviarPorEmail() {
        driver.get(Urls.GMAIL_LOGIN.getUrl());
        googlePages.botaoEscrever.click();
        googlePages.getCampoPara().sendKeys("andradehugouff@gmail.com");
        googlePages.campoAssunto.sendKeys("Tarefas do Trello - To do at home");
        googlePages.getCampoCorpoEmail().click();
        preencherCorpoEmailFormatado();
        googlePages.getBotaoEnviar().click();

        String msg;
        do {
            msg = googlePages.getMsgSucesso().getText();
        } while (msg.contains("Enviando…"));
    }

}
