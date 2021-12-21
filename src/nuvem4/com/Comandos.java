package nuvem4.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author user
 */
public class Comandos {
    FTPClient ftp ;  
    String pastaLocal;
    public Comandos(FTPClient f) {
	ftp = f;
        pastaLocal = "";
    }
    
    public boolean cmdLogin(String usuario, String senha) {
        try {
            ftp.login(usuario, senha);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdDir(String filtro) {
        try {
            System.out.println("200 PORT command successful");
            System.out.println("150 Opening ASCII mode data connection for file list");
            FTPFile[] files = ftp.listFiles(filtro);
            for (int i = 0; i < files.length; i++) {
                FTPFile file = files[i];
                System.out.println(file.getRawListing());
            }
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdLs(String filtro) {
        try {
            System.out.println("200 PORT command successful");
            System.out.println("150 Opening ASCII mode data connection for file list");
            FTPFile[] files = ftp.listFiles(filtro);
            for (int i = 0; i < files.length; i++) {
                FTPFile file = files[i];
                System.out.println(file.getRawListing());
            }
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdCD(String pasta) {
        try {
            ftp.changeWorkingDirectory(pasta);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdLCD(String pasta) {
        pastaLocal = pasta;
        System.out.println("Local directory now " + pastaLocal + '.');
        return true;
    }
    public boolean cmdPWD() {
        try {
            String str = ftp.printWorkingDirectory();
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdPut(String arquivoLocal, String arquivoRemoto) {
        if (arquivoRemoto.equals("")) {
            arquivoRemoto = arquivoLocal;
        }
        try(
               InputStream input = new FileInputStream(new File(pastaLocal + arquivoLocal))
           )
        {
           ftp.storeFile(arquivoRemoto, input);
           mostraResposta();
           return true;
        } catch (FileNotFoundException ex) {
           System.out.println(ex.getMessage());
           return false;
        } catch (IOException ex) {
           System.out.println(ex.getMessage());
           return false;
        }
    }     
    public boolean cmdGet(String arquivoRemoto, String arquivoLocal) {
        if (arquivoLocal.equals("")) {
            arquivoLocal = arquivoRemoto;
        } 
        try(
            OutputStream output = new FileOutputStream(new File(pastaLocal + arquivoLocal));
        ) 
        {
            ftp.retrieveFile(arquivoRemoto, output);
            mostraResposta();
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdBin() {
        try {
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdRename(String de, String para) {
        try {
            ftp.rename(de, para);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(JFtp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean cmdDele(String arquivo) {
        try {
            ftp.dele(arquivo);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdMkdir(String pasta) {
        try {
            ftp.makeDirectory(pasta);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdRmdir(String pasta) {
        try {
            ftp.rmd(pasta);
            mostraResposta();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public boolean cmdLiteral(String token) {
	try {
	    ftp.sendCommand(token);
	    mostraResposta();
	    return true;
	} catch (IOException ex) {
            System.out.println(ex.getMessage());
	    return false;
	}
    }
    public boolean cmdQuit() {
	try {
	    ftp.logout();
	    mostraResposta();
	    ftp.disconnect();
	} catch (IOException ex) {
            System.out.println(ex.getMessage());
	    return false;
	}
	return true;
	
    }
    
    public void mostraResposta() {
        System.out.print(ftp.getReplyString());
    }    
    
    
}
