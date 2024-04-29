import java.util.*;

class Assembler1 {
    private Map<String, Integer> literalTable;
    private int locationCounter;
    private List<String> code;

    public Assembler1() {
        literalTable = new HashMap<>();
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
                if (line.startsWith("=") || line.contains("='")) {
                    processLiteral(line);
                } 
                processInstruction(line);
            }
        }
    }

    public void processInstruction(String line) {
        code.add(line);
        locationCounter++;
    }

    public void processLiteral(String line) {
        String literal;
        if (line.contains("='")) {
            literal = line.split("='")[1].split("'")[0];
        } else {
            literal = line.split(",")[1].trim();
        }
        if (!literalTable.containsKey(literal)) {
            literalTable.put(literal, literalTable.size() + locationCounter);
        }
    }

    public void generateTables() {
        System.out.println("\nLiteral Table:");
        System.out.println("Literal\tAddress");
        for (Map.Entry<String, Integer> entry : literalTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    public void assemble(List<String> sourceCode) {
        firstPass(sourceCode);
        generateTables();
    }
}

public class Literal {
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

        Assembler1 assembler = new Assembler1();
        assembler.assemble(sourceCode);
    }
}