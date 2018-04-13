## &nbsp;

# Marktübersicht

Welche Hilfestellungen gibt es?

--

## Module

- Ein Projekt in mehrere Teilprojekte mit definierten Abhängigkeiten aufteilen
- Multi-Module Projekte mit Maven oder Gradle
 

--

## Java 9 Module

module-info.java:

    module org.gradle.fairy.tale.bears {
        requires org.gradle.actors;
        requires transitive org.gradle.fairy.tale;
        requires org.gradle.fairy.tale.formula;
    
        exports org.gradle.fairy.tale.bears;
    }

Quelle: https://guides.gradle.org/building-java-9-modules/

--

## Gradle java-library plugin

--

## maven-dependency-plugin

- maven dependendcy:analyze
  - https://maven.apache.org/plugins/maven-dependency-plugin/source-repository.html
  - https://github.com/apache/maven-dependency-analyzer/tree/maven-dependency-analyzer-1.8 (verwendet ASM)

- gradle-dependency-analyze
  - https://stackoverflow.com/questions/48377905/is-there-a-gradle-plugin-equivalent-of-mvn-dependencyanalyze 

--

## JDeps (seit JDK 8)


--

## IDE's

- IntelliJ IDEA 'Analyze Dependencies ...'
 
--

## Kommerzielle Tools

- Sonargraph-Architect (https://www.hello2morrow.com/products/sonargraph/architect9)
- Structure101 (http://structure101.com/)

--

## Opensource-Tools

- Dependometer von Valtech (https://sourceforge.net/projects/dependometer/)
- JDepend (mit Plugins für Eclipse/Maven/etc.) (http://mcs.une.edu.au/doc/jdepend/docs/JDepend.html)
- degraph von Jens Schauder (http://blog.schauderhaft.de/degraph/)
- Diverse Open-Source Tools zur Visualisierung von Abhängigkeiten (https://dzone.com/articles/dependency-analysis-and-1)
