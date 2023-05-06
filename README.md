# Github Actions 101

Github Actions to funkcja Githuba do przeprowadzania różnych zadań na potrzebę repozytorium. Do zadań należą rzeczy jak uruchamianie skryptów, wdrażania itp.
Najważniejsze dla nas będzie teraz przeprowadzanie testów jednostkowych (choć pewnie inne funkcje z czasem też nabiorą na znaczeniu).

Zawarta instrukcja użytkowania została napisana dla języka programowania Java (wersja 17) oraz oprogramowania do testów JUnit-Jupiter (wersja 5.8.1). Narzędzie do zbudowania całości to Gradle. No i całość była sprawdzana na Windowsie, jak nie ma uwag do Linuxa, to albo nie zdążyłem, albo większych nie znalazłem. Konsultacje i uwagi mile widziane.

**Jeśli reszta zespołu ma jakieś propozycje do zmian czy preferencji proszę informować, w miarę możliwości zawrę na końcu tego dokumentu listę innych opcji**

**Dla wygody własnej w repozytorium umieściłem przykładowy kod, przykładowe testy (różne dla pokazania pozytywnego jak i negatywnego wyniku rozwiązania)**

## Część 1.  Zapoznanie się z kodem pokazowym
Repozytorium zawiera prosty kod w języku Java będący funkcjami typowymi dla przeciętnego kalkulatora: dodawanie, odejmowanie, mnożenie i dzielenie, czego chcieć więcej. Testy pokazowe mają prostą strukturę. Robimy klasę Calculator, tworzymy int będący wynikiem funkcji klasy, i z użyciem JUnit Jupiter Assertions, sprawdzamy czy wynik funkcji równa sie tyle ile oczekiwaliśmy
Przykładowy test (do całości odsyłam przejrzeć repo):

    @Test
    public void testAdd() {  
	    Calculator calculator = new Calculator();  
		int result = calculator.add(Integer.parseInt(System.getenv("NUM1")), Integer.parseInt(System.getenv("NUM2")));  
		Assertions.assertEquals(Integer.parseInt(System.getenv("RESULT")), result, "Expected result is " + System.getenv("RESULT"));
    }


## Część 2. Tworzenie pliku definiującego workflow

Zakładam, że jeśli ktoś jest w momencie zaciągnięcia repozytorium to ma przynajmniej folder "src" z kodem (struktura "main/java" i "test/java" w celach prezentacyjnych), ".git" i ".md", póki co dla wygody tylko na tym pracujemy. (omówienie struktury aplikacji w całości itp. na spotkanie) 

Ze względu, że będziemy używać Gradle'a, repozytorium będzie potrzebowało dla wykonywania workflow ustawienia pliku na wykonywalny. Możemy zrobić to na dwa sposoby, automatyczny czy ręczny (preferencje do tego zostawiam członkom zespołu do zadecydowania). Automatyczny podam już teraz, do ręcznego sposobu wrócimy za chwilę.
Automatyczny to po prostu ta linijka w CMD, czy innym interfejsie wiążącym repo lokalne z zdalnym: 

    git update-index --chmod=+x gradlew

Kolejnym ważnym elementem jest stworzenie ciągu folderów ".github/workflows", to tam będziemy umieszczać nasze akcje, które oczekujemy, że przeprowadzą się na działaniu.

Do tego folderu trafia plik "test.yml" (nazwa dowolna), który jest w tym przypadku definicją procesu CI/CD.

Kod pliku "test.yml":

    name: Java CI with Gradle
    
    on:
      push:
        branches: [ main ]
      pull_request:
        branches: [ main ]
    
    jobs:
      build:

    runs-on: ubuntu-latest

    env:
      NUM1: 2
      NUM2: 4
      RESULT: 6

    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    - name: Build with Gradle
      run: ./gradlew build

    - name: Test with Gradle
      run: ./gradlew test --tests "CalculatorTest.testAdd"

			 
			
Następujący kod wykonuje następujące działania:
1. Wybór czynności, które powodują rozpoczęcie worklow i na jakich branchach
2. Budowanie workflow
3. Uruchomienie na najnowszym ubuntu
4. Wprowadznie wartości do środowiska 
5. Pobieramy kod z gałęzi repozytorium
6. Ustawienie Javy (w tym przypadku 17)
7. Ustawienie plikowi "gradlew" status pliku wykonywalnego (wcześniej wspomniana metoda ręczna)
8. Budowanie projektu
9. Wykonywanie testu (lub więcej)
  
##  Instalacja Gradle'a (dodatek do instrukcji)

Jak już mamy pobrane na lokalny dysk repozytorium, wypadałoby zaimplementować coś, co sprawi że te akcje sprowokujemy. Zastosowałem tu Gradle, bo ma przyjemną obsługę z kilkoma językami i z interesującą nas Javą.  

Zakładam, że jeśli ktoś jest w momencie zaciągnięcia repozytorium to ma przynajmniej folder "src" z kodem (struktura "main/java" i "test/java", w późniejszym etapie wyjaśnienie jak zmienić tą strukturę dla Gradle'a), ".git" i "README.md", póki co dla wygody tylko na tym pracujemy.

**Szybki tutorial instalacji Gradle'a**
1. Wchodzimy w ten link: https://gradle.org/install/
2. Pobieramy wersje "Binary-Only"
3. Wypakowujemy folder, umieszczamy gdzie chcemy.
4. Ustawiamy zmienną środowiskową przez CMD:`
setx PATH "(ścieżka do folderu)\bin"`

Jak już mamy ogarnięte to, wpisujemy do CMD z położenia folderu z repozytorium `gradle init`

Na potrzebę tego kodu, zrobimy to z użyciem opcji "basic", a potem "Groovy" (adnotacja dla autora, ustalić czy korzystamy z Groovy czy Kotlin). Przeklikujemy dwa zapytania (chyba, że komuś zależy na zmianie nazwy projektu i czy używamy nowych API i zachowań).

Warto zwrócić uwagę na to, że w pliku ".gitignore" automatycznie wpisały się foldery ".gradle" i "build". Nie będą one potrzebne przy pushowaniu repo lokalnego na brancha.
