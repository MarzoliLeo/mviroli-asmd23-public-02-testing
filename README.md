# Lab 02 - ASMD
## **Task 1 - REORGANISE.**
Specification: The repo has a device example, discussed in room. Play with it. Reorganise tests, which are currently grouped by type of mock. Seek
for the perfect unit test!
### Task 1 - Implementazione

Per il refactoring dei test ho deciso di adattare il codice ai principi DRY e KISS, raggruppandolo cercando di evitare la ridondanza. Inoltre, ho cercato di applicare il più possibile i principi SOLID. Focalizzandomi maggiormente sul concetto di separation of responsability.

Ho da prima creato una interfaccia [TestTemplate.java](src/test/java/devices/TestTemplate.java) e una sua implementazione [TestTemplateImpl.java](src/test/java/devices/TestTemplateImpl.java) e quest'ultima appare nel seguente modo: 
```Java
public class TestTemplateImpl implements TestTemplate {

    // Task 1 Test - with Java
    Device device;
    FailingPolicy failingPolicy;

    // Task 2 Test - with Scala
    DeviceScala deviceScala;
    FailingPolicyScala failingPolicyScala;

    @Override
    public void initMock() {
        this.failingPolicy = mock(FailingPolicy.class);
        this.device = new StandardDevice(failingPolicy);
    }

    @Override
    public void initSpy() {
        this.failingPolicy = spy(new RandomFailing());
        this.device = new StandardDevice(failingPolicy);
    }

    @Override
    public void initMockScala() {
        this.failingPolicyScala = mock(FailingPolicyScala.class);
        this.deviceScala = DeviceScala.apply(failingPolicyScala);
    }

    @Override
    public void initSpyScala() {
        this.failingPolicyScala = spy(FailingPolicyScala.apply());
        this.deviceScala =  DeviceScala.apply(failingPolicyScala);
    }
}
```
Ciò che questa classe cerca di fare è fornire ai test tramite dei metodi "init" le dipendenze utilizzate da ciascuno di essi, inizializzandole una volta soltanto, evitando così ridondanza di codice. Siccome nel task 2 si è dovuto implementare una versione in Scala questa classe implementa anche le dipendenze alle classi scala sviluppate.

Poi, ho suddiviso in diverse classi quelli che prima apparivano test "@nested" e raggruppati in base alla categoria che essi volevano rappresentare. Ciascuno in una classe apposita. Di seguito riporto l'implementazione di quelli che ritengo più rilevanti notare, premettendo che gli altri sono stati implementati nella stesso modo.

```Java 
class DummiesShowcaseTest extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMock() { super.initMock(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        assertFalse(device.isOn());
    }

}
```
```Java
class SpiesShowcaseTest extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initSpy() { super.initSpy(); }

    @Test
    @DisplayName("AttemptOn is called as expected")
    void testReset() {
        device.isOn();
        verifyNoInteractions(failingPolicy);
        try {
            device.on();
        } catch (IllegalStateException e) {
        }
        verify(failingPolicy).attemptOn();
        device.reset();
        assertEquals(2, mockingDetails(failingPolicy).getInvocations().size());
    }

}
```
Ciascuno di essi implementa l'interfaccia descritta precedentemente. Facendo un "@Override" del metodo "init" che si preferisce utilizzare. In questo modo, tramite "super" posso accedere alle dipendenze della superclasse.

```Java
class FakesShowcaseTest extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMock() {
        super.initMock();
        when(failingPolicy.attemptOn()).thenReturn(true, true, false);
        when(failingPolicy.policyName()).thenReturn("mock");
    }

    @Test
    @DisplayName("Device switch on and off until failing")
    void testSwitchesOnAndOff() {
        IntStream.range(0, 2).forEach(i -> {
            device.on();
            assertTrue(device.isOn());
            device.off();
            assertFalse(device.isOn());
        });
        assertThrows(IllegalStateException.class, () -> device.on());
    }

}
```
Quelli visti precendentemente erano casi base. In questa classe di test si può vedere come espandere il metodo init per eventuali personalizzazioni dovute alla fase di testing.


## **Task 2 - TOOLING.**
Specification: Experiment with installing/using Mockito with Scala and/or in VSCode. Is VSCode better at all here? What’s the state of mocking
technologies for Scala?

### Task 2 - Implementazione 
Per l'implementazione in Scala come visto precedentemente in [TestTemplateImpl.java](src/test/java/devices/TestTemplateImpl.java) si è implementato la business logic tramite le classi [AdderScala.scala](src/main/java/coverage/AdderScala.scala), [DeviceScala.scala](src/main/java/devices/DeviceScala.scala) e [FailingPolicyScala.scala](src/main/java/devices/FailingPolicyScala.scala). Di seguito vengono riportate le loro implementazioni.


```Scala
trait AdderScala {
  def add(i1: Int, i2: Int): Int
}

object AdderScala {
  private class AdderScalaImpl extends AdderScala {
    override def add(i1: Int, i2: Int): Int = (i1, i2) match {

      case (x, y)
        if x > 0 && y > 0
      => x + y
      case _ => -1
    }
  }
  
  def apply(): AdderScala = new AdderScalaImpl
}
```
```Scala
trait DeviceScala {

  def on(): Unit

  def off(): Unit

  def isOn(): Boolean

  def reset(): Unit

}

object DeviceScala {
  private class StandardDeviceImpl(failingPolicy: FailingPolicyScala) extends DeviceScala {
    private var onFlag: Boolean = false

    override def on(): Unit =
      if (!failingPolicy.attemptOn())
        throw new IllegalStateException()
      else
        onFlag = true


    override def off(): Unit =
      onFlag = false

    override def isOn(): Boolean =
      onFlag

    override def reset(): Unit = {
      off()
      failingPolicy.reset()
    }

    override def toString: String =
      s"DeviceScala{policy=${failingPolicy.policyName()}, on=$onFlag}"

  }

  def apply(failingPolicy: FailingPolicyScala): DeviceScala = new StandardDeviceImpl(Objects.requireNonNull(failingPolicy))

}
```
```Scala
trait FailingPolicyScala {

  def attemptOn(): Boolean

  def reset(): Unit

  def policyName(): String

}

object FailingPolicyScala {
  private class RandomFailingImpl extends FailingPolicyScala {

    private val random = new Random
    private var failed = false

    override def attemptOn(): Boolean = {
      failed = failed || random.nextBoolean()
      !failed
    }

    override def reset(): Unit =
      failed = false

    override def policyName(): String =
      "random"

  }

  def apply(): FailingPolicyScala = new RandomFailingImpl

}
```

Queste vengono poi utilizzate tramite gli appositi "init" all'interno di ciascun test che vanno ad utilizzare le nuove dipendenze scala. Per vedere la lista dei [Test](src/test/java/devices). La struttura di base è la stessa descritta nel Task 1, cambiano soltanto i riferimenti. Di seguito si riporta un esempio di un test che utilizza la business logic Scala. 
```Java
class DummiesShowcaseTestScala extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initMockScala() { super.initMockScala(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        assertFalse(deviceScala.isOn());
    }

}
```
```Java
class SpiesShowcaseTestScala extends TestTemplateImpl {

    @Override
    @BeforeEach
    public void initSpyScala() { super.initSpyScala(); }

    @Test
    @DisplayName("AttemptOn is called as expected")
    void testReset() {
        deviceScala.isOn();
        verifyNoInteractions(failingPolicyScala);
        try {
            deviceScala.on();
        } catch (IllegalStateException e) {
        }
        verify(failingPolicyScala).attemptOn();
        deviceScala.reset();
        assertEquals(2, mockingDetails(failingPolicyScala).getInvocations().size());
    }

}
```

Per quanto riguarda l'utilizzo di Visual Studio Code, non c'è miglioramento riguardo l'utilizzo di Mockito e JUnit. Anzi, penso sia meglio IntelliJ IDEA per la facilità che questo mette a disposizione nel rilevare immediatamente un sbt file e importare le dipendenze, cosa che non è immediata in VS Code in quanto per rilevare un sbt file bisogna prima scaricare Metals (plugin di Scala) e cambiare le preferenze in sbt rispetto a bloop. Per non parlare del fatto che VSCode non riconosce immediatamente il linguaggio Java e ha bisogno di una serie di plugin integrati in uno solo chiamato "Extension Pack For Java". Questo non basterà per far eseguire Java, ma poi navigando nel file "settings.json" della cartella ".vscode" bisogna poi definire la JDK da voler utilizzare. In sostanza, per numero di passaggi ritengo più comodo testare JUnit e Mockito tramite IntelliJ IDEA.

## **Task 3 - REENGINEER.**
Specification: Take an existing implemented small app with GUI, e.g. an OOP exam. Add a requirement that it outputs to console some relevant
messages, through a log class. Now you have an App with at least 3 classes (GUI, Model, Log). How would you write integration tests
for it? Search here: https://bitbucket.org/mviroli/oop2023-esami (2023, 2022,. . . )

### Task 3 - Implementazione 
Ho deciso di implementare il codice a01a.sol2 del repo bitbucket. Una volta importato nel mio progetto, ciò che si richiedeva era implementare una classe di [Log.java](src/main/java/sol2/Log.java). Questa appare nel seguente modo: 
```Java
public class Log {
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }
}
```
Utilizzando un riferimento alla seguente classe è possibile definire dei log da visualizzare durante l'esecuzione. Ecco un esempio di utilizzo in [LogicImpl.java](src/main/java/sol2/LogicImpl.java).
```Java
 @Override
    public Optional<Integer> hit(Position position) {
        if (this.isOver()){
            this.log.info("Game over. Exiting...");
            return Optional.empty();
        }
        if (this.moving || startMoving(position)){
            this.moving = true;
            this.moveMarks();
            return Optional.empty();
        }
        this.marks.add(position);
        this.log.info("Cell marked at position: " + position);
        return Optional.of(this.marks.size());
    }
```

Implementando ciò si disponeva di una applicazione suddivisa in GUI, Model, Log. E si era richiesto di scrivere un integration test ed esso si può trovare dentro [IntegrationTest.java](src/test/java/integration_tests/IntegrationTest.java). Questo appare nel seguente modo:
```Java
class IntegrationTest {

    @Test
    void testGameIntegration() {
        Log logMock = mock(Log.class);
        // Arrange
        GUI guiSpy = spy(new GUI(10, logMock));
        LogicImpl logicMock = mock(LogicImpl.class);

        guiSpy.setLogic(logicMock);

        // Stubbing the behavior of LogicImpl
        when(logicMock.hit(any())).thenReturn(Optional.of(1));
        when(logicMock.isOver()).thenReturn(false);

        // Act
        // Simulate user actions by clicking on a button
        // Get the first button in the cells map of guiSpy
        JButton button = guiSpy.getCells().keySet().iterator().next();

        // Simulate a click on the button
        guiSpy.handleButtonClick(button);
        button.doClick(); // Simulate a click on the first button

        // Assert
        // Verify that the expected interactions between GUI, LogicImpl, and Log occurred
        verify(logMock, atLeastOnce()).info(anyString());
        verify(logMock, never()).error(anyString());
        verify(logicMock, atLeastOnce()).hit(any(Position.class));
        verify(logicMock, never()).moveMarks();
        verify(logicMock, atLeastOnce()).getMark(any(Position.class));

        // Verify that the GUI updated the button text
        assertEquals("1", guiSpy.getCells().keySet().iterator().next().getText());

        // Verify that the GUI did not exit the application (isOver is false)
        assertFalse(guiSpy.isGameFinished());

        // Verify Log messages
        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(logMock, atLeastOnce()).info(logCaptor.capture());

    }
}
```
In sintesi, il test definisce una GUI, in cui vengono "iniettati" due mock: uno di Log e uno di LogicImpl. Preferisco l'utilizzo di uno spy in quanto mi permette di eseguire l'applicazione e avere un riscontro visivo di ciò che sta succedendo, cosa che non accade quando utilizzi un mock. All'interno del test definisco una logica minimale che si focalizzi soltanto sul verificare che le dipendenze ci siano e che l'applicazione funzioni come dovrebbe simulando il comportamento di un utente. In questo caso la complessità dell'applicazione era banale, siccome le funzionalità erano limitate, anche i Log lo erano. Integrare i log nel testing è risultato difficile. Inoltre, ho imparato che Mockito non ama i metodi statici. Non a caso ho dovuto ridefinire la mia classe Log nel corso dello sviluppo per accedervi tramite riferimento.

## **Task 4 - GUI-TESTER.**
Specification: Generally, GUIs are a problem with testing. How do we test them? How do we automatise as most as possible testing of an app with a
GUI? Play with a simple example and derive some useful consideration.

### Task 3 - Implementazione 
