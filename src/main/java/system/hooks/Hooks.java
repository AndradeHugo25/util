package system.hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import system.tools.configuration.PropertiesLoader;
import system.tools.configuration.Windows;
import system.tools.database.ConexaoBD;
import system.tools.web_interaction.CapturaDeTela;
import system.tools.web_interaction.DriverFactory;
import system.pages.GooglePages;
import system.pages.TrelloPages;
import system.tools.web_interaction.Web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Hooks {

    public static final String PATH_BASE_REPORT = "src/main/java/system/report/";
    public static final String PATH_DOWNLOADS = "src/main/java/system/downloads";

    public static String ambiente;
    public static boolean debug;
    public static boolean acessoBanco;
    public static boolean modoSeguranca;
    public static boolean limpezaEvidencias;
    public static Properties props;
    public static WebDriver driver;
    public static Web web;
    public static CapturaDeTela capturaDeTela;
    public static ConexaoBD conexaoBD;
    public static Map<String, Object> globais;

    private static int qtdCenarios;
    public static String nomeCenario;
    public static String pathReportCenario;

    public static String loginCenario;
    public static String senhaCenario;

    public static TrelloPages trelloPages;
    public static GooglePages googlePages;

    @BeforeAll
    public static void beforeAll() throws Exception {
        props = PropertiesLoader.inicializarProperties("config.properties");

        ambiente = props.getProperty("ambiente");
        debug = Boolean.parseBoolean(props.getProperty("debug"));
        acessoBanco = Boolean.parseBoolean(props.getProperty("acessoBanco"));
        modoSeguranca = Boolean.parseBoolean(props.getProperty("modoSeguranca"));
        limpezaEvidencias = Boolean.parseBoolean(props.getProperty("limpezaEvidencias"));
        if (limpezaEvidencias) {
            System.out.println("[WINDOWS] Deletando arquivos de report");
            Windows.excluirArquivosPastaExceto(PATH_BASE_REPORT, "Dummy");
        }
        if (acessoBanco) {
            conexaoBD = new ConexaoBD(props);
            conexaoBD.conectarTunel();
            conexaoBD.conectarBanco();
        }
        globais = new HashMap<>();

        driver = DriverFactory.incializarDriver(props, PATH_DOWNLOADS);
        web = new Web(driver);

        trelloPages = new TrelloPages(driver);
        googlePages = new GooglePages(driver);
    }

    @AfterAll
    public static void afterAll() throws Exception {
        if (!debug) {
            driver.quit();
            Windows.finalizarProcesso("chromedriver.exe");
            System.out.println("\n\n[DRIVER] Driver encerrado");
        }
        if (acessoBanco) {
            conexaoBD.desconectarBanco();
            conexaoBD.fecharTunel();
        }
    }

    @Before
    public void before(Scenario cenario) {
        qtdCenarios++;
        nomeCenario = cenario.getName();
        System.out.println("CENÁRIO " + qtdCenarios + " EM ANDAMENTO: " + nomeCenario);
        pathReportCenario = PATH_BASE_REPORT + nomeCenario + "/";
        capturaDeTela = new CapturaDeTela(driver, pathReportCenario);
        System.out.println("\t[WINDOWS] Deletando arquivos de download");
        Windows.excluirArquivosPasta(PATH_DOWNLOADS);
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
