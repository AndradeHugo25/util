package system.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    public static final String MAP_TOAST = "notistack-snackbar";
    public WebDriver driver;
    private WebDriverWait wait;

    private static Integer aberturasModal = 0;
    private static Integer aberturasData = 0;

    public BasePage(WebDriver webDriver) {
        driver = webDriver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @FindBy(how = How.XPATH, using = "/html/body/app-root/section/nb-layout")
    public WebElement pagina;
    @FindBy(how = How.ID, using = MAP_TOAST)
    public WebElement toast;

    public void clearAndSendKeys(WebElement element, String valor) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
        element.sendKeys(valor);
    }

    public void justWait(How how, String mapElement) {
        switch (how) {
            case ID -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(mapElement)));
            case NAME -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(mapElement)));
            case XPATH -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(mapElement)));
            case LINK_TEXT -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(mapElement)));
            case PARTIAL_LINK_TEXT -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(mapElement)));
            case CLASS_NAME -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(mapElement)));
            case CSS -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(mapElement)));
        }
    }

    public void justWait(How how, String mapElement, Integer segundos) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(segundos));
        switch (how) {
            case ID -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(mapElement)));
            case NAME -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(mapElement)));
            case XPATH -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(mapElement)));
            case LINK_TEXT -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(mapElement)));
            case PARTIAL_LINK_TEXT -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(mapElement)));
            case CLASS_NAME -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(mapElement)));
            case CSS -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(mapElement)));
        }
    }

    public void justWaitClickable(How how, String mapElement) {
        switch (how) {
            case ID -> wait.until(ExpectedConditions.elementToBeClickable(By.id(mapElement)));
            case NAME -> wait.until(ExpectedConditions.elementToBeClickable(By.name(mapElement)));
            case XPATH -> wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mapElement)));
            case LINK_TEXT -> wait.until(ExpectedConditions.elementToBeClickable(By.linkText(mapElement)));
            case PARTIAL_LINK_TEXT -> wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(mapElement)));
            case CLASS_NAME -> wait.until(ExpectedConditions.elementToBeClickable(By.className(mapElement)));
            case CSS -> wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(mapElement)));
        }
    }

    public void waitAndClick(WebElement element, How how, String mapElement) {
        switch (how) {
            case ID -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(mapElement)));
            case NAME -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(mapElement)));
            case XPATH -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(mapElement)));
            case LINK_TEXT -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(mapElement)));
            case PARTIAL_LINK_TEXT -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(mapElement)));
            case CLASS_NAME -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(mapElement)));
            case CSS -> wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(mapElement)));
        }
        element.click();
    }

    public WebElement getElemBotaoArquivo(WebDriver driver, String tipoArquivos, String arquivo, String modulo) {
        final String XPATH = "/html/body/app-root/section/nb-layout/div/div/div/div/div/nb-layout-column/" +
                "app-card/div/app-MODULO-overview/div[1]/app-MODULO-TIPO_ARQUIVOS/div/div/ul/li[POSICAO]/app-MODULO-uploader/div";
        String xpathFinal = XPATH.replace("POSICAO", getPosicao(arquivo)).replace("MODULO", modulo)
                .replace("TIPO_ARQUIVOS", tipoArquivos).replace("sga-overview", "overview")
                .replace("sga-config", "config").replace("sga-monthly-files", "monthly-files");
        WebElement element = driver.findElement(By.xpath(xpathFinal));
        return element;
    }

    public WebElement getElemDataUploadArquivo(WebDriver driver, String tipoArquivos, String arquivo, String modulo) {
        final String XPATH = "/html/body/app-root/section/nb-layout/div[1]/div/div/div/div/nb-layout-column/" +
                "app-card/div/app-MODULO-overview/div[1]/app-MODULO-TIPO_ARQUIVOS/div/div/ul/li[POSICAO]/app-MODULO-uploader/div/p[2]";
        String xpathFinal = XPATH.replace("POSICAO", getPosicao(arquivo)).replace("MODULO", modulo)
                .replace("TIPO_ARQUIVOS", tipoArquivos).replace("sga-overview", "overview")
                .replace("sga-config", "config").replace("sga-monthly-files", "monthly-files");
        WebElement element = driver.findElement(By.xpath(xpathFinal));
        return element;
    }

    private String getPosicao(String arquivo) {
        String posicao = switch (arquivo) {
            case "G&A Configuration", "Segment Report" -> "1";
            case "G&A Template Base", "G&A SAP EMB" -> "2";
            case "G&A Template FollowUp", "G&A SAP MLB" -> "3";
//            case "G&A Template FollowUp", "G&A SAP YAB" -> "3";
//            case "G&A BUs Configuration", "G&A SAP MLB" -> "4";
            case "G&A BUs Configuration" -> "4";
            case "G&A BUs Template Commercial" -> "5";
            case "G&A BUs Template Executive" -> "6";
            case "G&A BUs Template Defense" -> "7";
            case "G&A BUs Template Services" -> "8";
            case "G&A BUs Template Others" -> "9";
            default -> "";
        };
        return posicao;
    }

    public WebElement getElemAreaUploadArquivo(WebDriver driver, String modulo) {
        final String XPATH = "//*[@id=\"mat-dialog-ABERTURAS_MODAL\"]/MODULO-uploader-dialog/div/div[2]/div/div";
        String xpath = XPATH.replace("ABERTURAS_MODAL", String.valueOf(aberturasModal)).replace("MODULO", modulo);
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public WebElement getElemItemListaArquivos(WebDriver driver, String modulo) {
        final String XPATH = "//*[@id=\"mat-dialog-ABERTURAS_MODAL\"]/MODULO-uploader-dialog/div/div[2]/div/app-file-item/div/ul/li";
        String xpath = XPATH.replace("ABERTURAS_MODAL", String.valueOf(aberturasModal)).replace("MODULO", modulo);
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public WebElement getElemBotaoOk(WebDriver driver, String modulo) {
        final String XPATH = "//*[@id=\"mat-dialog-ABERTURAS_MODAL\"]/MODULO-uploader-dialog/div/div[3]/app-button/button";
        String xpath = XPATH.replace("ABERTURAS_MODAL", String.valueOf(aberturasModal)).replace("MODULO", modulo);
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public WebElement getElemBotaoData(WebDriver driver, String modulo) {
        final String XPATH = "/html/body/app-root/section/nb-layout/div[1]/div/div/div/div/nb-layout-column/app-card/" +
                "div/app-MODULO-overview/div[1]/app-MODULO-monthly-files/div/app-date-picker/div/mat-datepicker-toggle/button";
        String xpathFinal = XPATH.replace("MODULO", modulo);
        WebElement element = driver.findElement(By.xpath(xpathFinal));
        return element;
    }

    public WebElement getElemAno(WebDriver driver) {
        final String XPATH = "//*[@id=\"mat-datepicker-ABERTURAS_DATA\"]/div/mat-multi-year-view/table/tbody/tr[6]/td[4]/div[1]";
        String xpath = XPATH.replace("ABERTURAS_DATA", String.valueOf(aberturasData));
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public WebElement getElemMes(WebDriver driver) {
        final String XPATH = "//*[@id=\"mat-datepicker-ABERTURAS_DATA\"]/div/mat-year-view/table/tbody/tr[2]/td[1]/div[1]";
        String xpath = XPATH.replace("ABERTURAS_DATA", String.valueOf(aberturasData));
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public WebElement getElemMsgExcecao(WebDriver driver, String modulo) {
        final String XPATH = "//*[@id=\"mat-dialog-ABERTURAS_MODAL\"]/MODULO-uploader-dialog/app-messenger-modal/div/app-card/div/div[2]";
        String xpath = XPATH.replace("ABERTURAS_MODAL", String.valueOf(aberturasModal));
        xpath = xpath.replace("MODULO", String.valueOf(modulo));
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public WebElement getElemIconeFecharExcecao(WebDriver driver, String modulo) {
        final String XPATH = "//*[@id=\"mat-dialog-ABERTURAS_MODAL\"]/MODULO-uploader-dialog/app-messenger-modal/div/app-card/div/div[2]/div[1]/mat-icon";
        String xpath = XPATH.replace("ABERTURAS_MODAL", String.valueOf(aberturasModal));
        xpath = xpath.replace("MODULO", String.valueOf(modulo));
        WebElement element = driver.findElement(By.xpath(xpath));
        return element;
    }

    public void addContagemAberturaModal() {
        aberturasModal++;
    }

    public void addContagemAberturaData() {
        aberturasData++;
    }

}
