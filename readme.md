# SWAGGER-JAVA-FIX-API

### Introdução

Este projeto simples emula um servidor FIX localmente através do java utilizando springboot e no mesmo dispõe uma conexão FIX client para disparo no mesmo servidor através das documentações do swagger.

Isto não é útil para o dia a dia mas com certeza te dará apoio para tarefas relacionadas a trâmites de mensageria financeira.

## Processo de build padrão

````bash
mvn clean install
````

## Executar a aplicação

````bash
mvn spring-boot:run
````

### URLs de acesso
- Banco h2: http://localhost:8080/h2-console
- Documentação: http://localhost:8080/swagger-ui/index.html

### Métodos

1. POST (http://localhost:8080/api/fix/send?price=500&qty=100&symbol=SOL&CID=54321)
````bash
curl -X 'POST' \
  'http://localhost:8080/api/fix/send?price=500&qty=100&symbol=SOL&CID=54321' \
  -H 'accept: */*' \
  -d ''
````
Isto permite a enviar uma mensagem FIX, o servidor primeiro escuta o protocolo Http simples a partir do controller e em seguida utiliza o método de envio FIX, por padrão o servidor só ativa o FIX client na primeira consulta realizada, nas demais ela será ignorada.

O servidor FIX escutará a mensagem enviada e salvará no banco H2, isso só é permitido no mesmo servidor porque na pasta resources existem dois arquivos de configuração (client.cfg (usado para disparo)) e (server.cfg (usado para recebimento)). Mude esses pacotes para estabelecer outras conexões.

2. GET (http://localhost:8080/api/fix/get-all)

````bash
curl -X 'GET' \
  'http://localhost:8080/api/fix/get-all' \
  -H 'accept: */*'
````

````json
[
  {
    "id": 1,
    "beginString": "FIX.4.4",
    "bodyLength": 100,
    "msgType": "D",
    "seqNum": 593,
    "senderCompID": "CLIENT",
    "sendingTime": "2025-01-16T22:37:37.538",
    "targetCompID": "SERVER",
    "clOrdID": "54321",
    "orderQty": 100,
    "ordType": 2,
    "price": 500,
    "side": 1,
    "symbol": "SOL",
    "transactTime": "2025-01-16T22:37:37.538",
    "checksum": "000",
    "bruteFixMsg": "8=FIX.4.4\u00019=122\u000135=D\u000134=593\u000149=CLIENT\u000152=20250116-22:37:37.538\u000156=SERVER\u000111=54321\u000138=100\u000140=2\u000144=500\u000154=1\u000155=SOL\u000160=20250116-22:37:37.538\u000110=086\u0001"
  }
]
````

Perceba que o que é na realidade o protocolo FIX é a "bruteFixMsg" o restante dos dados é apenas uma orquestração do que chega no nosso ouvinte de eventos, desestruturando essa mensagem a partir das FIX Tags.