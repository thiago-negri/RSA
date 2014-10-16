package rsa;

import java.io.File;
import java.io.PrintWriter;

public class GenerateEncodeDecodeMain {

    public static void main(String[] args) throws Exception {
        String bits              = "2048";
        String path              = "rsa" + bits + "\\";
        String clearTextFile     = path + "clear-message.txt";
        String cryptedTextFile   = path + "rsacrypted-message.txt";
        String decryptedTextFile = path + "decrypted-message.txt";
        String rsaFile           = path + "key.txt";

        createMessage(clearTextFile);

        String[] generateKey = { "generate-key", bits, rsaFile };
        String[] encode      = { "encode", rsaFile, clearTextFile, cryptedTextFile };
        String[] decode      = { "decode", rsaFile, cryptedTextFile, decryptedTextFile };
        
        Main.main(generateKey);
        Main.main(encode     );
        Main.main(decode     );
    }

    public static void createMessage(String filePath) throws Exception {
        File f = new File(filePath);
        f.mkdirs();
        f.delete();
        
        PrintWriter writer = new PrintWriter(filePath, "UTF-8");
        writer.println("------------------------");
        writer.println("** Shhh!! This is a secret message **");
        writer.println("** It will blow up few seconds after you read it! **");
        writer.println("Alice, our next meeting will be at 2nd avenue cafeteria - 2nd floor");
        writer.println("Be there 7:30 PM");
        writer.println("Regards, Bob");
        writer.println("------------------------");
        writer.close();
    }

}
