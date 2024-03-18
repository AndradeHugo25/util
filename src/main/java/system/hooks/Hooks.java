package system.hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import system.tools.configuration.PropertiesLoader;
import system.tools.database.ConexaoBD;
import system.tools.web_interaction.CapturaDeTela;
import system.tools.web_interaction.DriverFactory;
import system.pages.GooglePages;
import system.pages.TrelloPages;
import system.tools.web_interaction.Web;

import java.io.IOException;
import java.util.Properties;

public class Hooks {

    public static final String PATH_BASE_REPORT = "src/main/java/system/report/";
    public static final String PATH_DOWNLOADS = "src/main/java/system/downloads";

    public static Properties props;
    public static WebDriver driver;
    public static Web web;
    public static CapturaDeTela capturaDeTela;
    public static ConexaoBD conexaoBD;

    private static int qtdCenarios;
    public static String nomeCenario;
    public static String pathReportCenario;

    public static String loginCenario;
    public static String senhaCenario;

    public static TrelloPages trelloPages;
    public static GooglePages googlePages;

    @BeforeAll
    public static void beforeAll() throws IOException {
        props = PropertiesLoader.inicializarProperties("config.properties");
        driver = DriverFactory.incializarDriver(props, PATH_DOWNLOADS);
        web = new Web(driver);
        conexaoBD = new ConexaoBD(props);
        //conexaoBD.conectarBanco();

        trelloPages = new TrelloPages(driver);
        googlePages = new GooglePages(driver);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        driver.quit();
        //conexaoBD.desconectarBanco();
        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
    }

    @Before
    public void before(Scenario cenario) {
        qtdCenarios++;
        nomeCenario = cenario.getName();
        System.out.println("CENÁRIO " + qtdCenarios + " EM ANDAMENTO: " + nomeCenario);
        pathReportCenario = PATH_BASE_REPORT + nomeCenario + "/";
        capturaDeTela = new CapturaDeTela(driver, pathReportCenario);
        //ComparadorDeDados.textosSite = new ArrayList<>();
    }

    @Before("@loginGmail")
    public void before() {
        if (loginCenario == null) {
            loginCenario = props.getProperty("googleLogin");
            senhaCenario = props.getProperty("googleSenha");
        }
    }

}
