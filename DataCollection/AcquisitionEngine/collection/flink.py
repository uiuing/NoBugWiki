from scrapy.crawler import CrawlerProcess
from scrapy.utils.project import get_project_settings
from kafka import KafkaConsumer
import json

consumer = KafkaConsumer('toc2', bootstrap_servers = '106.55.22.18:9092')
for msg in consumer:
    print("\n")
    print("topic = %s" % msg.topic) # topic default is string
    print("value = %s" % msg.value.decode())
    print("\n")