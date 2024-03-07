# Lab 02 - ASMD
## **Task 1 - Reorganize.**
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
    public void initMock() { super.initMock(); }

    @Test
    @DisplayName("Device is initially off")
    void testInitiallyOff() {
        FailingPolicy dummyFailingPolicy = mock(FailingPolicy.class);
        device = new StandardDevice(dummyFailingPolicy);
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


## **Task 2 - Tooling.**
Specification: Experiment with installing/using Mockito with Scala and/or in VSCode. Is VSCode better at all here? What’s the state of mocking
technologies for Scala?

### Task 2 - Implementazione 

Link utile per vscode: [PippoBaudo] (https://stackoverflow.com/questions/56674827/junit-in-visual-studio-code)
