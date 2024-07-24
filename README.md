<h1>RU</h1>

<h2>Описание:</h2>

<p>
Простое погодное приложение основанное на <a href="https://www.weatherapi.com/">Weather API</a>. Главная задача - создать простой UI с использованием Material Design 3. 
Несмотря на всю простоту приложения, использовалась полноценная чистая архитектура для того, чтобы была возможность на простом примере показать свои навыки.
</p>

<h2>Стек:</h2>

  - Retrofit2
  - Room
  - Android View
  - Jetpack Navigation
  - Dagger + Hilt
  - kotlinx.coroutines
  - kotlinx.datetime
  - androidx.datastore
  - androidx.work

<h2>Функции приложения:</h2>

  - Поиск, добавление, удаление и редактирование позиции показа локации
  - Отображение текущей погоды
  - Отображение почасовой погоды
  - Отображение прогноза на несколько дней вперед
  - Уведомление каждые 30 минут о текущей погоде в центре уведомлений
  - Отображение текущей погоды в виджете на главном экране
  - Смена языка
    - Русский
    - Английский
  - Смена темы
    - Системная
    - Темная
    - Светлая
  - Выбор системы измерения
    - Метрическая
    - Имперская

<h2>Первоначальная настройка:</h2>

1. Клонируем репозиторий
```
git clone https://github.com/MUKIVA/open-weather
cd open-weather
```
2. Для корректной работы нужно получить API ключ, который можно получить по <a href="https://www.weatherapi.com/">ссылке</a>.
3. Желательно создать файл ```secrets.properties``` в корне проекта для хранения ключа, но также можно использовать ```secrets.defaults.properties```. Это необходимо для безопасного получения ключа через BuildConfig. Затем вставляем свой ключ в свойство:
```
KEY_WEATHER_API=<YOUR_API_KEY>
```
<h1>EN</h1>
<h2>Description:</h2>
<p> A simple weather application based on <a href="https://www.weatherapi.com/">Weather API</a>. The main goal is to create a simple UI using Material Design 3. Despite the simplicity of the application, a full-fledged clean architecture was used to demonstrate skills on a simple example. </p>

<h2>Application Features:</h2>

- Search, add, delete, and edit display location positions
- Display current weather
- Display hourly weather
- Display forecast for several days ahead
- Notification every 30 minutes about the current weather in the notification center
- Display current weather in a widget on the home screen
- Change language
  - Russian
  - English
- Change theme
  - System
  - Dark
  - Light
- Choose measurement system
  - Metric
  - Imperial
 
<h2>Initial Setup:</h2>

1. Clone the repository
```
git clone https://github.com/MUKIVA/open-weather
cd open-weather
```
2. To work correctly, you need to get an API key, which can be obtained at the <a href="https://www.weatherapi.com/">link</a>.
3. It is recommended to create a ```secrets.properties``` file in the project root for storing the key, but you can also use ```secrets.defaults.properties```. This is necessary for securely obtaining the key through BuildConfig. Then insert your key into the property:
```
KEY_WEATHER_API=<YOUR_API_KEY>
```
