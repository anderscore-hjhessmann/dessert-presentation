package com.anderscore.goldschmiede.dessert;

import de.spricom.dessert.assertions.SliceAssertions;
import de.spricom.dessert.groups.PackageSlice;
import de.spricom.dessert.groups.SliceGroup;
import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.slicing.SliceContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import static de.spricom.dessert.assertions.SliceAssertions.dessert;

public class DessertComponentsTest {

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

    @Test
    public void testCycleFree() throws IOException {
        Slice slice = new SliceContext().packageTreeOf("de.spricom.dessert");
        SliceGroup<PackageSlice> subPackages = SliceGroup.splitByPackage(slice);
        dessert(slice).splitByPackage().isCycleFree();
    }

    @Test
    public void testPackageDependencies() throws IOException {
        Slice slice = new SliceContext().packageTreeOf("de.spricom.dessert");
        SliceGroup<PackageSlice> packages = SliceGroup.splitByPackage(slice);

        packages.forEach(pckg -> SliceAssertions.assertThat(pckg)
                .doesNotUse(pckg.getParentPackage(packages)));
    }

}
