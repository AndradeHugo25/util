package system.tools.web_interaction;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;

public class Web {

    private final WebDriver driver;
    private static String xpathElemAtual;

    public Web(WebDriver driver) {
        this.driver = driver;
    }

    public void clickWithJSExecutor(WebElement elem) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elem);
    }

    public void moveMouseToElement(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
    }

    public void rolarScrollIntoView(WebElement elem) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", elem);
    }

    public void rolarScroll(int pixels, boolean praBaixo) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        if (!praBaixo) {
            pixels = pixels * -1;
        }
        jsExecutor.executeScript("window.scrollBy(0," + pixels + ")");
    }

    public void rolarScrollComElem(boolean praBaixo, WebElement elem) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(" + praBaixo + ");", elem);
    }

    public void rolarScrollExtraComElem(boolean praBaixo, WebElement elem) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(" + praBaixo + ");", elem);
        rolarScroll(120, true);
    }

    public void trocarAba(String parteUrlEsperada) {
        Set<String> janelas = driver.getWindowHandles();
        String urlAux = "";
        for (String janela : janelas) {
            driver.switchTo().window(janela);
            urlAux = driver.getCurrentUrl();
            if (urlAux.contains(parteUrlEsperada)) {
                break;
            }
        }
    }

    public void trocarAbaUrlIgualParametro(String urlEsperada) {
        Set<String> janelas = driver.getWindowHandles();
        String urlAux = "";
        for (String janela : janelas) {
            driver.switchTo().window(janela);
            urlAux = driver.getCurrentUrl();
            if (urlAux.equals(urlEsperada)) {
                break;
            }
        }
    }

    public void realizarUploadArquivo(String caminhoArquivo, int tempoEspera) throws InterruptedException, AWTException {
        ctrlCCtrlV(caminhoArquivo, tempoEspera);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void ctrlCCtrlV(String valor, int tempoEsperaEmMs) throws InterruptedException, AWTException {
        StringSelection ss = new StringSelection(valor);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
        Thread.sleep(tempoEsperaEmMs);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(tempoEsperaEmMs);
    }

    public void selecionarItemDaCombo(WebElement elemSelecao, String opcaoDesejada) throws Exception {
        int posicaoOpcaoNaLista = 0;
        int contador = 0;

        List<WebElement> opcoes = elemSelecao.findElements(By.tagName("option"));
        for (WebElement opcao : opcoes) {
            if (opcaoDesejada.equals(opcao.getText())) {
                posicaoOpcaoNaLista = contador;
                break;
            }
            contador++;

            if (contador >= opcoes.size()) {
                throw new Exception("Combo não contém opção " + opcaoDesejada + "!");
            }
        }

        Select selecao = new Select(elemSelecao);
        selecao.selectByIndex(posicaoOpcaoNaLista);
    }

    //TODO verificar se isso eh generico
    public String montarXpath(String xpathBaseTabela, String elemReferencia, int colunaElemDesejado) {
        encontrarLinhaDesejada(xpathBaseTabela, elemReferencia);
        String xpathMontado = xpathElemAtual.substring(0, xpathElemAtual.lastIndexOf('[') + 1) + colunaElemDesejado + "]";
        return xpathMontado;
    }

    //TODO verificar se isso eh generico
    public void encontrarLinhaDesejada(String xpathBaseTabela, String elemReferencia) {
        WebElement baseTable = driver.findElement(By.xpath(xpathBaseTabela));
        List<WebElement> linhas = baseTable.findElements(By.tagName("tr"));
        List<WebElement> colunas = baseTable.findElements(By.tagName("td"));

        int contadorLinha = 1;
        int contadorColuna = 1;
        if (linhas.size() < 2) {
            for (WebElement elem : colunas) {
                System.out.println(elem.getText());
                if (elem.getText().equals(elemReferencia)) {
                    xpathElemAtual = xpathBaseTabela + "/tr/td[" + contadorColuna + "]";
                    break;
                }
                contadorColuna++;
            }
        } else {
            int maxColuna = colunas.size() / linhas.size();
            for (WebElement elem : colunas) {
                if (elem.getText().equals(elemReferencia)) {
                    xpathElemAtual = xpathBaseTabela + "/tr[" + contadorLinha + "]/td[" + contadorColuna + "]";
                    break;
                }
                contadorColuna++;
                if (contadorColuna > maxColuna) {
                    contadorColuna = 1;
                    contadorLinha++;
                }
            }
        }
    }

    //==================METODOS DE INTERACAO COM O CAMPO====================================================

    //TODO verificar necessidade
    public void retryToClick(WebElement element) {
        int qtdMaxTentativas = 500;
        int qtdTentativas = 0;
        boolean clicou = false;

        while (!clicou && qtdTentativas <= qtdMaxTentativas) {
            try {
                element.click();
                clicou = true;
            } catch (Exception e) {
                System.out.println("Trying to click...");
                qtdTentativas++;
                if (qtdTentativas == qtdMaxTentativas) {
                    throw e;
                }
            }
        }
    }

    public void clickClearAndSendKeys(WebElement elem, String valor) {
        elem.click();
        elem.clear();
        elem.sendKeys(valor);
    }

    public void clickAndClearWithKeysChord(WebElement elem) {
        elem.click();
        elem.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    public void clickClearAndSendKeysWithKeysChord(WebElement elem, String valor) {
        clickAndClearWithKeysChord(elem);
        elem.sendKeys(valor);
    }

    public void clickClearSendKeysAndTabWithKeysChord(WebElement elem, String valor) {
        clickAndClearWithKeysChord(elem);
        elem.sendKeys(valor);
        elem.sendKeys(Keys.chord(Keys.TAB));
    }

    //    public static void sendKeysWithJquery(String tipodDoBy, String valorElem, String valorASerInputado) {
//        tipodDoBy = TratamentoDeDados.transformarPrimeiroCaractereParaMaiusculo(tipodDoBy);
//
//        String comando = "document.getElementBy" + tipodDoBy +"('" + valorElem + "').value = ''" + valorASerInputado + "';";
//        getJsExecutor().executeScript(comando);
//    }
//
//    public static void sendKeysWithJavaScriptAndTabWithKeysChord(String valor, WebElement elem) {
//        getJsExecutor().executeScript("arguments[0].value='"+ valor +"';", elem);
//        elem.sendKeys(Keys.chord(Keys.TAB));
//    }
//
//    public static void clearWithJquery(String tipodDoBy, String valorElem) {
//        sendKeysWithJquery(tipodDoBy, valorElem, "");
//    }
//
//    public static String waitAndGetText(String mapeamentoElemento, String tipoDoBy) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elemento = waitAndFind(mapeamentoElemento, tipoDoBy);
//        return elemento.getText();
//    }
//
//    public static void waitAndClick(String mapeamentoElemento, String tipoDoBy) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elemento = waitAndFind(mapeamentoElemento, tipoDoBy);
//        elemento.click();
//    }
//
//    public static void waitAndSendKeys(String mapeamentoElemento, String tipoDoBy, String valor) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elemento = waitAndFind(mapeamentoElemento, tipoDoBy);
//        elemento.sendKeys(valor);
//    }
//
//    public static void waitAndSubmit(String mapeamentoElemento, String tipoDoBy) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elemento = waitAndFind(mapeamentoElemento, tipoDoBy);
//        elemento.submit();
//    }
//
//    public static void waitAndClear(String mapeamentoElemento, String tipoDoBy) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elemento = waitAndFind(mapeamentoElemento, tipoDoBy);
//        elemento.clear();
//    }
//
//    //EXCLUIR METODOS APOS A APROVACAO DOS METODOS ABAIXO waitAndFind
//    public static WebElement waitAndFind(String mapeamentoElemento, String tipoDoBy) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elem = null;
//        wait = new WebDriverWait(getDriver(), 2);
//        elem = verificarTipoEEncontrarElemento(elem, mapeamentoElemento, tipoDoBy);
//        return elem;
//    }
//
//    public static WebElement waitAndFind(String mapeamentoElemento, String tipoDoBy, int segundos) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
//        WebElement elem = null;
//        wait = new WebDriverWait(getDriver(), segundos);
//        elem = verificarTipoEEncontrarElemento(elem, mapeamentoElemento, tipoDoBy);
//        return elem;
//    }
//    //------------------------------------------------------
//
////    public static WebElement waitAndFind(WebElement elemento) throws TipoDoByNaoEsperadoException, AcaoNaoEsperadaException {
////        WebElement elem = null;
////        wait = new WebDriverWait(getDriver(), 2);
////        elem = verificarTipoEEncontrarElemento(elem, mapeamentoElemento, tipoDoBy);
////        return elem;
////    }
////
////    public static WebElement waitAndFind(String mapeamentoElemento, String tipoDoBy, int segundos) {
////        WebElement elem = null;
////        wait = new WebDriverWait(getDriver(), segundos);
////        elem = verificarTipoEEncontrarElemento(elem, mapeamentoElemento, tipoDoBy);
////        return elem;
////    }
//
//
//    private static WebElement verificarTipoEEncontrarElemento(WebElement elem, String mapeamentoElemento, String tipoDoBy){
//        if (tipoDoBy.equalsIgnoreCase("xpath")) {
//            elem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(mapeamentoElemento)));
//        } else if (tipoDoBy.equalsIgnoreCase("id")) {
//            elem = wait.until(ExpectedConditions.elementToBeClickable(By.id(mapeamentoElemento)));
//        } else if (tipoDoBy.equalsIgnoreCase("name")) {
//            elem = wait.until(ExpectedConditions.elementToBeClickable(By.name(mapeamentoElemento)));
//        } else if (tipoDoBy.equalsIgnoreCase("cssSelector")) {
//            elem = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(mapeamentoElemento)));
//        }
//        return elem;
//    }
//
//
//    public static Set<String> getValoresCombo(WebElement elemSelecao) {
//        Set<String> valores = new HashSet<String>();
//        List<WebElement> opcoes = elemSelecao.findElements(By.tagName("option"));
//        for (WebElement opcao : opcoes) {
//            valores.add(opcao.getText());
//        }
//        return valores;
//    }
//
//    public static String trocarParaJanelaDoBoleto(WebDriver driver, String parteUrlEsperada){
//        Set<String> janelas = driver.getWindowHandles();
//        String urlAux = "";
//        String urlBoleto = "";
//        for(String janela : janelas){
//            driver.switchTo().window(janela);
//            urlAux = driver.getCurrentUrl();
//            if(urlAux.contains(parteUrlEsperada)){
//                urlBoleto = urlAux;
//                break;
//            }
//        }
//        return urlBoleto;
//    }
//
//    public int verificarPaginaContemTextos(WebDriver driver, String[] texto) {
//        int cont = 0;
//        for (int i = 0; i < texto.length; i++) {
//            if (!driver.getPageSource().contains(texto[i])){
//                //escrever texto no log
//                cont++;
//            }
//        }
//        return cont;
//    }
//
//    public static void validarTextosDaPagina(Set<String> textosDoSite, Set<String> textosEsperados) throws Exception {
//        ArrayList<String> arrayTextosEsperados = new ArrayList<String>();
//        ArrayList<String> arrayTextosDoSite = new ArrayList<String>();
//        arrayTextosEsperados.addAll(textosEsperados);
//        arrayTextosDoSite.addAll(textosDoSite);
//
//        int qtdTextosDiferentes = 0;
//        Set<Integer> posicoesJaVerificadas = new HashSet<Integer>();
//        if (textosDoSite.size() != textosEsperados.size()) {
//            throw new Exception("Quantidade de textos do site é diferente da quantidade de textos esperados!");
//        } else {
//            for (String textoSite : textosDoSite) {
//                if (!textosEsperados.contains(textoSite)) {
//                    if (!verificarAlgumTextoEsperadoEstaContidoNoTextoSite(arrayTextosEsperados, posicoesJaVerificadas, textoSite)) {
//                        getLog().escrever("DIFERENÇA DE TEXTO ENCONTRADA!");
//                        getLog().escreverLinhaSemDataEHora("TEXTO DO SITE DIFERENTE DO ESPERADO:\n\n" + textoSite, true);
//                        qtdTextosDiferentes++;
//                    }
//                } else {
//                    int pos = arrayTextosEsperados.indexOf(textoSite);
//                    posicoesJaVerificadas.add(pos);
//                }
//            }
//            if (qtdTextosDiferentes > 0){
//                getLog().escreverLinhaSemDataEHora("\n\nTEXTOS ESPERADOS: " + textosEsperados.toString(), true);
//                throw new TextoDiferenteException("");
//            }
//        }
//    }
//
//    public static boolean verificarAlgumTextoEsperadoEstaContidoNoTextoSite(ArrayList<String> arrayTextosEsperados,
//                                                                Set<Integer> posicoesJaVerificadas, String textoSite){
//        for (int i = 0; i < arrayTextosEsperados.size(); i++) {
//            if (!posicoesJaVerificadas.contains(i)) {
//                if (textoSite.contains(arrayTextosEsperados.get(i))) {
//                    return true;
//                }
//            }
//        } return false;
//    }
//
//
//    //================NOVOS METODOS WAIT============================================================================
//    public static WebElement tryToFindElement(Object webElement) throws Exception {
//        WebElement elem = (WebElement) webElement;
//        boolean achou = false;
//        int cont = 0;
//        while (!achou) {
//            try {
//                if (cont < 250) {
//                    elem.isEnabled();
//                } else if (cont < 500) {
//                    elem.isDisplayed();
//                } else if (cont < 750) {
//                    elem.getText();
//                }
//                achou = true;
//            } catch (Exception e) {
//                cont++;
//                if (cont > 750) {
//                    throw new Exception("Não foi possível encontrar o elemento! Página pode estar lenta");
//                }
//            }
//        } return elem;
//    }
//
//
//    public static WebElement waitAndTryToFindElement(Object webElement) throws Exception {
//        WebElement elem = (WebElement) webElement;
//        WebDriverWait wait = new WebDriverWait(getDriver(), 2);
//        boolean achou = false;
//        int cont = 1;
//        while (!achou) {
//            try {
//                if (cont <= 2) {
//                    wait.until(ExpectedConditions.visibilityOf(elem));
//                } else if (cont <= 4) {
//                    wait.until(ExpectedConditions.elementToBeClickable(elem));
//                } else {
//                    wait.until(ExpectedConditions.elementToBeSelected(elem));
//                }
//                achou = true;
//            } catch (Exception e) {
//                System.out.println("Ainda não achou o elemento a ser printado...");
//                cont++;
//                if (cont > 6) {
//                    throw new Exception("Não foi possível encontrar o elemento! Página pode estar lenta");
//                }
//            }
//        }
//        return elem;
//    }
//
//
//    public static String waitAndGetText(WebElement elemento) throws Exception {
//        WebElement elem = tryToFindElement(elemento);
//        return elem.getText();
//    }
//
//    public static void waitAndSendKeys(WebElement elemento, String texto) throws Exception {
//        WebElement elem = tryToFindElement(elemento);
//        elem.sendKeys(texto);
//    }
//
//    public static void waitAndClick(WebElement elemento) throws Exception {
//        WebElement elem = tryToFindElement(elemento);
//        elem.click();
//    }
//
//    public static void waitAndSelecionarItemCombo(WebElement elementoCombo, String opcaoDesejada) throws Exception {
//        WebElement elem = tryToFindElement(elementoCombo);
//        selecionarItemDaCombo(elem, opcaoDesejada);
//    }

}
