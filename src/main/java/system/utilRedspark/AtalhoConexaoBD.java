package system.utilRedspark;

import org.openqa.selenium.WebDriver;
import system.tools.configuration.PropertiesLoader;
import system.tools.database.ConexaoBD;
import system.tools.web_interaction.Web;

import java.util.Properties;

public class AtalhoConexaoBD {

    public static String ambiente;
    public static boolean acessoBanco;
    public static Properties props;
    public static WebDriver driver;
    public static Web web;
    public static ConexaoBD conexaoBD;

    public static void main(String[] args) throws Exception {
        conectar();
//        desconectar();
    }

    public static void conectar() throws Exception {
        props = PropertiesLoader.inicializarProperties("config.properties");

        ambiente = props.getProperty("ambiente");
        acessoBanco = Boolean.parseBoolean(props.getProperty("acessoBanco"));

        if (acessoBanco) {
            conexaoBD = new ConexaoBD(props);
            conexaoBD.conectarTunel();
            conexaoBD.conectarBanco();
        }
    }

    public static void desconectar() throws Exception {
        if (acessoBanco) {
            conexaoBD.desconectarBanco();
            conexaoBD.fecharTunel();
        }
    }

}
