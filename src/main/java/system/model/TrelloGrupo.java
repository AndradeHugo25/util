package system.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class TrelloGrupo {

    private final Date data;
    private final String dataFormatada;
    private final String tag;
    private final String chave;
    private final ArrayList<TrelloCard> trelloCards = new ArrayList<>();

    public TrelloGrupo(Date data, String dataFormatada, String tag) {
        this.data = data;
        this.dataFormatada = dataFormatada;
        this.tag = tag;
        this.chave = dataFormatada + tag;
    }

    public void addCard(TrelloCard trelloCard) {
        trelloCards.add(trelloCard);
    }

}
