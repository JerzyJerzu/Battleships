## 1. Jakiego agenta AI użyłeś/aś?

Zacząłem od użycia **Gemini 3 Pro**, któremu wkleiłem polecenie do zadania. Dodałem instrukcję, aby kod był czysty, podzielony na pliki, z użyciem wzorców projektowych, takich jak **Strategia** w przypadku strategii gracza, oraz **Singleton** dla samej gry i loggera. Następnie, mając już w ten sposób gotowy szkielet projektu, przejrzałem go. Prosiłem o tłumaczenie, jeśli było to potrzebne, oraz o poprawianie błędów i struktury kodu, aby była czytelniejsza, a także o optymalizowanie niektórych operacji. Do tłumaczenia fragmentów kodu czasem używałem **Gemini 2.5 Flash**, ze względu na większą szybkość odpowiedzi. Później użyłem **Gemini 3 Pro** do wygenerowania ulepszonych strategii dla graczy według moich instrukcji. Na koniec, z użyciem **Claude 4.5 Opus**, poprosiłem o sprawdzenie mojego kodu. Do automatycznego uzupełniania pisanego kodu używałem też wtyczki **github copilot**.

## 2. Przykłady promptów:

*   "When the ship is sunk, the board should somehow record it, so it knows when all the ships are sunk. Where does it do it?"

*   "I want you to make a major change in project structure. I think that the information about whether the ship is sunk or not should be contained in the ship class. And information about the ship types and number of ships should be passed and stored in the board object. When the `placeShip` method is called, it should also check against the number of ships included in the board to avoid an illegal number of ships. Please remember to refactor the code when you make a change."

*   "Now I want you to implement a class which inherits from `RandomPlayerStrategy` and is called `InformedRandomPlayerStrategy`. When it hits a ship and does not sink it, in the next moves it should search for the next segments of this ship (in a horizontal or vertical direction). When it establishes the direction, it should search for hits only in that direction until the ship is sunk. If you need to, you can create an additional utility class, `PotentialShip`."

*   "I want you to implement a `TryHardPlayerStrategy` which will inherit after `InformedRandomPlayerStrategy`. It should have a new functionality. When it is already known that there are no ships smaller than a certain size on the board, it should mark all the undiscovered fields (i.e., not hit and not in forbidden moves) where the remaining enemy ships cannot fit"

*   "OK, now finally I want you to scan this entire project for errors, potential bugs, and vulnerable code."

## 3. Przykłady poprawek:

Poniżej przedstawiam przykłady głównych, bardziej znaczących błędów i ich poprawek:

*   W pliku `Game.kt` było odwołanie do koordynatów początku statku poprzez `ship.position` zamiast `ship.head`, co uniemożliwiało kompilację.
*   Początkowa implementacja klasy `Board` pozwalała na strzelanie dwa razy w to samo miejsce. Zmieniłem to poprzez wyrzucenie `IllegalArgumentException`** w takim wypadku.
*   W klasie `Board` było możliwe dodanie innej liczby i rozmiarów statków niż przewidziano. Zmieniłem to poprzez przechowywanie listy statków w `Board`** i sprawdzanie przy dodawaniu nowego statku (szczegółowy prompt znajduje się w punkcie 2).
*   Zoptymalizowałem otrzymywanie strzałów (`Board.receiveShot`) poprzez zmiany w klasie `Ship`. Zamiast przechowywać koordynaty statku jako listę, są one przechowywane jako `HashSet`. Dzięki temu można w ten sposób sprawdzić, do jakiego statku należy dany punkt, w czasie `O(1)` zamiast `O(n)`, jak było to wcześniej zaimplementowane przez AI.

## 4. Co działało dobrze, co nie:

Najbardziej przyspieszyło mi pracę wygenerowanie szkieletu na początku. Dzięki temu nie musiałem zastanawiać się nad rozplanowaniem całej struktury, lecz skupiłem się na poprawianiu błędów, zmianie struktury tylko tam, gdzie było to konieczne, oraz optymalizowaniu kodu. Plusem było to, że początkowe rozwiązanie, po poprawieniu jednego błędu, już się kompilowało i działało poprawnie.

Dopracowania wymagała **złożonść** kodu oraz **zgodność ze wzorcami projektowymi**, zwłaszcza z zasadą pojedynczej odpowiedzialności (Single Responsibility Principle - SRP) i hermetyzacji (Encapsulation). Nie powodowało to bezpośrednich błędów, ale utrudniało rozbudowywanie projektu (na przykład o kolejne strategie) i obniżało jego czytelność. W wielu miejscach można było też napisać krótszy, bardziej czytelny kod i tam gdzie uznałem że warto to zrobić, zostało to poprawione.

Ogólny wniosek jest taki, że i tak udało się zaoszczędzić więcej czasu na generowaniu kodu i poprawieniu głównych niedociągnięć, niż na stosowaniu w każdym przypadku optymalnych rozwiązań od początku. Automatyczne uzupełnianie pisanego kodu za pomocą wtyczki GitHub Copilota również przyspieszyło pracę.