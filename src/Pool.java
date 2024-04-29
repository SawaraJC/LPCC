import java.util.*;

class Assembler3 {
    private Map<String, Integer> literalTable;
    private List<Integer> poolTable;
    private int locationCounter;
    private List<String> code;

    public Assembler3() {
        literalTable = new HashMap<>();
        poolTable = new ArrayList<>();
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
                } else if (line.startsWith("LTORG")) {
                    processLTORG();
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
            literalTable.put(literal, literalTable.size() + 1);
            poolTable.add(locationCounter);
        }
    }

    public void processLTORG() {
        for (Map.Entry<String, Integer> entry : literalTable.entrySet()) {
            if (poolTable.contains(entry.getValue())) {
                poolTable.remove(entry.getValue());
                literalTable.put(entry.getKey(), locationCounter);
                locationCounter++;
            }
        }
    }

    public void generateTables() {
        System.out.println("\nPool Table:");
        System.out.println("Pool Address");
        for (Integer address : poolTable) {
            System.out.println(address);
        }
    }

    public void assemble(List<String> sourceCode) {
        firstPass(sourceCode);
        generateTables();
    }
}

public class Pool {
    public static void main(String[] args) {
        List<String> sourceCode = Arrays.asList(
                "START 100",
                "READ A",
                "MOVER AREG, ='1'",
                "MOVEM AREG, B",
                "MOVER BREG, ='6'",
                "ADD AREG, BREG",
                "COMP AREG, A",
                "BC GT, LAST",
                "LTORG",
                "NEXT SUB AREG, ='1'",
                "MOVER CREG, B",
                "ADD CREG, ='8'",
                "MOVEM CREG, B",
                "PRINT B",
                "LAST STOP",
                "A DS 1",
                "B DS 1",
                "END");

        Assembler3 assembler = new Assembler3();
        assembler.assemble(sourceCode);
    }
}