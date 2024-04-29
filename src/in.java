import java.util.HashMap;
import java.util.Map;

public class in {

    public static String[] generateIntermediateCode(String[] assemblyCode, Map<String, Integer> symbolTable) {
        String[] intermediateCode = new String[assemblyCode.length];

        for (int i = 0; i < assemblyCode.length; i++) {
            String[] parts = assemblyCode[i].split("\\s+");
            StringBuilder intermediateInstruction = new StringBuilder();

            // Translate label if present
            if (parts[0].endsWith(":")) {
                parts = removeFirstElement(parts);
            }

            // Translate opcode
            String opcode = parts[0];
            if (symbolTable.containsKey(opcode)) {
                intermediateInstruction.append(symbolTable.get(opcode));
            } else {
                intermediateInstruction.append(opcode);
            }

            // Translate operands
            for (int j = 1; j < parts.length; j++) {
                String operand = parts[j];
                if (operand.startsWith("=")) {
                    intermediateInstruction.append(" ").append(operand);
                } else if (symbolTable.containsKey(operand)) {
                    intermediateInstruction.append(" ").append(symbolTable.get(operand));
                } else {
                    intermediateInstruction.append(" ").append(operand);
                }
            }

            intermediateCode[i] = intermediateInstruction.toString();
        }

        return intermediateCode;
    }

    private static String[] removeFirstElement(String[] array) {
        String[] result = new String[array.length - 1];
        System.arraycopy(array, 1, result, 0, array.length - 1);
        return result;
    }

    public static void main(String[] args) {
        String[] assemblyCode = {
            "START: LDA 100",
            "STA X",
            "LDX Y",
            "ADD X",
            "SUB Y",
            "HLT",
            "X: DC 0",
            "Y: DC 0"
        };

        Map<String, Integer> symbolTable = new HashMap<>();
        symbolTable.put("START", 0);
        symbolTable.put("X", 1);
        symbolTable.put("Y", 2);

        String[] intermediateCode = generateIntermediateCode(assemblyCode, symbolTable);

        System.out.println("Intermediate Code:");
        for (String code : intermediateCode) {
            System.out.println(code);
        }
    }
}
