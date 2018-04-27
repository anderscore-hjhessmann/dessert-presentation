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

<div style="position: relative;">
    <img class="plain" src="images/circular1.svg" height="566"/>
    <img class="plain fragment" src="images/circular2.svg" height="566" style="position: absolute; left: 40%"/>
</div>

--

## Hotspot

Von einer zentralen Klasse kann man alles aufrufen

&rArr; Diese Klasse wird überall verwendet

<div style="position:relative">
<img class="plain" src="images/hotspot.svg" width="444"/>
<div style="position:absolute; bottom: 20%; left: 50%">
<p style="margin-bottom:5ex">An der Commit-History zu erkennen</p>
Lösung: Registry + Events<br/>
(erfordert Interfaces und mehrere Klassen statt einer Klasse)
</div>
</div>

--

## Wie Zähneputzen...

Man muss etwas dafür tun, hat aber keinen unmittelbaren nutzen.

![Zähneputzen](images/man-2166254.svg) <!-- .element: height="500" -->
<div style="font-size: small">Quelle: https://pixabay.com/de/mann-spiegel-zahn-blau-badezimmer-2166254/</div>

