# 🐸 FROGGER - KLASICKÁ ARKÁDOVÁ HRA

## ČO JE TO?

Táto hra je založená na klasickej arkádovej hre **Frogger**. Cieľom hry je dostať žabu zospodu doska cez dopravu a rieku na bezpečný lístok vodného zvädnutia na druhej strane.

---

## AKO FUNGUJE

### Dva Hlavné Prekážky:

#### 1. **Cesta s Dopravou**
- Autá a nákladiaky sa pohybujú rôznymi rýchlosťami
- Môžu zraziť žabu a zabíť ju
- Musíš bezpečne prejsť cez všetky jazdné pruhy

#### 2. **Rieka**
- Nemôžeš skok priamo do vody - utopíš sa!
- Musíš skákať na kúsky dreva a korytnačky
- Drevo a korytnačky sa pohybujú tam a späť
- Pozor - v ďalších leveloch sa niektoré korytnačky potápajú (ak na nich stojíš, utopíš sa!)

### Dodatočné Nebezpečenstvá:
- Drevo a korytnačky ťa môžu zatlačiť za okraj obrazovky
- Musíš dosiahnuť cieľ pred vypršaním času

---

## AKO SA HRA HRÁ

### Tlačidlá:
- **NOVÁ HRA** - Resetuje hru s novými nastaveniami
- **ŠTART** - Spustí hru

### Ovládanie:
- **ŠÍPKY** (nahoru, nadolu, doľava, doprava) - Pohyb žaby

### Pozorovanie Stavu:
- **Životov** - Počet zostávajúcich životov
- **Level** - Aktuálny level
- **Čas** - Zostávajúci čas
- **Skokov** - Počet vykonaných skokov

### Nastavenia:
- **Počiatok životov** - Koľko životov začneš (1-10)
- **Počiatok času** - Koľko času máš (100-600 sekúnd)
- **Počiatok levelu** - Ktorý level chceš hrať (1-5)

---

## POSTAVY V HRE

| Prvok | Popis |
|-------|--------|
| 🟢 **Zelená Žaba** | Ty si! |
| 🟨 **Nákladiak** | Pomaly sa pohybuje - vyhni sa mu! |
| 🔴 **Auto** | Rýchle sa pohybuje - vyhni sa mu! |
| 🟤 **Drevo** | Skákaj na to aby si prešiel rieku |
| 🟢 **Korytnačka** | Tiež sa pohybuje - skákaj na to! |
| ⚫ **Lístok** | BEZPEČIE! Dosiahni to aby si vyhral level |
| 🔵 **Voda** | MUERTE! Neklesaj do vody |
| ⚙️ **Cesta** | Bezpečne sa pohybuj, ale pozor na dopravu |
| 💚 **Tráva** | BEZPEČIE! Pohodlné miesto |

---

## 5 LEVELOV

### Level 1
- Základný level pre tréning
- 7 nákladiakov, 4 autá
- 6 kusov dreva, 5 skupín korytnačiek

### Level 2
- Zvýšená zložitosť
- 7 nákladiakov, 7 áut
- 6 kusov dreva, 4 skupiny korytnačiek

### Level 3
- Viacero prekážok
- 9 nákladiakov, 8 áut
- 3 kusy dreva, 4 skupiny korytnačiek

### Level 4
- Náročný level
- 6 nákladiakov, 12 áut
- 3 kusy dreva, 6 skupín korytnačiek

### Level 5
- ULTRA ŤAŽKÝ!
- 0 nákladiakov, 22 áut
- 3 kusy dreva, 6 skupín korytnačiek

---

## SKÓRE A CIELE

### Pokúste sa:
- ✅ Prejsť všetky 5 levelov
- ✅ Zvítiť si lepší čas ako doteraz
- ✅ Vykonať čo najmenej skokov
- ✅ Stratif čo najmenej životov

---

## UŽITOČNÉ TIPY

1. **Plánovanie** - Pred skokmi si všimni rytmus pohybu dopravy
2. **Časovanie** - Čakaj na správny moment na skok
3. **Teda** - Drevo sa pohybuje doľava/doprava - vzťahuj sa na to
4. **Bezpečnosť prvá** - Nie je nutné skákať na každé drevo, nájdi bezpečné cesty
5. **Čas** - Sleduj odpočítávač - nepálch všetok čas!

---

## TECHNICKÉ INFORMÁCIE

### Špecifikácie Hry:
- **Autor**: Jednotkova & Fabia
- **Základ**: NetLogo Frogger model (Wilensky, U. 2002)
- **Implementácia**: Java s Swing GUI
- **Obrazy**: Custom graphics z obrazky/ zložky

### Zdrojový Kód Štruktúra:
- `Frogger.java` - Hlavná trieda hry, logika levelov
- `Platno.java` - GUI a vykresľovanie
- `zaba.java` - Žaba (hráč)
- `Auto.java` - Autá
- `Kamion.java` - Nákladiaky
- `Kmen.java` - Drevo
- `Korytnacka.java` - Korytnačky
- `Lekno.java` - Lístky
- `Obrazok.java` - Základná trieda
- `obdlznik.java` - Obdĺžnik (tvar)
- `manazer.java` - Manager (pomocná trieda)

---

## ZARAĎOVANIE HRÁČOV

| Pozícia | Úkol | Čas |
|---------|------|------|
| 🏆 EXPERT | Všetky levely < 3 min | Expert |
| 🥈 PRO | Všetky levely < 5 min | Pro |
| 🥉 SKILL | Všetky levely < 10 min | Skill |
| 👶 ZAČIATOČNÍK | Všetky levely | Začiatočník |

---

## VIAC MOŽNOSTÍ

### Budúce Rozšírenia:
- Systém bodov a výsledkov
- Ďalšie levely
- Bonusy a špeciálne prvky
- Zvuk a hudba
- Automatický režim (AI)
- Multiplayer

---

**Zabav sa a nespadni do vody! 🐸💦**
