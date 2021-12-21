package nuvem4.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;

/**
@author Vinicius Céar @viniciuscgp
@since  10/01/2019
@version 0.5
*/
public class JFtp {
    private static FTPClient ftp;
    
    private static boolean passivo = false;
    private static Comandos cm;
    private static final String versao = "0.5";
    
    public static void main(String[] args) {
	String host = "";
	String comandos = "";
        
        boolean autoLogin = true;

	ftp = new FTPClient();
        
	cm = new Comandos(ftp);

	//----------------------------
	// pega parametros
	//----------------------------
	for (String str : args) {
	    if (str.startsWith("-s:")) {
		str = str.replace("-s:", "");
		comandos = str;
	    } else if (str.startsWith("--help")) 
            {
		ajuda();
		return;
	    } else if (str.startsWith("-p")) 
            {
		str = str.replace("-p", "");
                passivo = true;
	    } else if (str.startsWith("-n")) 
            {
		str = str.replace("-n", "");
                autoLogin = false;
	    } else if (str.startsWith("-v")) 
            {
		str = str.replace("-v", "");
	    } else if (str.startsWith("-i")) 
            {
		str = str.replace("-i", "");
	    } else if (str.startsWith("-d")) 
            {
		str = str.replace("-f", "");
	    } else if (str.startsWith("-a")) 
            {
		str = str.replace("-a", "");
	    } else if (str.startsWith("-A")) 
            {
		str = str.replace("-A", "");
            } else if (str.startsWith("--version")) 
            {
                System.out.println("jftp - Versão " + versao);
                return;
            } else 
            {
		host = str;
	    }
	}
	if (host.equals("")) {
	    ajuda();
	    return;
	}
	try {
	    ftp.connect(host);
            cm.mostraResposta();
            
            if (passivo) {
                ftp.enterLocalPassiveMode();
		cm.mostraResposta();
            }
            
            interpretador(comandos);
            
            System.exit(0);
            
	} catch (IOException e) {
	    System.out.println("Erro:" + e.getMessage());
            System.exit(1);
	}
    }
    
    private static void interpretador(String arquivo) {
        boolean continua = true;
        Scanner scanner = null;
        
        if (!arquivo.isEmpty())
        {
            File file = new File(arquivo);
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JFtp.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        
        String linha;
        
        while (continua) 
        {
            if (arquivo.isEmpty()) {
                System.out.print("ftp> ");
                linha = System.console().readLine();
            } else {
                linha = scanner.nextLine();
                System.out.print("ftp> " + linha + "\n");
            }
            
            if (linha.trim().equals("")) 
                continue;
            
            interpretaComandos(linha);

            if (linha.equals("quit"))
                break;
        }
        if (scanner != null)
            scanner.close();
    }
    
    private static boolean interpretaComandos(String linha){
        String[] tokens = linha.split(" ");
        String cmd = tokens[0].toLowerCase();
        String par = "";

        for (int i = 1; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }
        
        for (int i = 1; i < tokens.length; i++) {
            par += tokens[i] + " ";
        }
        par = par.trim();
        
        boolean result = false;

        if (cmd.equals("quit")) {
            result = cm.cmdQuit();
        } else

        if (cmd.equals("user")) {
	    String user, psw;
	    if (tokens.length == 1) {
		System.out.print("usuario:");
		user = System.console().readLine();
		System.out.print("senha:");
		psw = new String(System.console().readPassword());
	    } else if(tokens.length == 2) {
		user = tokens[1];
		System.out.print("senha:");
		psw = new String(System.console().readPassword());
	    } else {
		user = tokens[1];
		psw  = tokens[2];
	    }
         
	    result = cm.cmdLogin(user, psw);
        } else

        if (cmd.equals("ls")) {
            if (tokens.length >= 2)
                result = cm.cmdLs(tokens[1]);
            else
                result = cm.cmdLs("");
        } else
	    
        if (cmd.equals("dir")) {
            if (tokens.length >= 2)
                result = cm.cmdDir(tokens[1]);
            else
                result = cm.cmdDir("");
        } else

        if (cmd.equals("bin")) {
            result = cm.cmdBin();
        } else

        if (cmd.equals("cd")) {
            if (tokens.length >= 2)
                result = cm.cmdCD(tokens[1]);
            else
                result = cm.cmdCD("");
        } else

        if (cmd.equals("lcd")) {
            if (tokens.length >= 2)
                result = cm.cmdLCD(tokens[1]);
            else
                result = cm.cmdLCD("");
        } else

        if (cmd.equals("pwd")) {
            result = cm.cmdPWD();
        } else

        if (cmd.equals("put")) {
            if (tokens.length >= 3)
                result = cm.cmdPut(tokens[1], tokens[2]);
            else
                result = cm.cmdPut(tokens[1], "");
        } else

        if (cmd.equals("get")) {
            if (tokens.length >= 3)
                result = cm.cmdGet(tokens[1], tokens[2]);
            else
                result = cm.cmdGet(tokens[1], "");
        } else
        
        if (cmd.equals("rename")) {
            result = cm.cmdRename(tokens[1], tokens[2]);
        } else

        if (cmd.equals("dele")) {
            result = cm.cmdDele(tokens[1]);
        } else

        if (cmd.equals("rmdir")) {
            result = cm.cmdRmdir(tokens[1]);
        } else

        if (cmd.equals("mkdir")) {
            result = cm.cmdMkdir(tokens[1]);
        } else
           
        if (cmd.equals("literal")) {
            result = cm.cmdLiteral(par);
        } else {
            System.out.println("Invalid command.");
            result = false;
        }
        
        return result;
    }

    
    public static void ajuda() {
	System.out.println("JFTP ".concat(versao));
	System.out.println("jftp [-p] [-v] [-d] [-i] [-n] [-s:filename] [-A] [-cp] [--help] host");
	System.out.println("");
	System.out.println("-p\t\t Conecta em modo Passivo.");
	System.out.println("-v\t\t Não mostra as respostas do servidor.");
	System.out.println("-d\t\t Ativa o modo debug.");
	System.out.println("-i\t\t Desliga as perguntas interativas quando");
	System.out.println("-n\t\t Suprime o auto login ao iniciar.");
        System.out.println("\t\t multiplos arquivos são transferidos.");
	System.out.println("-s:filename\t especifica um arquivo texto com comandos FTP.");
	System.out.println("\t\t Os comandos serão executados logo após o FTP iniciar.");
	System.out.println("-A\t\t Login em modo anônimo.");
    }

}
