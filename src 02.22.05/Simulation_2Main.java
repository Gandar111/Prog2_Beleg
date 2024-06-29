import Simulation.Simulation_2;

import java.util.Scanner;

public class Simulation_2Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("n threads =?");
        int n = sc.nextInt();

        for (int i = 1; i <= n; i++) {
            Simulation_2 simulation = new Simulation_2("Simulation Thread " + i, i);
            simulation.start();
        }
    }}