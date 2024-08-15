package system.core;

import org.openqa.selenium.WebElement;
import system.map.Urls;

import static system.hooks.Hooks.*;

public class TrelloLogin {

    public static void realizarLogin() {
        driver.get(Urls.TRELLO_LOGIN.getUrl());
        trelloPages.getContinuarGoogle().click();
        trelloPages.campoLogin.sendKeys(loginCenario);
        trelloPages.botaoAvancarLogin.click();
        trelloPages.getSenha().sendKeys(senhaCenario);
        WebElement next = trelloPages.getBotaoAvancarSenha();
        web.clickWithJSExecutor(next);
    }

    public static boolean isLogado() {
        String urlAtual = driver.getCurrentUrl();
        return urlAtual.contains(Urls.TRELLO_PARTE_URL_LOGADA.getUrl()) && !urlAtual.contains("login");
    }

}
