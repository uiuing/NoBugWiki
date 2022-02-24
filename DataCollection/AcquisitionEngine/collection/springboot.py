from kafka import KafkaProducer
import redis

def start_producer():
    pool = redis.ConnectionPool(host='106.55.22.18', port=6379, password='Cooluiu955330!',decode_responses=True)
    r = redis.Redis(connection_pool=pool)
    producer = KafkaProducer(bootstrap_servers='106.55.22.18:9092')
    for i in range(0,100000):
        JobId = str(input("JobId: "))
        KeyWord = str(input("KeyWord: "))
        r.set(JobId, KeyWord)
        msg = JobId
        producer.send('toc1', msg.encode("utf-8"))
        print(msg)

if __name__ == '__main__':
    start_producer()

