package system.tools.web_interaction;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import system.tools.data_manipulation.DataEHora;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class CapturaDeTela {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String pathReportCenario;

    //os métodos são estáticos, mas é necessário ter um construtor para os métodos que utilizam o wait
    public CapturaDeTela(WebDriver driver, String pathReportCenario) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.pathReportCenario = pathReportCenario;
    }

    //printar tela sem nenhuma condição
    public void printarTela() throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String data = DataEHora.getDataAtualFormatada("yyyy_MM_dd HH-mm-ss");

        FileUtils.copyFile(scrFile, new File(pathReportCenario + data + ".png"));
    }

    //printar tela esperando um tempo determinado passado como parametro
    public void printarTela(long tempoEmMiliSeg) throws IOException, InterruptedException {
        Thread.sleep(tempoEmMiliSeg);
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String data = DataEHora.getDataAtualFormatada("yyyy_MM_dd HH-mm-ss");

        FileUtils.copyFile(scrFile, new File(pathReportCenario + data + ".png"));
    }

    //TODO implementar printar tela esperando um elemento passado como parâmetro aparecer
    public void printarTela(String valorElemento, String tipoDoBy) throws IOException, InterruptedException {
        if (tipoDoBy.equals("id")) {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(valorElemento)));
        } else if (tipoDoBy.equals("xpath")) {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(valorElemento)));
        } else if (tipoDoBy.equals("name")) {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(valorElemento)));
        } else if (tipoDoBy.equals("cssSelector")) {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(valorElemento)));
        }

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String data = DataEHora.getDataAtualFormatada("yyyy_MM_dd HH-mm-ss");

        FileUtils.copyFile(scrFile, new File(pathReportCenario + data + ".png"));
    }

}
