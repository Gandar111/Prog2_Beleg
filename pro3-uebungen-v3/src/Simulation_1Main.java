import Simulation.Simulation_1;
public class Simulation_1Main {
    public static void main(String[] args) {
        Simulation_1 simulation_1 = new Simulation_1("thread_1");
        Simulation_1 simulation_2 = new Simulation_1("thread_2");
        simulation_1.start();
        simulation_2.start();

    }
}