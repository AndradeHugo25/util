package system.map;

public enum Urls {

    TRELLO_LOGIN("Trello login", "https://trello.com/pt-BR/login"),
    TRELLO_PARTE_URL_LOGADA("Trello login", "https://trello.com/u"),
    TRELLO_HUGO_BOARDS("Trello hugo boards", "https://trello.com/u/hugoandrade69/boards"),
    TRELLO_PERSONAL_TASKS("Trello personal", "https://trello.com/b/jmHpeD6Y/personal-tasks"),
    GMAIL_LOGIN("Gmail login", "https://mail.google.com/mail/u/0/#inbox");

    private final String descricao;
    private final String url;

    Urls(String descricao, String url) {
        this.descricao = descricao;
        this.url = url;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }

}
