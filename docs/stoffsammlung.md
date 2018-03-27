# Wie alles begann ...

Ausgangssituation:
- 2002
- EJB 2.0 (OC4J)
- Webanwendung mit Servlet+HTML (später Struts)
- JBuilder, später Eclipse

--

Probleme
- Unittests sehr aufwendig
  - Umfangreiches Setup
  - Für Tests musste immer die ganze Anwendung lauffähig sein
  - Tests waren sehr fragil und mussten ständig angepasst werden
  - Lange Laufzeiten
- Wiederverwendung von Komponenten unmöglich
  - Ein Source-Repository (ein Projekt in der IDE)
  - Viele IF/ELSE
  - Ganze Sourcezweige wurden kopiert

Note: Suppy-Chain-Lösung (Lieferantenportal, Verschiffung), später Handwerkerportal

--

Dann kam ein Berater...

Ursachen
  - Packages nach Inhalten sortiert
  - Unklare Zuordnung von Packages zu Funktionsblöcken
  - Abhängigkeiten nicht klar definiert
  - Zirkuläre Abhängigkeiten
  - Hotspot

Note: Valtech mit Dependometer, Funktionsblöcke und Abhängigkeiten mussten als XML definiert werden

--

Wie es weiterging...
  - Umfangreiches Refactoring
  - CI (Report hat keiner beachtet)

---

- Was ist strukturelle Architektur
  - Schichten
  - Vertical Slices
  - Funktionsblöcke
  - Abhängigkeiten
  - Relaxed Layered Architecture
- Was ist physikalische Architektur
  - Tatsächliche Packagestruktur und deren Abhängigkeiten
  - Abbildungsvorschrift: 
    com.anderscore.<product>.<vertial-slice>.<layer>.<module>
  - Abhängigkeiten entweder nach oben oder nach unten (nie beides)

Note: Begriffe von Valtech

--

Beispiel: Spring-Batch

--

Beispiel: Dessert

--

Aufwand: Zähneputzen

--

# Sünden/Bad Smells

Problem mit zirkulären Abhängigkeiten

--

# Hotspot

Von einer zentralen Klasse kann man alles aufrufen
==> Diese Klasse wird überall verwendet

Lösung: Registry + Events

(erfordert mehrere statt einer Klasse und Interfaces)

--

# Packages nach Inhalt sortiert

- entities
- daos
- enums
- CommonConstants
- utils mit Abhängigkeiten

---

Alternative Produkte

- maven dependendcy:analyze
  - https://maven.apache.org/plugins/maven-dependency-plugin/source-repository.html
  - https://github.com/apache/maven-dependency-analyzer/tree/maven-dependency-analyzer-1.8 (verwendet ASM)