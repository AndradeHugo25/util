package system.tools.web_interaction;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import system.tools.configuration.Windows;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

public class DriverFactory {

    public static WebDriver incializarDriver(Properties props, String pathDownloadDireto) {
        System.out.println("[DRIVER] Inicializando driver...");

        boolean headless = Boolean.parseBoolean(props.getProperty("headless"));
        boolean downloadDireto = Boolean.parseBoolean(props.getProperty("downloadDireto"));

        WebDriver driver;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--enable-javascript");
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
            File file = Windows.criarDiretorio(pathDownloadDireto);
            System.out.println("[WINDOWS] Diretório de downloads criado");
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.default_directory", file.getAbsolutePath());
            chromePrefs.put("download.prompt_for_download", false);
            chromePrefs.put("download.directory_upgrade", true);
            options.setExperimentalOption("prefs", chromePrefs);
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        System.out.println("[DRIVER] Driver inicializado\n\n");

        return driver;
    }

    public static void desabilitarCaptchaCookie(WebDriver driver) {
        Cookie ck = new Cookie("captcha", "off");
        driver.manage().addCookie(ck);
    }

}
