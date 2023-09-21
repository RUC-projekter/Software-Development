import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        // System.out.println(" ");
        // System.out.println("############################################");
        // System.out.println("################# RoRo ###################");
        // System.out.println("############################################");
        // System.out.println(" ");
        // RoRo b = new RoRo("DK", 10, 400, 20, 200);
        // System.out.println(b.checkCargo());
        // b.loadingCargo(2, 2);
        // b.loadingCargo(2, 2);
        // b.loadingCargo(2, 1);
        // System.out.println(b.checkLoad());
        // System.out.println(b.utilityLevelOfCapacity());

        // System.out.println(" ");
        // System.out.println("############################################");
        // System.out.println("################ Containers ################");
        // System.out.println("############################################");
        // System.out.println(" ");
        // Containers k = new Containers("DE", 10, 200, 30, 10);
        // System.err.println(k.checkCargo());
        // k.loadingCargo(2);
        // k.loadingCargo(2);
        // k.loadingCargo(7);
        // System.out.println(k.utilityLevelOfCapacity());
        // System.out.println(k.checkLoad());

        System.out.println(" ");
        System.out.println("############################################");
        System.out.println("################# Tankers ################");
        System.out.println("############################################");
        System.out.println(" ");

        Tankers c = new Tankers("DE", 10, 200, 30, 20);
        c.setCompartmentSize(10);
        System.out.println(c.checkCargo());
        c.loadingCargo(0, 8);
        c.loadingCargo(1, 13);
        c.loadingCargo(5, 16);
        System.out.println("Total sum of containers: " + c.sumOfContainers());
        System.out.println("List of compartments: " + c.checkLoad());
        System.out.println("Fraction: " + c.utilityLevelOfCapacity());
    }
}

abstract class Vessels {
    String flagNation;
    int draft, length, width, cargo;

    public Vessels(String flagNation, int draft, int length, int width, int cargo) {
        this.flagNation = flagNation;
        this.draft = draft;
        this.length = length;
        this.width = width;
        this.cargo = cargo;
    }

    public abstract String checkCargo();

    public abstract String checkLoad();

    public abstract double utilityLevelOfCapacity();
}

class RoRo extends Vessels {
    int carLength = 8;
    int truckLength = 30;
    int totalCarsLength, totalTruckLength;
    double fraction;

    public RoRo(String flagNation, int draft, int length, int width, int cargo) {
        super(flagNation, draft, length, width, cargo);
    }

    public String checkCargo() {
        return "Flag Nation: " + flagNation + " | draft: " + draft
                + " | length: " + length + " | width: " + width;
    }

    public String checkLoad() {
        return (totalCarsLength + totalTruckLength) + "/" + cargo;
    }

    public void loadingCargo(int addedCars, int addedTrucks) {
        if (addedCars <= 0 && addedTrucks <= 0) {
            return;
        }
        if (cargo >= (this.totalCarsLength + (addedCars * carLength))
                + (this.totalTruckLength + (addedTrucks * truckLength))) {
            this.totalCarsLength += addedCars * carLength;
            this.totalTruckLength += addedTrucks * truckLength;
        } else {
            return;
        }
    }

    public double utilityLevelOfCapacity() {
        double cargo2 = cargo;
        this.fraction = ((this.totalCarsLength + this.totalTruckLength) / cargo2) * 100.0;
        return fraction;
    }
}

class Tankers extends Vessels {
    int[] compartment;
    int loadCargo, amountOfCompartments;
    double fraction;

    public Tankers(String flagNation, int draft, int length, int width, int cargo) {
        super(flagNation, draft, length, width, cargo);
    }

    public String checkCargo() {
        return "Flag Nation: " + flagNation + " | draft: " + draft
                + " | length: " + length + " | width: " + width + " | Cargo: " + cargo + " | Amount of compartments: "
                + compartment.length;
    }

    public String checkLoad() {
        return "Load on each compartment: " + Arrays.toString(compartment);
    }

    public void setCompartmentSize(int size) {
        compartment = new int[size];
    }

    public double sumOfContainers() {
        int sum = 0;
        int i;

        // Iterate through all elements and add them to sum
        for (i = 0; i < compartment.length; i++)
            sum += compartment[i];

        return sum;
    }

    void loadingCargo(int compartmentId, int loadCargo) {
        if (compartment[compartmentId] + loadCargo > cargo) {
            System.out.println("Cant add this amount of containers to this compartment! Please remove "
                    + ((compartment[compartmentId] + loadCargo) - cargo));
            return;
        }
        if (cargo >= compartment[compartmentId] + loadCargo) {
            compartment[compartmentId] += loadCargo;
        }

    }

    public double utilityLevelOfCapacity() {
        double fraction = (sumOfContainers() / (compartment.length * cargo)) * 100.0;
        return fraction;
    }
}

class Containers extends Vessels {
    int amountOfContainers;
    double fraction;

    public Containers(String flagNation, int draft, int length, int width, int cargo) {
        super(flagNation, draft, length, width, cargo);
    }

    public String checkLoad() {
        return "Containers amount: " + amountOfContainers;
    }

    public String checkCargo() {
        return "Flag Nation: " + flagNation + " | draft: " + draft
                + " | length: " + length + " | width: " + width + " | Cargo: " + cargo;
    }

    void loadingCargo(int amountOfContainers) {
        if (cargo >= this.amountOfContainers + amountOfContainers) {
            System.out.println("Added " + amountOfContainers + " Containers");
            this.amountOfContainers += amountOfContainers;
        } else {
            System.out.println("There is no space for " + amountOfContainers + " containers. There is only space for "
                    + (cargo - this.amountOfContainers) + " more");
        }
    }

    public double utilityLevelOfCapacity() {
        double amountOfContainers = this.amountOfContainers;
        this.fraction = (amountOfContainers / cargo) * 100.0;
        return fraction;
    }
}