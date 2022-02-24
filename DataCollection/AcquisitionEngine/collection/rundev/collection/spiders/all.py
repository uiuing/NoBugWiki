import scrapy
import re
import redis
import json
from fake_useragent import UserAgent
import time

class AllSpider(scrapy.Spider):

    name = 'all'
    ua = UserAgent()
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
        'Accept-Encoding': 'gzip, deflate, br',
        'Upgrade-Insecure-Requests': '1',
        'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8,en-US;q=0.7',
        'User-Agent': ua.random}

    def __init__(self, JobId=None):
        self.start = time.time()
        self.r = redis.Redis(connection_pool=redis.ConnectionPool(
            host='106.55.22.18', port=6379, password='Cooluiu955330!', decode_responses=True))

        self.JobId = JobId

        Standard = 'https://www.baidu.com/s?wd='+self.r.get(self.JobId)+' site:'

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

        self.Value = "["

    def start_requests(self):
        for url in self.start_urls:
            yield scrapy.FormRequest(
                url, callback=self.parse, headers=self.headers, dont_filter=True)

    def parse(self, response):

        text = re.sub('\s+', " ", response.text)
        text_list = re.findall(
            '(?<=<div id="content_left">).*?(?=<div id="page")', text)

        if len(text_list) != 0:

            text = text_list[0]

            data = re.findall(
                '(?<=data-tools=\'{").*?(?=\')', text)

            data_len = len(data)

            if data_len != 0:

                introductions = re.findall(
                    '(?<=<div class="c-abstract">).*?(?=</div><style>)', text)

                if len(introductions) == data_len:

                    for index in range(0, data_len):

                        introduction_str = introductions[index]

                        frequency = len(re.findall('<em>', introduction_str))

                        if frequency > 0:

                            data_str = data[index]

                            self.Value += json.dumps('{"title":"'+re.findall('(?<=title":").*?(?=","url":")', data_str)[0]+'","url":"'+re.findall('(?<=","url":").*?(?="})', data_str)[
                                0]+'","introduction":"'+re.sub('[0-9]+年+[0-9]+月+[0-9]+日&nbsp;', '', re.sub('<.*?>', '', introduction_str))+'","frequency":"'+str(frequency)+'"}')+','

    def close(self):
        end = time.time()
        print(end-self.start)
        print(json.loads(self.Value[:-1]+"]"))
