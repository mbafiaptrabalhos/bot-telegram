# bot-telegram
Bot para realização de pedidos na FIAP Food, desenvolvido para avaliação da disciplina de Java Platform.

## Integrantes

- Caroline Nascimento de Oliveira - 337401
- Harold Isaac Campero Stoffers Murillo - 337233
- Jonathas da Cruz Garcia - 338900
- Renato Santana Brito - 337364
- Victor Tokudo Kiam - 337385

## Tecnologias e API's

* [Java 11]
* [Maven]
* [Java Telegram BOT API]
* [API Climatempo]

## Uso
- Utilize o BotFather para criação do seu bot e geração do Token.
- Preencher variável TOKEN_TELEGRAM da classe TesteBot com o token gerado pelo BotFather.

## Funcionalidades

- Cadastro de pedidos para entrega ou retirada.
- Visualização das informações do cardápio disponível.
- Acesso a comanda com informações gerais sobre o pedido.
- Informações de clima por cidade ou resumo do clima no Brasil.

## Comandos

### Cadastro de pedidos:
- **/cardapio**
> Obtém informações de comidas e bebidas disponíveis, e seus respectivos comandos para adicionar a comanda do usuário.

- **/frango**
> Adiciona frango ao pedido do usuário.

- **/carne**
> Adiciona carne ao pedido do usuário.

- **/cocacola**
>Adiciona coca-cola ao pedido do usuário.

- **/agua**
>Adiciona água ao pedido do usuário.

- **/comanda**
>Obtém informações dos produtos adicionados à comanda, e o valor total.

- **/retirar**
>Obtém código do pedido para retirada no restaurante.

### Obter informações sobre clima:

- **/clima**
> Bot solicita nome da cidade para recuperar informações de clima/temperatura.

### Outros comandos:

- **/start**
> Inicia iteração com o Bot

- **/help**
>Obtém comandos disponíveis para interação com o bot.

   [Java 11]: <https://www.oracle.com/java/technologies/javase-jdk11-downloads.html>
   [Maven]: <https://maven.apache.org/>
   [Java Telegram BOT Api]: <https://github.com/pengrad/java-telegram-bot-api>
   [API Climatempo]: <https://advisor.climatempo.com.br/>



