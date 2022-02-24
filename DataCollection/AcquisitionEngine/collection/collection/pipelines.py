# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html

import json
import time

class CollectionPipeline(object):
    def __init__(self):
        self.start = time.time()
    def open_spider(self, spider):
        self.Value = "["
	
    def process_item(self, item,spider):
        self.Value += json.dumps(dict(item), ensure_ascii=False) + ','
        return item

    def close_spider(self, spider):
        end = time.time()
        print("\n\n\n\n\n\n\n\n\n\n",end-self.start,"\n\n\n\n\n\n")
        spider.r.set(spider.JobId,self.Value[:-1]+"]")   

''' Save In Json Format
    # class CollectionPipeline(object):
    #     # 构造方法（初始化对象时执行的方法）
    #     def __init__(self):
    #         self.start = time.time()
    #         # 必须使用 w+ 模式打开文件，以便后续进行 读写操作（w+模式，意味既可读，亦可写）
    #         # 注意：此处打开文件使用的不是 python 的 open 方法，而是 codecs 中的 open 方法
    #         self.json_file = codecs.open('data.json', 'a+', encoding='UTF-8')

    #     # 爬虫开始时执行的方法
    #     def open_spider(self, spider):
    #         # 在爬虫开始时，首先写入一个 '[' 符号，构造一个 json 数组
    #         # 为使得 Json 文件具有更高的易读性，我们辅助输出了 '\n'（换行符）
    #         self.json_file.write('[\n')
        
    #     # 爬虫 pipeline 接收到 Scrapy 引擎发来的 item 数据时，执行的方法
    #     def process_item(self, item, spider):
    #         # 将 item 转换为 字典类型，并编码为 json 字符串，写入文件
    #         # 为使得 Json 文件具有更高的易读性，我们辅助输出了 '\t'（制表符） 与 '\n'（换行符）
    #         item_json = json.dumps(dict(item), ensure_ascii=False)
    #         self.json_file.write('\t' + item_json + ',\n')
    #         return item

    #     # 爬虫结束时执行的方法
    #     def close_spider(self, spider):
    #         # 在结束后，需要对 process_item 最后一次执行输出的 “逗号” 去除
    #         # 当前文件指针处于文件尾，我们需要首先使用 SEEK 方法，定位到文件尾前的两个字符（一个','(逗号), 一个'\n'(换行符)）的位置
    #         self.json_file.seek(-2, os.SEEK_END)
    #         # 使用 truncate() 方法，将后面的数据清空
    #         self.json_file.truncate()
    #         # 重新输出'\n'，并输出']'，与 open_spider(self, spider) 时输出的 '[' 相对应，构成一个完整的数组格式
    #         self.json_file.write('\n]')
    #         end = time.time()
    #         print("\n\n\n\n\n\n\n\n\n\n",end - self.start,"\n\n\n\n\n\n")
    #         # 关闭文件
    #         self.json_file.close()
'''