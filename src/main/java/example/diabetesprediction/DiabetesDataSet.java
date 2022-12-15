package example.diabetesprediction;

import de.fhws.easyml.linearalgebra.Vector;

public class DiabetesDataSet {
    private final int pregnancies;
    private final int glucose;
    private final int bloodPressure;
    private final int skinThickness;
    private final int insulin;
    private final double bmi;
    private final double diabetesPedigreeFunction;
    private final int age;
    private final boolean diabetes;

    public DiabetesDataSet(String[] values){
        pregnancies = Integer.parseInt(values[0]);
        glucose = Integer.parseInt(values[1]);
        bloodPressure = Integer.parseInt(values[2]);
        skinThickness = Integer.parseInt(values[3]);
        insulin = Integer.parseInt(values[4]);
        bmi = Double.parseDouble(values[5]);
        diabetesPedigreeFunction = Double.parseDouble(values[6]);
        age = Integer.parseInt(values[7]);
        diabetes = values[8].equals("1");
    }

    public Vector toVector(){
        return new Vector(pregnancies,
                glucose,
                bloodPressure,
                skinThickness,
                insulin,
                bmi,
                diabetesPedigreeFunction,
                age);
    }

    public boolean hasDiabetes() {
        return diabetes;
    }

}
