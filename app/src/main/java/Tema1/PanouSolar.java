package Tema1;

public class PanouSolar extends ProducatorEnergie {
    private double putereMaxima;

    public PanouSolar(String id, double putereMaxima) {
        super(id);
        this.putereMaxima = putereMaxima;
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (!isStatusOperational()) {
            return 0;
        }
        // factorExtern este procentaj soare (0.0 - 1.0, dar poate fi si mai mare
        // teoretic, cerinta zice "factor")
        // Presupunem ca factorExtern e multiplicator direct.
        return putereMaxima * factorExtern;
    }

    public double getPutereMaxima() {
        return putereMaxima;
    }
}
