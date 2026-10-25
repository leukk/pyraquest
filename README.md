# PyraQuest

Textová adventura ovládaná příkazy v konzoli. Hráč je hledač pokladů, který se v noci protáhl škvírou do staré egyptské pyramidy. Musí získat zlatou pohřební masku z Faraonovy hrobky a vynést ji ven ke vstupu.

Hra vznikla jako týmový semestrální projekt v Javě.

## Tým

- Martin Šilar
- Kristian Kalmar
- Matej Eibner
- Dominik Wells

## Požadavky

- JDK 17
- Maven 3.x

## Sestavení a spuštění

Sestavení a testy:

    mvn clean package

Spuštění hry (po sestavení):

    java -jar target/pyraquest.jar

Hru jde také spustit přímo z IntelliJ přes třídu `Main`.

## Ovládání

Příkazy se píšou bez diakritiky, na velikosti písmen nezáleží. Seznam příkazů vypíše `pomoc`, radu `napoveda`.

## Struktura projektu

- `domain`: prostory, věci, postavy, batoh, hráč
- `command`: příkazy a jejich zpracování
- `game`: hlavní herní smyčka a stav hry
- `data`: načítání herních dat a ukládání hry (JSON)

## Dokumentace

Kompletní návrh hry je v dokumentu PyraQuest GDD.