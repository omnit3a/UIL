import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Translate extends ReadDefinitions{

    public static String globalTokens = "([a\\$]){2}[^\n\\S]*(\n?\\*?)|([r\\$]){2}[^\\n\\S]*(\\n?\\*?)([v\\$]){2}[^\\n\\S]*(\\n?\\*?)([s\\$]){2}[^\\n\\S]*(\\n?\\*?)";
    private static final Scanner scan = new Scanner(System.in);
    public static String setBytecodeFile() {
        System.out.println("File to open:");
        String temp = scan.nextLine();
        openFile(temp);
        return temp;
    }

    public static void setOutputFile() throws IOException {
        String readname = setBytecodeFile();
        System.out.println("File to write:");
        String filename = scan.nextLine();
        File fileWrite = new File(filename);

        if(!fileWrite.exists()){
            fileWrite.createNewFile();
        }
        FileWriter fw = new FileWriter(fileWrite);
        BufferedWriter bw = new BufferedWriter(fw);

        //change the translationSource of the translateBytecode to whatever definition file you want
        bw.write(translateBytecode("definitions/x86.txt",readname));
        bw.close();
    }

    private static String drawFromRegister(String input, int offset){
        String temp = input;
        boolean hasStar = temp.endsWith("*");
        temp = temp
                .replace("*","")
                .replace("r","")
                .replace("$","");
        temp = temp.toLowerCase();
        int reg = Integer.parseInt(temp)+offset;
        if (reg < regist.size() && regist.get(reg) != null) {
            temp = regist.get(reg);
        }
        if (hasStar){
            temp = temp.concat("*");
        }
        return temp;
    }    
    
    public static String translateBytecode(String translationSource, String readname) throws IOException {
        define(translationSource);
        openFile(readname);
        StringTokenizer st = new StringTokenizer(readTextFromFile());
        String currentToken = "", nextToken, finalString = "", temp;
        String[] arguments = new String[26];
        int ptr = 0;

        while (st.hasNext()){
            temp = st.nextToken();
            String currentKeyword = "";
            String[] args;
            if (Opcodes.contains(temp)) {
                args = instr.get(Opcodes.valueOf(temp)).split("\s|\n\r");
                for (int i = 1 ; i < args.length ; i++) {
                    if (args[i].matches("([a\\$]){2}[^\\n\\S]*(\\n?\\*?)")) {
                        args[i] = st.nextToken();
                        if (StringUtils.isNumeric(args[i])) {
                            args[i] = "0x" + Integer.toHexString(Integer.parseInt(args[i]));
                            if (i + 1 < args.length){
                                args[i] = args[i].concat("*");
                            }
                        } else if (args[i].matches("(\\dr\\$)*(\n?\\*?)")){
                            args[i] = drawFromRegister(args[i],0);
                            if (i + 1 < args.length){
                                args[i] = args[i].concat("*");
                            }
                        } else {
                            System.out.println("String in non-string operand");
                            break;
                        }
                    } else if (args[i].matches("([v\\$]){2}[^\\n\\S]*(\\n?\\*?)")) {
                        args[i] = st.nextToken();
                        if (StringUtils.isNumeric(args[i])) {
                            args[i] = "0x" + Integer.toHexString(Integer.parseInt(args[i]));
                            if (i + 1 < args.length){
                                args[i] = args[i].concat("*");
                            }
                        } else {
                            System.out.println("Non-numeric value in numeric operand");
                            break;
                        }
                    } else if (args[i].matches("([r\\$]){2}[^\\n\\S]*(\\n?\\*?)")) {
                        args[i] = st.nextToken();
                        args[i] = drawFromRegister(args[i], 0);
                        if (i + 1 < args.length){
                            args[i] = args[i].concat("*");
                        }
                    } else if (args[i].matches("([s\\$]){2}[^\\n\\S]*(\\n?\\*?)")){
                        args[i] = st.nextToken();
                        args[i] = args[i].toLowerCase();
                        if (i + 1 < args.length){
                            args[i] = args[i].concat("*");
                        }
                    }
                }
                finalString = finalString.concat(Arrays.toString(args)
                        .replace("[", "")
                        .replace("]", System.lineSeparator())
                        .replace(",","")
                        .replace("*",",")
                        .replace("NEWLINE",System.lineSeparator())
                        .replace("(","{")
                        .replace(")","}")
                        .replace("{","[")
                        .replace("}","]")
                        .replace(", ]"," ]")
                        .replaceAll("(?<=(\\n|\\r))([\\s]+(?!([\\s])))","")   //strip leading indents
                        .replaceAll("(?<=(\\n\\n))[a-zA-Z0-9]","\b"));
            }
        }
        return finalString;
    }

}
