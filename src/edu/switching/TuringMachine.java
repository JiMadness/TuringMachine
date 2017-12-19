package edu.switching;

import java.util.Arrays;
import java.util.Scanner;

public class TuringMachine {
    private String[] alphabet;
    private int nStates;
    private String[] transitions;
    private char[] input;
    private int currentState = 0;
    private int currentIndex = 1;
    private String currentConfig;

    private TuringMachine(String[] alphabet, int nStates) {
        this.alphabet = alphabet;
        this.nStates = nStates;
    }

    private boolean evaluate() {
        return realEvaluate(currentState, currentIndex);
    }

    private boolean realEvaluate(int state, int index) {
        if (index <= 0) {
            System.err.println("Error:: Out of input bounds.");
            System.exit(1);
        }
        if (index - 1 == input.length) {
            input = Arrays.copyOf(input, input.length + 1);
            input[index - 1] = '#';
        }
        char currentCharacter = input[index - 1];
        for (String transition : transitions) {
            String[] tokens = transition.split(",");
            if (Integer.parseInt(tokens[0]) == state && tokens[1].toCharArray()[0] == currentCharacter) {
                currentConfig = transition;
                currentState = Integer.parseInt(tokens[2]);
                input[index - 1] = tokens[3].toCharArray()[0];
                switch (tokens[4]) {
                    case "L":
                        currentIndex--;
                        break;
                    case "R":
                        currentIndex++;
                        break;
                    case "Y":
                        System.out.println(currentState + " " + currentIndex);
                        return true;
                    default:
                        return false;
                }
                return realEvaluate(currentState, currentIndex);
            }
        }
        return false;
    }

    private void setTransitions(String[] transitions) {
        for (String transaction : transitions) {
            String[] tokens = transaction.split(",");
            if (Integer.parseInt(tokens[0]) <= 0 || Integer.parseInt(tokens[0]) > this.nStates ||
                    Integer.parseInt(tokens[2]) <= 0 || Integer.parseInt(tokens[2]) > this.nStates ||
                    !Arrays.asList(this.alphabet).contains(tokens[1]) || !Arrays.asList(this.alphabet).contains(tokens[3])) {
                System.err.println("Error:: Invalid Transaction.");
                System.exit(1);
            }
        }
        this.transitions = transitions;
    }

    private void setInput(String input) {
        char[] charInput = input.toCharArray();
        for (char c : charInput) {
            if (!Arrays.asList(this.alphabet).contains(Character.toString(c))) {
                System.err.println("Error:: Invalid Input.");
                System.exit(1);
            }
        }
        this.input = charInput;
    }

    private void setInitState(int initState) {
        this.currentState = initState;
    }


    private void setInitIndex(int initIndex) {
        this.currentIndex = initIndex;
    }

    private int getCurrentIndex() {
        return currentIndex;
    }

    private int getCurrentState() {
        return currentState;
    }

    private char[] getInput() {
        return input;
    }

    private String getCurrentConfig() { return currentConfig; }

    public static void main(String[] args) {
        System.out.println("Enter alphabet (Ex: a,b,#):");
        Scanner scanner = new Scanner(System.in);
        String[] alphabet = scanner.next().split(",");

        System.out.println("Enter number of states:");
        int nStates = scanner.nextInt();

        TuringMachine machine = new TuringMachine(alphabet, nStates);

        int nTransitions = nStates * alphabet.length;

        String[] transitions = new String[nTransitions];

        System.out.println("Transaction  Ex: 1,a,2,x,R");
        for (int i = 0; i < nTransitions; i++) {
            System.out.println("Enter Transition #" + (i + 1) + ":");
            transitions[i] = scanner.next();
        }

        machine.setTransitions(transitions);

        System.out.println("Enter your input:");
        String input = scanner.next();
        machine.setInput(input);

        System.out.println("Enter your initial state:");
        int initState = scanner.nextInt();
        machine.setInitState(initState);

        System.out.println("Enter your initial index:");
        int initialIndex = scanner.nextInt();
        machine.setInitIndex(initialIndex);

        boolean result = machine.evaluate();

        int currentIndex = machine.getCurrentIndex();
        int currentState = machine.getCurrentState();
        String finalConfig = machine.getCurrentConfig();
        String finalString = new String(machine.getInput());

        System.out.println("\n\nString is " + (result ? "Accepted" : "Rejected"));
        System.out.println("Halting Config: " + finalConfig);
        System.out.println("Final State: " + currentState);
        System.out.println("Final Index: " + currentIndex);
        System.out.println("Final String: " + finalString);
    }
}
