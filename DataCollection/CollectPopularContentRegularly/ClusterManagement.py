''' Annotation
    Author        : uiuing
    Date          : 2021-06-06 19:33:58
    LastEditTime  : 2021-06-06 19:34:28
    LastEditors   : uiuing
    Description   : Resource Scheduling
    FilePath      : /AcquisitionEngine/ClusterManagement.py
    ©️ uiuing.com
'''
import os
from kafka import KafkaConsumer
from kafka import KafkaProducer

consumer = KafkaConsumer('toc1', bootstrap_servers = '106.55.22.18:9092')
for msg in consumer:
    os.system('cd collection/执行版本 && scrapy crawl all -a JobId="'+msg.value.decode()+'"')
    # producer = KafkaProducer(bootstrap_servers='106.55.22.18:9092')
    # producer.send('toc2', msg.value)