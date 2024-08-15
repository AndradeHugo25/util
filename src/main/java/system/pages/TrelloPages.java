package system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import system.tools.web_interaction.Web;

import java.util.ArrayList;

import static system.hooks.Hooks.web;

public class TrelloPages extends BasePage {

    public TrelloPages(WebDriver driver) {
        super(driver);
    }

    //LOGIN
    @FindBy(how = How.ID, using = "identifierId")
    public WebElement campoLogin;
    @FindBy(how = How.ID, using = "identifierNext")
    public WebElement botaoAvancarLogin;

    public WebElement getContinuarGoogle() {
        final String MAP = "google-auth-button";
        justWait(How.ID, MAP);
        return driver.findElement(By.id(MAP));
    }

    public WebElement getSenha() {
        justWait(How.NAME, "Passwd");
        return driver.findElement(By.name("Passwd"));
    }

    public WebElement getBotaoAvancarSenha() {
        justWaitClickable(How.ID, "passwordNext");
        return driver.findElement(By.id("passwordNext"));
    }

    public WebElement getBoard(String tituloBoard) {
        String xpath = "//*[@id=\"content\"]/div/div[2]/div/div/div/div/div/div/div/div[3]/div/div[2]/ul/li[POSICAO]/a/div";
        WebElement elem = null;
        int cont = 1;
        boolean notFinished = true;

        while (notFinished) {
            try {
                String xpathAtual = xpath.replace("POSICAO", String.valueOf(cont));
                justWait(How.XPATH, xpathAtual);
                elem = driver.findElement(By.xpath(xpathAtual));
                String tituloAtual = elem.getText();
                if (tituloAtual.equals(tituloBoard)) {
                    break;
                }
            } catch (Exception e) {
                notFinished = false;
            }
            cont++;
        }

        return elem;
    }

    public WebElement getTask(String titulo) throws Exception {
        WebElement elem;
        final String map = "//*[@id=\"board\"]/li[1]/div/ol/li[POSICAO]/div/div[2]";
        int pos = 1;
        do {
            String mapFinal = map.replace("POSICAO", String.valueOf(pos));
            Thread.sleep(2000);
            try {
                elem = driver.findElement(By.xpath(mapFinal));
                String tituloAtual = elem.getText().toLowerCase();
                if (tituloAtual.contains(titulo.toLowerCase())) {
                    return elem;
                } else {
                    pos++;
                }
            } catch (Exception e) {
                throw new Exception("Não achou task com título: " + titulo);
            }
        } while (true);
    }

    public WebElement getPrimeiraTag(String coluna) {
        int index;
        switch (coluna) {
            case "To do at home" -> index = 3;
            case "To do outside" -> index = 4;
            default -> index = -1;
        }
        final String MAP_PRIMEIRA_TAG = "//*[@id=\"board\"]/li[" + index + "]/div/ol/li[1]/div/div/div[1]/div/span" ;
        justWait(How.XPATH, MAP_PRIMEIRA_TAG);
        return driver.findElement(By.xpath(MAP_PRIMEIRA_TAG));
    }

    public ArrayList<String> getListaToDoAtHome() {
        String xpath = "//*[@id=\"board\"]/li[3]/div/ol/li[POSICAO]/div/div";
        ArrayList<String> lista = new ArrayList<>();
        boolean notFinished = true;
        WebElement elem;
        int cont = 1;

        while (notFinished) {
            try {
                String xpathAtual = xpath.replace("POSICAO", String.valueOf(cont));
                if (cont == 1) {
                    justWait(How.XPATH, xpathAtual);
                } else {
                    justWait(How.XPATH, xpathAtual, 2);
                }
                elem = driver.findElement(By.xpath(xpathAtual));
                web.rolarScrollIntoView(elem);
                String conteudoCard = elem.getText();
                lista.add(conteudoCard);
//                System.out.println("Conteúdo: " + conteudoCard);
            } catch (Exception e) {
                notFinished = false;
            }
            cont++;
        }

        return lista;
    }
}
