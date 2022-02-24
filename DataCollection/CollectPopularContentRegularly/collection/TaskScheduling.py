from scrapy.crawler import CrawlerProcess
from scrapy.utils.project import get_project_settings

settings = get_project_settings()
crawler = CrawlerProcess(settings)

# pool = redis.ConnectionPool(host='106.55.22.18', port=6379, password='Cooluiu955330!',decode_responses=True)
# r = redis.Redis(connection_pool=pool)

# JobId = (json.loads(r.get("ReptileSourcePipeline").split('\t')[1]))['JobId']

# r.append(JobId,"[")

crawler.crawl('juejin', 'csdn', 'gitee')

crawler.start()


# r.append(JobId,"{}]")

# r.expire(JobId, 60)

# gets = r.get("ReptileSourcePipeline").split('\t')
# if(len(gets) == 2):
#     r.set("ReptileSourcePipeline","")
# else:
#     r.set("ReptileSourcePipeline","\t"+"\t".join(gets[2:len(gets)]))
