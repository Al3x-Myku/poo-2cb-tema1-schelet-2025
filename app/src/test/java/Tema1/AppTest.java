package Tema1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeAll
    public static void setLocale() {
        Locale.setDefault(Locale.US);
    }

    @BeforeEach
    public void setUp() {

        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {

        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String runMain(String input) {

        String finalInput = input + "7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(finalInput.getBytes());
        System.setIn(in);

        App.main(null);

        return outputStreamCaptor.toString().trim().replaceAll("\\r\\n", "\n");
    }

    private void assertOutputContains(String expected, String actual) {
        if (!actual.contains(expected)) {

            fail("Output-ul asteptat nu a fost gasit.\nExpected: <" + expected + ">\nActual:   <" + actual + ">");
        }
        assertTrue(true);
    }

    private void assertOutputDoesNotContain(String unexpected, String actual) {
        if (actual.contains(unexpected)) {
            fail("Output-ul contine un text neasteptat.\nUnexpected: <" + unexpected + ">\nActual:     <" + actual + ">");
        }
        assertTrue(true);
    }

    @Test
    @Order(1)
    @DisplayName("Cmd 0: Adaugare Producator Solar (Succes)")
    public void testAddProducatorSolar() {
        String input = "0 solar s1 100\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat producatorul s1 de tip solar", output);
    }

    @Test
    @Order(2)
    @DisplayName("Cmd 0: Adaugare Producator Turbina (Succes)")
    public void testAddProducatorTurbina() {
        String input = "0 turbina t1 150\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat producatorul t1 de tip turbina", output);
    }

    @Test
    @Order(3)
    @DisplayName("Cmd 0: Adaugare Producator Reactor (Succes)")
    public void testAddProducatorReactor() {
        String input = "0 reactor r1 1000\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat producatorul r1 de tip reactor", output);
    }

    @Test
    @Order(4)
    @DisplayName("Cmd 0: Eroare ID Duplicat")
    public void testAddProducatorDuplicateId() {
        String input = "0 solar s1 100\n0 reactor s1 500\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat producatorul s1 de tip solar", output);
        assertOutputContains("EROARE: Exista deja o componenta cu id-ul s1", output);
    }

    @Test
    @Order(5)
    @DisplayName("Cmd 0: Eroare Tip Invalid")
    public void testAddProducatorInvalidType() {
        String input = "0 fuziune f1 1000\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Tip producator invalid", output);
    }

    @Test
    @Order(6)
    @DisplayName("Cmd 0: Eroare Putere Negativa")
    public void testAddProducatorNegativePower() {
        String input = "0 solar s1 -100\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Putere invalida", output);
    }

    @Test
    @Order(7)
    @DisplayName("Cmd 0: Eroare Putere Zero")
    public void testAddProducatorZeroPower() {
        String input = "0 solar s1 0\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Putere invalida", output);
    }

    @Test
    @Order(8)
    @DisplayName("Cmd 0: Eroare Format Invalid (Lipsa Putere)")
    public void testAddProducatorMalformed() {
        String input = "0 solar s1\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Format comanda invalid", output);
    }

    @Test
    @Order(9)
    @DisplayName("Cmd 1: Adaugare Consumator Suport Viata (Succes)")
    public void testAddConsumatorSuportViata() {
        String input = "1 suport_viata sv1 800\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat consumatorul sv1 de tip suport_viata", output);
    }

    @Test
    @Order(10)
    @DisplayName("Cmd 1: Adaugare Consumator Laborator (Succes)")
    public void testAddConsumatorLaborator() {
        String input = "1 laborator lab1 300\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat consumatorul lab1 de tip laborator", output);
    }

    @Test
    @Order(11)
    @DisplayName("Cmd 1: Adaugare Consumator Iluminat (Succes)")
    public void testAddConsumatorIluminat() {
        String input = "1 iluminat i1 100\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat consumatorul i1 de tip iluminat", output);
    }

    @Test
    @Order(12)
    @DisplayName("Cmd 1: Eroare ID Duplicat (cu Producator)")
    public void testAddConsumatorDuplicateId() {
        String input = "0 solar s1 100\n1 laborator s1 200\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat producatorul s1 de tip solar", output);
        assertOutputContains("EROARE: Exista deja o componenta cu id-ul s1", output);
    }

    @Test
    @Order(13)
    @DisplayName("Cmd 1: Eroare Tip Invalid")
    public void testAddConsumatorInvalidType() {
        String input = "1 bucatarie k1 100\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Tip consumator invalid", output);
    }

    @Test
    @Order(14)
    @DisplayName("Cmd 1: Eroare Cerere Negativa")
    public void testAddConsumatorNegativeDemand() {
        String input = "1 laborator lab1 -100\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Cerere putere invalida", output);
    }

    @Test
    @Order(15)
    @DisplayName("Cmd 1: Eroare Cerere Zero")
    public void testAddConsumatorZeroDemand() {
        String input = "1 laborator lab1 0\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Cerere putere invalida", output);
    }

    @Test
    @Order(16)
    @DisplayName("Cmd 1: Eroare Format Invalid (Lipsa Cerere)")
    public void testAddConsumatorMalformed() {
        String input = "1 laborator lab1\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Format comanda invalid", output);
    }

    @Test
    @Order(17)
    @DisplayName("Cmd 2: Adaugare Baterie (Succes)")
    public void testAddBaterie() {
        String input = "2 b1 5000\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat bateria b1 cu capacitatea 5000", output);
    }

    @Test
    @Order(18)
    @DisplayName("Cmd 2: Eroare ID Duplicat")
    public void testAddBaterieDuplicateId() {
        String input = "0 solar s1 100\n2 s1 1000\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Exista deja o componenta cu id-ul s1", output);
    }

    @Test
    @Order(19)
    @DisplayName("Cmd 2: Eroare Capacitate Negativa")
    public void testAddBaterieNegativeCapacity() {
        String input = "2 b1 -1000\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Capacitate invalida", output);
    }

    @Test
    @Order(20)
    @DisplayName("Cmd 2: Eroare Capacitate Zero")
    public void testAddBaterieZeroCapacity() {
        String input = "2 b1 0\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Capacitate invalida", output);
    }

    @Test
    @Order(21)
    @DisplayName("Cmd 2: Eroare Format Invalid (Lipsa Capacitate)")
    public void testAddBaterieMalformed() {
        String input = "2 b1\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Format comanda invalid", output);
    }

    @Test
    @Order(22)
    @DisplayName("Cmd 4: Setare Producator Defect (Succes)")
    public void testSetProducatorDefect() {
        String input = "0 reactor r1 1000\n4 r1 false\n";
        String output = runMain(input);
        assertOutputContains("Componenta r1 este acum defecta", output);
    }

    @Test
    @Order(23)
    @DisplayName("Cmd 4: Setare Consumator Defect (Succes)")
    public void testSetConsumatorDefect() {
        String input = "1 laborator lab1 300\n4 lab1 false\n";
        String output = runMain(input);
        assertOutputContains("Componenta lab1 este acum defecta", output);
    }

    @Test
    @Order(24)
    @DisplayName("Cmd 4: Setare Baterie Defecta (Succes)")
    public void testSetBaterieDefect() {
        String input = "2 b1 5000\n4 b1 false\n";
        String output = runMain(input);
        assertOutputContains("Componenta b1 este acum defecta", output);
    }

    @Test
    @Order(25)
    @DisplayName("Cmd 4: Eroare ID Inexistent")
    public void testSetDefectNotFound() {
        String input = "4 id_inexistent false\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Nu exista componenta cu id-ul id_inexistent", output);
    }

    @Test
    @Order(26)
    @DisplayName("Cmd 4: Eroare Status Invalid")
    public void testSetDefectInvalidStatus() {
        String input = "0 reactor r1 1000\n4 r1 defect\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Status invalid", output);
    }

    @Test
    @Order(27)
    @DisplayName("Cmd 4: Reparare Componenta (Succes)")
    public void testSetDefectReparare() {
        String input = "0 reactor r1 1000\n4 r1 false\n4 r1 true\n";
        String output = runMain(input);
        assertOutputContains("Componenta r1 este acum defecta", output);
        assertOutputContains("Componenta r1 este acum operationala", output);
    }

    @Test
    @Order(28)
    @DisplayName("Cmd 5: Status Grid Gol")
    public void testStatusGridEmpty() {
        String input = "5\n";
        String output = runMain(input);
        assertOutputContains("Reteaua este goala", output);
    }

    @Test
    @Order(29)
    @DisplayName("Cmd 5: Status Grid Cu Componente")
    public void testStatusGridPopulated() {
        String input = "0 solar s1 100\n1 laborator lab1 200\n2 b1 1000\n5\n";
        String output = runMain(input);
        assertOutputContains("Stare Retea: STABILA", output);
        assertOutputContains("Producator s1 (PanouSolar) - PutereBaza: 100.00 - Status: Operational", output);
        assertOutputContains("Consumator lab1 (LaboratorStiintific) - Cerere: 200.00 - Prioritate: 2 - Status: Alimentat", output);
        assertOutputContains("Baterie b1 - Stocare: 0.00/1000.00 - Status: Operational", output);
    }

    @Test
    @Order(30)
    @DisplayName("Cmd 6: Istoric Gol")
    public void testIstoricEmpty() {
        String input = "6\n";
        String output = runMain(input);
        assertOutputContains("Istoric evenimente gol", output);
    }

    @Test
    @Order(31)
    @DisplayName("Cmd 7: Iesire (Mesaj)")
    public void testExitMessage() {
        String input = "";
        String output = runMain(input);
        assertOutputContains("Simulatorul se inchide.", output);
    }

    @Test
    @Order(32)
    @DisplayName("Comanda Invalida")
    public void testInvalidCommand() {
        String input = "8\nabc\n";
        String output = runMain(input);

        assertTrue(output.split("EROARE: Comanda necunoscuta.").length >= 2);
    }

    @Test
    @Order(33)
    @DisplayName("Cmd 3: Scenariu Surplus (Bateriile se incarca)")
    public void testTickSurplus() {
        String input = "0 reactor r1 1000\n" +
                "1 laborator lab1 300\n" +
                "2 b1 5000\n" +
                "3 0.0 0.0\n";
        String output = runMain(input);

        assertOutputContains("TICK: Productie 1000.00, Cerere 300.00. Baterii: 700.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(34)
    @DisplayName("Cmd 3: Scenariu Surplus Mare (Bateriile se umplu, surplusul se pierde)")
    public void testTickSurplusOverflow() {
        String input = "0 reactor r1 2000\n" +
                "1 laborator lab1 500\n" +
                "2 b1 1000\n" +
                "3 0.0 0.0\n";
        String output = runMain(input);

        assertOutputContains("TICK: Productie 2000.00, Cerere 500.00. Baterii: 1000.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(35)
    @DisplayName("Cmd 3: Scenariu Echilibru (Productie = Cerere)")
    public void testTickEchilibru() {
        String input = "0 reactor r1 500\n" +
                "1 laborator lab1 500\n" +
                "2 b1 1000\n" +
                "3 0.0 0.0\n";
        String output = runMain(input);
        assertOutputContains("TICK: Productie 500.00, Cerere 500.00. Baterii: 0.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(36)
    @DisplayName("Cmd 3: Scenariu Deficit Mic (Bateriile acopera)")
    public void testTickDeficitMic() {
        String input = "0 reactor r1 500\n" +
                "1 laborator lab1 800\n" +
                "2 b1 1000\n" +
                "3 0.0 0.0\n" +
                "0 reactor r_charge 300\n" +
                "3 0.0 0.0\n" +
                "4 r_charge false\n" +
                "3 0.0 0.0\n";

        String setup = "2 b1 1000\n" +
                "0 reactor r_charge 500\n" +
                "3 0.0 0.0\n" +
                "4 r_charge false\n" +
                "0 reactor r_prod 500\n" +
                "1 laborator lab1 800\n" +
                "3 0.0 0.0\n";

        String output = runMain(setup);

        assertOutputContains("TICK: Productie 500.00, Cerere 800.00. Baterii: 200.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(37)
    @DisplayName("Cmd 3: Scenariu Triage P3 (Bateriile se golesc, P3 decuplat)")
    public void testTickTriageP3() {
        String input = "0 reactor r1 500\n" +
                "2 b1 200\n" +
                "1 laborator lab1 400\n" +
                "1 iluminat i1 300\n" +
                "3 0.0 0.0\n";

        String setup = "2 b1 200\n" +
                "0 reactor r_charge 200\n" +
                "3 0.0 0.0\n" +
                "4 r_charge false\n" +
                "0 reactor r1 500\n" +
                "1 laborator lab1 400\n" +
                "1 iluminat i1 350\n" +
                "3 0.0 0.0\n";

        String output = runMain(setup);

        String[] outputs = output.split("\n");
        String lastTickOutput = outputs[outputs.length-2];

        assertOutputContains("TICK: Productie 500.00, Cerere 750.00. Baterii: 0.00 MW. Decuplati: [i1]", lastTickOutput);
    }

    @Test
    @Order(38)
    @DisplayName("Cmd 3: Scenariu Triage P2+P3 (Bateriile se golesc, P2 si P3 decuplati)")
    public void testTickTriageP2P3() {
        String setup = "2 b1 100\n" +
                "0 reactor r1 500\n" +
                "1 suport_viata sv1 400\n" +
                "1 laborator lab1 300\n" +
                "1 iluminat i1 200\n" +
                "3 0.0 0.0\n";

        String setupP2P3 = "2 b1 100\n" +
                "0 reactor r1 500\n" +
                "1 suport_viata sv1 400\n" +
                "1 laborator lab1 300\n" +
                "1 iluminat i1 201\n" +
                "3 0.0 0.0\n";

        String setupFinal = "2 b1 100\n" +
                "0 reactor r1 500\n" +
                "1 suport_viata sv1 400\n" +
                "1 laborator lab1 300\n" +
                "1 iluminat i1 300\n" +
                "3 0.0 0.0\n";

        String setupP2P3Final = "2 b1 100\n" +
                "0 reactor r1 500\n" +
                "1 suport_viata sv1 400\n" +
                "1 laborator lab1 300\n" +
                "1 iluminat i1 101\n" +
                "3 0.0 0.0\n";

        String setupTriageAll = "2 b1 100\n" +
                "0 reactor r1 500\n" +
                "1 suport_viata sv1 400\n" +
                "1 laborator lab1 300\n" +
                "1 laborator lab2 300\n" +
                "1 iluminat i1 101\n" +
                "3 0.0 0.0\n";

        String output = runMain(setupTriageAll);

        boolean foundCase1 = output.contains("Decuplati: [i1, lab2, lab1]") ||
                output.contains("Decuplati: [i1, lab1, lab2]");

        String setupTriageAll2 = "2 b1 100\n" +
                "0 reactor r1 500\n" +
                "1 suport_viata sv1 400\n" +
                "1 laborator lab1 300\n" +
                "1 laborator lab2 101\n" +
                "1 iluminat i1 101\n" +
                "3 0.0 0.0\n";

        String output2 = runMain(setupTriageAll2);

        boolean P3decuplat = output2.contains("i1");
        boolean P2decuplat1 = output2.contains("lab1");
        boolean P2decuplat2 = output2.contains("lab2");

        assertOutputContains("Decuplati: [i1", output2);
        assertTrue(P2decuplat1 || P2decuplat2, "Cel putin un P2 (lab1 sau lab2) ar trebui decuplat.\nOutput: " + output2);
    }

    @Test
    @Order(39)
    @DisplayName("Cmd 3: Scenariu BLACKOUT (Productie + Baterii < Cerere P1)")
    public void testTickBlackout() {
        String input = "0 reactor r1 500\n" +
                "2 b1 100\n" +
                "1 suport_viata sv1 800\n" +
                "1 laborator lab1 300\n" +
                "1 iluminat i1 200\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("BLACKOUT! SIMULARE OPRITA.", output);
    }

    @Test
    @Order(40)
    @DisplayName("Cmd 3: Scenariu Post-Blackout (Comenzile esueaza)")
    public void testPostBlackout() {
        String input = "0 reactor r1 100\n" +
                "1 suport_viata sv1 800\n" +
                "3 0.0 0.0\n" +
                "0 solar s1 1000\n" +
                "5\n" +
                "6\n";

        String output = runMain(input);
        assertOutputContains("BLACKOUT! SIMULARE OPRITA.", output);

        assertOutputContains("EROARE: Reteaua este in BLACKOUT. Simulare oprita.", output);

        assertOutputContains("Stare Retea: BLACKOUT", output);

        assertOutputContains("Tick 1: BLACKOUT! SIMULARE OPRITA.", output);
    }

    @Test
    @Order(41)
    @DisplayName("Cmd 3: Tick cu Producator Defect")
    public void testTickProducatorDefect() {
        String input = "0 reactor r1 1000\n" +
                "1 laborator lab1 500\n" +
                "4 r1 false\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 0.00, Cerere 500.00. Baterii: 0.00 MW. Decuplati: [lab1]", output);
    }

    @Test
    @Order(42)
    @DisplayName("Cmd 3: Tick cu Consumator Defect")
    public void testTickConsumatorDefect() {
        String input = "0 reactor r1 1000\n" +
                "1 laborator lab1 500\n" +
                "4 lab1 false\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 1000.00, Cerere 0.00. Baterii: 0.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(43)
    @DisplayName("Cmd 3: Tick cu Baterie Defecta (Incarcare)")
    public void testTickBaterieDefectaIncarcare() {
        String input = "0 reactor r1 1000\n" +
                "1 laborator lab1 500\n" +
                "2 b1 5000\n" +
                "4 b1 false\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);

        assertOutputContains("TICK: Productie 1000.00, Cerere 500.00. Baterii: 0.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(44)
    @DisplayName("Cmd 3: Tick cu Baterie Defecta (Descarcare)")
    public void testTickBaterieDefectaDescarcare() {
        String input = "2 b1 5000\n" +
                "0 reactor r_charge 1000\n" +
                "3 0.0 0.0\n" +
                "4 r_charge false\n" +
                "4 b1 false\n" +
                "1 laborator lab1 500\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);

        String[] outputs = output.split("\n");
        String lastTickOutput = outputs[outputs.length-2];

        assertOutputContains("TICK: Productie 0.00, Cerere 500.00. Baterii: 1000.00 MW. Decuplati: [lab1]", lastTickOutput);
    }

    @Test
    @Order(45)
    @DisplayName("Cmd 3: Tick Triage Multiplu P3 si P2")
    public void testTickTriageMultiplu() {
        String input = "0 reactor r1 100\n" +
                "1 suport_viata sv1 100\n" +
                "1 laborator lab1 50\n" +
                "1 laborator lab2 50\n" +
                "1 iluminat i1 50\n" +
                "1 iluminat i2 50\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);

        assertOutputContains("Decuplati: [i", output);
        assertOutputContains("i2", output);
        assertOutputContains("lab1", output);
        assertOutputContains("lab2", output);
    }

    @Test
    @Order(46)
    @DisplayName("Cmd 3: Tick Factori Soare/Vant (Productie Corecta)")
    public void testTickFactori() {
        String input = "0 solar s1 100\n" +
                "0 turbina t1 100\n" +
                "0 reactor r1 100\n" +
                "2 b1 100\n" +
                "1 laborator lab1 200\n" +
                "3 0.8 0.5\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 230.00, Cerere 200.00. Baterii: 30.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(47)
    @DisplayName("Cmd 3: Tick Factori Zero (Doar Reactor)")
    public void testTickFactoriZero() {
        String input = "0 solar s1 100\n" +
                "0 turbina t1 100\n" +
                "0 reactor r1 100\n" +
                "1 laborator lab1 101\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 100.00, Cerere 101.00. Baterii: 0.00 MW. Decuplati: [lab1]", output);
    }

    @Test
    @Order(48)
    @DisplayName("Cmd 3: Triage P3 Partial (Un P3 decuplat e suficient)")
    public void testTickTriagePartialP3() {
        String input = "0 reactor r1 90\n" +
                "1 iluminat i1 50\n" +
                "1 iluminat i2 50\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        boolean decuplatI1 = output.contains("Decuplati: [i1]");
        boolean decuplatI2 = output.contains("Decuplati: [i2]");
        assertTrue(decuplatI1 || decuplatI2, "Unul din P3 (i1 sau i2) ar trebui decuplat.\nOutput: " + output);
        assertFalse(decuplatI1 && decuplatI2, "Doar UNUL din P3 ar trebui decuplat.\nOutput: " + output);
    }

    @Test
    @Order(49)
    @DisplayName("Cmd 3: Eroare Factori Invalizi (Text)")
    public void testTickInvalidFactorsText() {
        String input = "3 abc def\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Factori invalizi", output);
    }

    @Test
    @Order(50)
    @DisplayName("Cmd 3: Eroare Factori Invalizi (Lipsa)")
    public void testTickInvalidFactorsMissing() {
        String input = "3 0.5\n";
        String output = runMain(input);
        assertOutputContains("EROARE: Format comanda invalid", output);
    }

    @Test
    @Order(51)
    @DisplayName("Lant: Incarcare Baterii Multiple")
    public void testChainIncarcareBateriiMultiple() {
        String input = "0 reactor r1 1000\n" +
                "2 b1 300\n" +
                "2 b2 300\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 1000.00, Cerere 0.00. Baterii: 600.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(52)
    @DisplayName("Lant: Descarcare Baterii Multiple")
    public void testChainDescarcareBateriiMultiple() {
        String input = "2 b1 300\n" +
                "2 b2 300\n" +
                "0 reactor r_charge 600\n" +
                "3 0.0 0.0\n" +
                "4 r_charge false\n" +
                "1 laborator lab1 500\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);

        String[] outputs = output.split("\n");
        String lastTickOutput = outputs[outputs.length-2];

        assertOutputContains("TICK: Productie 0.00, Cerere 500.00. Baterii: 100.00 MW. Decuplati: []", lastTickOutput);
    }

    @Test
    @Order(53)
    @DisplayName("Lant: Reparare Componenta si Revenire Retea")
    public void testChainReparareSiRevenire() {
        String input = "0 reactor r1 1000\n" +
                "1 laborator lab1 800\n" +
                "2 b1 500\n" +
                "4 r1 false\n" +
                "3 0.0 0.0\n" +
                "4 r1 true\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 0.00, Cerere 800.00. Baterii: 0.00 MW. Decuplati: [lab1]", output);
        assertOutputContains("Componenta r1 este acum operationala", output);
        assertOutputContains("TICK: Productie 1000.00, Cerere 800.00. Baterii: 200.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(54)
    @DisplayName("Lant: Triage P1 Neafectat")
    public void testChainTriageP1Neafectat() {
        String input = "0 reactor r1 500\n" +
                "1 suport_viata sv1 500\n" +
                "1 laborator lab1 500\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 500.00, Cerere 1000.00. Baterii: 0.00 MW. Decuplati: [lab1]", output);

    }

    @Test
    @Order(55)
    @DisplayName("Cmd 5: Formatare Status (Defect, Decuplat)")
    public void testStatusFormatDefectDecuplat() {
        String input = "0 reactor r1 100\n" +
                "1 laborator lab1 500\n" +
                "2 b1 1000\n" +
                "4 r1 false\n" +
                "3 0.0 0.0\n" +
                "5\n";

        String output = runMain(input);
        assertOutputContains("Producator r1 (ReactorNuclear) - PutereBaza: 100.00 - Status: Defect", output);
        assertOutputContains("Consumator lab1 (LaboratorStiintific) - Cerere: 500.00 - Prioritate: 2 - Status: Decuplat", output);
        assertOutputContains("Baterie b1 - Stocare: 0.00/1000.00 - Status: Operational", output);
    }

    @Test
    @Order(56)
    @DisplayName("Cmd 6: Istoric Evenimente (Contine Tick si Blackout)")
    public void testIstoricEvenimenteContinut() {
        String input = "0 reactor r1 100\n" +
                "3 0.0 0.0\n" +
                "1 suport_viata sv1 200\n" +
                "3 0.0 0.0\n" +
                "6\n";
        String output = runMain(input);
        assertOutputContains("Tick 2: BLACKOUT! SIMULARE OPRITA.", output);
    }

    @Test
    @Order(57)
    @DisplayName("Cmd 0: Adaugare ID lung (Succes)")
    public void testAddIdLung() {
        String id = "reactor_fisiune_model_X_sector_Alpha";
        String input = "0 reactor " + id + " 1000\n";
        String output = runMain(input);
        assertOutputContains("S-a adaugat producatorul " + id + " de tip reactor", output);
    }

    @Test
    @Order(58)
    @DisplayName("Cmd 3: Triage P2 Neafectat (P3 e suficient)")
    public void testTriageP3Suficient() {
        String input = "0 reactor r1 100\n" +
                "1 laborator lab1 50\n" +
                "1 iluminat i1 101\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("Decuplati: [i1]", output);
    }

    @Test
    @Order(59)
    @DisplayName("Cmd 3: Ticks Succesive (Acumulare Baterie)")
    public void testTicksSuccesiveAcumulare() {
        String input = "0 reactor r1 100\n" +
                "2 b1 1000\n" +
                "3 0.0 0.0\n" +
                "3 0.0 0.0\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);
        assertOutputContains("TICK: Productie 100.00, Cerere 0.00. Baterii: 100.00 MW. Decuplati: []", output);
        assertOutputContains("TICK: Productie 100.00, Cerere 0.00. Baterii: 200.00 MW. Decuplati: []", output);
        assertOutputContains("TICK: Productie 100.00, Cerere 0.00. Baterii: 300.00 MW. Decuplati: []", output);
    }

    @Test
    @Order(60)
    @DisplayName("Cmd 3: Ticks Succesive (Descarcare Baterie)")
    public void testTicksSuccesiveDescarcare() {
        String input = "2 b1 1000\n" +
                "0 reactor r_charge 500\n" +
                "3 0.0 0.0\n" +
                "4 r_charge false\n" +
                "1 laborator lab1 200\n" +
                "3 0.0 0.0\n" +
                "3 0.0 0.0\n";

        String output = runMain(input);

        String[] outputs = output.split("\n");
        String tick2 = outputs[outputs.length-3];
        String tick3 = outputs[outputs.length-2];

        assertOutputContains("TICK: Productie 0.00, Cerere 200.00. Baterii: 300.00 MW. Decuplati: []", tick2);
        assertOutputContains("TICK: Productie 0.00, Cerere 200.00. Baterii: 100.00 MW. Decuplati: []", tick3);
    }
}