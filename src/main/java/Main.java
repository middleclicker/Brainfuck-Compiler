import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * < = move left
 * > = move right
 * + = add 1
 * - = minus 1
 * . = print byte (output ascii at that location
 * , = input byte, changes byte and pointer location to user input
 * [ = open loop
 *      If non-zero at pointer location, enter loop
 *      If zero at pointer location, jump to close bracket
 * ] = close loop
 */

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length <= 1) {
            System.out.println("Not enough arguments!");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        StringBuilder sb = new StringBuilder();

        Character[] memory = new Character[Integer.parseInt(args[1])];
        int pointer = 0;

        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        String code = sb.toString().replaceAll("\\s", "");
        // System.out.println(code);

        Scanner scanner = new Scanner(System.in);

        parser(code, pointer, memory, scanner);

    }

    public static void parser(String code, int pointer, Character[] memory, Scanner scanner) throws Exception {
        for (int i = 0; i < code.length(); i++) {
            switch (code.charAt(i)) {
                case '<':
                    if (pointer != 0) pointer--;
                    // System.out.println("Step " + i + ": Moved back 1 to position " + pointer);
                    continue;
                case '>':
                    if (pointer < memory.length-1) pointer++;
                    // System.out.println("Step " + i + ": Moved forward 1 to position " + pointer);
                    continue;
                case '+':
                    if (memory[pointer] == null) {
                        memory[pointer] = 1;
                    } else {
                        memory[pointer]++;
                    }
                    // System.out.println("Step " + i + ": Added 1 to position " + pointer);
                    continue;
                case '-':
                    if (memory[pointer] == null) {
                        throw new Exception("Cannot subtract 1 from the current pointer as the current value is 0!");
                    } else {
                        memory[pointer]--;
                    }
                    // System.out.println("Step " + i + ": Subtracted 1 in position " + pointer);
                    continue;
                case '.':
                    System.out.print(memory[pointer]);
                    continue;
                case ',':
                    memory[pointer] = scanner.next().charAt(0);
                    continue;
                case '[':
                    String loopedPortion = findNext(i, code);
                    boolean is0 = false;
                    while (!is0) {
                        if (memory[pointer] == 0) {
                            is0 = true;
                        } else {
                            // System.out.println("Step " + i + ": Entered Loop with code: " + loopedPortion);
                            parser(loopedPortion, pointer, memory, scanner);
                        }
                    }
                    i = i + loopedPortion.length();
                    // System.out.println("Step " + i + ": Pointer was 0, skipped to " + pointer);
                    continue;
                case ']':
                    continue;

            }
        }
    }

    public static String findNext(int start, String st) throws Exception {
        boolean found = false;
        int j = start;
        while (!found) {
            j++;
            if (st.charAt(j) == ']') {
                break;
            }
        }

        if (j-start > 1) {
            return st.substring(start+1, j);
        }
        throw new Exception("Did not find function end symbol!");
    }
}
