package rsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class GenerateEncodeDecodeMain {

	public static void main(String[] args) throws Exception {
		String rsaFile = "key-32bits.rsa";
		String clearTextFile = "clear-message.txt"; 
		String cryptedTextFile = "crypted-message.txt";
		String decryptedTextFile = "decrypted-message.txt";
		String bits = "32";
		
		createMessage(clearTextFile);
		
		Main.main(new String[] {"generate-key", bits, rsaFile});
		Main.main(new String[] {"encode", rsaFile, clearTextFile, cryptedTextFile});
		Main.main(new String[] {"decode", rsaFile, cryptedTextFile, decryptedTextFile});
	}
	
	
	
	public static void createMessage(String filePath) {
		PrintWriter writer;
		try {
			File f = new File(filePath);
			f.delete();
			writer = new PrintWriter(filePath, "UTF-8");
			writer.println("------------------------");
			writer.println("** Shhh!! This is a secret message **");
			writer.println("** It will blow up few seconds after you read it! **");
			writer.println("Alice, our next meeting will be at 2md avenue cafeteria - 2nd floor");
			writer.println("Be there 7:30 PM");
			writer.println("Regards, Bob");
			writer.println("------------------------");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
