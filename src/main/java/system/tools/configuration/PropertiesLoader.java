package system.tools.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties inicializarProperties(String nomeArquivoProperties) throws IOException {
        Properties props = new Properties();
        InputStream resource = PropertiesLoader.class.getClassLoader().getResourceAsStream(nomeArquivoProperties);
        props.load(new InputStreamReader(resource, StandardCharsets.UTF_8));
        return props;
    }

}
