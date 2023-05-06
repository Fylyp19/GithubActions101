# Github Actions 101

Github Actions to funkcja Githuba do przeprowadzania różnych zadań na potrzebę repozytorium. Do zadań należą rzeczy jak uruchamianie skryptów, wdrażania itp.
Najważniejsze dla nas będzie teraz przeprowadzanie testów jednostkowych (choć pewnie inne funkcje z czasem też nabiorą na znaczeniu).

Zawarta instrukcja użytkowania została napisana dla języka programowania Java (wersja 17) oraz oprogramowania do testów JUnit-Jupiter (wersja 5.8.1). Narzędzie do zbudowania całości to Gradle. No i całość była sprawdzana na Windowsie, jak nie ma uwag do Linuxa, to albo nie zdążyłem, albo większych nie znalazłem. Konsultacje i uwagi mile widziane.

**Jeśli reszta zespołu ma jakieś propozycje do zmian czy preferencji proszę informować**

**Dla wygody własnej w repozytorium umieściłem przykładowy kod i dwa przykładowe testy (różne dla pokazania pozytywnego jak i negatywnego wyniku rozwiązania)**

## Część 1.  Zapoznanie się z kodem pokazowym
Repozytorium zawiera prosty kod w języku Java będący funkcjami typowymi dla przeciętnego kalkulatora: dodawanie, odejmowanie, mnożenie i dzielenie. Klasa z testem ma prostą strukturę. Robimy klasę Calculator, tworzymy int będący wynikiem funkcji klasy, i z użyciem JUnit Jupiter Assertions, sprawdzamy czy wynik funkcji równa się, tyle ile oczekiwaliśmy
Przykładowy test (do całości odsyłam przejrzeć repo):

    @Test
    public void testAdd() {  
	    Calculator calculator = new Calculator();  
		int result = calculator.add(Integer.parseInt(System.getenv("NUM1")), Integer.parseInt(System.getenv("NUM2")));  
		Assertions.assertEquals(Integer.parseInt(System.getenv("RESULT")), result, "Expected result is " + System.getenv("RESULT"));
    }


## Część 2. Tworzenie pliku definiującego workflow

Zakładam, że jeśli ktoś jest w momencie zaciągnięcia repozytorium to ma przynajmniej folder "src" z kodem (struktura "main/java" i "test/java" w celach prezentacyjnych), ".git" i ".md", póki co dla wygody tylko na tym pracujemy. (omówienie struktury aplikacji w całości itp. na spotkanie, chyba, że osoby, które pracowały z Gradle'm są obeznane) 

Ze względu, że będziemy używać Gradle'a, repozytorium będzie potrzebowało dla wykonywania workflow ustawienia pliku na wykonywalny. Możemy zrobić to na dwa sposoby, automatyczny czy ręczny (preferencje do tego zostawiam członkom zespołu do zadecydowania). Automatyczny podam już teraz, do ręcznego sposobu wrócimy za chwilę.
Automatyczny to po prostu ta linijka w CMD, czy innym interfejsie wiążącym repo lokalne z zdalnym: 

    git update-index --chmod=+x gradlew

Kolejnym ważnym elementem jest stworzenie ciągu folderów ".github/workflows", to tam będziemy umieszczać nasze akcje, które oczekujemy, że przeprowadzą się na działaniu.

Do tego folderu trafia plik "test.yml" (nazwa dowolna), który jest w tym przypadku definicją procesu CI/CD.

Kod pliku "GoodTest.yml":

    name: GoodTest
    
    on:
      push:
        branches: [master]
      pull_request:
        branches: [master]
        
    env:
      NUM1: 2
      NUM2: 2
      RESULT: 4
          
    jobs:
      build:
        runs-on: ubuntu-latest
    
        steps:
          - name: Checkout code
            uses: actions/checkout@v3
    
          - name: Set up JDK 17
            uses: actions/setup-java@v3
            with:
              java-version: '17'
              distribution: 'adopt'
    
          - name: Grant execute permission for gradlew
            run: chmod +x gradlew
            
          - name: Test with Gradle
            run: ./gradlew test --console=plain


			 
			
Następujący kod wykonuje następujące działania:
1. Wybór czynności, które powodują rozpoczęcie worklow i na jakich branchach (można albo przy pushowaniu, albo przy pull'owaniu)
2. Ustawienia zmiennych środowiskowych 
3. Budowanie workflow
4. Uruchomienie na najnowszym ubuntu
5. Wprowadznie wartości do środowiska 
6. Pobieramy kod z gałęzi repozytorium
7. Ustawienie Javy (w tym przypadku 17 i dystrybucją adopt)
8. Ustawienie plikowi "gradlew" status pliku wykonywalnego (wcześniej wspomniana metoda ręczna)
9. Wykonywanie testu (lub więcej)

W przypadku powyżej przedstawionego workflow mamy do czynienia z sytuacją, gdzie spodziewamy się pozytywnego rezultatu, błędny test ma zmienioną tylko wartość środowiskową "RESULT" na "5".

Mamy gotowe cztery pliki, "GoodTest.yml" i "WrongTest.yml", "TestOnTwoTests.yml" i "TestOnAll" add, commit i push, i sprawdzamy zakładkę **Actions** w repozytorium Githuba. Wyświetli nam się lista wszystkich workflow, po lewej mamy widok na krótszą listę konkretnych workflow. 
Errory powinny wyświetlić się dla WrongTest i TestOnAll, ze względu na wyniki, jest to oczekiwany efekt.
  
