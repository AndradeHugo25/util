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
    @FindBy(how = How.XPATH, using = "//*[@id=\":v5\"]/div/div/div[1]/div")
    public WebElement setaExpandirEditor;
    @FindBy(how = How.XPATH, using = "//*[@id=\":v1\"]/div/div/div")
    public WebElement maiorTab;
    @FindBy(how = How.XPATH, using = "//*[@id=\":v0\"]/div/div/div")
    public WebElement menorTab;
    @FindBy(how = How.XPATH, using = "//*[@id=\":ut\"]/div/div/div")
    public WebElement negrito;

    public WebElement getCampoPara() {
        justWait(How.XPATH, "//*[@id=\":vk\"]");
        return driver.findElement(By.xpath("//*[@id=\":vk\"]"));
    }

    public WebElement getCampoCorpoEmail() {
        justWait(How.XPATH, "//*[@id=\":t6\"]");
        return driver.findElement(By.xpath("//*[@id=\":t6\"]"));
    }

    public WebElement getBotaoEnviar() {
        justWait(How.XPATH, "//*[@id=\":rm\"]");
        return driver.findElement(By.xpath("//*[@id=\":rm\"]"));
    }

    public WebElement getMsgSucesso() {
        justWaitClickable(How.XPATH, "//*[@id=\":b\"]/div/div/div[2]");
        return driver.findElement(By.xpath("//*[@id=\":b\"]/div/div/div[2]"));
    }

}
