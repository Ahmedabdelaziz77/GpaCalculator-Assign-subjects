package com.example.gpa_calculator;
import java.util.Random;

public class gpaCalculator  implements Runnable{
    private double gpa;
    private int grade1;
    private int grade2;
    private int grade3;
    private int grade4;
    private int grade5;
    public gpaCalculator(int g1, int g2, int g3, int g4, int g5){
        grade1 = g1;
        grade2 = g2;
        grade3 = g3;
        grade4 = g4;
        grade5 = g5;
    }
    @Override
    public void run() {
        Random random = new Random();
        double[] grades = new double[5];
        grades[0] = convertToGrade(grade1);
        grades[1] = convertToGrade(grade2);
        grades[2] = convertToGrade(grade3);
        grades[3] = convertToGrade(grade4);
        grades[4] = convertToGrade(grade5);
        double sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += grades[i];
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gpa = sum / 5;
    }
    private double convertToGrade(int sub_take) {
        if (sub_take >= 90) {
            return 4.0;
        } else if (sub_take >= 85 && sub_take < 90) {
            return 3.7;
        } else if (sub_take >= 80 && sub_take < 85) {
            return 3.3;
        } else if (sub_take >= 75 && sub_take < 80) {
            return 3.0;
        } else if (sub_take >= 70 && sub_take < 75) {
            return 2.7;
        } else if (sub_take >= 65 && sub_take < 70) {
            return 2.4;
        } else if (sub_take >= 60 && sub_take < 65) {
            return 2.2;
        } else if (sub_take >= 50 && sub_take < 60) {
            return 2.0;
        } else {
            return 0.0;
        }
    }

    public double getGpa() {
        return gpa;
    }

}
