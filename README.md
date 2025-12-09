# MarsGrid - Sistem de Management Energetic

## Descriere Implementare

Acest proiect implementează un sistem de management energetic pentru o colonie pe Marte, simulând interacțiunea dintre producători, consumatori și baterii într-o micro-rețea (MarsGrid).

### Arhitectura Sistemului

Sistemul este construit pe baza principiilor Programării Orientate pe Obiecte (POO):

1.  **Abstractizare și Moștenire**:
    *   `ComponentaRetea`: Clasa de bază abstractă pentru toate entitățile din rețea (producători, consumatori, baterii).
    *   `ProducatorEnergie`: Clasa abstractă derivată pentru sursele de energie.
        *   `PanouSolar`, `TurbinaEoliana`, `ReactorNuclear`: Implementări concrete cu logici specifice de calcul al producției.
    *   `ConsumatorEnergie`: Clasa abstractă derivată pentru consumatori.
        *   `SistemSuportViata` (P1), `LaboratorStiintific` (P2), `SistemIluminat` (P3): Implementări concrete cu priorități diferite.
    *   `Baterie`: Clasă concretă pentru stocarea energiei.

2.  **Încapsulare**:
    *   Toate atributele interne sunt `private` sau `protected`.
    *   Accesul la starea componentelor se face prin metode publice (ex: `incarca`, `descarca`, `cupleazaLaRetea`).

3.  **Polimorfism**:
    *   `GridController` gestionează liste generice de `ProducatorEnergie` și `ConsumatorEnergie`, tratându-le uniform în bucla de simulare.

### Logica de Balansare (`simuleazaTick`)

La fiecare pas de simulare (tick):
1.  Se calculează producția totală și cererea totală.
2.  Se calculează diferența (`delta`).
3.  **Surplus**: Energia este stocată în baterii.
4.  **Deficit**:
    *   Se încearcă acoperirea din baterii.
    *   Dacă deficitul persistă, se activează procedura de **Triage**: consumatorii sunt decuplați în ordinea inversă a priorității (3 -> 2). Sistemele critice (P1) nu sunt decuplate.
    *   Dacă nici după Triage nu se poate susține rețeaua (deficit > 0 și doar P1 activi), se declară **BLACKOUT**.

---

## Bonus

### 1. Cazuri Limită Tratate Suplimentar

1.  **Eficiența Bateriilor**: În realitate, bateriile nu au eficiență 100%. Am putea implementa un factor de eficiență la încărcare/descărcare (ex. 95%), astfel încât o parte din energie să se piardă sub formă de căldură.
2.  **Uzura Componentelor**: Componentele s-ar putea degrada în timp. Am putea adăuga un atribut `health` care scade la fiecare tick sau la suprasolicitare, reducând capacitatea de producție/stocare sau crescând riscul de defectare spontană.
3.  **Priorități Dinamice**: În situații de criză prelungită, anumite laboratoare ar putea deveni critice (ex. cercetare medicală). Am putea implementa o metodă de a schimba prioritatea unui consumator în timp real, nu doar la inițializare.

### 2. Refactorizare Comenzi și Răspunsuri

1.  **Pattern-ul Command**: În loc de un `switch` mare în `App.java`, am putea folosi design pattern-ul **Command**. Fiecare comandă (ex. `add_producator`) ar fi o clasă separată care implementează o interfață `Command`. Acest lucru ar face codul mai modular și mai ușor de extins.
2.  **JSON pentru Output**: Răspunsurile curente sunt text simplu, greu de parsat de alte sisteme. Am putea refactoriza output-ul comenzii `status_grid` sau `istoric` să returneze un format structurat precum **JSON**.
3.  **Validare Centralizată**: Logica de validare a input-ului (număr parametri, tipuri de date) se repetă în fiecare handler. Am putea crea o clasă utilitară `InputValidator` sau un sistem de adnotări pentru a valida automat parametrii comenzilor înainte de execuție.
