package system.model;

import lombok.Data;

import java.util.Date;

@Data
public class TrelloCard {

    private Date data;
    private final String dataFormatada;
    private final String tag;
    private final String conteudo;
    private final String chave;

    public TrelloCard(String tag, String conteudo, String dataFormatada, Date data) {
        this.tag = tag;
        this.conteudo = conteudo;
        this.dataFormatada = dataFormatada;
        this.chave = dataFormatada + tag;
        this.data = data;
    }

    public TrelloCard(String tag, String conteudo) {
        this.tag = tag;
        this.conteudo = conteudo;
        this.dataFormatada = "Sem data:";
        this.chave = this.dataFormatada + tag;
    }
}
