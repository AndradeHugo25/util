package system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class GooglePages extends BasePage {

    public GooglePages(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.XPATH, using = "/html/body/div[7]/div[3]/div/div[2]/div[1]/div[1]/div/div")
    public WebElement botaoEscrever;
    @FindBy(how = How.NAME, using = "subjectbox")
    public WebElement campoAssunto;
    @FindBy(how = How.CSS, using = "#\\:v7 > div > div > div.J-J5-Ji.J-Z-M-I-Jm > div")
    public WebElement setaExpandirEditor;
    @FindBy(how = How.CSS, using = "#\\:v3 > div > div > div")
    public WebElement maiorTab;
    @FindBy(how = How.CSS, using = "#\\:v2 > div > div > div")
    public WebElement menorTab;
    @FindBy(how = How.CSS, using = "#\\:uv > div > div > div")
    public WebElement negrito;

    public WebElement getCampoPara() {
        justWait(How.CSS, "#\\:vm");
        return driver.findElement(By.cssSelector("#\\:vm"));
    }

    public WebElement getCampoCorpoEmail() {
        justWait(How.CSS, "#\\:t8");
        return driver.findElement(By.cssSelector("#\\:t8"));
    }

    public WebElement getBotaoEnviar() {
        justWait(How.CSS, "#\\:ro");
        return driver.findElement(By.cssSelector("#\\:ro"));
    }

    public WebElement getMsgSucesso() {
        justWaitClickable(How.CSS, "#\\:b > div > div > div.vh > span > span.bAq");
        return driver.findElement(By.cssSelector("#\\:b > div > div > div.vh > span > span.bAq"));
    }

}
