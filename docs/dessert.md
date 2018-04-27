## &nbsp;

# Dessert

Dependency Assertions Library für Unit-Tests

Features:
- Prüfung von Abhängigkeitsregeln
- Erkennung von zyklischen Abhängigkeiten

--

## Dessert's Designziele

- Keine Abhängigkeiten außer JDK 6 (oder neuer)
- Einfache und intuitive API (motiviert von AssertJ)
- Assertions sind robust gegenüber Refactorings 
  (keine Strings für Klassen- oder Packagenamen notwendig)
- Einfache Integration mit anderen Test- oder Assertion-Frameworks
- Speed

--

## Einschränkungen

dessert analysiert .class Dateien

- (+) Keine Sourcen notwendig
- (+) Ablageort der Sourcen egal
- (+) Sprachunabhängig (Groovy, Kotlin, Scala, ...)
- (--) Erkennt keine Laufzeitabhängigkeiten
- (--) Evtl. hat Compiler Abhängigkeiten wegoptimiert

--

## Eine Klasse im Sinne von dessert

ist somit alles, was als eigene .class Datei repräsentiert wird: 

- einfache Java Klassen
- Interfaces
- Annotationen
- jede Art von Innerclasses
- Enums 

--

## Abhängigkeit zwischen Klassen

'Klasse' X hängt von der 'Klasse' Y ab, wenn X 'Klasse' Y verwendet, also

- X erweitert oder implementiert Y
- X hat ein Feld vom Typ Y
- X verwendet Y in einer Methodensignatur (Parameter, Rückgabewert, Throws)
- X hat lokale Variable vom Typ Y (nicht per Reflection ermittelbar)
- X verwendet (statische) Methode von Y (direkt oder als Methodenreferenz)
- X wirft Y
- X implementiert Y über einen λ-Ausdruck
- X ist Innerclass von Y oder umgekehrt
- X verwendet generischen Typ von Y
- X ist mit Y annotiert
- X verwendet Y als Parameter einer Annotation (Unterschied zu jdeps)
- (Imports spielen keine Rolle)

--

## Elemente der dessert API

- **Slice** (Ausschnitt aus der Menge aller Klassen)
  - Methoden: with, without, slice
- **SliceEntry** ('Klasse', d. h. .class Datei)
  - Methoden: getClassName, getClazz
- **SliceContext** (Flyweight, Einstiegspunkt)
  - Methoden: packageOf, packageTreeOf, sliceOf
- **SliceAssertions** (statische Methoden, analog zu [AssertJ](https://joel-costigliola.github.io/assertj/))
  - Methoden: assertThat, dessert
  - Fluent-API: doesNotUse, usesOnly, uses .and ...only

--

## Spring Batch Architektur

    @Test
    public void testArchitecture() {
        SliceContext sc = new SliceContext();
        Slice springBatch = sc.packageTreeOf("org.springframework.batch");
        Slice springBatchCore = sc.packageTreeOf(Job.class);
        Slice springBatchTest = sc.packageTreeOf(StepRunner.class);
        Slice springBatchInfrastructure =
                springBatch.without(springBatchCore).without(springBatchTest);
        SliceAssertions.assertThat(springBatchInfrastructure).doesNotUse(springBatchCore);
    }

<!-- .slide style="position:relative;" -->
<img src="images/spring-batch-layers.png" class="plain" style="position: absolute; right: 4ex;"/>

--

## Dessert-Komponenten

    @Test
    public void testDessertComponents() {
        SliceContext sc = new SliceContext();

        Slice java = sc.packageTreeOf(String.class)
                .with(sc.packageTreeOf(URL.class))
                .with(sc.packageTreeOf(File.class))
                .with(sc.packageTreeOf(Collection.class));

        String prefix = "de.spricom.dessert";
        Slice assertions = sc.packageTreeOf(prefix + ".assertions");
        Slice groups = sc.packageTreeOf(prefix + ".groups");
        Slice slicing = sc.packageTreeOf(prefix + ".slicing");
        Slice resolve = sc.packageTreeOf(prefix + ".resolve");
        Slice util = sc.packageTreeOf(prefix + ".util");
        Slice classfile = sc.packageTreeOf(prefix + ".classfile");

        dessert(assertions).usesOnly(groups, slicing, util, java);
        dessert(groups).usesOnly(slicing, java);
        dessert(slicing).usesOnly(resolve, util, classfile, java);
        dessert(resolve).usesOnly(util, classfile, java);
        dessert(classfile).usesOnly(java);
    }

<img src="images/dessert-components.svg" class="plain" width="312" 
    style="position: fixed; right: 0; top: 10%"/>

--

## MVP-Regel

Man kann Slices auch über Arten von Klassen bilden:

    private SliceContext sc = new SliceContext();
    private Slice mvp = sc.packageTreeOf(MvpSampleTest.class.getPackageName() + ".mvp");

    @Test
    public void testUsingNamingConvention() {
        Slice presenters = mvp.slice(se -> se.getClassname().endsWith("Presenter"));
        Slice views = mvp.slice(se -> se.getClassname().endsWith("ViewImpl"));
        assertThat(presenters).doesNotUse(views);
    }

    @Test
    public void testUsingImplementedIterfaces() {
        Slice presenters = mvp.slice(se -> Presenter.class.isAssignableFrom(se.getClazz()));
        Slice views = mvp.slice(se -> ViewBase.class.isAssignableFrom(se.getClazz()));
        assertThat(presenters).doesNotUse(views);
    }

GWT: Presenter sollen keine UI-Elemente (JavaScript) verwenden, damit die UI-Logik per
Unittest testbar ist. 

--

## Zyklenfreiheit

Packages von dessert sind zyklenfrei:

    @Test
    public void testCycleFree() throws IOException {
        Slice slice = new SliceContext().packageTreeOf("de.spricom.dessert");
        SliceGroup<PackageSlice> subPackages = SliceGroup.splitByPackage(slice);
        SliceAssertions.dessert(slice).splitByPackage().isCycleFree();
    }

Es sind auch andere Gruppierungen als nach package möglich.

--

## Schachtelungsregel

Packages dürfen auf tiefer geschachtelte Packages zugreifen, 
umgekehrt aber nicht.

    @Test
    public void testPackageDependencies() throws IOException {
        Slice slice = new SliceContext().packageTreeOf("de.spricom.dessert");
        SliceGroup<PackageSlice> packages = SliceGroup.splitByPackage(slice);

        packages.forEach(pckg -> SliceAssertions.assertThat(pckg)
                .doesNotUse(pckg.getParentPackage(packages)));
    }

--

## Abhängigkeiten explorativ ergründen

Welche Abhängigkeiten hat Spring-Batch-Core?

    @Test
    public void testCoreDependencies() {
        SliceContext sc = new SliceContext();
        Slice springBatchCore = sc.packageTreeOf(Job.class);
        SliceAssertions.assertThat(springBatchCore)
                .uses(sc.packageTreeOf("org.springframework"))
                .and(sc.packageTreeOf("java"))
                .and(sc.packageTreeOf("org.apache.commons.logging"))
                .and(sc.packageTreeOf("org.aspectj.lang.annotation"))
                .and(sc.packageTreeOf("org.aopalliance"))
                .and(sc.packageTreeOf("com.thoughtworks.xstream"))
                .and(sc.packageTreeOf("com.fasterxml.jackson"))
                .and(sc.packageTreeOf(Element.class))
                .only();
    }

--

## Ausblick

- Besserer SliceGroup-Support (Simulation von Refactorings)
- API für Modulbibliotheken
- Java 9 Moduldefinitionen auswerten
- Support für [@API Guardian](https://github.com/apiguardian-team/apiguardian)
- Generierung von Abhängigkeitsdiagrammen für Softwarebausteine