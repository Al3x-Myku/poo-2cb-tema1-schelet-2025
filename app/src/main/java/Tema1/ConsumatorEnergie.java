package Tema1;

public abstract class ConsumatorEnergie extends ComponentaRetea {
    private double cerereEnergie;
    private int prioritate;
    private boolean esteAlimentat;

    public ConsumatorEnergie(String id, double cerereEnergie, int prioritate) {
        super(id);
        this.cerereEnergie = cerereEnergie;
        this.prioritate = prioritate;
        this.esteAlimentat = true; // Implicit alimentat la creare/resetare
    }

    public double getCerereCurenta() {
        if (esteAlimentat && isStatusOperational()) {
            return cerereEnergie;
        }
        return 0;
    }

    public void cupleazaLaRetea() {
        this.esteAlimentat = true;
    }

    public void decupleazaDeLaRetea() {
        this.esteAlimentat = false;
    }

    public int getPrioritate() {
        return prioritate;
    }

    public double getCerereEnergie() {
        return cerereEnergie;
    }

    public boolean isEsteAlimentat() {
        return esteAlimentat;
    }
}
