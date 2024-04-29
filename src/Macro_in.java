import java.util.*;

class MacroProcessor {
    static class MacroEntry {
        String name;
        int start, end;

        public MacroEntry(String name, int start) {
            this.name = name;
            this.start = start;
            this.end = -1;
        }
    }

    List<String> MDT = new ArrayList<>();
    Map<String, MacroEntry> MNT = new HashMap<>();
    boolean collecting = false;

    public void processLine(String line, int[] index) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length > 0) {
            if (parts[0].equals("MACRO") && parts.length > 1) {
                collecting = true;
                String macroName = parts[1];
                MNT.put(macroName, new MacroEntry(macroName, index[0]));
                MDT.add(line);
                index[0]++;
            } else if (parts[0].equals("MEND")) {
                collecting = false;
                MDT.add(line);
                if (index[0] > 0 && MDT.size() > 1 && MDT.get(index[0] - 1).split("\\s+").length > 1) {
                    String lastMacroName = MDT.get(index[0] - 1).split("\\s+")[1];
                    MacroEntry lastEntry = MNT.get(lastMacroName);
                    if (lastEntry != null) {
                        lastEntry.end = index[0];
                    }
                }
                index[0]++;
            } else if (collecting) {
                MDT.add(line);
                index[0]++;
            }
        }
    }

    public void passOne(List<String> inputLines) {
        int[] index = new int[]{0};

        for (String line : inputLines) {
            processLine(line, index);
        }
    }

    public void displayTables() {
        System.out.println("Macro Definition Table (MDT):");
        for (int i = 0; i < MDT.size(); i++) {
            System.out.println(i + " : " + MDT.get(i));
        }

        System.out.println("\nMacro Name Table (MNT):");
        for (Map.Entry<String, MacroEntry> entry : MNT.entrySet()) {
            System.out.println(entry.getKey() + " -> Start: " + entry.getValue().start + ", End: " +
                    entry.getValue().end);
        }
    }

    public List<String> expandMacros(List<String> inputCode) {
        List<String> expandedCode = new ArrayList<>();

        for (String line : inputCode) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length > 0) {
                String macroName = parts[0];
                MacroEntry entry = MNT.get(macroName);
                if (entry != null) {
                    for (int i = entry.start + 1; i < entry.end; i++) {
                        expandedCode.add(MDT.get(i));
                    }
                } else {
                    expandedCode.add(line);
                }
            }
        }

        return expandedCode;
    }
}

public class Macro_in {
    public static void main(String[] args) {
        MacroProcessor processor = new MacroProcessor();
        Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();

        System.out.println("Enter your code lines (type 'end' to finish):");

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("end")) break;
            lines.add(input);
        }

        processor.passOne(lines);
        processor.displayTables();

        // Now let's expand macros and display the expanded code
        List<String> expandedCode = processor.expandMacros(lines);
        System.out.println("\nExpanded code:");
        for (String line : expandedCode) {
            System.out.println(line);
        }

        scanner.close();
    }
}
