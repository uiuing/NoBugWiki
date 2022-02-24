''' Document Notes
    Author        : uiuing
    Date          : 2021-05-28 22:55:28
    LastEditTime  : 2021-06-05 18:46:06
    LastEditors   : uiuing
    Description   : CSDN 数据采集
    FilePath      : /collection/collection/spiders/all.py
    ©️ uiuing.com
'''

from collection.items import CollectionItem
import scrapy
import re
import redis
from fake_useragent import UserAgent

''' Class Annotation
    @description  :  CrawlingData
    @param         {*}
    @return        {*}
'''


class AllSpider(scrapy.Spider):

    # ReptileName
    name = 'all'

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
            # JobId -> SpringBoot Generated Workid ｜ The Scope Is Single Request 
        @param         {*} self
        @param         {*} JobId
        @return        {*}
    '''

    def __init__(self, JobId=None):

        # Connection To Redis Due To Reading The User S Search
        self.r = redis.Redis(connection_pool=redis.ConnectionPool(
            host='106.55.22.18', port=6379, password='Cooluiu955330!', decode_responses=True))

        # 此任务的ID
        self.JobId = JobId

        # Default Request Link Form
        Standard = self.r.get('EncryptUrl')+'{} site:'.format(
            self.r.get(JobId))

        # Link Set
        self.start_urls = [
            Standard+'blog.csdn.net',
            Standard+'www.cnblogs.com',
            Standard+'www.jianshu.com',
            Standard+'juejin.cn',
            Standard+'www.zhihu.com',
            Standard+'www.v2ex.com',
            Standard+'segmentfault.com',
            Standard+'developer.aliyun.com',
            Standard+'developer.huawei.com',
            Standard+'cloud.tencent.com',
            Standard+'leetcode-cn.com',
            Standard+'www.nowcoder.com',
            Standard+'www.runoob.com',
            Standard+'www.php.cn',
            Standard+'c.biancheng.net',
            Standard+'www.apiref.com',
            Standard+'www.liaoxuefeng.com',
            Standard+'www.w3school.com.cn'
        ]

    ''' Function Annotations
        @description  : RequestToStart
            # RequestGeneration -> scrapy.FormRequest      
        @param         {*} self
        @return        {*}
    '''

    def start_requests(self):

        for url in self.start_urls:
            # Execution Request
            yield scrapy.FormRequest(
                url, callback=self.parse, headers=self.headers, dont_filter=True)

    ''' Function Annotations
        @description  : ParseRequest 
        @param         {*} self
        @param         {*} response
        @return        {*}
    '''

    def parse(self, response):

        # Remove Line Breaks
        text = re.sub('\s+', " ", response.text)
        # Identify Key Information
        text_list = re.findall(
            '(?<=<div id="content_left">).*?(?=<div id="page")', text)

        # Prevent Anomalies
        # ! Remove The Websites Without Keywords
        if len(text_list) != 0:

            # Convert To String
            text = text_list[0]

            # Keep It
            # with open("sss.html", 'w') as f:
            #     f.write(text)

            item = CollectionItem()

            # This Is A List Of Url And Titles Provided By Baidu
            data = re.findall(
                '(?<=data-tools=\'{").*?(?=\')', text)

            data_len = len(data)

            # ! The Same Is To Avoid The Exception Caused By The Result Does Not Exist
            if data_len != 0:

                # Article Introduction Collection
                introductions = re.findall(
                    '(?<=<div class="c-abstract">).*?(?=</div><style>)', text)

                if len(introductions) == data_len:

                    # ! Traversal Storage
                    for index in range(0, data_len):

                        # Converts A Collection To A String
                        introduction_str = introductions[index]

                        # Frequency Of Keywords
                        frequency = len(re.findall('<em>', introduction_str))
                        # ! Exclude All Low Frequencies
                        if frequency != 0:

                            # Converts A Collection To A String
                            data_str = data[index]

                            # Extract Title And Store It
                            item['title'] = re.findall(
                                '(?<=title":").*?(?=","url":")', data_str)

                            # Extract Links And Store It
                            item['url'] = re.findall(
                                '(?<=","url":").*?(?="})', data_str)

                            # Extract The Introduction Of The Article And Store It
                            item['introduction'] = re.sub('[0-9]+年+[0-9]+月+[0-9]+日&nbsp;', '', re.sub(
                                '<.*?>', '', introduction_str))

                            # Store Word Frequency
                            item['frequency'] = frequency

                            yield item


''' Parsing With XPath Method
    # # Get The Number Of Entries
    # page_size = len(response.xpath(
    #     '//*[@id="content_left"]/div').extract())

    # # Parse The Entries One By One & Limit Acquisition Length & Avoid Grabbing Null Values
    # for index in range(1, page_size+1):

    #     # Analysis Of Article Title
    #     # Rule : /h3/a
    #     title = self.LawOfSubstitution(response, index, "/h3/a")

    #     # Parsing And Storing The Word Frequency Of Keywords
    #     frequency = len(response.xpath(
    #         '//*[@class="result c-container new-pmd" and @id="'+str(index)+'"]/*[@class="c-abstract" or @class="c-abstract c-abstract-en"]/em').extract())

    #     # Exclude Non Article Entries
    #     if title[0] and frequency>1:

    #         # Analysis-And-Storage-Of-Article-Introduction
    #         # Rule : //*[@class="c-abstract" or @class="c-abstract c-abstract-en"]
    #         introduction = self.LawOfSubstitution(
    #         response, index, '//*[@class="c-abstract" or @class="c-abstract c-abstract-en"]')

    #         # Analysis Of The Source Of The Article
    #         url = self.LawOfSubstitution(response, index, "/h3/a/@href")

    #         # Store Title
    #         item['title'] = title

    #         # Store Sntroduction
    #         item['introduction'] = introduction

    #         # Store Frequency
    #         item['frequency'] = frequency

    #         item['url'] = url

    #         yield item
    # def LawOfSubstitution(self, response, index, rules=None):
    #     return response.xpath(
    #         'string(//*[@class="result c-container new-pmd" and @id="'+str(index)+'"]'+rules+')').extract()
'''
