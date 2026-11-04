# 🌍 Darwin World

Projekt zaliczeniowy z przedmiotu **Programowanie Obiektowe** (2025, WT 18:30)  
**Autorzy:** Drozyński & Kroker

---

## Opis projektu

**Darwin World** to symulator ewolucji inspirowany teorią Darwina. Na mapie żyją zwierzęta, które poruszają się, jedzą rośliny, rozmnażają się i dziedziczą genomy. Z czasem populacja ewoluuje – przeżywają najlepiej przystosowane osobniki.

Projekt jest napisany w **Javie 21** z graficznym interfejsem użytkownika opartym na **JavaFX**.

---

## Funkcjonalności

### 🐾 Symulacja
- Zwierzęta poruszają się po mapie zgodnie ze swoim **genomem** (sekwencja 8 kierunków)
- Każdy ruch kosztuje energię; brak energii = śmierć
- Zwierzęta **jedzą rośliny** i odzyskują energię
- Zwierzęta **rozmnażają się**, gdy mają wystarczająco energii – potomek dziedziczy genomy rodziców (z możliwymi mutacjami)
- Rośliny odrastają każdego dnia, częściej w strefie **dżungli** (środkowy pas mapy)

### 🗺️ Warianty mapy
| Wariant | Opis |
|---------|------|
| `STANDARD` | Klasyczna mapa kulista (glob) – zwierzęta przechodzą przez krawędzie lewo/prawo |
| `FIRE` | Mapa z pożarami – pożary mogą wybuchnąć podczas jedzenia, rozprzestrzeniają się na sąsiednie pola z roślinami i ranią przebywające na nich zwierzęta |

### 🧬 Genom
- Każde zwierzę posiada **genom** – tablicę genów (wartości 0–7) określających kierunek obrotu
- Potomek dziedziczy część genomu od każdego rodzica proporcjonalnie do ich energii
- Mutacje losowo zmieniają wybrane geny

### 📊 Statystyki (na żywo)
- Numer dnia
- Liczba zwierząt i roślin
- Liczba wolnych pól
- Średnia energia, długość życia i liczba dzieci
- Najpopularniejszy genotyp
- Liczba aktywnych pożarów (tryb FIRE)

### 🖱️ Interakcja
- **Pauza/Wznowienie** symulacji
- **Kliknięcie na zwierzę** (podczas pauzy) wyświetla jego szczegółowe informacje:
  - Genom i aktywny gen
  - Energia, wiek
  - Liczba zjedzonych roślin, dzieci, potomków
  - Dzień śmierci (jeśli martwe)

---

## Wymagania

- **Java 21** (JDK)
- **Gradle** (wbudowany wrapper `gradlew`)
- Połączenie z internetem przy pierwszym uruchomieniu (pobieranie zależności)

---

## Uruchomienie

```bash
cd DarwinWorld
./gradlew run
```

Na Windows:
```bash
cd DarwinWorld
gradlew.bat run
```

---

## Konfiguracja symulacji

Po uruchomieniu aplikacji pojawia się okno konfiguracji z następującymi parametrami:

| Parametr | Opis |
|----------|------|
| Szerokość / Wysokość | Rozmiar mapy (maks. 500x500) |
| Liczba zwierząt startowych | Ile zwierząt pojawia się na początku |
| Energia startowa | Energia każdego zwierzęcia na starcie |
| Energia rośliny | Ile energii daje zjedzenie rośliny |
| Koszt ruchu | Energia tracona za każdy krok |
| Rośliny na dzień | Ile roślin wyrasta każdego dnia |
| Energia do rozmnażania | Minimalna energia potrzebna do rozrodu |
| Koszt rozmnażania | Energia przekazywana potomkowi przez każdego rodzica |
| Min/Max mutacji | Zakres losowej liczby mutacji w genomie potomka |
| Długość genomu | Liczba genów w sekwencji |
| Wariant | `STANDARD` lub `FIRE` |

---

## Struktura projektu

```
DarwinWorld/
├── src/
│   ├── main/
│   │   ├── java/darwin/
│   │   │   ├── DarwinWorld.java          # Punkt wejścia (JavaFX Application)
│   │   │   ├── model/
│   │   │   │   ├── Animal.java           # Klasa zwierzęcia
│   │   │   │   ├── Genotype.java         # Genom i dziedziczenie
│   │   │   │   ├── Plant.java            # Roślina
│   │   │   │   ├── FireTile.java         # Pole pożaru
│   │   │   │   ├── MapDirection.java     # Kierunki (8 stron świata)
│   │   │   │   ├── Vector2d.java         # Pozycja 2D
│   │   │   │   ├── maps/
│   │   │   │   │   ├── WorldMap.java         # Interfejs mapy
│   │   │   │   │   ├── AbstractWorldMap.java # Wspólna logika mapy
│   │   │   │   │   ├── GlobeMap.java         # Mapa kulista (STANDARD)
│   │   │   │   │   └── FireWorldMap.java     # Mapa z pożarami (FIRE)
│   │   │   │   └── simulation/
│   │   │   │       ├── Simulation.java       # Pętla symulacji
│   │   │   │       ├── SimulationConfig.java # Parametry konfiguracyjne
│   │   │   │       ├── SimulationStats.java  # Statystyki rundy
│   │   │   │       └── SimulationVariant.java# Enum wariantów
│   │   │   └── presenter/
│   │   │       ├── SimulationPresenter.java  # Widok symulacji
│   │   │       └── ParametersPresenter.java  # Widok konfiguracji
│   │   └── resources/
│   │       ├── simulation.fxml           # Layout widoku symulacji
│   │       └── parameters.fxml          # Layout konfiguracji
│   └── test/
└── build.gradle
```

---

## Technologie

- **Java 21**
- **JavaFX 21** (`javafx.controls`, `javafx.fxml`)
- **Gradle** (build system)
- **JUnit 5** (testy jednostkowe)

---

## Autorzy

| Imię i Nazwisko | Rola |
|-----------------|------|
| Drozyński | Współautor |
| Kroker | Współautor |
