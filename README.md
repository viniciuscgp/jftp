# jftp
JFtp é um FTP de linha de comando feito em Java.

### Porque ?
Eu criei esse app porque no Windows a opção -p (passivo)
não funciona, e eu sempre passava perrengue com isso.

### Como utilizar ?
```
java -jar jftp.jar

### Parâmetros reconhecidos até o momento:

-p  Conecta em modo Passivo.

-v  Não mostra as respostas do servidor.

-d  Ativa o modo debug.

-i  Desliga as perguntas interativas quando

-n  Suprime o auto login ao iniciar.
    multiplos arquivos são transferidos.
       
-s:filename  especifica um arquivo texto com comandos FTP.
       Os comandos serão executados logo após o FTP iniciar.
       
-A Login em modo anônimo.
```
