package system.tools.web_interaction;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Properties;

public class DriverFactory {

    public static WebDriver incializarDriver(Properties props, String pathDownloadDireto) {
        boolean headless = Boolean.parseBoolean(props.getProperty("headless"));
        boolean downloadDireto = Boolean.parseBoolean(props.getProperty("downloadDireto"));

        WebDriver driver;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");

        if (headless) {
            options.addArguments("--headless");
        }

        if (downloadDireto) {
            //TODO verificar se precisa criar um diretorio ou se tacar o diretorio direto ja dá certo
//            ManipuladorArquivo.criarDiretorio(pathDownloads);
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.default_directory", pathDownloadDireto);
            chromePrefs.put("plugins.always_open_pdf_externally", true);
            options.setExperimentalOption("prefs", chromePrefs);
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        return driver;
    }

    public static void desabilitarCaptchaCookie(WebDriver driver) {
        Cookie ck = new Cookie("captcha", "off");
        driver.manage().addCookie(ck);
    }

}
