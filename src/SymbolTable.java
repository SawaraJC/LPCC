import java.util.*;

class Assembler {
    private Map<String, Integer> symbolTable;
    private int locationCounter;
    private List<String> code;

    public Assembler() {
        symbolTable = new HashMap<>();
        locationCounter = 0;
        code = new ArrayList<>();
    }

    public void firstPass(List<String> sourceCode) {
        for (String line : sourceCode) {
            line = line.trim();
            if (line.startsWith("START")) {
                locationCounter = Integer.parseInt(line.split("\\s+")[1]);
            } else if (line.startsWith("END")) {
                break;
            } else if (!line.isEmpty()) {
                    processLabel(line);

                processInstruction(line);
            }
        }
    }

    public void processInstruction(String line) {
        code.add(line);
        locationCounter++;
    }

    public void processLabel(String line) {
        String[] parts = line.split("\\s+");
        String label = parts[0];
        symbolTable.put(label, locationCounter);
    }

    public void generateTables() {
        System.out.println("Symbol Table:");
        System.out.println("Label\tAddress");
        for (Map.Entry<String, Integer> entry : symbolTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }

    }

    public void assemble(List<String> sourceCode) {
        firstPass(sourceCode);
        generateTables();
    }
}

public class SymbolTable {
    public static void main(String[] args) {
        List<String> sourceCode = Arrays.asList(
                "START 300",
                "READ M",
                "READ N",
                "MOVER AREG, ='51'",
                "MOVER BREG, ='61'",
                "ADD AREG, BREG",
                "LOOP MOVER CREG, M",
                "ADD CREG, ='11'",
                "COMP CREG, N",
                "BC LT, LOOP",
                "NEXT SUB AREG, ='11'",
                "COMP AREG, N",
                "BC GT, NEXT",
                "STOP",
                "M DS 1",
                "N DS 1",
                "END");

        Assembler assembler = new Assembler();
        assembler.assemble(sourceCode);
    }
}
