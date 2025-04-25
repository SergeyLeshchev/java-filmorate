# java-filmorate
Template repository for Filmorate project.

![На данной диаграмме БД изображена схема связей между таблицами.](https://github.com/SergeyLeshchev/java-filmorate/blob/dbdiagram/dbdiagram.png)

Примеры некоторых запросов:  
Для получения списка с названиями фильмов:  
SELECT name  
FROM movie;  

Для получения названия фильма по id:  
SELECT name  
FROM movie  
WHERE film_id = {id}; -- вставьте id на место {id}  

Запрос для проверки статуса дружбы между пользователями:  
SELECT status  
FROM user_friend  
WHERE user_id = {user_id} -- вставьте на место {user_id} id пользователя  
      AND friend_id = {friend_id}; -- вставьте на место {friend_id} id предполагаемого друга  