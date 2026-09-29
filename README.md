# Twitter Clone - Backend

Workintech FSWEB-s19-Challenge kapsamında yazdığım Twitter benzeri bir uygulamanın backend'i.
Spring Boot ve PostgreSQL kullanarak Tweet, Comment, Like ve Retweet ozelliklerini içeren bir REST API.

## Kullanılan teknolojiler

- Java 17
- Spring Boot 4.1.1
- Spring Security (session tabanlı giris, BCrypt ile sifreleme)
- Spring Data JPA + PostgreSQL
- Maven

## Nasıl çalıştırılır

1. PostgreSQL'de `twitter_clone` adında bir veritabanı oluştur
2. `src/main/resources/application.properties` dosyasındaki veritabanı bilgilerini kendine göre düzenle
3. Projeyi çalıştır:
   ```
   mvn spring-boot:run
   ```
4. Uygulama `http://localhost:8080` adresinde ayağa kalkar

## Endpoint'ler

### Auth
- `POST /register` - yeni kullanıcı kaydı
- `POST /login` - giriş yapma

### Tweet
- `POST /tweet` - tweet oluşturma (giriş yapmış olmak gerekir)
- `GET /tweet/findByUserId?userId=` - bir kullanıcının tüm tweetleri
- `GET /tweet/findById?id=` - tek bir tweet
- `PUT /tweet/{id}` - tweet güncelleme (sadece sahibi)
- `DELETE /tweet/{id}` - tweet silme (sadece sahibi)

### Comment
- `POST /comment` - yoruma yorum yazma
- `PUT /comment/{id}` - yorum güncelleme (sadece yorum sahibi)
- `DELETE /comment/{id}` - yorum silme (tweet sahibi veya yorum sahibi)

### Like / Dislike
- `POST /like` - tweete like atma
- `POST /dislike` - like'ı geri çekme

### Retweet
- `POST /retweet` - tweeti retweetleme
- `DELETE /retweet/{id}` - retweet'i silme (sadece retweet sahibi)

## Testler

Service katmanı için Mockito ile yazılmış unit testler mevcut. Çalıştırmak için:
```
mvn test
```

## Frontend

Bu backend'i test etmek için basit bir React arayüzü ayrı bir repoda:
https://github.com/sinembaglar/twitter-clone-frontend
