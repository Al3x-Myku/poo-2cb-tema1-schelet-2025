package Tema1;

public class Baterie extends ComponentaRetea {
    private double capacitateMaxima;
    private double energieStocata;

    public Baterie(String id, double capacitateMaxima) {
        super(id);
        this.capacitateMaxima = capacitateMaxima;
        this.energieStocata = 0; // Initial goala? Sau plina? Presupunem goala sau specificat. Testele par sa
                                 // implice goala initial.
    }

    public double incarca(double energieDisponibila) {
        if (!isStatusOperational()) {
            return energieDisponibila; // Daca e defecta, nu incarca nimic, returneaza tot surplusul
        }

        double spatiuLiber = capacitateMaxima - energieStocata;
        if (energieDisponibila <= spatiuLiber) {
            energieStocata += energieDisponibila;
            return 0; // Totul a fost stocat
        } else {
            energieStocata = capacitateMaxima;
            return energieDisponibila - spatiuLiber; // Returneaza surplusul
        }
    }

    public double descarca(double energieCeruta) {
        if (!isStatusOperational()) {
            return 0; // Daca e defecta, nu poate furniza nimic
        }

        if (energieStocata >= energieCeruta) {
            energieStocata -= energieCeruta;
            return energieCeruta;
        } else {
            double energieFurnizata = energieStocata;
            energieStocata = 0;
            return energieFurnizata;
        }
    }

    public double getCapacitateMaxima() {
        return capacitateMaxima;
    }

    public double getEnergieStocata() {
        return energieStocata;
    }
}
