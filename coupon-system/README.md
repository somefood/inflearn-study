# 섹션 1 프로젝트 세팅

## 요구사항정의

```text
선찬숙 100명에게 할인쿠폰을 제공하는 이벤트를 진행

이 이벤트는 아래와 같은 조건을 만족하여야 함
- 선착순 100명에게만 지급되어야 한다.
- 101개 이상이 지급되면 안된다.
- 순간적으로 몰리는 트래픽을 버틸 수 있어야 한다.
```

# 섹션 2 Redis 활용하여 문제 해결하기

```shell
docker pull redis
docker run --name myredis -d -p 6739:6739 redis

# build.gradle 의존성 추가
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

## 문제점 해결하기

- 서버 자체를 싱글 쓰레드로 돌리면 성능 이슈가 생길 것이고
- Java의 synchronized를 활용할 수 있지만, 서버가 여러 대가 되면 또 문제가 발생됨
- DB의 락을 걸면 발급된 쿠폰 개수를 갖고오는 것부터 쿠폰을 생성할 때까지 락을 걸어야 하기에 성능에 이슈가 생길 수 있음
 
- 쿠폰 개수의 정합성을 관리하기에 Redis의 incr을 활용해서 key에 대한 value를 1씩 올릴 수 있음
- Redis를 사용하면 싱글쓰레드 형태로 갖고 오기에 정합성이 보장되고, incr의 성능도 빠름

- 아래와 같이 incr "key" 형태로 입력하면 1씩 증가하는 것을 볼 수 있음

```shell
127.0.0.1:6379> incr coupon_count
(integer) 13
127.0.0.1:6379> incr coupon_count
(integer) 14
127.0.0.1:6379> incr coupon_count
(integer) 15
127.0.0.1:6379> incr coupon_count
(integer) 16
127.0.0.1:6379> incr coupon_count
(integer) 17
127.0.0.1:6379> incr coupon_count
(integer) 18
127.0.0.1:6379> incr coupon_count
(integer) 19
127.0.0.1:6379> incr coupon_count
(integer) 20
127.0.0.1:6379> incr coupon_count
(integer) 21
```
