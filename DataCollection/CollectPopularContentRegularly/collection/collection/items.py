# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy


class CollectionItem(scrapy.Item):
    # Article Title
    title = scrapy.Field()
    # Access Links To Articles
    url = scrapy.Field()
    # Sort
    sort = scrapy.Field()
    pass
