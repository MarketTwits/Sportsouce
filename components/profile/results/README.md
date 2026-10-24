# Мои результаты

Отдельный модуль профиля: MVIKotlin Store → repository → API, навигация и модальные формы через Decompose. Экран открывается из профиля; опубликованный результат ведёт в существующий нативный протокол старта, предстоящий старт — в его детали.

Основные данные:

- `GET my-results/summary` — статистика и личные рекорды.
- `GET my-results/feed` — `category` (необязательно), `skipCount`, `maxResultCount`; пагинация из core, адаптивная сетка.
- `GET my-results/series` — `{key, name, count, categories}[]`.
- `GET my-results/progress` — `series` и `category` необязательны, `compare=self|group|age`. Без категории сервер возвращает сравнение по темпу. `point.compare` — `{value, sample, label}` либо `null`.

Формы:

- `GET start/autocomplete?query=…&limit=12` и `GET identity/unmatched-results?q=…&startId=…&maxResultCount=20`.
- `GET/POST identity/claims`; POST: `{resultId}`.
- `GET identity/candidates`, `GET identity/candidates/dismissed`, `POST identity/candidates/:id/accept|dismiss|restore`.
- `GET identity/suggestions`, `POST identity/suggestions/:id/respond`: `{accept}`.
- `POST identity/conflicts/:id/respond`: `{transfer}`.
- `POST identity/missing-event-requests`: название, дата, дистанция, время, ссылка или `proofFileId`. Файл сначала загружается multipart-запросом в `file/upload` (`file`, `lastModified`). PDF/изображения до 10 МБ.

Контракты сверены с примерами пользователя и публичными клиентскими схемами сайта (версия `sport-web@f2d3394`, 23 октября 2026). Браузерная сессия не используется приложением. Токен обновляет общий AuthDataSource; тела авторизованных запросов не логируются.

Повторная отправка блокируется, при ошибке сохраняется форма и id уже загруженного протокола. Переключение фильтров отменяет устаревший запрос графика. Обновление истории сохраняет показанные записи. Привязка, объединение и передача требуют подтверждения пользователя в интерфейсе.

Проверки: `:components:profile:results:jvmTest`, `compileKotlinJs`, `compileAndroidMain`. Compose-тесты проверяют обе темы, ширины 320/390/800 и масштаб шрифта 200%; изображения для проверки создаются в `build/design-system/`.
