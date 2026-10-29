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

## Sestavení

    mvn clean package

Vznikne spustitelný soubor `target/pyraquest.jar` (včetně všech knihoven). Součástí sestavení jsou i JUnit testy. Bez testů:

    mvn clean package -DskipTests

Soubor `target/original-pyraquest.jar` je jen meziprodukt sestavení, nespouští se.

## Spuštění

Z terminálu v kořeni projektu:

    java -jar target/pyraquest.jar

Na Windows se může česká diakritika zobrazit špatně. Pomůže přepnout konzoli na UTF-8:

    chcp 65001
    java -Dfile.encoding=UTF-8 -jar target/pyraquest.jar

Hru jde také spustit z IntelliJ přes třídu `Main`.

## Ovládání

Příkazy se píšou bez diakritiky, na velikosti písmen nezáleží. Seznam příkazů vypíše `pomoc`, radu `napoveda`, hru ukončí `konec`.

## Ukládání her

Příkaz `uloz <nazev>` uloží rozehranou hru do souboru `save-files/<nazev>.json` (složka vznikne vedle místa, odkud se hra spouští). Příkaz `nacti <nazev>` ji načte. Uloží se seznam provedených tahů, který se při načtení znovu přehraje.

## Testy

    mvn test

Testy zahrnují jednotlivé herní třídy a příkazy a také celý průchod hrou podle GDD (`WalkthroughTest`).

## Struktura projektu

- `domain`: prostory, věci, postavy, batoh, hráč, hádanka
- `command`: příkazy a jejich zpracování
- `game`: hlavní herní smyčka, stav hry, výpis prostoru
- `data`: načítání herních dat a textů z JSON souborů, ukládání hry
- `src/main/resources`: herní data (`data/`) a texty hry (`texts/`)

## Dokumentace

Kompletní návrh hry je v dokumentu PyraQuest GDD.