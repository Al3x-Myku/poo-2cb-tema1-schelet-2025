package Tema1;

public class TurbinaEoliana extends ProducatorEnergie {
    private double putereBaza;

    public TurbinaEoliana(String id, double putereBaza) {
        super(id);
        this.putereBaza = putereBaza;
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (!isStatusOperational()) {
            return 0;
        }
        // factorExtern este factor vant
        return putereBaza * factorExtern;
    }

    public double getPutereBaza() {
        return putereBaza;
    }
}
