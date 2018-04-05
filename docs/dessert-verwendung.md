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

<img src="images/dessert-components.svg" class="plain" width="594" 
    style="position: fixed; right: -15%; top: 15%"/>

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
