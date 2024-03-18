#language: pt
#encoding: UTF-8

@loginGmail
Funcionalidade: Trello

  Contexto:
    Dado que o usuário esteja logado

  Cenário: Gerar tasks para fazer em casa
    E acesse o board "Personal Tasks"
    E capture a lista dos cards da coluna "To do at home"
    E agrupe os cards de maneira formatada
    Quando ele preparar o resultado
    Então o sistema envia resultado por email


