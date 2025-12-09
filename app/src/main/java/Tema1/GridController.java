package Tema1;

import java.util.ArrayList;
import java.util.List;

public class GridController {
    private List<ProducatorEnergie> producatori;
    private List<ConsumatorEnergie> consumatori;
    private List<Baterie> baterii;
    private List<String> istoricEvenimente;
    private boolean esteInBlackout;
    private int tickCount;

    public GridController() {
        this.producatori = new ArrayList<>();
        this.consumatori = new ArrayList<>();
        this.baterii = new ArrayList<>();
        this.istoricEvenimente = new ArrayList<>();
        this.esteInBlackout = false;
        this.tickCount = 0;
    }

    public void adaugaProducator(ProducatorEnergie producator) {
        producatori.add(producator);
    }

    public void adaugaConsumator(ConsumatorEnergie consumator) {
        consumatori.add(consumator);
    }

    public void adaugaBaterie(Baterie baterie) {
        baterii.add(baterie);
    }

    public ComponentaRetea getComponenta(String id) {
        for (ProducatorEnergie p : producatori) {
            if (p.getId().equals(id))
                return p;
        }
        for (ConsumatorEnergie c : consumatori) {
            if (c.getId().equals(id))
                return c;
        }
        for (Baterie b : baterii) {
            if (b.getId().equals(id))
                return b;
        }
        return null;
    }

    public void simuleazaTick(double factorSoare, double factorVant) {
        if (esteInBlackout) {
            return;
        }

        tickCount++;

        // 1. Resetare consumatori
        for (ConsumatorEnergie c : consumatori) {
            c.cupleazaLaRetea();
        }

        // 2. Calcul Productie
        double productieTotala = 0;
        for (ProducatorEnergie p : producatori) {
            productieTotala += p.calculeazaProductie(factorSoare); // Folosim factorSoare generic pentru toti, dar
                                                                   // intern fiecare isi ia ce trebuie?
            // Wait, cerinta zice: "simuleazaTick(double factorSoare, double factorVant)"
            // Si "PanouSolar: ... factorExtern (procentaj soare)"
            // "TurbinaEoliana: ... factorExtern (viteză vânt)"
            // Deci trebuie sa pasam factorul corect in functie de tip.
        }

        // Refacem calcul productie corect
        productieTotala = 0;
        for (ProducatorEnergie p : producatori) {
            if (p instanceof PanouSolar) {
                productieTotala += p.calculeazaProductie(factorSoare);
            } else if (p instanceof TurbinaEoliana) {
                productieTotala += p.calculeazaProductie(factorVant);
            } else {
                productieTotala += p.calculeazaProductie(0); // Reactor ignora
            }
        }

        // 3. Calcul Cerere
        double cerereTotala = 0;
        for (ConsumatorEnergie c : consumatori) {
            cerereTotala += c.getCerereCurenta();
        }

        // 4. Balansare
        double delta = productieTotala - cerereTotala;
        List<String> decuplati = new ArrayList<>();

        if (delta > 0) {
            // Surplus
            for (Baterie b : baterii) {
                delta = b.incarca(delta);
            }
            // Energia ramasa in delta se pierde
        } else if (delta < 0) {
            // Deficit
            double deficit = -delta;

            // Baterii
            for (Baterie b : baterii) {
                double energieFurnizata = b.descarca(deficit);
                deficit -= energieFurnizata;
                if (deficit <= 0.0001)
                    break; // Small epsilon for float comparison
            }

            // Triage
            if (deficit > 0.0001) {
                // Sortare consumatori dupa prioritate descrescator (3, 2, 1)
                List<ConsumatorEnergie> consumatoriSortati = new ArrayList<>(consumatori);
                consumatoriSortati.sort((c1, c2) -> Integer.compare(c2.getPrioritate(), c1.getPrioritate()));

                for (ConsumatorEnergie c : consumatoriSortati) {
                    if (c.getPrioritate() == 1)
                        continue; // Nu decupla P1

                    if (deficit > 0.0001) {
                        c.decupleazaDeLaRetea();
                        decuplati.add(c.getId());
                        deficit -= c.getCerereEnergie();
                    }
                }

                // Blackout check
                if (deficit > 0.0001) {
                    esteInBlackout = true;
                    istoricEvenimente.add("Tick " + tickCount + ": BLACKOUT! SIMULARE OPRITA.");
                    System.out.println("BLACKOUT! SIMULARE OPRITA.");
                    return;
                }
            }
        }

        double energieBaterii = 0;
        for (Baterie b : baterii)
            energieBaterii += b.getEnergieStocata();

        String statusTick = String.format("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: %s",
                productieTotala, cerereTotala, energieBaterii, decuplati.toString());
        System.out.println(statusTick);

        // Adaugare in istoric (doar evenimente majore? Cerinta zice "ex. Tick 5:
        // Deficit - Decuplat lab1")
        // Dar testele par sa verifice output la consola pentru fiecare tick.
        // Istoricul e pentru comanda 6.
        if (!decuplati.isEmpty()) {
            istoricEvenimente.add("Tick " + tickCount + ": Deficit - Decuplat " + String.join(", ", decuplati));
        }
    }

    public List<String> getIstoricEvenimente() {
        return istoricEvenimente;
    }

    public boolean isEsteInBlackout() {
        return esteInBlackout;
    }

    public List<ProducatorEnergie> getProducatori() {
        return producatori;
    }

    public List<ConsumatorEnergie> getConsumatori() {
        return consumatori;
    }

    public List<Baterie> getBaterii() {
        return baterii;
    }
}
