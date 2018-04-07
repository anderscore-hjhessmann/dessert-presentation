## &nbsp;

# Was ist 
# strukturelle 
# Architektur?

--

## Spring Batch Layered Architecture

<div style="position:relative">
    <img class="plain" src="images/spring-batch-layers.png"/>
    <div style="position:absolute; bottom:1em; right: 5em">Relaxed Layered<br/>Architecture</div>
</div>

--

## Dessert Bausteine und Abhängigkeiten

<img class="plain" src="images/dessert-components.svg" width="312"/>

--

## &nbsp;

# Strukturelle Architektur

  - Schichten
  - Vertical Slices
  - Funktionsblöcke
  - Abhängigkeiten

Note: Begriffe von Valtech

--

## Strukturelle Architektur

<img class="plain" src="images/strukturelle_architektur.svg"/>

--

## &nbsp;

# Sünden (Bad Smells)

--

## Packages nach Inhalt sortiert

Indikatoren:
- Top-Level Packages
  - entities
  - daos
  - enums
- Klasse CommonConstants
- util Package mit Abhängigkeiten

--

## Zirkuläre Abhängigkeiten

--

## Hotspot

Von einer zentralen Klasse kann man alles aufrufen
==> Diese Klasse wird überall verwendet

Lösung: Registry + Events

(erfordert mehrere statt einer Klasse und Interfaces)

--

Aufwand: Zähneputzen

