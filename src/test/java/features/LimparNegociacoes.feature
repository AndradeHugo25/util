#language: pt
#encoding: UTF-8

Funcionalidade: Limpeza de massa

#  TODO só poderá ser concluído no retorno das férias para não deletar massa que eles estão usando
  Cenário: Limpar negociações por data
    Dado que acesse o banco de dados do PNLD
    E que set a data "2024-08-01"
    E que delete round_detail
    E que delete round_document
    E que delete round_publisher_item
    E que delete publisher_round_action
    E que delete round
    E que delete negotiation_edital_object_item
    E que delete negotiation_edital_object
    E que delete negotiation_justification_register
    E que delete negotiation_justification
    E que delete register_negotiation_object
    E que delete register_file_signed
    E que delete register_file_publisher
    E que delete register_negotiation_object
    E que delete register_negotiation_action
    E que delete negotiation_history_download_request
    Quando que delete negotiation
    Então o sistema confirma limpeza de negociações
    E que feche a conexão

