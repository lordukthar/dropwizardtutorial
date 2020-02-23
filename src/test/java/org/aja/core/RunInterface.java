package org.aja.core;

public class RunInterface {

    public static void main(String...args) {

        RunInterface ri = new RunInterface();
        Calculate multiply = ri.useIf(true);
        Calculate divide = ri.useIf(false);

        System.out.println("Multiply: " + multiply.calculate(5));
        System.out.println("Divide: " + divide.calculate(5));
    }

    private Calculate useIf(boolean multiply) {

        int justForExample=10;

        if (multiply) {
            return (in) -> in*justForExample;
        }

        return (in) -> justForExample/in;
    }
}
