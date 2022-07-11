import java.io.*;
import java.util.Objects;

import org.apache.commons.text.StringTokenizer;

import static java.lang.System.exit;

public class ReadDefinitions extends Defintions{

    private static File file;

    /**
     * Open a file for reading
     * @param filename
     */
    public static void openFile(String filename){
        file = new File(filename);
    }

    /**
     * Return the text from within a file
     * @return
     */
    public static String readTextFromFile(){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String st;
        String tempString = "";
        try {
            while ((st = br.readLine()) != null) {
                tempString = tempString.concat(st).concat("\n");
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return tempString;
    }

    public static void define(String filename){
        openFile(filename);
        StringTokenizer st = new StringTokenizer(readTextFromFile());
        String currentToken, nextToken = "";
        while (st.hasNext()){

            currentToken = st.nextToken();

            if (!currentToken.equals("FAILSAFE")) {
                if (currentToken.equals("REGISTERS:")){
                    int ptr = 0;
                    nextToken = st.nextToken();
                    while (!nextToken.equals("END")) {
                        regist.put(ptr, nextToken);
                        ptr++;
                        nextToken = st.nextToken();
                    }
                    System.out.println(regist.values());
                    continue;
                } else {
                    nextToken = st.nextToken();
                }
                while (!nextToken.endsWith(" END")) {
                    String argToken = st.nextToken();
                    nextToken = nextToken.concat(" " + argToken);
                    if (nextToken.endsWith("FAILSAFE")) {
                        System.out.print("FAILSAFE REACHED");
                        exit(1);
                    } else if (nextToken.endsWith("NEWLINE")) {  //introduce newline to translation
                        nextToken = nextToken.replace(" NEWLINE", "\n");
                    }
                }
                nextToken = nextToken.replace(" END", "");
            } else {
                System.out.print("FAILSAFE REACHED");
                exit(0);
            }

            //set basic syntax for each
            String keyword = currentToken.replace(":", "");
            instr.put(Opcodes.valueOf(keyword), nextToken);
            values.put(keyword, Opcodes.valueOf(keyword));
        }
    }
}
