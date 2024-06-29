import Simulation.Simulation_3;

import java.util.Scanner;

public class Simulation_3Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("n threads =?");
        int n = sc.nextInt();

        for (int i = 1; i <= n; i++) {
            Simulation_3 simulation = new Simulation_3("Simulation Thread " + i, i);
            simulation.start();
        }
    }}