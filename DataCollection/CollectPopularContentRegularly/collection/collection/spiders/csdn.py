'''
Author        : uiuing
Date          : 2021-07-1 17:19:02
LastEditTime  : 2021-07-20 17:24:29
LastEditors   : uiuing
Description   : csdn热门内容采集
FilePath      : \collection\collection\spiders\csdn.py
©️ uiuing.com
'''

from collection.items import CollectionItem
import scrapy
from fake_useragent import UserAgent

''' Class Annotation
    @description  :  CrawlingData
    @param         {*}
    @return        {*}
'''


class AllSpider(scrapy.Spider):

    # ReptileName
    name = 'csdn'

    # Randomly For geBrowser RequestHeader
    ua = UserAgent()

    # Custom Request Header
    # RequestHeader 'User-Agent' -> fake_useragent().random
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
        'Accept-Encoding': 'gzip, deflate, br',
        'Upgrade-Insecure-Requests': '1',
        'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8,en-US;q=0.7',
        'User-Agent': ua.random}

    ''' Function Annotations
        @description  :  SetUpURL
        @param         {*} self
        @param         {*} JobId
        @return        {*}
    '''

    def __init__(self, JobId=None):

        # Link Set
        self.start_urls = [
            "https://www.csdn.net/"
        ]

    ''' Function Annotations
        @description  : RequestToStart
            # RequestGeneration -> scrapy.FormRequest      
        @param         {*} self
        @return        {*}
    '''

    def start_requests(self):
        # Execution Request
        yield scrapy.FormRequest(
            start_urls[0], callback=self.parse, headers=self.headers, dont_filter=True)

    ''' Function Annotations
        @description  : ParseRequest 
        @param         {*} self
        @param         {*} response
        @return        {*}
    '''

    def parse(self, response):
        pass
